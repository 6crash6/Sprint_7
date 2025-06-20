package baseconfig;

public class URL {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    public static String getBaseUri() {
        if (System.getProperty("host") != null) {
            return System.getProperty("host");
        } else {
            return BASE_URI;
        }
    }
}
