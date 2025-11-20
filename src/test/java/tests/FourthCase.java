package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.AnalyticsManager;
import utils.FlightData;

import java.util.List;

public class FourthCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FourthCase.class);

    @Test(description = "Case 4: Data Analysis & Visualization")
    public void analyzeFlightData() throws Exception {
        logger.info("🚀 CASE 4 BAŞLIYOR: Veri Analizi ve Görselleştirme");

        HomePage homePage = new HomePage(driver);
        homePage.closeCookies();

        homePage.enterOrigin("İstanbul");
        homePage.enterDestination("Lefkoşa");

        homePage.selectDepartureDate(28);

        homePage.closeCloseHotels();
        homePage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        List<FlightData> data = resultsPage.scrapeFlightData();
        Assert.assertFalse(data.isEmpty(), "HATA: Hiç veri çekilemedi!");
        logger.info("📊 Toplam " + data.size() + " uçuş verisi işleniyor...");

        AnalyticsManager.saveToCSV(data, "flights_report.csv");

        AnalyticsManager.createPriceAnalysisGraph(data);
        AnalyticsManager.createHeatMap(data);

        AnalyticsManager.findBestFlight(data);

        logger.info("✅ CASE 4 TAMAMLANDI! Raporlar 'reports' klasöründe oluşturuldu.");
    }
}