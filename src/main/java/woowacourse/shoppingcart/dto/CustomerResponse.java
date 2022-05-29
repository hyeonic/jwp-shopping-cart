package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Customer;

public class CustomerResponse {

    private Long id;
    private String email;
    private String name;

    private CustomerResponse() {
    }

    public CustomerResponse(final Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getUsername();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
