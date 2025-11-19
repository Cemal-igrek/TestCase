package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS ---

    // 1. "Uçak Bileti" Sekmesi (Garanti olsun diye buna tıklayacağız)
    private By flightTab = By.xpath("//div[contains(@class, 'flight-tab') or contains(text(), 'Uçak Bileti')]");

    // 2. Nereden / Nereye Inputları
    private By originInput = By.cssSelector("input[data-testid='endesign-flight-origin-autosuggestion-input']");
    private By destinationInput = By.cssSelector("input[data-testid='endesign-flight-destination-autosuggestion-input']");

    // 3. Özel Seçim Maddeleri (Enter'a basmak yerine bunlara tıklayacağız)
    // İstanbul (Tüm Havalimanları)
    private By istanbulSuggestion = By.xpath("//div[contains(@data-testid, 'istanbul')][1]");
    // Ankara Esenboğa
    private By ankaraSuggestion = By.xpath("//div[contains(@data-testid, 'ankara')][1]");

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
            System.out.println("✅ Çerez kapatıldı.");
        } catch (Exception e) {
            System.out.println("⚠️ Çerez çıkmadı.");
        }
    }

    public void clickFlightTab() {
        try {
            // Sayfa açılınca Uçak sekmesine tıkla ki Otel'e gitmesin
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(flightTab));
            tab.click();
        } catch (Exception e) {
            // Zaten uçak sekmesindeysek hata vermesin
        }
    }

    public void clickRoundTrip() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(roundTripLabel));
        el.click();
    }

    public void enterOrigin(String city) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(originInput));
        input.click();
        input.clear();
        input.sendKeys(city);

        // ÖNEMLİ: Enter'a basma! Listeden İstanbul seçeneğinin çıkmasını bekle ve tıkla.
        try {
            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(istanbulSuggestion));
            suggestion.click();
        } catch (Exception e) {
            input.sendKeys(Keys.ENTER); // Bulamazsa Enter dene
        }
    }

    public void enterDestination(String city) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(destinationInput));
        input.click();
        input.sendKeys(city);

        // ÖNEMLİ: Ankara seçeneğine tıkla (Otel seçeneğine tıklamamak için)
        try {
            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(ankaraSuggestion));
            suggestion.click();
        } catch (Exception e) {
            input.sendKeys(Keys.ENTER);
        }
    }

    public void selectDepartureDate(int day) {
        wait.until(ExpectedConditions.elementToBeClickable(departureDateLabel)).click();
        selectDayFromCalendar(day);
    }

    public void selectReturnDate(int day) {
        wait.until(ExpectedConditions.elementToBeClickable(returnDateLabel)).click();
        selectDayFromCalendar(day);
    }

    // Ortak Takvim Seçici
    private void selectDayFromCalendar(int day) {
        // Takvimde o günü bul (Button yapısı)
        try {
            // HTML yapısına göre button[data-day='25'] gibi arıyoruz
            By dayLocator = By.xpath("//button[@data-day='" + day + "']");
            // Eğer görünmüyorsa bir sonraki aya tıkla mantığı eklenebilir ama şimdilik basit tutalım
            WebElement dayBtn = wait.until(ExpectedConditions.elementToBeClickable(dayLocator));
            dayBtn.click();
            Thread.sleep(500); // Animasyon beklemesi
        } catch (Exception e) {
            System.out.println("❌ Tarih seçilemedi! Gün: " + day);
        }
    }

    public void clickSearchButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
    }

    public void closeCloseHotels() {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(closeHotelRadio));
        el.click();
    }
}