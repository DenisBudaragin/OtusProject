package config;

public class TestConfig {
    private final String baseUrl;
    private final boolean headless;
    private final int timeout;
    private final int slowMo;

    public TestConfig(String baseUrl, boolean headless, int timeout, int slowMo) {
        this.baseUrl = baseUrl;
        this.headless = headless;
        this.timeout = timeout;
        this.slowMo = slowMo;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isHeadless() {
        return headless;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getSlowMo() {
        return slowMo;
    }
}
