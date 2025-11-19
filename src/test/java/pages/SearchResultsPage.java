package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- LOCATORS (Bulduğun koddan entegre edildi) ---

    // 1. Yükleme Ekranı
    private By loadingScreen = By.id("SearchRootLoading");
    private By filterBlocker = By.cssSelector(".filter-disabled");

    // 2. Filtre Başlığı (Bulduğun koddaki XPath)
    // "Gidiş kalkış / varış saatleri" kutusu
    private By departureTimeFilterDropdown = By.xpath("//div[contains(@class, 'ctx-filter-departure-return-time') and contains(@class, 'card-header')]");

    // 3. Slider Tutacakları (Bulduğun koddan)
    // Not: (..)[1] kullanıyoruz çünkü sayfada Dönüş slider'ı da var, ilki Gidiş'tir.
    private By leftSliderHandle = By.xpath("(//div[contains(@class, 'rc-slider-handle-1')])[1]");
    private By rightSliderHandle = By.xpath("(//div[contains(@class, 'rc-slider-handle-2')])[1]");

    // 4. Uçuş Kartları ve Saatler
    private By flightCard = By.cssSelector(".flight-item");
    private By flightsDepartureTimes = By.cssSelector(".flight-departure-time"); // Saat text class'ı
    private By airlineFilterHeader = By.cssSelector(".ctx-filter-airline.card-header");

    // THY Seçeneği (Label olarak tıklıyoruz)
    private By thyCheckboxLabel =  By.xpath("//span[contains(text(),'Türk Hava Yolları')]");

    // Uçuş Kartındaki Fiyat Bilgisi
    private By flightPrice = By.cssSelector(".flight-price span.money-int"); // Class ismi değişebilir, kontrol etmelisin.

    // Uçuş Kartındaki Havayolu İsmi
    private By flightAirlineName = By.cssSelector(".summary-marketing-airlines"); // Havayolu ismi class'ı
    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // --- METODLAR ---

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
    public void filterTHY(){
        System.out.println("🔍 Havayolu filtresi açılıyor...");

        // 1. Başlığı bul ve tıkla (Accordion aç)
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(airlineFilterHeader));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", header);
        sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", header);

        // 2. THY seçeneğini bul ve tıkla
        System.out.println("✈️ Türk Hava Yolları seçiliyor...");
        WebElement thyOption = wait.until(ExpectedConditions.elementToBeClickable(thyCheckboxLabel));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", thyOption);

        // Sonuçların filtrelenmesini bekle (Loader çıkıp kaybolabilir)
        sleep(3000);
    }
    public boolean checkTHY(){
        System.out.println("🛡️ Havayolları kontrol ediliyor...");

        // Kartların güncellenmesini bekle
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(flightCard, 0));
        List<WebElement> airlineNames = driver.findElements(flightAirlineName);

        for (WebElement airline : airlineNames) {
            String name = airline.getText().toLowerCase();
            if (!name.contains("türk hava yolları") && !name.contains("turkish airlines") && !name.contains("anadolujet")) {
                // AnadoluJet de THY sayılabilir, case'e göre karar ver. Genelde THY filtreleyince AJet de gelir.
                System.out.println("HATA: Listede farklı havayolu var -> " + name);
                return false;
            }
        }
        return true;
    }
    public boolean checkPricesAreSortedTHY(){
        System.out.println("💰 Fiyat sıralaması kontrol ediliyor...");

        List<WebElement> priceElements = driver.findElements(flightPrice);
        if (priceElements.size() < 2) return true; // Tek uçuş varsa zaten sıralıdır.

        double previousPrice = 0;

        for (WebElement priceEl : priceElements) {
            // Fiyat metnini sayıya çevir (Örn: "1.250 TL" -> 1250.0)
            String priceText = priceEl.getText().replace(".", "").replace(",", ".").replaceAll("[^0-9.]", "");
            double currentPrice = Double.parseDouble(priceText);

            System.out.println("Fiyat: " + currentPrice);

            if (currentPrice < previousPrice) {
                System.out.println("HATA: Sıralama bozuk! " + previousPrice + " -> " + currentPrice);
                return false;
            }
            previousPrice = currentPrice;
        }
        return true;
    }

    public void filterDepartureTime(int startOffset, int endOffset) {
        System.out.println("🔍 Filtre başlığı aranıyor...");
        WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(departureTimeFilterDropdown));

        // Scroll ve Tıklama
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", header);
        sleep(500);

        System.out.println("🖱️ Başlığa tıklanıyor...");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", header);

        // Sliderları Bekle
        System.out.println("⏳ Slider bekleniyor...");
        WebElement leftHandle = wait.until(ExpectedConditions.visibilityOfElementLocated(leftSliderHandle));
        WebElement rightHandle = wait.until(ExpectedConditions.visibilityOfElementLocated(rightSliderHandle));

        // Kaydırma (Actions)
        System.out.println("🎚️ Slider ayarlanıyor...");
        Actions actions = new Actions(driver);

        // Solu kaydır
        actions.clickAndHold(leftHandle).moveByOffset(startOffset, 0).release().perform();
        sleep(1000);

        // Sağı kaydır
        actions.clickAndHold(rightHandle).moveByOffset(endOffset, 0).release().perform();

        sleep(2000); // Sonuçların filtrelenmesi için bekle
    }

    // Bulduğun koddan uyarlanan harika doğrulama metodu
    public boolean areDepartureTimesInRange(int startHour, int endHour) {
        // Uçuşların yüklenmesini bekle
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(flightCard, 0));

        List<WebElement> timeElements = driver.findElements(flightsDepartureTimes);

        // Saat formatı (HH:mm)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.of(startHour, 0);
        LocalTime endTime = LocalTime.of(endHour, 0);

        System.out.println("Kontrol edilen uçuş sayısı: " + timeElements.size());

        for (WebElement timeEl : timeElements) {
            String timeText = timeEl.getText();
            if (timeText.isEmpty()) continue;

            // Gelen saati (String) LocalTime formatına çevir
            LocalTime flightTime = LocalTime.parse(timeText, formatter);

            System.out.println("Uçuş Saati: " + flightTime);

            // Eğer saat aralık dışındaysa FALSE döndür
            if (flightTime.isBefore(startTime) || flightTime.isAfter(endTime)) {
                System.out.println("HATALI SAAT BULUNDU: " + flightTime);
                return false;
            }
        }
        return true; // Hepsi doğruysa TRUE döner
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }
}