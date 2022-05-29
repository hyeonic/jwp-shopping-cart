package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CustomerDaoTest {

    private final CustomerDao customerDao;

    public CustomerDaoTest(JdbcTemplate jdbcTemplate) {
        customerDao = new CustomerDao(jdbcTemplate);
    }

    @DisplayName("email을 기반으로 고객을 조회한다.")
    @Test
    void 고객_email_기반_조회() {
        final String email = "test@email.com";

        final Customer customer = customerDao.findIdByEmail(email);

        assertThat(customer.getEmail()).isEqualTo(email);
    }

    @DisplayName("email을 기반으로 존재 유무를 반환한다.")
    @Test
    void 고객_email_존재유무() {
        final String email = "test@email.com";

        final boolean result = customerDao.existsByEmail(email);

        assertThat(result).isTrue();
    }
}
