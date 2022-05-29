package woowacourse.shoppingcart.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Customer;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@Repository
public class CustomerDao {

    private static final RowMapper<Customer> CUSTOMER_ROW_MAPPER = (resultSet, rowNum) ->
            new Customer(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("username")
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CustomerDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Customer findIdByEmail(final String email) {
        try {
            final String sql = "SELECT id, email, password, username FROM customer WHERE email = :email";

            final SqlParameterSource param = new MapSqlParameterSource("email", email);

            return jdbcTemplate.queryForObject(sql, param, CUSTOMER_ROW_MAPPER);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public boolean existsByEmail(final String email) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE email = :email)";

        final SqlParameterSource param = new MapSqlParameterSource("email", email);

        return jdbcTemplate.queryForObject(sql, param, Boolean.class);
    }

    public boolean existsByEmailAndPassword(final String email, final String password) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE email = :email AND password = :password)";

        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }
}
