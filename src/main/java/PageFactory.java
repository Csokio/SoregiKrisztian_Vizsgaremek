import org.openqa.selenium.WebDriver;

public class PageFactory {

    public Pages makePage(PageType page)
    {
        switch(page)
        {
            case BASE:
                return new BasePage();
            default:
                return null;
        }
    }
}
