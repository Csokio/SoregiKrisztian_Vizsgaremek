import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.asserts.SoftAssert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


import java.io.*;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionsTest{


    PageFactory pageFactory = new PageFactory();
    BasePage basePageRegData = new BasePage("Maya", "maya11", "maya@gmail.com");


    @AfterAll
    public static void quitDriver()
    {
        Pages.driver.quit();
    }

    @Test
    @Order(1)
    @DisplayName("Register to Blondesite")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Register a new account to Blondesite page, " +
            "by providing username, email address and password")
    public void testRegister()
    {
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        basePage.navigate();
        try {
            Pages.javaScriptExecute("acceptTAndC()");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        basePage.clickRegisterPage();
        basePage.setUserName(basePageRegData.getUserName());
        basePage.setPassword(basePageRegData.getPassword());
        basePage.setEmail(basePageRegData.getEmail());
        Pages.javaScriptExecute("registerUser()");

        String expected = "User registered!";
        String actual = basePage.getSuccessRegisterMessage();

        Assertions.assertEquals(expected, actual, "User registered message appeared after registration");
    }

    @Test
    @Order(2)
    @DisplayName("Login to Blondesite")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login to Blondesite page, using appropriate username and password")
    public void testLogin() throws InterruptedException
    {
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        testRegister();

        Thread.sleep(1500);
        basePage.clickLoginPage();
        basePage.typeUserName(basePageRegData.getUserName());
        basePage.typePassword(basePageRegData.getPassword());
        Pages.javaScriptExecute("myFunction()");

        Assertions.assertTrue(basePage.isLogoutDisplayed(), "Logout button is displayed after user is logged in");
    }

    @Test
    @Order(3)
    @DisplayName("Logout from Blondesite")
    @Severity(SeverityLevel.CRITICAL)
    @Description("As a logged in user:\n     Click on Logout button")
    public void testLogOut() throws InterruptedException
    {
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        testLogin();

        Pages.javaScriptExecute("logout()");

        Assertions.assertTrue(basePage.isLoginDisplayed(), "Login button is displayed after user is logged out");
    }


    @Test
    @Order(4)
    @DisplayName("Comparing Table Data")
    @Severity(SeverityLevel.NORMAL)
    @Description("As a logged in user:\n    " +
            "Verifying that the table content equals with the expected file's content")
    public void testTableData() throws InterruptedException, IOException {
        testLogin();
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        basePage.clickArchives();
        Thread.sleep(1500);

        MarkDownPage markDownPage = basePage.goToMarkDown();
        Pages.scrollIntoView(markDownPage.TABLE_ROW);
        Thread.sleep(1500);

        markDownPage.writeMapToFile("mapPeople.txt");

        Path path = Paths.get("mapPeople.txt");
        String fileContent = Files.readString(path);
        Allure.addAttachment("mapPeople.txt", "text/plain", fileContent);

        List<String> originalPeopleList = MarkDownPage.ReadFile("originalPeople.txt");
        List<String> mapPeopleList = MarkDownPage.ReadFile("mapPeople.txt");
        String[] originalPeopleArray = originalPeopleList.toArray(new String[0]);
        String[] mapPeopleArray = mapPeopleList.toArray(new String[0]);

        Assertions.assertArrayEquals(originalPeopleArray, mapPeopleArray,"Table data equals with the file content");

    }

    @Test
    @Order(5)
    @DisplayName("Select Options")
    @Severity(SeverityLevel.NORMAL)
    @Description("As a logged in user:\n    " +
            "Verifying that the options in the select menu are equals with the expected ones")
    public void testKatexSelectButtonArray() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        String selectValue = "error";
        String[] actualResult = mathTypesettingPage.selectStrict(selectValue);
        String[] expectedResult = {"error", "warn", "ignore"};

        Arrays.sort(actualResult);
        Arrays.sort(expectedResult);
        Assertions.assertArrayEquals(expectedResult, actualResult,"Select options are appropriate");
    }

    @Test
    @Order(6)
    @DisplayName("Verify Selected Value")
    @Severity(SeverityLevel.NORMAL)
    @Description("As a logged in user:\n    " +
            "Verifying that the chosen select option is displayed")
    public void testKatexSelectButtonVisibleValue() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        String expectedResult = "ignore";

        mathTypesettingPage.selectStrict(expectedResult);
        String actualResult = mathTypesettingPage.getSelectValue();
        Allure.addAttachment("Selected Value", new ByteArrayInputStream(((TakesScreenshot) Pages.driver).getScreenshotAs(OutputType.BYTES)));

        Assertions.assertEquals(expectedResult, actualResult, "Selected option is displayed");
    }

    @Test
    @Order(7)
    @DisplayName("Change Text")
    @Severity(SeverityLevel.MINOR)
    @Description("As a logged in user:\n    " +
            "Change the content of the textarea based on the provided file and navigate back to index page")
    public void testChangeTextAndNavigateBack() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);
        String expectedUrl = Pages.driver.getCurrentUrl();

        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        Allure.addAttachment("Original Katex Code", new ByteArrayInputStream(((TakesScreenshot) Pages.driver).getScreenshotAs(OutputType.BYTES)));
        mathTypesettingPage.changeKatexCode("katexCode.txt");
        Allure.addAttachment("Updated Katex Code", new ByteArrayInputStream(((TakesScreenshot) Pages.driver).getScreenshotAs(OutputType.BYTES)));
        Thread.sleep(1500);

        Assertions.assertTrue(mathTypesettingPage.goBackToUrl(expectedUrl));
    }

    @Test
    @Order(8)
    @DisplayName("List Types")
    @Severity(SeverityLevel.NORMAL)
    @Description("As a logged in user:\n    " +
            "Check if the structure of the list types equals with the content of the expected json file")
    public void testGetListTypes() throws InterruptedException, IOException, ParseException
    {
        testLogin();
        SoftAssert softAssert = new SoftAssert();
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        Path path = Paths.get("expected.json");
        String fileContent = Files.readString(path);
        Allure.addAttachment("expected.json", "application/json", fileContent);

        MarkDownPage markDownPage = basePage.goToMarkDown();
        Thread.sleep(1500);
        markDownPage.scrollIntoVIewFruits();
        HashMap<String, List<String>> actualResult = markDownPage.getListTypes();


        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("expected.json"));
        JSONArray categories = (JSONArray) obj;

        Map<String, List<String>> expectedResult = new HashMap<>();
        for(Object category: categories){
            String key = (String) ((JSONObject) category).get("category");
            JSONArray items = (JSONArray) ((JSONObject)category).get("items");
            List<String> values = new ArrayList<>();
            for(Object item: items){
                String itemName = item.toString();
                values.add(itemName);
            }
            expectedResult.put(key, values);
        }


        List<String> expectedKeys = new ArrayList<>(expectedResult.keySet());
        List<List<String>> expectedValues = new ArrayList<>();
        for (List<String> categoryValues : expectedResult.values()) {
            List<String> letterValues = new ArrayList<>();
            for (String value : categoryValues) {
                String letterValue = value.replaceAll("[^a-zA-Z]", "");
                letterValues.add(letterValue);
            }
            expectedValues.add(letterValues);
        }

        List<String> actualKeys = new ArrayList<>(actualResult.keySet());
        List<List<String>> actualValues = new ArrayList<>();
        for (List<String> categoryValues : actualResult.values()) {
            List<String> letterValues = new ArrayList<>();
            for (String value : categoryValues) {
                String letterValue = value.replaceAll("[^a-zA-Z]", "");
                letterValues.add(letterValue);
            }
            actualValues.add(letterValues);
        }


        softAssert.assertEquals(expectedKeys, actualKeys, "Keys are equals with json file's content");

        for (int i = 0; i < expectedValues.size(); i++) {
            List<String> expectedValueResult = expectedValues.get(i);
            List<String> actualValueResult = actualValues.get(i);
            softAssert.assertEquals(expectedValueResult, actualValueResult, "Values are equals with json file's content");
        }

        softAssert.assertAll();

    }



}
