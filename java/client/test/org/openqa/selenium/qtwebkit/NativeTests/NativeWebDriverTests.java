package org.openqa.selenium.qtwebkit.NativeTests;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 4/1/13
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WindowTest.class,
        ClickTest.class,
        FindingTest.class
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
