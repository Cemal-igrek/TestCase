package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            initializeDriver();
        }
        return driver.get();
    }

    public static void initializeDriver() {
        String browser = ConfigReader.getProperty("browser");
        if (browser == null) browser = "chrome";

        logger.info(" Tarayıcı başlatılıyor: " + browser);

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();

            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

            ChromeDriver chromeDriver = new ChromeDriver(options);

            Map<String, Object> params = new HashMap<>();
            params.put("source", "Object.defineProperty(navigator, 'webdriver', { get: () => undefined })");
            chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);

            driver.set(chromeDriver);

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--disable-notifications");
            driver.set(new FirefoxDriver(options));
        }

        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
        driver.get().manage().window().maximize();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info(" Tarayıcı kapatılıyor.");
            driver.get().quit();
            driver.remove();
        }
    }
}