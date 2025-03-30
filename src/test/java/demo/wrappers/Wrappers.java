package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v123.storage.model.InterestGroupAccessType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;




public class Wrappers {

    static ArrayList<Oscar> oscar = new ArrayList<>();
    static ArrayList<Team> teamData = new ArrayList<>();
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void click(WebElement elementToClick, WebDriver driver) {
        if (elementToClick.isDisplayed()) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("var rect = arguments[0].getBoundingClientRect();" +
                    "window.scrollTo({ top: rect.top + window.pageYOffset - (window.innerHeight / 2), behavior: 'smooth' });",
                    elementToClick);
            elementToClick.click();
        }
    }

    public static void sendKeys(WebDriver driver, WebElement inputBox, String keysToSend) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("var rect = arguments[0].getBoundingClientRect();" +
                "window.scrollTo({ top: rect.top + window.pageYOffset - (window.innerHeight / 2), behavior: 'smooth' });",
                inputBox);
        inputBox.clear();
        inputBox.sendKeys(keysToSend);
    }

    public static void navigate(WebDriver driver, String url) {
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
        }
    }

    public static void selectWebData(WebDriver driver, List<WebElement> list, String webTable){
        for(WebElement element : list){
            if(element.getText().contains(webTable)){
                Wrappers.click(element, driver);
                break;
            }
        }
    }

    public static void selectTableData(WebDriver driver, List<WebElement> list, int pages) throws InterruptedException{
        
        for(int i = 1 ; i <= pages ; i++){
            WebElement nextPage = driver.findElement(By.xpath("//a[@aria-label='Next']"));
            System.out.println("Page: "+ i);
            for(WebElement element : list){
                double winPct = Double.parseDouble(element.getText());
                WebElement nameElement = element.findElement(By.xpath("preceding-sibling::td[contains(@class,'name')]"));
                String name = nameElement.getText();
                WebElement yearElement = element.findElement(By.xpath("preceding-sibling::td[contains(@class,'year')]"));            
                int year = Integer.parseInt(yearElement.getText());
                long epoch = System.currentTimeMillis()/1000;
                teamData.add(new Team(epoch, name, winPct, year));
            }   
            Wrappers.click(nextPage, driver);
            Thread.sleep(2000);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            list = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//td[contains(@class,'pct') and number(text()) > 0.40 ]"))); 
           
        }

        String json = gson.toJson(teamData);

        // Write JSON to a file
        try (FileWriter writer = new FileWriter("hockey-team-data.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void selectYear(List<WebElement> list, WebDriver driver, int limit) throws InterruptedException{

        boolean isBestPicture = false;

        for(WebElement element : list){
            Wrappers.click(element, driver);
            Thread.sleep(3000);
            int year = Integer.parseInt(element.getText().trim());
            List<WebElement> titles = driver.findElements(By.xpath("//td[@class='film-title']"));
            int topFive = limit;
            for(WebElement filmTitle : titles){
                String title = filmTitle.getText();
                WebElement nominationsElement = filmTitle.findElement(By.xpath("following-sibling::td[contains(@class,'nominations')]"));
                int nominations = Integer.parseInt(nominationsElement.getText().trim());
                WebElement awardsElement = filmTitle.findElement(By.xpath("following-sibling::td[contains(@class,'awards')]"));
                int awards = Integer.parseInt(awardsElement.getText().trim());
                try{
                    WebElement bestPictureElement = filmTitle.findElement(By.xpath("following-sibling::td[contains(@class,'best')]/i"));
                    isBestPicture = bestPictureElement.isDisplayed();
                }
                catch(Exception e){
                    isBestPicture = false;
                }
                
                long epoch = System.currentTimeMillis()/1000;
                oscar.add(new Oscar(epoch, title, year, nominations, awards, isBestPicture));

                topFive--;
                if(topFive==0) break;
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            titles = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//td[@class='film-title']")));
        }

        String json = gson.toJson(oscar);

        // Write JSON to a file
        try (FileWriter writer = new FileWriter("oscar-winner-data.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void verifyFilePresenceAndContent(String filePath){
        File file = new File(filePath);
        Assert.assertTrue(file.exists());
        Assert.assertTrue(file.length()>0);
    }

   

   
    public static WebElement findElementWithRetry(WebDriver driver, By by, int retryCount) {
        return driver.findElement(by);
    }

    public static String capture(WebDriver driver) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(
                System.getProperty("user.dir") + File.separator + "reports" + System.currentTimeMillis() + ".png");
        String errflPath = dest.getAbsolutePath();
       // FileUtils.copyFile(srcFile, dest);
        return errflPath;
    }
}
