import io.github.bonigarcia.wdm.WebDriverManager;
import org.bouncycastle.jcajce.provider.asymmetric.EC;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasePage extends Pages{


    public BasePage()
    {
        super();
    }

    @Override
    public WebDriver getDriver()
    {
        return driver;
    }

    public String userName = "Maya";
    public String password = "Maya111";
    public String email = "maya@gmail.com";

    public BasePage(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
    public String getUserName()
    {
        return userName;
    }
    public String getPassword()
    {
        return password;
    }
    public String getEmail()
    {
        return email;
    }
    private final String url = "https://lennertamas.github.io/blondesite";
    public void navigate()
    {
        driver.navigate().to(url);
    }


    //TODO Register User

    private final By REGISTER_PAGE = By.id("register-form-button");
    public void clickRegisterPage()
    {
        driver.findElement(REGISTER_PAGE).click();
    }

    private final By REGISTER_USERNAME = By.id("register-username");
    public void setUserName(String userName)
    {
        driver.findElement(REGISTER_USERNAME).sendKeys(userName);
    }

    private final By REGISTER_PASSWORD = By.id("register-password");
    public void setPassword(String password)
    {
        driver.findElement(REGISTER_PASSWORD).sendKeys(password);
    }

    private final By REGISTER_EMAIL = By.id("register-email");
    public void setEmail(String email)
    {
        driver.findElement(REGISTER_EMAIL).sendKeys(email);
    }


    //TODO Login

    private final By LOGIN_PAGE = By.xpath("//div[@id='register']/button[@id='login-form-button']");
    public void clickLoginPage()
    {
        driver.findElement(LOGIN_PAGE).click();
    }

    private final By LOGIN_USERNAME = By.xpath("//div[@id='login']//input[@id='email']");
    public void typeUserName(String userName)
    {
        driver.findElement(LOGIN_USERNAME).sendKeys(userName);
    }

    private final By LOGIN_PASSWORD = By.id("password");
    public void typePassword(String password)
    {
        driver.findElement(LOGIN_PASSWORD).sendKeys(password);
    }


    //TODO click Archives

    private final By archive = By.xpath("//a[contains(@href,'2019')]");

    public void clickArchives()
    {

        Actions actions = new Actions(getDriver());
        actions.moveToElement(driver.findElement(archive),2,2).click().perform();
    }


    //TODO go to MarkDownPage

    private final By BUTTON_MARKDOWN = By.xpath("//div[@class=\"col-span-3 lg:col-span-2 mb-3\"]/a[contains(@href,'markdown')]");
    public MarkDownPage goToMarkDown()
    {
        driver.findElement(BUTTON_MARKDOWN).click();
        return new MarkDownPage(getDriver());
    }






}
