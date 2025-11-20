package utils;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsManager {

    private static final String REPORT_DIR = "reports/";
    private static final Logger logger = LogManager.getLogger(AnalyticsManager.class);

    private static void createReportDirectory() {
        File directory = new File(REPORT_DIR);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("📂 Rapor klasörü oluşturuldu: " + REPORT_DIR);
            }
        }
    }

    public static void saveToCSV(List<FlightData> flights, String fileName) {
        createReportDirectory();
        String filePath = REPORT_DIR + fileName;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(new String[]{"Airline", "Departure", "Arrival", "Duration", "Connection", "Price"});
            for (FlightData flight : flights) {
                writer.writeNext(flight.toStringArray());
            }
            logger.info("📄 Veriler CSV'ye kaydedildi: " + filePath);
        } catch (IOException e) {
            logger.error("❌ CSV Hatası: " + e.getMessage());
        }
    }

    public static void createPriceAnalysisGraph(List<FlightData> flights) throws IOException {
        createReportDirectory();

        Map<String, List<Double>> pricesByAirline = flights.stream()
                .collect(Collectors.groupingBy(FlightData::getAirline,
                        Collectors.mapping(FlightData::getPrice, Collectors.toList())));

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                .title("Havayolu Fiyat Analizi").xAxisTitle("Havayolu").yAxisTitle("Fiyat (TL)").build();

        List<String> airlines = new ArrayList<>();
        List<Double> avgPrices = new ArrayList<>();
        List<Double> minPrices = new ArrayList<>();

        for (Map.Entry<String, List<Double>> entry : pricesByAirline.entrySet()) {
            String airline = entry.getKey();
            List<Double> prices = entry.getValue();

            double min = Collections.min(prices);
            double avg = prices.stream().mapToDouble(d -> d).average().orElse(0.0);

            airlines.add(airline);
            avgPrices.add(avg);
            minPrices.add(min);
        }

        chart.addSeries("Ortalama Fiyat", airlines, avgPrices);
        chart.addSeries("Minimum Fiyat", airlines, minPrices);

        BitmapEncoder.saveBitmap(chart, REPORT_DIR + "PriceStatsGraph", BitmapEncoder.BitmapFormat.PNG);
        logger.info("📊 Grafik kaydedildi: " + REPORT_DIR + "PriceStatsGraph.png");
    }

    public static void createHeatMap(List<FlightData> flights) throws IOException {
        createReportDirectory();

        XYChart chart = new XYChartBuilder().width(800).height(600)
                .title("Saatlik Fiyat Dağılımı").xAxisTitle("Saat").yAxisTitle("Fiyat").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        Set<String> airlines = flights.stream().map(FlightData::getAirline).collect(Collectors.toSet());

        for (String airline : airlines) {
            List<Double> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();

            for (FlightData f : flights) {
                if (f.getAirline().equals(airline)) {
                    try {
                        String[] timeParts = f.getDepartureTime().split(":");
                        double hour = Double.parseDouble(timeParts[0]) + (Double.parseDouble(timeParts[1]) / 60.0);
                        xData.add(hour);
                        yData.add(f.getPrice());
                    } catch (Exception e) {
                    }
                }
            }
            if (!xData.isEmpty()) {
                chart.addSeries(airline, xData, yData);
            }
        }

        BitmapEncoder.saveBitmap(chart, REPORT_DIR + "PriceHeatMap", BitmapEncoder.BitmapFormat.PNG);
        logger.info("🔥 Isı haritası kaydedildi: " + REPORT_DIR + "PriceHeatMap.png");
    }

    public static void findBestFlight(List<FlightData> flights) {
        FlightData best = flights.stream().min(Comparator.comparingDouble(FlightData::getPrice)).orElse(null);
        if (best != null) {
            logger.info("🏆 EN FİYAT/PERFORMANS UÇUŞ: " +
                    best.getAirline() + " | Saat: " + best.getDepartureTime() + " | Fiyat: " + best.getPrice() + " TL");
        }
    }
}