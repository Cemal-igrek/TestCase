package tests;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

import java.time.Duration;

public class ThirdCase extends BaseTest {

    @Test(description = "Case 3: Critical User Path (Search -> Select -> Reservation)")
    public void verifyCriticalBookingPath() {
        System.out.println("🚀 CASE 3 BAŞLIYOR...");

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

        System.out.println("⏳ Ödeme sayfası kontrol ediliyor...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            wait.until(ExpectedConditions.urlContains("rezervasyon"));
            System.out.println("✅ Ödeme sayfasına başarıyla gidildi! URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            Assert.fail("Ödeme sayfasına gidilemedi! Mevcut URL: " + driver.getCurrentUrl());
        }
    }
}