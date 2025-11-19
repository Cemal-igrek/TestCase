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
        homePage.closeCookies(); // Varsa kapatır

        homePage.enterOrigin("İstanbul");
        homePage.enterDestination("Ankara");
        homePage.clickRoundTrip();
        // İleri tarih seçimi (Bugünden sonraki günler olmalı)
        homePage.selectDepartureDate(25);
        homePage.selectReturnDate(28);
        homePage.closeCloseHotels();
        System.out.println("Arama yapılıyor...");
        homePage.clickSearchButton();

        // --- SONUÇ SAYFASI ---
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        System.out.println("Filtre uygulanıyor: 10:00 - 18:00");
        // Slider kaydırma (Piksel değerleri deneme yanılma ile bulunur)
        // Solu 100px sağa, Sağı 80px sola çek
        resultsPage.filterDepartureTime(100, -60);

        // --- DOĞRULAMA (Tek satırda kontrol) ---
        System.out.println("Saatler kontrol ediliyor...");

        // Sayfa sınıfındaki akıllı metod tüm saatleri kontrol eder
        boolean isSuccess = resultsPage.areDepartureTimesInRange(10, 18);

        Assert.assertTrue(isSuccess, "HATA! Bazı uçuşlar 10:00 - 18:00 aralığında değil!");

        System.out.println("TEST BAŞARIYLA TAMAMLANDI! Tüm uçuşlar istenen saat aralığında.");
    }
}