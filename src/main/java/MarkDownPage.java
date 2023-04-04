import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.*;

public class MarkDownPage extends Pages{

    public MarkDownPage(WebDriver driver)
    {
        //super();
        Pages.driver = driver;
    }

    public WebDriver getDriver()
    {
        return driver;
    }

    //TODO get Table Data
    private final By TABLE_ROW = By.xpath("//h2[@id='tables']//following-sibling::table[1]/tbody/tr");

    public void scrollToTable()
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("window.scrollTo(20, 1600)");
        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(TABLE_ROW));
    }

    public HashMap<String, Integer> getPerson()
    {
        List<WebElement> rows = driver.findElements(TABLE_ROW);

        HashMap<String, Integer> personAge = new HashMap<>();
        for(WebElement row: rows){
            String name = row.findElement(By.xpath("./td[1]")).getText();
            Integer age = Integer.parseInt(row.findElement(By.xpath("./td[2]")).getText());
            personAge.put(name, age);
        }

        return personAge;
    }

    //TODO ReadFile method
    public static List<String> ReadFile(String fileName)
    {
        List<String> peopleList = new ArrayList<>();

        try
        {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                peopleList.add(scanner.nextLine());
            }
        } catch ( Exception e) {
            e.getMessage();
        }

        return peopleList;
    }

    //TODO go to Math TypeSetting Page

    private final By PREVIOUS_CONTENT = By.xpath("//a[@class='previous lg:text-2xl']");
    private final By BOTTOM_OF_PAGE = By.xpath("//footer");
    public void scrollToPreviousButton()
    {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView", driver.findElement(BOTTOM_OF_PAGE));
    }
    public MathTypesettingPage goToMathTypeSettingPage()
    {
        String pageUrl = "";
        while(!pageUrl.equals("https://lennertamas.github.io/blondesite/post/emoji-support/")){
            scrollToPreviousButton();

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOfElementLocated(BOTTOM_OF_PAGE));

            driver.findElement(PREVIOUS_CONTENT).click();
            pageUrl = driver.findElement(PREVIOUS_CONTENT).getAttribute("href");
        }
        return new MathTypesettingPage(getDriver());
    }
}
