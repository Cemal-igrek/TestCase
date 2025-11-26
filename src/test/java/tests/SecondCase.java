package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ResultPage;
import pages.SearchResultsPage;

public class SecondCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(SecondCase.class);

    @Test(description = "Case 2: THY Filter and Price Sort")
    public void filterForTHY() {
        logger.info(" CASE 2 BAŞLIYOR: THY ve Fiyat Sıralama Testi");

        ResultPage resultPage = new ResultPage(driver);
        resultPage.closeCookies();

        resultPage.enterOrigin("İstanbul");
        resultPage.enterDestination("Ankara");
        resultPage.clickRoundTrip();
        resultPage.selectDepartureDate(27);
        resultPage.selectReturnDate(28);
        resultPage.closeCloseHotels();

        logger.info("Arama yapılıyor...");
        resultPage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        logger.info("Saat filtresi uygulanıyor: 10:00 - 18:00");
        resultsPage.filterDepartureTime(100, -60);

        logger.info(" THY Filtresi uygulanıyor...");
        resultsPage.filterTHY();

        logger.info("Sadece THY mi kontrol ediliyor...");
        boolean isOnlyTHY = resultsPage.checkTHY();
        Assert.assertTrue(isOnlyTHY, "HATA: Listede Türk Hava Yolları dışındaki uçuşlar var!");

        logger.info("Fiyat sıralaması kontrol ediliyor...");
        boolean isSorted = resultsPage.checkPricesAreSortedTHY();
        Assert.assertTrue(isSorted, "HATA: Fiyatlar küçükten büyüğe sıralı değil!");

        logger.info("CASE 2 BAŞARIYLA TAMAMLANDI!");
    }
}