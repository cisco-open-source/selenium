package org.openqa.selenium.qtwebkit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import com.google.common.collect.Iterables;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.testing.JUnit4TestBase;
import java.util.Set;
import java.util.logging.Level;


public class PerfLoggingTest extends JUnit4TestBase {

    @Before
    public void createDriver() {
    }

    @After
    public void cleanUp() {
        if (null == driver) {
            return;
        }
        driver.quit();
        driver = null;
    }

    @BeforeClass
    public static void cleanUpExistingDriver() {
        JUnit4TestBase.removeDriver();
    }

    @Test
    public void performanceLogShouldBeDisabledByDefault() {
        driver = actuallyCreateDriver();
        Set<String> logTypes = driver.manage().logs().getAvailableLogTypes();
        assertFalse("Performance log should not be enabled by default",
                    logTypes.contains(LogType.PERFORMANCE));
        JUnit4TestBase.removeDriver();
    }

    private void createDriverWithPerformanceLogType() {
        DesiredCapabilities caps = DesiredCapabilities.qtwebkit();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
        driver = new RemoteWebDriver(executor, caps);
    }

    @Test
    public void shouldBeAbleToEnablePerformanceLog() {
        createDriverWithPerformanceLogType();
        Set<String> logTypes = driver.manage().logs().getAvailableLogTypes();
        assertTrue("Profiler log should be enabled", logTypes.contains(LogType.PERFORMANCE));
    }

    @Test
    public void pageLoadShouldProducePerformanceLogEntries() {
        createDriverWithPerformanceLogType();
        driver.get(pages.simpleTestPage);
        LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
        assertNotEquals(0, Iterables.size(entries));
    }


    @Test
    public void performanceLogEntriesShouldHaveNeededStructure() {
        createDriverWithPerformanceLogType();
        driver.get(pages.simpleTestPage);
        LogEntries entries = driver.manage().logs().get(LogType.PERFORMANCE);
        for(LogEntry e : entries.getAll())   {
            assertTrue(e.getMessage().contains("webview"));
            assertTrue(e.getMessage().contains("message"));
        }
    }

}
