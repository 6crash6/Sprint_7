import com.github.javafaker.Faker;
import courier.couriarapi.CourierApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.ThreadLocalRandom;

public abstract class CourierCreationBase {
    String courierLoginRandom;
    CourierApiClient courierApiClient;
    protected Integer courierId;
    StepCheck stepCheck;

    @BeforeEach
    public void setUp() {
        Faker faker = new Faker();
        courierLoginRandom = "test_user" + faker.number().numberBetween(100, 100000);
        courierApiClient = new CourierApiClient();
        stepCheck = new StepCheck();
        courierId = null;
    }

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierApiClient.deleteCourier(courierId);
            courierId = null;
        }
        stepCheck.assertAll();
    }
    static Object[][] provideInvalidCourierData() {
        return new Object[][]{
                {null, "1234", "saske"}, // Логин null
                {"login", null, "saske"}, // Пароль null
                {"test_user" + ThreadLocalRandom.current().nextInt(100, 100000), "1234", null}    // Имя null
        };
    }
}
