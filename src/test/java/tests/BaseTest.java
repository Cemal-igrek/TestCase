package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.DriverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setup() {
        logger.info(" Test başlatılıyor...");
        DriverFactory.initializeDriver();
        driver = DriverFactory.getDriver();

        String url = ConfigReader.getProperty("url");
        driver.get(url);
        logger.info("Siteye gidildi: " + url);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("TEST BAŞARISIZ OLDU! Ekran görüntüsü alınıyor...");
            takeScreenshot(result.getName());
        } else {
            logger.info(" Test başarıyla tamamlandı.");
        }

        DriverFactory.quitDriver();
        logger.info("Tarayıcı kapatıldı.");
    }

    public void takeScreenshot(String testName) {
        if (driver == null) return;

        File directory = new File("screenshots");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("screenshots/" + testName + "_" + timestamp + ".png");

        try {
            Files.copy(srcFile.toPath(), destFile.toPath());
            logger.info(" Ekran görüntüsü kaydedildi: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Ekran görüntüsü kaydedilemedi: " + e.getMessage());
        }
    }
}