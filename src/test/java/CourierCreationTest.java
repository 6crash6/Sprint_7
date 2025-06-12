import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Создание курьера")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourierCreationTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/courier";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static String VALID_LOGIN = "testCourier" + System.currentTimeMillis();
    private static final String VALID_PASSWORD = "Qwerty123!";
    private static final String VALID_FIRST_NAME = "Иван";
    private static String createdCourierId; // Variable to store the created courier ID

    @Test
    @Story("Успешное создание курьера")
    public void testSuccessfulCourierCreation() {
        Response response = createCourier(VALID_LOGIN, VALID_PASSWORD, VALID_FIRST_NAME);

        response.then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("ok", equalTo(true));
    }

    @Test
    @Story("Создание курьера с существующим логином")
    public void testDuplicateLogin() {
        createCourier(VALID_LOGIN, VALID_PASSWORD, VALID_FIRST_NAME);

        Response response = createCourier(VALID_LOGIN, VALID_PASSWORD, VALID_FIRST_NAME);

        response.then()
                .statusCode(409)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Этот логин уже используется."));
    }

    @Test
    @Story("Создание курьера Без login")
    public void testMissingLogin() {
        VALID_LOGIN = "testCourier" + System.currentTimeMillis();
        Response response = createCourier("", VALID_PASSWORD, VALID_FIRST_NAME);
        checkErrorResponse(response);

    }

    @Test
    @Story("Создание курьера Без password")
    public void testMissingPassword() {
        VALID_LOGIN = "testCourier" + System.currentTimeMillis();
        Response response = createCourier(VALID_LOGIN, "", VALID_FIRST_NAME);
        checkErrorResponse(response);
    }

    @Test
    @Story("Создание курьера без firstName")
    public void testMissingFirstName() {
        VALID_LOGIN = "testCourier" + System.currentTimeMillis();
        Response response = createCourier(VALID_LOGIN, VALID_PASSWORD, "");
        checkErrorResponse(response);
    }

    @Test
    @Story("Создание курьера без всех полей")
    public void testWithoutData() {
        VALID_LOGIN = "testCourier" + System.currentTimeMillis();
        Response response = createCourier("", "", "");
        checkErrorResponse(response);
    }

    @Test
    @Story("Получение ID курьера после логина")
    public void testGetCourierIdAfterLogin() {
        // Log in the courier
        Response loginResponse = loginCourier(VALID_LOGIN, VALID_PASSWORD);

        // Verify successful login
        loginResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", notNullValue());

        // Capture the courier ID
        createdCourierId = loginResponse.jsonPath().getString("id");
    }

    @Test
    @Story("Удаление курьера")
    public void ztestDeleteCourier() {
        // Check if courier ID is available
        if (createdCourierId == null) {
            throw new IllegalStateException("Courier ID is null. Ensure successful login test was executed.");
        }

        // Delete the courier
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + "/" + createdCourierId);

        // Verify successful deletion
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("ok", equalTo(true));

        System.out.println("Курьер с ID " + createdCourierId + " успешно удален.");
    }

    private Response createCourier(String login, String password, String firstName) {
        Map<String, Object> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);
        body.put("firstName", firstName);

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_URL);
    }

    private void checkErrorResponse(Response response) {
        response.then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    // Method to log in a courier
    private Response loginCourier(String login, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);

        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(LOGIN_URL);
    }
}
