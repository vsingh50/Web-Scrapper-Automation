package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
   

    @Test(enabled = true)
    public void testCase01() throws InterruptedException{

        //Navigating to Data site
        String url = "https://www.scrapethissite.com/pages/";
        Wrappers.navigate(driver, url);
        System.out.println("Navigated to Data Site");

        //Selecting WebData to scrape
        List<WebElement> list = driver.findElements(By.xpath("//h3[@class='page-title']/a"));
        String webTable = "Hockey";
        Wrappers.selectWebData(driver, list, webTable);
        System.out.println("Selected Hockey Team Data");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@class='team']")));
        
        //Scrapping data according to win percentage
        List<WebElement> teamsWinPct = driver.findElements(By.xpath("//tr[@class='team']"));
        Wrappers.selectTableData(driver, teamsWinPct, 4);

        //Checking if data is properly retrieved
        String filePath = "D:\\singh98virendra-ME_QA_XSCRAPE_DATA\\hockey-team-data.json";
        Wrappers.verifyFilePresenceAndContent(filePath);
        System.out.println("Verified Hocket Team data presence");

    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException{

        //Navigating to Data site
        String url = "https://www.scrapethissite.com/pages/";
        Wrappers.navigate(driver, url);
        System.out.println("Navigated to Data Site");

        //Selecting WebData to scrape
        List<WebElement> list = driver.findElements(By.xpath("//h3[@class='page-title']/a"));
        String webTable = "Oscar";
        Wrappers.selectWebData(driver, list, webTable);
        System.out.println("Selected Oscar Movies Data");

        //waiting for years to be loaded.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='year-link']")));
        //Scrapping data according to year
        List<WebElement> years = driver.findElements(By.xpath("//a[@class='year-link']"));
        Wrappers.selectYear(years, driver, 5);

        //Checking if data is properly retrieved
        String filePath = "D:\\singh98virendra-ME_QA_XSCRAPE_DATA\\oscar-winner-data.json";
        Wrappers.verifyFilePresenceAndContent(filePath);
        System.out.println("Verified Oscar Winner data presence");


    }


    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}