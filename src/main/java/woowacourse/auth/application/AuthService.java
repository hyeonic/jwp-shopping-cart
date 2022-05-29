package woowacourse.auth.application;

import org.springframework.stereotype.Service;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.auth.support.JwtTokenProvider;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.exception.InvalidCustomerException;
import woowacourse.shoppingcart.exception.UnmatchedCustomerException;

@Service
public class AuthService {

    private final CustomerDao customerDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final CustomerDao customerDao, final JwtTokenProvider jwtTokenProvider) {
        this.customerDao = customerDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        if (!customerDao.existsByEmail(tokenRequest.getEmail())) {
            throw new InvalidCustomerException(tokenRequest.getEmail() + "은 존재하지 않는 email 입니다.");
        }

        if (!customerDao.existsByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new UnmatchedCustomerException();
        }

        final String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public String findEmailByToken(final String token) {
        return jwtTokenProvider.getPayload(token);
    }
}
