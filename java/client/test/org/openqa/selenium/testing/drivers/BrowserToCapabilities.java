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


package org.openqa.selenium.testing.drivers;

import com.opera.core.systems.OperaProduct;
import com.opera.core.systems.OperaProfile;
import com.opera.core.systems.OperaSettings;

import static org.openqa.selenium.remote.CapabilityType.HAS_NATIVE_EVENTS;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserToCapabilities {
  public static DesiredCapabilities of(Browser browser) {
    if (browser == null) {
      return null;
    }

    DesiredCapabilities caps;

    switch (browser) {
      case chrome:
        caps = DesiredCapabilities.chrome();
        break;

      case ff:
        caps = DesiredCapabilities.firefox();
        break;

      case htmlunit:
        caps = DesiredCapabilities.htmlUnit();
        caps.setJavascriptEnabled(false);
        break;

      case htmlunit_js:
        caps = DesiredCapabilities.htmlUnit();
        caps.setJavascriptEnabled(true);
        break;

      case ie:
        caps = DesiredCapabilities.internetExplorer();
        break;

      case opera:
        OperaProfile profile = new OperaProfile();
        profile.preferences().set("Geolocation", "Enable geolocation", true);
        // This pref allows all sites to access geolocation without prompting.
        // A 0 value would deny all sites and -1 would prompt for all sites.
        profile.preferences().set("User Prefs", "Geolocation site state", 1);

        caps = DesiredCapabilities.opera();
        caps.setCapability("opera.profile", profile);
        break;

      case opera_mobile:
        caps = DesiredCapabilities.opera();
        caps.setCapability(OperaSettings.Capability.PRODUCT.getCapability(), OperaProduct.MOBILE);
        break;

      case phantomjs:
        caps = DesiredCapabilities.phantomjs();
        break;

      case safari:
        caps = DesiredCapabilities.safari();
        break;

      case qtwebkit:
          caps = new DesiredCapabilities("qtwebkit", "", Platform.ANY);
          break;

      default:
        throw new RuntimeException("Cannot determine browser config to use");
    }

    String version = System.getProperty("selenium.browser.version");
    if (version != null) {
      caps.setVersion(version);
    }

    caps.setCapability(HAS_NATIVE_EVENTS,
        Boolean.getBoolean("selenium.browser.native_events"));

    return caps;
  }
}
