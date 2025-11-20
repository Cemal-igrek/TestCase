package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ResultPage;
import pages.SearchResultsPage;
import utils.AnalyticsManager;
import utils.FlightData;

import java.util.List;

public class FourthCase extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FourthCase.class);

    @Test(description = "Case 4: Data Analysis & Visualization")
    public void analyzeFlightData() throws Exception {
        logger.info(" CASE 4 BAŞLIYOR: Veri Analizi ve Görselleştirme");

        ResultPage resultPage = new ResultPage(driver);
        resultPage.closeCookies();

        resultPage.enterOrigin("İstanbul");
        resultPage.enterDestination("Lefkoşa");

        resultPage.selectDepartureDate(28);

        resultPage.closeCloseHotels();
        resultPage.clickSearchButton();

        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        resultsPage.waitForPageLoad();

        List<FlightData> data = resultsPage.scrapeFlightData();
        Assert.assertFalse(data.isEmpty(), "HATA: Hiç veri çekilemedi!");
        logger.info(" Toplam " + data.size() + " uçuş verisi işleniyor...");

        AnalyticsManager.saveToCSV(data, "flights_report.csv");

        AnalyticsManager.createPriceAnalysisGraph(data);
        AnalyticsManager.createHeatMap(data);

        AnalyticsManager.findBestFlight(data);

        logger.info(" CASE 4 TAMAMLANDI! Raporlar 'reports' klasöründe oluşturuldu.");
    }
}