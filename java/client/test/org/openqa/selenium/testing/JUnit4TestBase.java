/*
Copyright 2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium.testing;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.environment.Pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.environment.webserver.AppServer;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.profiler.EventType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;

import java.util.List;
import java.util.logging.Logger;

@RunWith(SeleniumTestRunner.class)
public abstract class JUnit4TestBase implements WrapsDriver {

  private static final Logger logger = Logger.getLogger(JUnit4TestBase.class.getName());

  protected InProcessTestEnvironment environment;
  protected AppServer appServer;
  protected Pages pages;
  private static ThreadLocal<WebDriver> storedDriver = new ThreadLocal<WebDriver>();
  protected WebDriver driver;
  protected Wait<WebDriver> wait;
  protected Wait<WebDriver> shortWait;

  @Before
  public void prepareEnvironment() throws Exception {
    environment = GlobalTestEnvironment.get(InProcessTestEnvironment.class);
    appServer = environment.getAppServer();

    pages = environment.getTestContent();

    String hostName = environment.getAppServer().getHostName();
    String alternateHostName = environment.getAppServer().getAlternateHostName();

    assertThat(hostName, is(not(equalTo(alternateHostName))));
  }

  @Before
  public void createDriver() throws Exception {
    driver = actuallyCreateDriver();
    wait = new WebDriverWait(driver, 30);
    shortWait = new WebDriverWait(driver, 5);
  }

  @Rule
  public TestName testName = new TestName();

  @Rule
  public TestRule traceMethodName = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      super.starting(description);
      logger.info(">>> Starting " + description);
    }

    @Override
    protected void finished(Description description) {
      super.finished(description);
      logger.info("<<< Finished " + description);
      if (driver != null && ((RemoteWebDriver) driver).getSessionId() != null) {
        List<LogEntry> entries;
        try {
          entries = driver.manage().logs().get("profiler").getAll();
        } catch (Exception e) {
          logger.warning("<<< Error: can't get valid logs...");
          e.printStackTrace();
          return;
        }
        String[] testCase = new String[2];
        for (LogEntry entry : entries) {
          try {
            JSONObject json = new JSONObject(entry.getMessage());
            if (json.getString("event").equals(EventType.HTTP_COMMAND.toString())) {
              testCase[0] = description.getClassName();
              testCase[1] = description.getMethodName();
              environment.addTestToCommand(json.getString("command"), json.getString("url"),
                                           json.getString("method"), testCase);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
    }
  };
  
  public WebDriver getWrappedDriver() {
    return storedDriver.get();
  }

  public static WebDriver actuallyCreateDriver() {
    WebDriver driver = storedDriver.get();

    if (driver == null ||
        (driver instanceof RemoteWebDriver && ((RemoteWebDriver)driver).getSessionId() == null)) {
      DesiredCapabilities caps = new DesiredCapabilities();
      caps.setCapability(CapabilityType.ENABLE_PROFILING_CAPABILITY, true);
      WebDriverBuilder builder = new WebDriverBuilder();

      String startClass = System.getProperty("webdriver.browserClass");
      if (null != startClass) {
        caps.setCapability("browserClass", startClass);
      }
      builder.setDesiredCapabilities(caps);
      driver = builder.get();
      storedDriver.set(driver);
    }
    return storedDriver.get();
  }

  public static void removeDriver() {
    if (Boolean.getBoolean("webdriver.singletestsuite.leaverunning")) {
      return;
    }

    WebDriver current = storedDriver.get();

    if (current == null) {
      return;
    }

    try {
      current.quit();
    } catch (RuntimeException ignored) {
      // fall through
    }

    storedDriver.remove();
  }

  protected boolean isIeDriverTimedOutException(IllegalStateException e) {
    // The IE driver may throw a timed out exception
    return e.getClass().getName().contains("TimedOutException");
  }

}
