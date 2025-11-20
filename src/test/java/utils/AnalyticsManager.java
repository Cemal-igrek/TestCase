package utils;

import com.opencsv.CSVWriter;
import org.knowm.xchart.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsManager {

    public static void saveToCSV(List<FlightData> flights, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(new String[]{"Airline", "Departure", "Arrival", "Duration", "Connection", "Price"});
            for (FlightData flight : flights) {
                writer.writeNext(flight.toStringArray());
            }
            System.out.println("📄 Veriler kaydedildi: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ CSV Hatası: " + e.getMessage());
        }
    }

    public static void createPriceAnalysisGraph(List<FlightData> flights) throws IOException {
        Map<String, List<Double>> pricesByAirline = flights.stream()
                .collect(Collectors.groupingBy(FlightData::getAirline,
                        Collectors.mapping(FlightData::getPrice, Collectors.toList())));

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                .title("Havayolu Fiyat Analizi").xAxisTitle("Havayolu").yAxisTitle("Fiyat (TL)").build();

        List<String> airlines = new ArrayList<>();
        List<Double> avgPrices = new ArrayList<>();
        List<Double> minPrices = new ArrayList<>();
        List<Double> maxPrices = new ArrayList<>();

        System.out.println("\n--- İSTATİSTİKLER ---");
        for (Map.Entry<String, List<Double>> entry : pricesByAirline.entrySet()) {
            String airline = entry.getKey();
            List<Double> prices = entry.getValue();

            double min = Collections.min(prices);
            double max = Collections.max(prices);
            double avg = prices.stream().mapToDouble(d -> d).average().orElse(0.0);

            System.out.printf("%s -> Min: %.0f, Max: %.0f, Avg: %.0f TL\n", airline, min, max, avg);

            airlines.add(airline);
            avgPrices.add(avg);
            minPrices.add(min);
            maxPrices.add(max);
        }

        chart.addSeries("Ortalama Fiyat", airlines, avgPrices);
        chart.addSeries("Minimum Fiyat", airlines, minPrices);

        BitmapEncoder.saveBitmap(chart, "PriceStatsGraph", BitmapEncoder.BitmapFormat.PNG);
        System.out.println("📊 Grafik kaydedildi: PriceStatsGraph.png");
    }

    public static void createHeatMap(List<FlightData> flights) throws IOException {

        XYChart chart = new XYChartBuilder().width(800).height(600).title("Saatlik Fiyat Dağılımı").xAxisTitle("Saat").yAxisTitle("Fiyat").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        for (String airline : flights.stream().map(FlightData::getAirline).distinct().collect(Collectors.toList())) {
            List<Double> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();

            for (FlightData f : flights) {
                if (f.getAirline().equals(airline)) {
                    String[] timeParts = f.getDepartureTime().split(":");
                    double hour = Double.parseDouble(timeParts[0]) + (Double.parseDouble(timeParts[1]) / 60.0);
                    xData.add(hour);
                    yData.add(f.getPrice());
                }
            }
            if (!xData.isEmpty()) {
                chart.addSeries(airline, xData, yData);
            }
        }
        BitmapEncoder.saveBitmap(chart, "PriceHeatMap", BitmapEncoder.BitmapFormat.PNG);
        System.out.println("🔥 Isı haritası (Scatter) kaydedildi: PriceHeatMap.png");
    }

    public static void findBestFlight(List<FlightData> flights) {
        FlightData best = flights.stream().min(Comparator.comparingDouble(FlightData::getPrice)).orElse(null);
        if (best != null) {
            System.out.println("\n🏆 EN FİYAT/PERFORMANS UÇUŞ: " +
                    best.getAirline() + " | Saat: " + best.getDepartureTime() + " | Fiyat: " + best.getPrice() + " TL");
        }
    }
}