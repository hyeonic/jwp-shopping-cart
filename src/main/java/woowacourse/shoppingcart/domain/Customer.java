package woowacourse.shoppingcart.domain;

public class Customer {

    private final Long id;
    private final String email;
    private final String password;
    private final String username;

    public Customer(final Long id, final String email, final String password, final String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = name;
    }

    public Customer(final String email, final String password, final String name) {
        this(null, email, password, name);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
