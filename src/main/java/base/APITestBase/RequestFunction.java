package base.APITestBase;

public enum RequestFunction {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DEL("DEL");

    public final String key;

    RequestFunction(String key) {
        this.key = key;
    }
}
