import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Pages {

    protected static WebDriver driver;

    public Pages() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.setAcceptInsecureCerts(true);
            options.addArguments("ignore-certificate-errors");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-extensions");
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("start-maximized");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        }
    }

    public abstract WebDriver getDriver();

    protected static void javaScriptExecute(String functionName)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(functionName);
    }

    protected static void waitVisibilityOfElement(By xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
    }

    protected static void scrollIntoView(By xpath)
    {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView", driver.findElement(xpath));
    }

}
