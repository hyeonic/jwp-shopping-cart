package woowacourse.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;

@SpringBootTest
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
class AuthServiceTest {

    private final AuthService authService;

    @Autowired
    public AuthServiceTest(final AuthService authService) {
        this.authService = authService;
    }

    @DisplayName("email과 password를 활용하여 검증을 진행한 뒤 적절하면 token을 생성하여 반환한다.")
    @Test
    void 토큰_생성() {
        final String email = "test@email.com";
        final String password = "test";

        final TokenRequest tokenRequest = new TokenRequest(email, password);

        assertDoesNotThrow(() -> authService.createToken(tokenRequest));
    }

    @DisplayName("token 정보를 기반으로 email 정보를 반환한다.")
    @Test
    void 이메일_조회() {
        final String email = "test@email.com";
        final String password = "test";
        final TokenResponse tokenResponse = authService.createToken(new TokenRequest(email, password));

        final String findEmail = authService.findEmailByToken(tokenResponse.getAccessToken());

        assertThat(findEmail).isEqualTo(email);
    }
}
