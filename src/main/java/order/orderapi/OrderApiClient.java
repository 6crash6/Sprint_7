package order.orderapi;

import baseconfig.BaseHttpClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.ordermodel.OrderCreationModel;

public class OrderApiClient extends BaseHttpClient {
    private final String apiBasePath = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createNewOrder(OrderCreationModel orderToCreate) {
        return doPostRequest(apiBasePath, orderToCreate);
    }
    @Step("Получение заказа")
    public Response getOrder() {
        return doGetRequest(apiBasePath);
    }
}
