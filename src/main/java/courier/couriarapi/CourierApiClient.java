package courier.couriarapi;

import baseconfig.BaseHttpClient;
import courier.couriermodel.CourierCreationModel;
import courier.couriermodel.CourierLoginModel;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CourierApiClient extends BaseHttpClient {
    private final String apiBasePath = "/api/v1/courier";
    private final String apiPathLogin = "/login";

    @Step("Создание курьера")
    public Response createNewCourier(CourierCreationModel courierToCreate) {
        return doPostRequest(apiBasePath, courierToCreate);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierLoginModel courierToLogin) {
        return doPostRequest(apiBasePath + apiPathLogin, courierToLogin);
    }

    @Step("Удаление курьера")
    public void deleteCourier(int courierId) {
        doDeleteRequest(String.format("%s/%d", apiBasePath, courierId));
    }

    @Step("Получение id курьера для удаления")
    public int loginCourierForCleanUp(String login, String password) {
        CourierLoginModel courierLoginModel = new CourierLoginModel(login, password);
        Response courierLogin = loginCourier(courierLoginModel);
        return courierLogin.jsonPath().getInt("id");
    }
}