import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

@Feature("Получение списка заказов")
public class OrderListTest {

    private static final String ORDER_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @Test
    @Story("Получение списка заказов")
    public void testGetOrderList() {
        Response response = getOrderList();

        // Attach the response body to the Allure report
        Allure.addAttachment("Response Body", response.getBody().asString());

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("orders", notNullValue()); // Проверяем, что список заказов не null

        // Verify that each order object contains all the specified keys
        List<Map<String, Object>> orders = response.jsonPath().getList("orders");
        for (Map<String, Object> order : orders) {
            assertTrue("Order object does not contain 'id' key", order.containsKey("id"));
            assertTrue("Order object does not contain 'courierId' key", order.containsKey("courierId"));
            assertTrue("Order object does not contain 'firstName' key", order.containsKey("firstName"));
            assertTrue("Order object does not contain 'lastName' key", order.containsKey("lastName"));
            assertTrue("Order object does not contain 'address' key", order.containsKey("address"));
            assertTrue("Order object does not contain 'metroStation' key", order.containsKey("metroStation"));
            assertTrue("Order object does not contain 'phone' key", order.containsKey("phone"));
            assertTrue("Order object does not contain 'rentTime' key", order.containsKey("rentTime"));
            assertTrue("Order object does not contain 'deliveryDate' key", order.containsKey("deliveryDate"));
            assertTrue("Order object does not contain 'track' key", order.containsKey("track"));
            assertTrue("Order object does not contain 'color' key", order.containsKey("color"));
            assertTrue("Order object does not contain 'comment' key", order.containsKey("comment"));
            assertTrue("Order object does not contain 'createdAt' key", order.containsKey("createdAt"));
            assertTrue("Order object does not contain 'updatedAt' key", order.containsKey("updatedAt"));
            assertTrue("Order object does not contain 'status' key", order.containsKey("status"));
        }
    }

    private Response getOrderList() {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_URL);
    }
}
