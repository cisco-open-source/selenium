package org.openqa.selenium.qtwebkit.NativeTests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//        WindowTest.class,
//        ClickTest.class,
//        FindingTest.class,
//        CoordinatesTest.class,
        ClickScrollingTest.class,
        TakesScreenshotTest.class,
		ElementAttributeTest.class		
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
