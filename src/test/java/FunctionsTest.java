import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class FunctionsTest{


    PageFactory pageFactory = new PageFactory();
    String userName = "Maya";
    String password = "Maya111";
    String email = "maya@gmail.com";

    @Test
    public void testRegister()
    {

        BasePage basePage = (BasePage) pageFactory.makePage( "Base");

        basePage.navigate();
        basePage.acceptTermCond();

        basePage.clickRegisterPage();
        basePage.setUserName(userName);
        basePage.setPassword(password);
        basePage.setEmail(email);
        basePage.registerUser();

    }

    @Test
    public void testLogin() throws InterruptedException {
        BasePage basePage = (BasePage) pageFactory.makePage("Base");

        basePage.navigate();
        basePage.acceptTermCond();
        testRegister();

        Thread.sleep(1500);
        basePage.clickLoginPage();
        basePage.typeUserName(userName);
        basePage.typePassword(password);
        basePage.loginUser();
    }



    @Test
    public void testTableData() throws InterruptedException {
        testLogin();
        BasePage basePage = (BasePage) pageFactory.makePage("Base");

        basePage.clickArchives();
        Thread.sleep(1500);


        MarkDownPage markDownPage = basePage.goToMarkDown();
        markDownPage.scrollToTable();
        Thread.sleep(1500);
        HashMap<String, Integer> personDataMap = markDownPage.getPerson();
        //System.out.println(markDownPage.getPerson());

        File file = new File("mapPeople.txt");
        try
        {
            FileWriter fileWriter = new FileWriter(file);

            for (Map.Entry<String, Integer> entryMap : personDataMap.entrySet()){
                fileWriter.write("Name = " + entryMap.getKey() + ", Age = " + entryMap.getValue() + "\n");
            }
            fileWriter.close();
            System.out.println("File is written");
        } catch (Exception e) {
            e.getMessage();
        }

        List<String> originalPeopleList = MarkDownPage.ReadFile("originalPeople.txt");
        List<String> mapPeopleList = MarkDownPage.ReadFile("mapPeople.txt");
        String[] originalPeopleArray = originalPeopleList.toArray(new String[0]);
        String[] mapPeopleArray = mapPeopleList.toArray(new String[0]);

        Assertions.assertArrayEquals(originalPeopleArray, mapPeopleArray);

    }

    @Test
    public void testKatexSelectButtonArray() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage("Base");

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

        Assertions.assertEquals(selectValue, mathTypesettingPage.getSelectValue());
    }

    @Test
    public void testKatexSelectButtonVisibleValue() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage("Base");

        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        String expectedResult = "ignore";

        mathTypesettingPage.selectStrict(expectedResult);

        Assertions.assertEquals(expectedResult, mathTypesettingPage.getSelectValue());
    }

    @Test
    public void testChangeTextAndNavigateBack() throws InterruptedException
    {
        testLogin();

        BasePage basePage = (BasePage) pageFactory.makePage("Base");
        String expectedUrl = Pages.driver.getCurrentUrl();
        MarkDownPage markDownPage = basePage.goToMarkDown();

        MathTypesettingPage mathTypesettingPage = markDownPage.goToMathTypeSettingPage();
        mathTypesettingPage.goToKatexPage();
        mathTypesettingPage.goToExercisePage();
        mathTypesettingPage.changeKatexCode("katexCode.txt");
        Thread.sleep(1500);
        //String expectedUrl = "https://lennertamas.github.io/blondesite/post/markdown-syntax/";



        Assertions.assertTrue(mathTypesettingPage.goBackToUrl(expectedUrl));
    }


}
