package com.myWebServer.application.config.auth;

import com.myWebServer.application.config.auth.dto.OAuthAttributes;
import com.myWebServer.application.config.auth.dto.SessionUser;
import com.myWebServer.application.domain.user.User;
import com.myWebServer.application.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Service // Spring Bean으로 등록
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository; // DB에 사용자 저장/조회
    private final HttpSession httpSession;       // 세션에 사용자 정보를 저장하기 위한 HttpSession

    /**
     * OAuth2 로그인 이후 호출되는 메서드
     * 사용자 정보를 가져오고, 필요한 경우 DB 저장 후 Security Context에 등록
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 기본 제공 OAuth2UserService를 통해 사용자 정보 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2. 현재 로그인 중인 서비스(Google, Naver 등)의 식별자
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 예: google, naver, kakao 등

        // 3. OAuth2 제공자가 전달하는 사용자 식별자 필드명
        // 예: Google은 "sub", Naver는 "id"
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. OAuthAttributes로 사용자 정보 변환 (ofGoogle(), ofNaver() 등 분기 처리 포함)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 5. 사용자 정보 저장/업데이트 처리
        User user = saveOrUpdate(attributes);

        // 6. 세션에 사용자 정보 저장 (직렬화 문제 해결을 위해 DTO 사용)
        httpSession.setAttribute("user", new SessionUser(user));

        // 7. 스프링 시큐리티에서 사용하는 인증 객체 반환
        return new DefaultOAuth2User(
                // 사용자의 권한 설정 (Spring Security의 Role)
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                // OAuth2 로그인 시 받은 사용자 정보 Map (name, email, picture 등)
                attributes.getAttributes(),
                // 사용자의 고유 식별자 (예: "sub")
                attributes.getNameAttributeKey()
        );
    }

    /**
     * 기존 사용자라면 이름과 프로필 이미지만 업데이트,
     * 없다면 신규 생성 후 저장
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                // 이미 존재하는 사용자면 이름, 사진 정보만 업데이트
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                // 없으면 새로 생성
                .orElse(attributes.toEntity());

        // 저장 또는 업데이트된 사용자 반환
        return userRepository.save(user);
    }
}
