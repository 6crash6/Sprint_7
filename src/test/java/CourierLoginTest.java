import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Авторизация курьера")
public class CourierLoginTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1/courier";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String VALID_PASSWORD = "Qwerty123!";
    private static final String VALID_FIRST_NAME = "Иван";
    private static String createdCourierId; // Variable to store the created courier ID
    private static Map<String, String> credentials; // Variable to store courier credentials

    // Helper method to create a unique courier and return its credentials
    private static Map<String, String> createUniqueCourier() {
        String uniqueLogin = "testCourier" + UUID.randomUUID().toString();
        Map<String, String> creds = new HashMap<>();
        creds.put("login", uniqueLogin);
        creds.put("password", VALID_PASSWORD);

        Map<String, Object> body = new HashMap<>();
        body.put("login", uniqueLogin);
        body.put("password", VALID_PASSWORD);
        body.put("firstName", VALID_FIRST_NAME);

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_URL);

        response.then().statusCode(201); // Проверяем, что создание прошло успешно

        return creds;
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

    @Test
    @Story("Успешная авторизация курьера")
    public void testSuccessfulCourierLogin() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response loginResponse = loginCourier(credentials.get("login"), credentials.get("password"));

        loginResponse.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", notNullValue());

        // Capture the courier ID
        createdCourierId = loginResponse.jsonPath().getString("id");
    }

    @Test
    @Story("Авторизация с неверным логином")
    public void testInvalidLogin() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response response = loginCourier("wrongLogin", credentials.get("password"));

        response.then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Story("Авторизация с неверным паролем")
    public void testInvalidPassword() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response response = loginCourier(credentials.get("login"), "wrongPassword");

        response.then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Story("Авторизация без логина")
    public void testMissingLogin() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response response = loginCourier("", VALID_PASSWORD);
        response.then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Story("Авторизация без пароля")
    public void testMissingPassword() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response response = loginCourier(credentials.get("login"), "");
        response.then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Story("Авторизация без всех полей")
    public void testWithoutData() {
        if (credentials == null) {
            credentials = createUniqueCourier();
        }

        Response response = loginCourier("", "");
        response.then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Story("Удаление курьера")
    public void testDeleteCourier() {
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

        // Verify that the courier is actually deleted
        Response checkResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/" + createdCourierId);

        // Expect that the courier is not found
        checkResponse.then()
                .statusCode(404);

        System.out.println("Курьер с ID " + createdCourierId + " не найден. Удаление подтверждено.");
    }

}
