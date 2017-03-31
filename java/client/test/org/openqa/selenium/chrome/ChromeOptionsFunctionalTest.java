/*
 Copyright 2011 Selenium committers
 Copyright 2011 Software Freedom Conservancy

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

package org.openqa.selenium.chrome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.NeedsLocalEnvironment;

import org.openqa.selenium.testing.Ignore;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;

/**
 * Functional tests for {@link ChromeOptions}.
 */
public class ChromeOptionsFunctionalTest extends JUnit4TestBase {
  private ChromeDriver driver = null;

  @After
  public void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
    }
  }

  @Before
  @Override
  public void createDriver() throws Exception {
    // do nothing, don't want to have it create a driver for these tests
  }

  @NeedsLocalEnvironment
  @Test
  public void canStartChromeWithCustomOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("user-agent=foo;bar");
    driver = new ChromeDriver(options);

    driver.get(pages.clickJacker);
    Object userAgent = driver.executeScript("return window.navigator.userAgent");
    assertEquals("foo;bar", userAgent);
  }

  @NeedsLocalEnvironment
  @Test
  public void optionsStayEqualAfterSerialization() throws Exception {
    ChromeOptions options1 = new ChromeOptions();
    ChromeOptions options2 = new ChromeOptions();
    assertTrue("empty chrome options should be equal", options1.equals(options2));
    options1.toJson();
    assertTrue("empty chrome options after one is .toJson() should be equal",
               options1.equals(options2));
  }
}
