package com.myWebServer.application.config.auth;

import com.myWebServer.application.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor // final 필드를 생성자 주입받기 위한 Lombok 어노테이션
@EnableWebSecurity       // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // OAuth2 로그인 이후 사용자 정보를 처리할 서비스 (직접 구현한 커스텀 서비스)
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화
                // - 기본적으로 Spring Security는 CSRF 공격을 방지하지만,
                // - H2 콘솔 사용 등으로 인해 임시로 비활성화함 (개발 중에만 허용!)
                .csrf().disable()

                // 2. H2 콘솔 사용을 위한 frame 옵션 비활성화
                .headers().frameOptions().disable()

                .and()
                // 3. URL 접근 권한 설정 시작
                .authorizeRequests()

                // 3-1. 인증 없이 접근 허용할 경로 설정
                .antMatchers("/",               // 메인 페이지
                        "/css/**",         // 정적 CSS 파일
                        "/images/**",      // 정적 이미지
                        "/js/**",          // 정적 JS 파일
                        "/h2-console/**"   // H2 DB 콘솔
                        , "/profile"
                ).permitAll()

                // 3-2. "/api/v1/**" 경로는 ROLE_USER 권한을 가진 사용자만 접근 가능
                // - 여기서 hasRole()에는 "ROLE_" prefix가 자동으로 붙음.
                //   즉, Role.USER.name() → "USER" → "ROLE_USER"로 인식됨.
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())

                // 3-3. 나머지 모든 요청은 인증된 사용자만 접근 가능
                .anyRequest().authenticated()

                .and()
                // 4. 로그아웃 설정
                // - 로그아웃 시 "/" 경로로 리다이렉트
                .logout()
                .logoutSuccessUrl("/")

                .and()
                // 5. OAuth2 로그인 설정
                .oauth2Login()
                .userInfoEndpoint()
                // 로그인 성공 후 사용자 정보를 가져올 때의 설정
                // customOAuth2UserService가 사용자 정보를 가져오고 후처리 수행
                .userService(customOAuth2UserService);
    }
}
