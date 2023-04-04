import org.openqa.selenium.WebDriver;

public class PageFactory {

    public Pages makePage(String word)
    {
        switch(word)
        {
            case "Base":
                return new BasePage();
            default:
                return null;
        }
    }
}
