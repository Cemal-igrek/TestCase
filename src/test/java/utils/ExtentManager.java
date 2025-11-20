package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.Locale;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("reports/Test-Report.html");
        }
        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        Locale.setDefault(Locale.ENGLISH);

        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Test Raporu");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Otomasyon Test Sonuçları");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Tester", "QA Engineer");
        extent.setSystemInfo("Browser", ConfigReader.getProperty("browser"));

        return extent;
    }
}