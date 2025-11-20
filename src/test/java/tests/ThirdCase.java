package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

import java.time.Duration;

public class ThirdCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(ThirdCase.class);

    @Test(description = "Case 3: Critical User Path (Search -> Select -> Reservation)")
    public void verifyCriticalBookingPath() {
        logger.info("🚀 CASE 3 BAŞLIYOR: Kritik Yol Testi");

        HomePage homePage = new HomePage(driver);
        homePage.closeCookies();

        homePage.enterOrigin("İstanbul");
        homePage.enterDestination("Ankara");
        homePage.selectDepartureDate(30);
        homePage.closeCloseHotels();
        homePage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        resultsPage.selectFirstFlight();

        logger.info("⏳ Ödeme sayfası kontrol ediliyor...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("rezervasyon"),
                    ExpectedConditions.urlContains("checkout"),
                    ExpectedConditions.urlContains("odeme")
            ));
            logger.info("✅ Ödeme sayfasına başarıyla gidildi! URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            logger.error("❌ Ödeme sayfasına gidilemedi! Mevcut URL: " + driver.getCurrentUrl());
            Assert.fail("Ödeme sayfasına gidilemedi!");
        }

        logger.info("✅ CASE 3 BAŞARIYLA TAMAMLANDI!");
    }
}