package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Logger Tanımlaması
    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // --- LOCATORS ---

    // 1. "Uçak Bileti" Sekmesi
    private By flightTab = By.xpath("//div[contains(@class, 'flight-tab') or contains(text(), 'Uçak Bileti')]");

    // 2. Nereden / Nereye Inputları
    private By originInput = By.cssSelector("input[data-testid='endesign-flight-origin-autosuggestion-input']");
    private By destinationInput = By.cssSelector("input[data-testid='endesign-flight-destination-autosuggestion-input']");

    // 3. Otomatik Tamamlama Listesi (İlk Sonuç)
    private By firstSuggestionItem = By.cssSelector("[data-testid*='autosuggestion-custom-item']");

    // 4. Tarih ve Butonlar
    private By departureDateLabel = By.cssSelector("div[data-testid='enuygun-homepage-flight-departureDate-label']");
    private By returnDateLabel = By.cssSelector("div[data-testid='enuygun-homepage-flight-returnDate-label']");
    private By searchButton = By.cssSelector("button[data-testid='enuygun-homepage-flight-submitButton']");

    // Checkbox & Cookie
    private By roundTripLabel = By.xpath("//div[contains(text(),'Gidiş-dönüş')]");
    private By cookieAcceptButton = By.id("onetrust-accept-btn-handler");
    private By closeHotelRadio = By.xpath("//div[contains(text(),'Bu tarihler için otelleri de listele')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // --- METODLAR ---

    public void closeCookies() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(cookieAcceptButton));
            btn.click();
            logger.info("✅ Çerez (Cookie) kapatıldı.");
        } catch (Exception e) {
            logger.warn("⚠️ Çerez çıkmadı veya zaten kapalı.");
        }
    }

    public void clickFlightTab() {
        try {
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(flightTab));
            tab.click();
            logger.info("✈️ Uçak bileti sekmesine tıklandı.");
        } catch (Exception e) {
            logger.debug("Uçak sekmesi zaten aktif olabilir.");
        }
    }

    public void clickRoundTrip() {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(roundTripLabel));
            el.click();
            logger.info("🔄 Gidiş-Dönüş seçeneği tıklandı.");
        } catch (Exception e) {
            logger.warn("⚠️ Gidiş-Dönüş seçilemedi.");
        }
    }

    public void enterOrigin(String city) {
        logger.info("🛫 Nereden: " + city + " giriliyor...");
        selectCity(originInput, city);
    }

    public void enterDestination(String city) {
        logger.info("🛬 Nereye: " + city + " giriliyor...");
        selectCity(destinationInput, city);
    }

    private void selectCity(By inputLocator, String cityName) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        input.click();
        input.clear();
        input.sendKeys(cityName);

        try {
            // Listenin açılmasını bekle
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(firstSuggestionItem, 0));

            List<WebElement> suggestions = driver.findElements(firstSuggestionItem);
            if (!suggestions.isEmpty()) {
                suggestions.get(0).click();
                logger.info("✅ Listeden ilk seçenek seçildi.");
            } else {
                input.sendKeys(Keys.ENTER);
                logger.info("ℹ️ Liste boş, Enter tuşuna basıldı.");
            }
        } catch (Exception e) {
            logger.warn("⚠️ Liste açılmadı, Enter tuşu deneniyor: " + cityName);
            input.sendKeys(Keys.ENTER);
        }
    }

    public void selectDepartureDate(int day) {
        logger.info("📅 Gidiş tarihi seçiliyor: Ayın " + day + "'i");
        wait.until(ExpectedConditions.elementToBeClickable(departureDateLabel)).click();
        selectDayFromCalendar(day);
    }

    public void selectReturnDate(int day) {
        logger.info("📅 Dönüş tarihi seçiliyor: Ayın " + day + "'i");
        wait.until(ExpectedConditions.elementToBeClickable(returnDateLabel)).click();
        selectDayFromCalendar(day);
    }

    private void selectDayFromCalendar(int day) {
        try {
            By dayLocator = By.xpath("//button[@data-day='" + day + "']");
            WebElement dayBtn = wait.until(ExpectedConditions.elementToBeClickable(dayLocator));
            dayBtn.click();
            // Animasyon beklemesi
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        } catch (Exception e) {
            logger.error("❌ Tarih seçilemedi! Gün: " + day + ". Hata: " + e.getMessage());
        }
    }

    public void clickSearchButton() {
        logger.info("🔎 Arama butonuna basılıyor...");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
    }

    public void closeCloseHotels() {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(closeHotelRadio));
            el.click();
            logger.info("🏨 Otel önerisi kapatıldı.");
        } catch (Exception e) {
            // Bu seçenek her zaman çıkmayabilir, hata değil.
            logger.debug("Otel önerisi çıkmadı.");
        }
    }
}