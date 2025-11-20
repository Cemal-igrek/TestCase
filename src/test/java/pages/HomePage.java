package pages;

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


    private By flightTab = By.xpath("//div[contains(@class, 'flight-tab') or contains(text(), 'Uçak Bileti')]");

    private By originInput = By.cssSelector("input[data-testid='endesign-flight-origin-autosuggestion-input']");
    private By destinationInput = By.cssSelector("input[data-testid='endesign-flight-destination-autosuggestion-input']");


    private By firstSuggestionItem = By.cssSelector("[data-testid*='autosuggestion-custom-item']");

    private By departureDateLabel = By.cssSelector("div[data-testid='enuygun-homepage-flight-departureDate-label']");
    private By returnDateLabel = By.cssSelector("div[data-testid='enuygun-homepage-flight-returnDate-label']");
    private By searchButton = By.cssSelector("button[data-testid='enuygun-homepage-flight-submitButton']");

    private By roundTripLabel = By.xpath("//div[contains(text(),'Gidiş-dönüş')]");
    private By cookieAcceptButton = By.id("onetrust-accept-btn-handler");
    private By closeHotelRadio = By.xpath("//div[contains(text(),'Bu tarihler için otelleri de listele')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }


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
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(flightTab));
            tab.click();
        } catch (Exception e) {}
    }

    public void clickRoundTrip() {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(roundTripLabel));
            el.click();
        } catch (Exception e) {}
    }

    public void enterOrigin(String city) {
        selectCity(originInput, city);
    }

    public void enterDestination(String city) {
        selectCity(destinationInput, city);
    }

    private void selectCity(By inputLocator, String cityName) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        input.click();
        input.clear();
        input.sendKeys(cityName);

        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(firstSuggestionItem, 0));

            List<WebElement> suggestions = driver.findElements(firstSuggestionItem);
            if (!suggestions.isEmpty()) {
                suggestions.get(0).click();
            } else {
                input.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Liste açılmadı, Enter tuşu deneniyor: " + cityName);
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

    private void selectDayFromCalendar(int day) {
        try {
            By dayLocator = By.xpath("//button[@data-day='" + day + "']");
            WebElement dayBtn = wait.until(ExpectedConditions.elementToBeClickable(dayLocator));
            dayBtn.click();
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("❌ Tarih seçilemedi! Gün: " + day);
        }
    }

    public void clickSearchButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        btn.click();
    }

    public void closeCloseHotels() {
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(closeHotelRadio));
            el.click();
        } catch (Exception e) {}
    }
}