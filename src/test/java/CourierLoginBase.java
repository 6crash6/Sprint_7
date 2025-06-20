import com.github.javafaker.Faker;
import courier.couriarapi.CourierApiClient;
import courier.couriermodel.CourierCreationModel;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.apache.http.HttpStatus.SC_CREATED;

public class CourierLoginBase {
    String courierLoginRandom;
    CourierApiClient courierApiClient;
    private Integer courierId;
    StepCheck stepCheck;
    Faker faker;
    CourierCreationModel newCourier;
    Response courier;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        courierLoginRandom = "test_user" + faker.number().numberBetween(100, 100000);
        courierApiClient = new CourierApiClient();
        courierId = null;
        stepCheck = new StepCheck();
        //Создание курьера
        newCourier = new CourierCreationModel(courierLoginRandom, "1234", "saske");
        courier = courierApiClient.createNewCourier(newCourier);
        stepCheck.checkResponseStatusCode(courier, SC_CREATED);
        stepCheck.checkResponseOk(courier, true);
        courierId = courierApiClient.loginCourierForCleanUp(newCourier.getLogin(), newCourier.getPassword());
    }

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierApiClient.deleteCourier(courierId);
            courierId = null;
        }
        stepCheck.assertAll();
    }

    static Object[][] provideInvalidCourierLoginData() {
        return new Object[][]{
                {null, "1234", "Недостаточно данных для входа"}, // Логин null
                {"login", null, "Недостаточно данных для входа"}, // Пароль null
                {null, null, "Недостаточно данных для входа"}    // Логин и Пароль null
        };
    }
}
