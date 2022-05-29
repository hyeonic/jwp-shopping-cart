package woowacourse.shoppingcart.exception;

public class UnmatchedCustomerException extends RuntimeException {

    public UnmatchedCustomerException() {
        this("email과 password가 일치하지 않습니다.");
    }

    public UnmatchedCustomerException(final String msg) {
        super(msg);
    }
}
