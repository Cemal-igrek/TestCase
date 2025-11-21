package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

public class FirstCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FirstCase.class);

    @Test(description = "Case 1: Basic Flight Search and Time Filter")
    public void testBasicFlightSearchAndFilter() {
        logger.info("CASE 1 BAŞLIYOR: Temel Arama ve Saat Filtresi");

        HomePage homePage = new HomePage(driver);
        homePage.closeCookies();

        homePage.enterOrigin("İstanbul");
        homePage.enterDestination("Ankara");
        homePage.clickRoundTrip();
        homePage.selectDepartureDate(26);
        homePage.selectReturnDate(28);
        homePage.closeCloseHotels();

        logger.info("Arama butonuna basılıyor...");
        homePage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        logger.info("Filtre uygulanıyor: 10:00 - 18:00");
        resultsPage.filterDepartureTime(100, -60);

        logger.info("Saatler kontrol ediliyor...");
        boolean isSuccess = resultsPage.areDepartureTimesInRange(10, 18);

        Assert.assertTrue(isSuccess, "HATA! Bazı uçuşlar 10:00 - 18:00 aralığında değil!");

        logger.info("CASE 1 BAŞARIYLA TAMAMLANDI.");
    }
}
