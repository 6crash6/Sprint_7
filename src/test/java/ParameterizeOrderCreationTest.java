import io.restassured.response.Response;
import order.orderapi.OrderApiClient;
import order.ordermodel.OrderCreationModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.apache.http.HttpStatus.*;

public class ParameterizeOrderCreationTest{
    OrderApiClient orderApiClient;
    StepCheck stepCheck;

    @BeforeEach
    public void setUp() {
        orderApiClient = new OrderApiClient();
        stepCheck = new StepCheck();
    }

    @AfterEach
    public void tearDown() {
        stepCheck.assertAll();
    }

    static Stream<Arguments> orderValidParameters() {
        return Stream.of(
                Arguments.of(new OrderCreationModel(
                        "Иван",
                        "Иванов",
                        "Улица Пушкина, 5",
                        "Киевская",
                        "+7 123 456 78 90",
                        5,
                        "2025-06-20",
                        "Комментарий 1",
                        new String[]{"BLACK"} // color
                )),
                Arguments.of(new OrderCreationModel(
                        "Светлана",
                        "Петрова",
                        "Проспект Мира, 20",
                        "Тверская",
                        "+7 098 765 43 21",
                        7,
                        "2025-06-25",
                        "Комментарий 2",
                        new String[]{"GREY"} // color
                )),
                Arguments.of(new OrderCreationModel(
                        "Алексей",
                        "Сидоров",
                        "Улица Ленина, 10",
                        "Курская",
                        "+7 234 567 89 01",
                        3,
                        "2025-07-01",
                        "Комментарий 3",
                        new String[]{"BLACK", "GREY"} // color
                )),
                Arguments.of(new OrderCreationModel(
                        "Ольга",
                        "Кузнецова",
                        "Площадь Революции, 15",
                        "Смоленская",
                        "+7 345 678 90 12",
                        10,
                        "2025-07-10",
                        "Комментарий 4",
                        new String[]{}// color
                )),
                Arguments.of(new OrderCreationModel(
                        "Вера",
                        "Игнатова",
                        "Площадь Восстания, 69",
                        "Медведково",
                        "+7 800 555 35 35",
                        13,
                        "2025-07-11",
                        "Комментарий 4"
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("orderValidParameters")
    @DisplayName("Проверка создания заказа")
    public void testOrderCreate(OrderCreationModel order) {
        Response response = orderApiClient.createNewOrder(order);
        stepCheck.checkResponseStatusCode(response, SC_CREATED);
        stepCheck.checkResponseFieldExists(response, "track");
        stepCheck.checkResponseFieldNotNull(response, "track");
    }
}
