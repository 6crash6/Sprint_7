import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
@Feature("Создание заказа")
public class OrderCreationTest {

    private static final String ORDER_URL = "https://qa-scooter.praktikum-services.ru/api/v1/orders";

    @Parameterized.Parameter(0)
    public String firstName;

    @Parameterized.Parameter(1)
    public String lastName;

    @Parameterized.Parameter(2)
    public String address;

    @Parameterized.Parameter(3)
    public String metroStation;

    @Parameterized.Parameter(4)
    public String phone;

    @Parameterized.Parameter(5)
    public int rentTime;

    @Parameterized.Parameter(6)
    public String deliveryDate;

    @Parameterized.Parameter(7)
    public String comment;

    @Parameterized.Parameter(8)
    public String[] color;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Иван", "Иванов", "Музыкальная, 22.", "2", "+71234567890", 1, "2020-06-06", "Чёрный", new String[]{"BLACK"}},
                {"Иван", "Иванов", "Музыкальная, 22.", "6", "+71234567890", 2, "2020-06-06", "Серый", new String[]{"GREY"}},
                {"Иван", "Иванов", "Музыкальная, 21.", "15", "+71234567890", 3, "2020-06-06", "Оба цвета", new String[]{"BLACK", "GREY"}},
                {"Иван", "Иванов", "Музыкальная, 20.", "25", "+71234567890", 30, "2020-06-06", "Без цвета", null}
        });
    }

    @Test
    @Story("Создание заказа")
    public void testCreateOrder() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response = createOrder(order);

        response.then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("track", notNullValue());
    }

    private Response createOrder(Order order) {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", order.getFirstName());
        body.put("lastName", order.getLastName());
        body.put("address", order.getAddress());
        body.put("metroStation", order.getMetroStation());
        body.put("phone", order.getPhone());
        body.put("rentTime", order.getRentTime());
        body.put("deliveryDate", order.getDeliveryDate());
        body.put("comment", order.getComment());
        body.put("color", order.getColor());

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(ORDER_URL);
    }
}
