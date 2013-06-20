package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.qtwebkit.nativetests.interactions.ActionsTest;
import org.openqa.selenium.qtwebkit.nativetests.interactions.BasicKeyboardInterfaceTest;
import org.openqa.selenium.qtwebkit.nativetests.interactions.BasicMouseInterfaceTest;
import org.openqa.selenium.qtwebkit.nativetests.interactions.IndividualKeyboardActionsTest;
import org.openqa.selenium.qtwebkit.nativetests.interactions.IndividualMouseActionsTest;
import org.openqa.selenium.qtwebkit.nativetests.interactions.InteractionTests;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WindowTest.class,
        ClickTest.class,
        FindingTest.class,
        CoordinatesTest.class,
        ClickScrollingTest.class,
        TakesScreenshotTest.class,
        ElementAttributeTest.class,
        ElementSelectingTest.class,
        TypingTest.class,
        TextHandlingTest.class,
        FormHandlingTest.class,
        MiscTest.class,
        XPathElementFindingTest.class,
        StaleElementReferenceTest.class,
        VisibilityTest.class,
        InteractionTests.class
})
public class NativeWebDriverTests {
    @BeforeClass
    public static void prepareCommonEnvironment() {
        GlobalTestEnvironment.get(InProcessTestEnvironment.class);
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }
}
