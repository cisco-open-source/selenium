package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.StandardSeleniumTests;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;

import org.openqa.selenium.qtwebkit.touch.TouchTests;

import java.util.logging.Logger;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StandardSeleniumTests.class,
        GraphicsWebSanityTest.class,
        TouchTests.class,
        QtWebDriverSwitchesTest.class,
        HTML5VideoTagTest.class,
        HTML5AudioTagTest.class,
})

public class QtWebkitDriverTests {

    private static final String testsName = QtWebkitDriverTests.class.getName();
    private static final Logger logger = Logger.getLogger(testsName);

    @AfterClass
    public static void flushReport() {
      GlobalTestEnvironment.get(InProcessTestEnvironment.class).getStatisticCommands().writeToXml(testsName);
    }
}
