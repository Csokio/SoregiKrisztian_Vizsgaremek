import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

public class MathTypesettingPage extends Pages{

    public MathTypesettingPage(WebDriver driver){
        //super();
        Pages.driver = driver;
    }

    public WebDriver getDriver()
    {
        return driver;
    }

    //TODO Navigate to and on Katex page
    private final By BUTTON_KATEX = By.xpath("//a[@href='https://katex.org/']");

    public void goToKatexPage()
    {
        driver.findElement(BUTTON_KATEX).click();
    }

    private final By BUTTON_DOCUMENTATION = By.xpath("//a[@href='docs/api.html']");
    private final By BUTTON_TRY = By.xpath("//a[@href='/#demo']");

    public void goToExercisePage()
    {
        driver.findElement(BUTTON_DOCUMENTATION).click();
        driver.findElement(BUTTON_TRY).click();
    }

    //TODO Select button options and value
    private final By BUTTON_SELECT = By.id("strict");

    public String[] selectStrict(String valueName)
    {
        Select select = new Select(driver.findElement(BUTTON_SELECT));
        select.selectByValue(valueName);

        List<WebElement> options = select.getOptions();

        String[] optionsArray = new String[options.size()];
        int count = 0;
        for(WebElement option: options){
            optionsArray[count++] = option.getText();
        }
        return optionsArray;
    }

    public String getSelectValue()
    {
        Select select = new Select(driver.findElement(BUTTON_SELECT));
        return select.getFirstSelectedOption().getText();
    }

    //TODO navigate back from DOCUMENTATION to BlondsSite page
    public boolean goBackToUrl(String url)
    {
        while(!driver.getCurrentUrl().equals(url)) {
            driver.navigate().back();
        }
        return driver.getCurrentUrl().equals(url);
        /*int count = 6;
        while(count > 0){
            getDriver().navigate().back();
            count--;
            if(count < 2){
                return true;
            }
        }
        return false;*/
    }

    //TODO change Katex Code in Textarea
    private final By TEXTAREA = By.id("demo-input");
    public void changeKatexCode(String fileName)
    {
        driver.findElement(TEXTAREA).clear();
        List<String> newTextareaText = MarkDownPage.ReadFile(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        for(String str : newTextareaText){
            stringBuilder.append(str);
        }
        String result = stringBuilder.toString();
        driver.findElement(TEXTAREA).sendKeys(result);
    }
}
