# QA Automation Case Study

Bu proje  "Junior QA Engineer" pozisyonu için hazırlanan teknik değerlendirme çalışmasıdır. Proje, **Page Object Model (POM)** tasarım deseni kullanılarak **Java** ve **Selenium WebDriver** ile geliştirilmiştir.

## 🛠 Teknoloji Yığını (Tech Stack)
* **Dil:** Java 21
* **Otomasyon:** Selenium WebDriver (4.36.0)
* **Test Framework:** TestNG
* **Raporlama:** ExtentReports 5
* **Loglama:** Log4j2
* **Veri İşleme:** OpenCSV (CSV Çıktısı), XChart (Grafik Oluşturma)
* **Build Tool:** Maven

### Ön Koşullar
* Java JDK 17 veya üzeri (Önerilen: JDK 21)
* Maven

🧪 Test Senaryoları (Test Cases)
✅ Case 1: Temel Uçuş Araması ve Filtreleme
Amaç: Temel arama fonksiyonunun ve saat filtrelerinin doğruluğunu test etmek.

Adımlar: İstanbul-Ankara araması yapılır, 10:00-18:00 saat filtresi uygulanır ve sonuçların bu aralıkta olduğu doğrulanır.

✅ Case 2: THY Filtresi ve Fiyat Sıralaması
Amaç: Havayolu filtresi ve fiyat sıralama mantığının kontrolü.

Adımlar: Türk Hava Yolları seçilir, gelen uçuşların sadece THY olduğu ve fiyatların ucuzdan pahalıya (Ascending) sıralandığı doğrulanır.

✅ Case 3: Kritik Yol Testi (Critical Path)
Senaryo: Misafir Kullanıcı Tek Yön Uçuş Rezervasyonu -> Ödeme Sayfası Geçişi.

Neden Bu Yol Seçildi? (Identification): Bir e-ticaret sitesinin en kritik fonksiyonu, kullanıcının ürünü bulup ödeme aşamasına geçebilmesidir ("Sales Funnel"). Bu akışın bozulması doğrudan gelir kaybına neden olur. Tek yön seçimi, testin daha hızlı ve stabil çalışmasını sağlamak için tercih edilmiştir.

Doğrulama: "Seç" butonuna basıldıktan sonra kullanıcının başarıyla "Ödeme/Yolcu Bilgileri" sayfasına yönlendirildiği URL (reservation veya checkout) ve sayfa elementleri ile doğrulanır.

✅ Case 4: Veri Analizi ve Görselleştirme
Amaç: Veri kazıma (Scraping) ve işleme yeteneğinin gösterilmesi.

İşlem: İstanbul-Lefkoşa uçuşları taranır.

Çıktılar (Proje klasöründeki reports/ altına kaydedilir):

📄 flights_report.csv: Tüm uçuş verilerinin Excel/CSV formatı.

📊 PriceStatsGraph.png: Havayolu bazlı Min/Max/Ortalama fiyat grafiği.

🔥 PriceHeatMap.png: Saatlere göre fiyat dağılım haritası.



📊 Raporlar ve Loglar
Testler tamamlandığında proje ana dizininde otomatik olarak reports klasörü oluşur:

Test-Report.html: Detaylı, görsel HTML test raporu (ExtentReports).

*.png: Oluşturulan analiz grafikleri.

*.csv: Çekilen uçuş verileri.

Screenshots: Hatalı biten testlerin ekran görüntüleri screenshots/ klasörüne otomatik kaydedilir.

Loglar: Tüm test adımları konsola ve log dosyalarına Log4j2 ile detaylı olarak yazılır.

## 🚀 Kurulum ve Çalıştırma

Projeyi IDE'nizde açın.

Proje ana dizinindeki testng.xml dosyasına sağ tıklayın.

"Run '...\testng.xml'" seçeneğine tıklayın.

### Projeyi Klonlama
```bash
git clone [https://github.com/Cemal-igrek/TestCase.git]