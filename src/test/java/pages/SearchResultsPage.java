package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.FlightData;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;


    private By loadingScreen = By.id("SearchRootLoading");
    private By filterBlocker = By.cssSelector(".filter-disabled");

    private By departureTimeFilterDropdown = By.xpath("//div[contains(@class, 'ctx-filter-departure-return-time') and contains(@class, 'card-header')]");
    private By airlineFilterHeader = By.cssSelector(".ctx-filter-airline.card-header");

    private By leftSliderHandle = By.xpath("(//div[contains(@class, 'rc-slider-handle-1')])[1]");
    private By rightSliderHandle = By.xpath("(//div[contains(@class, 'rc-slider-handle-2')])[1]");

    private By flightCard = By.cssSelector(".flight-item");
    private By flightsDepartureTimes = By.cssSelector(".flight-departure-time");
    private By flightsArrivalTimes = By.cssSelector(".flight-arrival-time");
    private By flightDuration = By.cssSelector("[data-testid='departureFlightTime']");

    private By flightPrice = By.cssSelector(".summary-average-price .money-int");
    private By flightAirlineName = By.cssSelector(".summary-marketing-airlines");

    private By thyCheckboxLabel = By.xpath("//span[contains(text(),'Türk Hava Yolları')]");


    private By allFlightItems = By.cssSelector(".flight-list-body .flight-item");

    private By selectButtonLocator = By.cssSelector(".action-select-btn");


    private By providerSelectButton = By.cssSelector("[data-testid='providerSelectBtn']");
    private By transferInfoLocator = By.cssSelector(".summary-transit");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }


    public void waitForPageLoad() {
        try {
            System.out.println("⏳ Loader bekleniyor...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingScreen));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(filterBlocker));
            System.out.println("✅ Sayfa yüklendi.");
        } catch (Exception e) {
            System.out.println("⚠️ Loader yakalanamadı.");
        }
    }

    // --- CASE 1 & 2 ---

    public void filterDepartureTime(int startOffset, int endOffset) {
        System.out.println("🔍 Filtre başlığı aranıyor...");
        WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(departureTimeFilterDropdown));

        scrollAndClick(header);

        System.out.println("⏳ Slider bekleniyor...");
        WebElement leftHandle = wait.until(ExpectedConditions.visibilityOfElementLocated(leftSliderHandle));
        WebElement rightHandle = wait.until(ExpectedConditions.visibilityOfElementLocated(rightSliderHandle));

        System.out.println("🎚️ Slider ayarlanıyor...");
        Actions actions = new Actions(driver);
        actions.clickAndHold(leftHandle).moveByOffset(startOffset, 0).release().perform();
        sleep(1000);
        actions.clickAndHold(rightHandle).moveByOffset(endOffset, 0).release().perform();

        sleep(2000);
    }

    public void filterTHY() {
        System.out.println("🔍 Havayolu filtresi açılıyor...");
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(airlineFilterHeader));
        scrollAndClick(header);

        System.out.println("✈️ Türk Hava Yolları seçiliyor...");
        WebElement thyOption = wait.until(ExpectedConditions.elementToBeClickable(thyCheckboxLabel));
        scrollAndClick(thyOption);

        sleep(3000);
    }

    public boolean checkTHY() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(flightCard, 0));
        List<WebElement> airlineNames = driver.findElements(flightAirlineName);

        for (WebElement airline : airlineNames) {
            String name = airline.getText().toLowerCase();
            if (!name.contains("türk hava yolları") && !name.contains("turkish airlines") && !name.contains("anadolujet")) {
                System.out.println("HATA: Listede farklı havayolu var -> " + name);
                return false;
            }
        }
        return true;
    }

    public boolean checkPricesAreSortedTHY() {
        List<WebElement> priceElements = driver.findElements(flightPrice);
        if (priceElements.size() < 2) return true;

        double previousPrice = 0;
        for (WebElement priceEl : priceElements) {
            String priceText = priceEl.getText().replace(".", "").replace(",", ".").replaceAll("[^0-9.]", "");
            double currentPrice = Double.parseDouble(priceText);

            if (currentPrice < previousPrice) {
                System.out.println("HATA: Sıralama bozuk! " + previousPrice + " -> " + currentPrice);
                return false;
            }
            previousPrice = currentPrice;
        }
        return true;
    }

    public boolean areDepartureTimesInRange(int startHour, int endHour) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(flightCard, 0));
        List<WebElement> timeElements = driver.findElements(flightsDepartureTimes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.of(startHour, 0);
        LocalTime endTime = LocalTime.of(endHour, 0);

        for (WebElement timeEl : timeElements) {
            String timeText = timeEl.getText();
            if (timeText.isEmpty()) continue;
            LocalTime flightTime = LocalTime.parse(timeText, formatter);

            if (flightTime.isBefore(startTime) || flightTime.isAfter(endTime)) {
                System.out.println("HATALI SAAT BULUNDU: " + flightTime);
                return false;
            }
        }
        return true;
    }

    // --- CASE 3 ---

    public void selectFirstFlight() {
        System.out.println("🎫 İlk uçuş seçiliyor...");

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(allFlightItems, 0));

            WebElement firstCard = driver.findElements(allFlightItems).get(0);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", firstCard);
            Thread.sleep(500);

            WebElement selectBtn = firstCard.findElement(selectButtonLocator);

            System.out.println("🖱️ 'Seç' butonuna tıklanıyor...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectBtn);

            System.out.println("⏳ 'Seç ve İlerle' butonu bekleniyor...");
            WebElement finalSelectBtn = wait.until(ExpectedConditions.elementToBeClickable(providerSelectButton));
            scrollAndClick(finalSelectBtn);
            System.out.println("✅ Uçuş seçimi tamamlandı.");

        } catch (Exception e) {
            System.out.println("❌ Uçuş seçilemedi: " + e.getMessage());
            Assert.fail("Uçuş seçimi başarısız!");
        }
    }

    // --- CASE 4 --

    public List<FlightData> scrapeFlightData() {
        System.out.println("📊 Uçuş verileri toplanıyor...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(flightCard, 0));

        List<WebElement> cards = driver.findElements(flightCard);
        List<FlightData> flightDataList = new ArrayList<>();

        System.out.println("Bulunan kart sayısı: " + cards.size());

        for (WebElement card : cards) {
            try {
                String airline = card.findElement(flightAirlineName).getText();
                String depTime = card.findElement(flightsDepartureTimes).getText();
                String arrTime = card.findElement(flightsArrivalTimes).getText();
                String duration = card.findElement(flightDuration).getText();
                String connection = card.findElement(transferInfoLocator).getText();

                String priceText = card.findElement(flightPrice).getText()
                        .replace(".", "")
                        .replace(",", ".")
                        .replaceAll("[^0-9.]", "");

                double price = Double.parseDouble(priceText);

                flightDataList.add(new FlightData(airline, depTime, arrTime, duration, connection, price));
                System.out.println("Veri çekildi: " + airline + " - " + price); // Her kart için log

            } catch (Exception e) {
                System.out.println("⚠️ Kart okunamadı: " + e.getMessage());
            }
        }
        System.out.println("✅ Toplam " + flightDataList.size() + " uçuş verisi çekildi.");
        return flightDataList;
    }


    private void scrollAndClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}