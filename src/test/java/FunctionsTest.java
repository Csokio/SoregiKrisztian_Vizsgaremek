import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import org.testng.asserts.SoftAssert;

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

    }

    @Test
    @Order(2)
    public void testLogin() throws InterruptedException {
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        testRegister();

        Thread.sleep(1500);
        basePage.clickLoginPage();
        basePage.typeUserName(basePageRegData.getUserName());
        basePage.typePassword(basePageRegData.getPassword());
        Pages.javaScriptExecute("myFunction()");
    }



    @Test
    @Order(3)
    public void testTableData() throws InterruptedException {
        testLogin();
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

        basePage.clickArchives();
        Thread.sleep(1500);

        MarkDownPage markDownPage = basePage.goToMarkDown();
        Pages.scrollIntoView(markDownPage.TABLE_ROW);
        Thread.sleep(1500);

        markDownPage.writeMapToFile("mapPeople.txt");

        List<String> originalPeopleList = MarkDownPage.ReadFile("originalPeople.txt");
        List<String> mapPeopleList = MarkDownPage.ReadFile("mapPeople.txt");
        String[] originalPeopleArray = originalPeopleList.toArray(new String[0]);
        String[] mapPeopleArray = mapPeopleList.toArray(new String[0]);

        Assertions.assertArrayEquals(originalPeopleArray, mapPeopleArray);

    }

    @Test
    @Order(4)
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
        Assertions.assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    @Order(5)
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

        Assertions.assertEquals(expectedResult, mathTypesettingPage.getSelectValue());
    }

    @Test
    @Order(6)
    public void testChangeTextAndNavigateBack() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);
        String expectedUrl = Pages.driver.getCurrentUrl();

        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        mathTypesettingPage.changeKatexCode("katexCode.txt");
        Thread.sleep(1500);

        Assertions.assertTrue(mathTypesettingPage.goBackToUrl(expectedUrl));
    }

    @Test
    @Order(7)
    public void testGetListTypes() throws InterruptedException, IOException, ParseException
    {
        testLogin();
        SoftAssert softAssert = new SoftAssert();
        BasePage basePage = (BasePage) pageFactory.makePage(PageType.BASE);

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

        //softAssert.assertEquals(expectedResult, actualResult);

        List<String> expectedKeys = new ArrayList<>(expectedResult.keySet());
        //List<List<String>> expectedValues = new ArrayList<>(expectedResult.values());
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
       // List<List<String>> actualValues = new ArrayList<>(actualResult.values());
        List<List<String>> actualValues = new ArrayList<>();
        for (List<String> categoryValues : actualResult.values()) {
            List<String> letterValues = new ArrayList<>();
            for (String value : categoryValues) {
                String letterValue = value.replaceAll("[^a-zA-Z]", "");
                letterValues.add(letterValue);
            }
            actualValues.add(letterValues);
        }


        softAssert.assertEquals(expectedKeys, actualKeys);

        for (int i = 0; i < expectedValues.size(); i++) {
            List<String> expectedValueResult = expectedValues.get(i);
            List<String> actualValueResult = actualValues.get(i);
            softAssert.assertEquals(expectedValueResult, actualValueResult);
        }

        softAssert.assertAll();

    }



}
