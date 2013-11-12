package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        QtWebDriverVisualizerTest.class
})

public class QtWebDriverVisualizerTests {

    @BeforeClass
    public static void prepareCommonEnvironment() {
        GlobalTestEnvironment.get(InProcessTestEnvironment.class);
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }
}
