/*
Copyright 2011-2014 Software Freedom Conservancy

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

package org.openqa.selenium.html5;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.testing.JUnit4TestBase;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class AppCacheTest extends JUnit4TestBase {

  @Before
  public void checkIsApplicationCache() {
    assumeTrue(driver instanceof ApplicationCache);
  }

  @Test
  public void testAppCacheStatus() {
    driver.get("http://www.google.com");
    driver.manage().timeouts().implicitlyWait(2000, MILLISECONDS);

    AppCacheStatus status = ((ApplicationCache) driver).getStatus();
    while (status == AppCacheStatus.DOWNLOADING) {
      status = ((ApplicationCache) driver).getStatus();
    }
    assertEquals(AppCacheStatus.UNCACHED, status);
  }

  @Test
  public void testBrowserLoadsFromCacheWhenOffline() {
    driver.get(pages.html5Page);
    driver.get(pages.formPage);
  }
}
