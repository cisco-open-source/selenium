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


package org.openqa.selenium.testing.drivers;

import java.util.logging.Logger;

public enum Browser {

  chrome,
  ff,
  htmlunit {
    @Override
    public boolean isJavascriptEnabled() {
      return false;
    }
  },
  htmlunit_js,
  ie,
  none, // For those cases where you don't actually want a browser
  opera,
  opera_mobile,
  phantomjs,
  safari,
  qtwebkit;

  private static final Logger log = Logger.getLogger(Browser.class.getName());

  public static Browser detect() {
    String browserName = System.getProperty("selenium.browser");
    if (browserName == null) {
      log.info("No browser detected, returning null");
      return null;
    }

    try {
      return Browser.valueOf(browserName);
    } catch (IllegalArgumentException e) {
      log.severe("Cannot locate matching browser for: " + browserName);
      return null;
    }
  }

  public boolean isJavascriptEnabled() {
    return true;
  }

}
