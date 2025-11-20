package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.AnalyticsManager;
import utils.FlightData;

import java.util.List;

public class FourthCase extends BaseTest {

    @Test(description = "Case 4: Data Analysis & Visualization")
    public void analyzeFlightData() throws Exception {
        System.out.println("🚀 CASE 4 BAŞLIYOR...");

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

        AnalyticsManager.saveToCSV(data, "flights_report.csv");

        AnalyticsManager.createPriceAnalysisGraph(data);
        AnalyticsManager.createHeatMap(data);

        AnalyticsManager.findBestFlight(data);

        System.out.println("✅ CASE 4 TAMAMLANDI! Raporlar proje klasöründe.");
    }
}