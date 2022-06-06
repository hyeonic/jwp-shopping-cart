package woowacourse.shoppingcart.acceptance;

import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.TWO_PRODUCT_SAVE_REQUEST;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.orders.OrdersDetailRequest;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;

@DisplayName("주문 관련 기능")
public class OrdersAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 진행한다.")
    @Test
    void addOrders() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));
        CustomerResponse customerResponse = findCustomer(accessToken);

        Long productId1 = generateProduct(ONE_PRODUCT_SAVE_REQUEST);
        Long productId2 = generateProduct(TWO_PRODUCT_SAVE_REQUEST);

        CartItemSaveRequest cartItemSaveRequest1 = new CartItemSaveRequest(productId1, 2);
        CartItemSaveRequest cartItemSaveRequest2 = new CartItemSaveRequest(productId2, 3);
        Long cartItemId1 = generateCartItem(customerResponse, cartItemSaveRequest1);
        Long cartItemId2 = generateCartItem(customerResponse, cartItemSaveRequest2);

        List<OrdersDetailRequest> cartItemSaveRequests = List.of(
                new OrdersDetailRequest(cartItemId1, 2),
                new OrdersDetailRequest(cartItemId2, 3)
        );

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemSaveRequests)
                .when().post("/api/customers/{customerName}/orders", customerResponse.getUsername())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private void generateCustomer(CustomerSaveRequest request) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/customers")
                .then().log().all()
                .extract();
    }

    private String generateToken(TokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/auth/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class)
                .getAccessToken();
    }

    private CustomerResponse findCustomer(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerResponse.class);
    }

    private Long generateProduct(ProductSaveRequest request) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .extract();

        return Long.parseLong(response.header("Location").split("/products/")[1]);
    }

    private Long generateCartItem(CustomerResponse customerResponse, CartItemSaveRequest cartItemSaveRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(cartItemSaveRequest)
                .when().post("/api/customers/{customerName}/cartItems", customerResponse.getUsername())
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return Long.parseLong(response.header("Location").split("/cartItems/")[1]);
    }
}
