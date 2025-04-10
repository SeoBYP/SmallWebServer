package com.myWebServer.application.config;

import com.myWebServer.application.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 설정을 위한 Config 클래스
 * - Spring MVC 관련 사용자 정의 설정을 할 수 있음
 * - 여기서는 커스텀 HandlerMethodArgumentResolver 등록 역할
 */
@RequiredArgsConstructor
@Configuration // Spring 설정 클래스임을 명시
public class WebConfig implements WebMvcConfigurer {

    // 직접 구현한 HandlerMethodArgumentResolver 주입
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * 스프링 MVC에서 사용하는 커스텀 ArgumentResolver 등록 메서드
     * - Controller의 메서드 파라미터에 특정 조건이 맞으면
     *   자동으로 값을 주입해주는 기능을 함.
     * - 여기서는 @LoginUser SessionUser 타입을 인식하도록 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
    {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}
