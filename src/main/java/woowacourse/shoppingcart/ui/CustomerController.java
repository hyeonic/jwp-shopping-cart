package woowacourse.shoppingcart.ui;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.application.AuthService;
import woowacourse.auth.support.AuthorizationExtractor;
import woowacourse.shoppingcart.application.CustomerService;
import woowacourse.shoppingcart.dto.CustomerResponse;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final AuthService authService;
    private final CustomerService customerService;

    public CustomerController(final AuthService authService, final CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    @GetMapping("/you")
    public ResponseEntity<CustomerResponse> findYourInfo(HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        final String email = authService.findEmailByToken(token);
        CustomerResponse customerResponse = customerService.findByEmail(email);

        return ResponseEntity.ok(customerResponse);
    }
}
