package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.InteractionTests;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.touch.TouchTests;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
                        WindowTest.class,
                        ElementEqualityTest.class,
                        ClickTest.class,
                        FindingTest.class,
                        CoordinatesTest.class,
                        TakesScreenshotTest.class,
                        ElementAttributeTest.class,
                        TypingTest.class,
                        FormHandlingTest.class,
                        MiscTest.class,
                        XPathElementFindingTest.class,
                        StaleElementReferenceTest.class,
                        VisibilityTest.class,
                        InteractionTests.class,
                        ExecutingJavascriptTest.class,
                        VideoTest.class,
                        TouchTests.class,
                    })
public class Quick2WebDriverTests {
    @BeforeClass
    public static void prepareCommonEnvironment() {
        InProcessTestEnvironment environment = GlobalTestEnvironment.get(InProcessTestEnvironment.class);
        environment.setTestContent(new Quick2TestContent(environment.getAppServer()));
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
        String testsName = Quick2WebDriverTests.class.getName();
        GlobalTestEnvironment.get(InProcessTestEnvironment.class).getStatisticCommands().writeToXml(testsName);
    }

}
