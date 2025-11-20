package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ResultPage;
import pages.SearchResultsPage;

public class FirstCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FirstCase.class);

    @Test(description = "Case 1: Basic Flight Search and Time Filter")
    public void testBasicFlightSearchAndFilter() {
        logger.info("CASE 1 BAŞLIYOR: Temel Arama ve Saat Filtresi");

        ResultPage resultPage = new ResultPage(driver);
        resultPage.closeCookies();

        resultPage.enterOrigin("İstanbul");
        resultPage.enterDestination("Ankara");
        resultPage.clickRoundTrip();
        resultPage.selectDepartureDate(26);
        resultPage.selectReturnDate(28);
        resultPage.closeCloseHotels();

        logger.info("Arama butonuna basılıyor...");
        resultPage.clickSearchButton();

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
