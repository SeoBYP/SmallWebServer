package com.myWebServer.application.config.auth.dto;

import com.myWebServer.application.domain.user.Role;
import com.myWebServer.application.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    // OAuth2 인증 응답 결과 전체를 담는 Map
    private Map<String, Object> attributes;

    // 사용자의 고유 식별자 키 (ex. "sub", "id" 등)
    private String nameAttributeKey;

    // 사용자 이름
    private String name;

    // 사용자 이메일
    private String email;

    // 사용자 프로필 사진 URL
    private String picture;

    /**
     * OAuthAttributes 생성자 - Builder 패턴 사용
     *
     * @param attributes         OAuth2 로그인 응답 전체 데이터
     * @param nameAttributeKey   사용자의 식별자 키
     * @param name               사용자 이름
     * @param email              사용자 이메일
     * @param picture            사용자 프로필 이미지
     */
    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    /**
     * OAuth2 제공자(Google, Naver 등)에 따라 사용자 정보 생성 방식 분기
     *
     * @param registrationId        로그인 요청한 서비스 구분자 (google, naver, etc)
     * @param userNameAttributeName 사용자 식별자 키 이름
     * @param attributes            OAuth2 응답 데이터
     * @return OAuthAttributes DTO
     */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        // 현재는 Google 로그인만 지원. 이후 네이버, 카카오 추가 시 이곳에 분기 처리
        if("naver".equals(registrationId))
        {
            return ofNaver("id",attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    /**
     * Google 로그인 응답에서 사용자 정보 추출
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    /**
     * OAuthAttributes → User 엔티티로 변환
     * 첫 가입 시 GUEST 권한 부여 (Role.GUEST)
     */
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
