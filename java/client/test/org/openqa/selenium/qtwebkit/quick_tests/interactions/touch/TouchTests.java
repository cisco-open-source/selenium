package org.openqa.selenium.qtwebkit.quick_tests.interactions.touch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.AfterClass;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.touch.TouchInteractionTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TouchInteractionTests.class,
})
public class TouchTests {
    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }
}
