import courier.couriermodel.CourierLoginModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.apache.http.HttpStatus.*;

public class CourierLoginTest extends CourierLoginBase {


    @Test
    @DisplayName("Проверка авторизации курьера")
    public void testCourierLogin() {
        CourierLoginModel courierLoginModel = new CourierLoginModel(newCourier.getLogin(), newCourier.getPassword());
        Response courierLogin = courierApiClient.loginCourier(courierLoginModel);
        stepCheck.checkResponseStatusCode(courierLogin, SC_OK);
        stepCheck.checkResponseFieldExists(courierLogin, "id");
        stepCheck.checkResponseFieldNotNull(courierLogin, "id");
        stepCheck.checkResponseFieldNotEmpty(courierLogin, "id");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCourierLoginData")
    @DisplayName("Проверка авторизации курьера без обязательных полей")
    public void testCourierLoginWithOutRequiredFields(String login, String password, String expectedMessage) {
        CourierLoginModel courierLoginModel = new CourierLoginModel(
                login == null || login.isBlank() ? null : newCourier.getLogin(),
                password == null || password.isBlank() ? null : newCourier.getPassword());
        Response courierLoginResponse = courierApiClient.loginCourier(courierLoginModel);

        stepCheck.checkResponseStatusCode(courierLoginResponse, SC_BAD_REQUEST);
        stepCheck.checkResponseField(courierLoginResponse, "message", expectedMessage);
    }

    @Test
    @DisplayName("Проверка авторизации курьера с несуществующим логином и паролем")
    public void testCourierLoginWithNonExistentCredentials() {
        String courierPasswordRandom = "1234" + faker.number().numberBetween(100, 100000);

        CourierLoginModel courierLoginModel = new CourierLoginModel(courierLoginRandom, courierPasswordRandom);
        Response courierLogin = courierApiClient.loginCourier(courierLoginModel);
        stepCheck.checkResponseStatusCode(courierLogin, SC_NOT_FOUND);
        stepCheck.checkResponseField(courierLogin, "code", SC_NOT_FOUND);
        stepCheck.checkResponseField(courierLogin, "message", "Учетная запись не найдена");
    }

}
