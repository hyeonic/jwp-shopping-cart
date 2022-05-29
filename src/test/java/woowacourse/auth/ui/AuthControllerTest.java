package woowacourse.auth.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "test";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("email과 password를 활용하여 로그인을 시도하고 성공 시 token을 발급 받는다.")
    @Test
    void 토큰_발급() {
        final String accessToken = createToken(EMAIL, PASSWORD);

        assertThat(accessToken).isNotBlank();
    }

    private String createToken(final String email, final String password) {
        return RestAssured.given().log().all()
                .body(new TokenRequest(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/auth")
                .then().log().all()
                .extract()
                .as(TokenResponse.class)
                .getAccessToken();
    }
}
