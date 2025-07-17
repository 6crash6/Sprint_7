import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

public class StepCheck {

    private final SoftAssertions softAssertions;

    public StepCheck() {
        this.softAssertions = new SoftAssertions();
    }

    @Step("Проверка статус-кода")
    public void checkResponseStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        softAssertions.assertThat(actualStatusCode)
                .as("Статус-код не совпадает с ожидаемым")
                .isEqualTo(expectedStatusCode);
    }

    @Step("Проверка поля ok")
    public void checkResponseOk(Response response, boolean expectedOk) {
        boolean actualOk = response.jsonPath().getBoolean("ok");
        softAssertions.assertThat(actualOk)
                .as("\"ok\" не совпадает с ожидаемым значением")
                .isEqualTo(expectedOk);
    }

    @Step("Проверка поля {expectedField} на null")
    public void checkResponseFieldNotNull(Response response, String expectedField) {
        Object fieldValue = response.jsonPath().get(expectedField);
        softAssertions.assertThat(fieldValue)
                .as("Поле '%s' ожидается ненулевым", expectedField)
                .isNotNull();
    }

    @Step("Проверка ожидаемого значения {expectedValue} поля {fieldName}")
    public void checkResponseField(Response response, String fieldName, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldName);
        softAssertions.assertThat(actualValue)
                .as("Поле '%s' не совпадает с ожидаемым значением", fieldName)
                .isEqualTo(expectedValue);
    }

    @Step("Проверка наличия поля {expectedField}")
    public void checkResponseFieldExists(Response response, String expectedField) {
        boolean fieldExists = response.jsonPath().getMap("$").containsKey(expectedField);
        softAssertions.assertThat(fieldExists)
                .as("Поле '%s' отсутствует в ответе", expectedField)
                .isTrue();
    }

    @Step("Проверка поля {expectedField} на не пустое значение")
    public void checkResponseFieldNotEmpty(Response response, String expectedField) {
        String fieldValue = response.jsonPath().getString(expectedField);
        softAssertions.assertThat(fieldValue)
                .as("Поле '%s' не должно быть пустым или null", expectedField)
                .isNotEmpty();
    }

    @Step("Проверка массива {expectedArray} на не пустое значение")
    public void checkResponseArrayNotEmpty(Response response, String expectedArray) {
        Object[] array = response.jsonPath().getObject(expectedArray, Object[].class);
        softAssertions.assertThat(array)
                .as("Массив '%s' не должен быть пустым", expectedArray)
                .isNotEmpty();
    }

    @Step("Проверка массива {arrayPath} на наличие ключа {expectedField} и на непустое значение")
    public void checkFieldExistsInArray(Response response, String arrayPath, String expectedField) {
        Object[] fieldValues = response.jsonPath().getObject(arrayPath + "." + expectedField, Object[].class);
        softAssertions.assertThat(fieldValues)
                .as("Все значения поля '%s' в массиве '%s' должны быть ненулевыми", expectedField, arrayPath)
                .allMatch(value -> value != null, "Значения должны быть ненулевыми");
    }

    @Step("Проверка накопленных ассертов")
    public void assertAll() {
        softAssertions.assertAll();
    }
}
