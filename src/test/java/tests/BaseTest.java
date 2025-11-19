package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.DriverFactory;

import java.time.Duration;

public class BaseTest {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        // 1. Driver'ı başlat
        DriverFactory.initializeDriver();
        driver = DriverFactory.getDriver();

        // 2. Siteye git (URL'i config dosyasından alıyoruz)
        driver.get(ConfigReader.getProperty("url"));

        // Sayfanın tam yüklenmesi için genel bir bekleme
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    @AfterMethod
    public void tearDown(){
        // Test bitince tarayıcıyı kapat
        DriverFactory.quitDriver();
    }

}