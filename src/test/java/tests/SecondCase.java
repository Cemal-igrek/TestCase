package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

public class SecondCase extends BaseTest {
    @Test
    public void filterForTHY(){
        System.out.println("TEST BAŞLIYOR: Ana sayfaya gidiliyor...");
        HomePage homePage = new HomePage(driver);
        homePage.closeCookies();

        homePage.enterOrigin("İstanbul");
        homePage.enterDestination("Ankara");
        homePage.clickRoundTrip();
        homePage.selectDepartureDate(25);
        homePage.selectReturnDate(28);
        homePage.closeCloseHotels();
        System.out.println("Arama yapılıyor...");
        homePage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        System.out.println("Saat filtresi uygulanıyor: 10:00 - 18:00");
        resultsPage.filterDepartureTime(100, -60);

        resultsPage.filterTHY();

        boolean isOnlyTHY = resultsPage.checkTHY();
        Assert.assertTrue(isOnlyTHY, "HATA: Listede Türk Hava Yolları dışındaki uçuşlar var!");

        boolean isSorted = resultsPage.checkPricesAreSortedTHY();
        Assert.assertTrue(isSorted, "HATA: Fiyatlar küçükten büyüğe sıralı değil!");

        System.out.println("✅ CASE 2 BAŞARIYLA TAMAMLANDI!");

    }

}
