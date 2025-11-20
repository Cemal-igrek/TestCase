package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;

public class FirstCase extends BaseTest {

    @Test
    public void testBasicFlightSearchAndFilter() {
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

        System.out.println("Filtre uygulanıyor: 10:00 - 18:00");
        resultsPage.filterDepartureTime(100, -60);

        System.out.println("Saatler kontrol ediliyor...");

        boolean isSuccess = resultsPage.areDepartureTimesInRange(10, 18);

        Assert.assertTrue(isSuccess, "HATA! Bazı uçuşlar 10:00 - 18:00 aralığında değil!");

        System.out.println("TEST BAŞARIYLA TAMAMLANDI! Tüm uçuşlar istenen saat aralığında.");
    }
}