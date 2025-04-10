package com.myWebServer.application.config.auth;

import com.myWebServer.application.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

/**
 * HandlerMethodArgumentResolver를 구현한 클래스.
 * 컨트롤러 메서드에서 @LoginUser라는 커스텀 어노테이션을 사용할 때,
 * 세션에서 SessionUser 객체를 주입해주는 기능을 수행함.
 */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    // 현재 사용자 정보를 보관 중인 세션 주입
    private final HttpSession httpSession;

    /**
     * 해당 파라미터가 @LoginUser 어노테이션이 붙어 있고,
     * 파라미터 타입이 SessionUser 클래스일 경우에만 true 반환.
     * → 즉, 해당 파라미터를 이 Resolver가 처리할지 여부 결정.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * 실제로 @LoginUser가 붙은 파라미터에 넣어줄 값을 세션에서 가져옴.
     * → 세션에 저장된 "user" 객체(SessionUser)를 반환.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception
    {
        return httpSession.getAttribute("user");
    }
}
