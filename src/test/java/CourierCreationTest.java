import courier.couriermodel.CourierCreationModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.apache.http.HttpStatus.*;

public class CourierCreationTest extends CourierCreationBase {
    @Test
    @DisplayName("Проверка создания курьера")
    public void testCourierCanBeCreated() {
        CourierCreationModel newCourier = new CourierCreationModel(courierLoginRandom, "1234", "saske");
        Response courier = courierApiClient.createNewCourier(newCourier);
        stepCheck.checkResponseStatusCode(courier, SC_CREATED);
        stepCheck.checkResponseOk(courier, true);

        courierId = courierApiClient.loginCourierForCleanUp(newCourier.getLogin(), newCourier.getPassword());
    }

    @Test
    @DisplayName("Проверка создания двух одинаковых курьеров")
    public void testDuplicationCourierCreated() {
        String dupPassword = "1234";
        String dupFirstName = "saske";

        CourierCreationModel newCourier = new CourierCreationModel(courierLoginRandom, dupPassword, dupFirstName);

        Response courierFirst = courierApiClient.createNewCourier(newCourier);
        stepCheck.checkResponseStatusCode(courierFirst, SC_CREATED);
        stepCheck.checkResponseOk(courierFirst, true);

        courierId = courierApiClient.loginCourierForCleanUp(newCourier.getLogin(), newCourier.getPassword());

        Response courierSecond = courierApiClient.createNewCourier(newCourier);
        stepCheck.checkResponseField(courierSecond, "code", SC_CONFLICT);
        stepCheck.checkResponseField(courierSecond, "message", "Этот логин уже используется");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCourierData")
    @DisplayName("Проверка создания курьера при отсутствии обязательных полей")
    public void testCourierCreatedWhenRequiredFieldsMissing(String login, String password, String firstName) {
        CourierCreationModel newCourier = new CourierCreationModel(login, password, firstName);
        Response courier = courierApiClient.createNewCourier(newCourier);
        if (login != null && password != null && courier.getStatusCode() == SC_CREATED) {
            courierId = courierApiClient.loginCourierForCleanUp(login, password);
        }
        stepCheck.checkResponseStatusCode(courier, SC_BAD_REQUEST);
        stepCheck.checkResponseField(courier, "message", "Недостаточно данных для создания учетной записи");
    }
}



