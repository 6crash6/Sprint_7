import io.restassured.response.Response;
import order.orderapi.OrderApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.apache.http.HttpStatus.*;

public class GetOrderTest{
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

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void testGetOrderList() {
        Response response = orderApiClient.getOrder();
        stepCheck.checkResponseStatusCode(response, SC_OK);
        stepCheck.checkResponseFieldExists(response, "orders");
        stepCheck.checkResponseArrayNotEmpty(response, "orders");
        stepCheck.checkFieldExistsInArray(response, "orders", "id");
        stepCheck.checkFieldExistsInArray(response, "orders", "firstName");
        stepCheck.checkFieldExistsInArray(response, "orders", "lastName");
        stepCheck.checkFieldExistsInArray(response, "orders", "address");
        stepCheck.checkFieldExistsInArray(response, "orders", "metroStation");
        stepCheck.checkFieldExistsInArray(response, "orders", "phone");
        stepCheck.checkFieldExistsInArray(response, "orders", "rentTime");
        stepCheck.checkFieldExistsInArray(response, "orders", "deliveryDate");
        stepCheck.checkFieldExistsInArray(response, "orders", "track");
        stepCheck.checkFieldExistsInArray(response, "orders", "comment");
        stepCheck.checkFieldExistsInArray(response, "orders", "createdAt");
        stepCheck.checkFieldExistsInArray(response, "orders", "updatedAt");
        stepCheck.checkFieldExistsInArray(response, "orders", "status");
        stepCheck.checkResponseFieldExists(response, "pageInfo");
        stepCheck.checkResponseFieldNotNull(response, "pageInfo.page");
        stepCheck.checkResponseFieldNotNull(response, "pageInfo.total");
        stepCheck.checkResponseFieldNotNull(response, "pageInfo.limit");
        stepCheck.checkResponseFieldExists(response, "availableStations");
        stepCheck.checkResponseArrayNotEmpty(response, "availableStations");
        stepCheck.checkFieldExistsInArray(response, "availableStations", "name");
        stepCheck.checkFieldExistsInArray(response, "availableStations", "number");
        stepCheck.checkFieldExistsInArray(response, "availableStations", "color");
    }
}
