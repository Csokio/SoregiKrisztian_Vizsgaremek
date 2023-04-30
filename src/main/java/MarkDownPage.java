import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v108.domsnapshot.model.StringIndex;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.util.*;

public class MarkDownPage extends Pages {

    public MarkDownPage(WebDriver driver) {

        Pages.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    //TODO get Table Data
    public final By TABLE_ROW = By.xpath("//h2[@id='tables']//following-sibling::table[1]/tbody/tr");


    public HashMap<String, Integer> getPerson()
    {
        List<WebElement> rows = driver.findElements(TABLE_ROW);

        HashMap<String, Integer> personAge = new HashMap<>();
        for (WebElement row : rows) {
            String name = row.findElement(By.xpath("./td[1]")).getText();
            Integer age = Integer.parseInt(row.findElement(By.xpath("./td[2]")).getText());
            personAge.put(name, age);
        }

        return personAge;
    }

    public void writeMapToFile(String fileName)
    {
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);

            for (Map.Entry<String, Integer> entryMap : getPerson().entrySet()) {
                fileWriter.write("Name = " + entryMap.getKey() + ", Age = " + entryMap.getValue() + "\n");
            }
            fileWriter.close();
            System.out.println("File is written");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //TODO ReadFile method
    public static List<String> ReadFile(String fileName)
    {
        List<String> peopleList = new ArrayList<>();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                peopleList.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return peopleList;
    }

    //TODO go to Math TypeSetting Page

    private final By PREVIOUS_CONTENT = By.xpath("//a[@class='previous lg:text-2xl']");
    private final By BOTTOM_OF_PAGE = By.xpath("//footer");


    public MathTypesettingPage goToMathTypeSettingPage()
    {
        String pageUrl = "";
        while (!pageUrl.equals("https://lennertamas.github.io/blondesite/post/emoji-support/")) {
            scrollIntoView(BOTTOM_OF_PAGE);
            waitVisibilityOfElement(BOTTOM_OF_PAGE);

            driver.findElement(PREVIOUS_CONTENT).click();
            pageUrl = driver.findElement(PREVIOUS_CONTENT).getAttribute("href");
        }
        return new MathTypesettingPage(getDriver());
    }

    //TODO Read and Compare JSON file

    private final By KEY_MAP = By.xpath("//div[@class='highlight'][2]//following-sibling::h4");
    private final By ORD_VALUES_MAP = By.xpath("//div[@class='highlight'][2]//following-sibling::ol/li");
    private final By UL_VALUES_MAP = By.xpath("//div[@class='highlight'][2]//following-sibling::ul[1]/li");
    private final By FRUITS = By.xpath("//div[@class='highlight'][2]//following-sibling::ul/li[1]/ul/li");
    private final By DAIRIES = By.xpath("//div[@class='highlight'][2]//following-sibling::ul/li[2]/ul/li");

    private final By FRUIT = By.xpath("(//div[@class='highlight'][2]//following-sibling::ul/li[1])[2][text()='Fruit']");
    private final By DAIRY = By.xpath("(//div[@class='highlight'][2]//following-sibling::ul/li)[5]");

    public void scrollIntoVIewFruits() throws InterruptedException
    {
        javaScriptExecute("window.scrollTo(50, 4200)");
        waitVisibilityOfElement(FRUITS);
        Thread.sleep(2000);
    }

    public HashMap<String, List<String>> getListTypes() throws InterruptedException
    {

        HashMap<String, List<String>> listHashMap = new HashMap<>();

        List<WebElement> keys = driver.findElements(KEY_MAP);
        List<WebElement> ordValues = driver.findElements(ORD_VALUES_MAP);
        List<WebElement> ulValues = driver.findElements(UL_VALUES_MAP);
        List<WebElement> fruits = driver.findElements(FRUITS);
        List<WebElement> dairies = driver.findElements(DAIRIES);

        String fruitOption = driver.findElement(FRUIT).getText();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 6; i++){
            stringBuilder.append(fruitOption.charAt(i));
        }
        String f = stringBuilder.toString();

        String dairyOption = driver.findElement(DAIRY).getText();
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < 6; i++){
            strBuilder.append(dairyOption.charAt(i));
        }
        String d = strBuilder.toString();

        int count = 1;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).getText();
            switch (count) {
                case 1:
                    List<String> ordList = new ArrayList<>();
                    for(WebElement value: ordValues){
                        ordList.add(value.getText());
                    }
                    count++;
                    listHashMap.put(key, ordList);
                    continue;
                case 2:
                    List<String> ulList = new ArrayList<>();
                    for(WebElement value: ulValues){
                        ulList.add(value.getText());
                    }
                    count++;
                    listHashMap.put(key, ulList);
                    continue;
                case 3:
                    HashMap<String, List<String>> subMap = new HashMap<>();
                    List<String> fruitsList = new ArrayList<>();
                    for (WebElement fruit : fruits) {
                        fruitsList.add(fruit.getText());
                    }
                    subMap.put(f, fruitsList);
                    List<String> dairiesList = new ArrayList<>();
                    for (WebElement dairy: dairies) {
                        dairiesList.add(dairy.getText());
                    }
                    subMap.put(d, dairiesList);
                    List<String> subList = new ArrayList<>();
                    subList.add(String.valueOf(subMap));
                    listHashMap.put(key, subList);
                    break;
                default:
                    return null;
            }
        }

        return listHashMap;
    }

}