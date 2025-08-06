package base.APITestBase;

public enum RequestInfo {
    Endpoint("endPoint"),
    Token("token"),
    ContentType("Content-Type"),
    Accept("Accept"),
    AcceptLanguage("Accept-Language"),
    Referer("Referer"),
    RequestBody("requestBody");

    public final String key;

    RequestInfo(String key) {
        this.key = key;
    }
}
