/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.qtwebkit.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.testing.JUnit4TestBase;

public class QtWebDriverJsBaseTest extends JUnit4TestBase {

  protected QtWebDriverJsPage page;
  protected WebDriver targetDriver;
  protected Wait<WebDriver> targetWait;

  @Before
  public void createDriver() throws Exception {
    DesiredCapabilities capabilities = DesiredCapabilities.qtwebkit();

    QtWebDriverExecutor webDriverExecutor = QtWebKitDriver.createDefaultExecutor();
    driver = new QtWebKitDriver(webDriverExecutor, capabilities);
    page = PageFactory.initElements(driver, QtWebDriverJsPage.class);
    page.setDriver(driver);
    wait = new WebDriverWait(driver, 30);

    DriverService webDriver2Service = QtWebDriverService.createDefaultService();
    QtWebDriverExecutor webDriver2Executor = new QtWebDriverServiceExecutor(webDriver2Service);
    targetDriver = new QtWebKitDriver(webDriver2Executor, capabilities);
    page.setTargetDriver(targetDriver);
    targetWait = new WebDriverWait(targetDriver, 30);

    String targetWebDriverUrl = webDriver2Service.getUrl().toExternalForm();
    page.setWebDriverUrl(targetWebDriverUrl);
  }

  @After
  public void tearDown() throws Exception {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
    if (targetDriver != null) {
      targetDriver.quit();
      targetDriver = null;
    }
  }
}
