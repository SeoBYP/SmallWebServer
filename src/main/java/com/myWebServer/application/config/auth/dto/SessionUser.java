package com.myWebServer.application.config.auth.dto;

import com.myWebServer.application.domain.user.User;
import lombok.Getter;

@Getter
public class SessionUser {
    // 세션에 저장할 사용자 정보 (직렬화 안전한 DTO)

    private String name;
    private String email;
    private String picture;

    /**
     * SessionUser는 인증된 사용자 정보를 세션에 저장하기 위한 DTO입니다.
     *
     * ⚠️ 왜 User 엔티티 대신 SessionUser를 사용하나요?
     * - User는 JPA @Entity 이므로 직렬화 시 문제가 발생할 수 있습니다.
     * - 프록시 객체나 Lazy 로딩 등으로 인해 직렬화에 취약합니다.
     * - 특히 HttpSession은 직렬화 가능한 객체만 저장 가능하므로,
     *   JPA 엔티티 대신 순수 데이터만 담은 DTO(SessionUser)를 사용하는 것이 안전합니다.
     *
     * ✅ SessionUser는 name, email, picture 필드만 가지며
     *    직렬화가 안정적이고 세션에 저장하기 적합합니다.
     */

    public SessionUser(User user) {
        // User 엔티티로부터 필요한 정보만 추출하여 세션에 저장
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
