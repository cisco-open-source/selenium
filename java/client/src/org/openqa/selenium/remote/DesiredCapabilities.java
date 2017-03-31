/*
Copyright 2007-2009 Selenium committers

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

package org.openqa.selenium.remote;

import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;
import static org.openqa.selenium.remote.CapabilityType.LOGGING_PREFS;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM;
import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_JAVASCRIPT;
import static org.openqa.selenium.remote.CapabilityType.VERSION;

import com.google.common.collect.Maps;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.logging.LogLevelMapping;
import org.openqa.selenium.logging.LoggingPreferences;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DesiredCapabilities implements Serializable, Capabilities {
  private final Map<String, Object> capabilities = new HashMap<String, Object>();

  public DesiredCapabilities(String browser, String version, Platform platform) {
    setCapability(BROWSER_NAME, browser);
    setCapability(VERSION, version);
    setCapability(PLATFORM, platform);
  }

  public DesiredCapabilities() {
    // no-arg constructor
  }

  public DesiredCapabilities(Map<String, ?> rawMap) {
    if (rawMap.containsKey(LOGGING_PREFS) && rawMap.get(LOGGING_PREFS) instanceof Map) {
      LoggingPreferences prefs = new LoggingPreferences();
      Map<String, String> prefsMap = (Map<String, String>) rawMap.get(LOGGING_PREFS);

      for (String logType : prefsMap.keySet()) {
        prefs.enable(logType, LogLevelMapping.toLevel(prefsMap.get(logType)));
      }
      capabilities.put(LOGGING_PREFS, prefs);
      // So it does not get added twice
      rawMap.remove(LOGGING_PREFS);
    }
    capabilities.putAll(rawMap);
    Object value = capabilities.get(PLATFORM);
    if (value instanceof String) {
      capabilities.put(PLATFORM, Platform.valueOf((String) value));
    }
  }

  public DesiredCapabilities(Capabilities other) {
    if (other != null) {
      merge(other);
    }
  }

  public DesiredCapabilities(Capabilities... others) {
    for (Capabilities caps : others) {
      if (caps != null) {
        merge(caps);
      }
    }
  }

  public String getBrowserName() {
    Object browserName = capabilities.get(BROWSER_NAME);
    return browserName == null ? "" : browserName.toString();
  }

  public void setBrowserName(String browserName) {
    setCapability(BROWSER_NAME, browserName);
  }

  public String getVersion() {
    Object version = capabilities.get(VERSION);
    return version == null ? "" : version.toString();
  }

  public void setVersion(String version) {
    setCapability(VERSION, version);
  }

  public Platform getPlatform() {
    if (capabilities.containsKey(PLATFORM)) {
      Object raw = capabilities.get(PLATFORM);
      if (raw instanceof String) {
        return Platform.valueOf((String) raw);
      } else if (raw instanceof Platform) {
        return (Platform) raw;
      }
    }
    return null;
  }

  public void setPlatform(Platform platform) {
    setCapability(PLATFORM, platform);
  }

  public boolean isJavascriptEnabled() {
    if (capabilities.containsKey(SUPPORTS_JAVASCRIPT)) {
      Object raw = capabilities.get(SUPPORTS_JAVASCRIPT);
      if (raw instanceof String) {
        return Boolean.parseBoolean((String) raw);
      } else if (raw instanceof Boolean) {
        return ((Boolean) raw).booleanValue();
      }
    }
    return true;
  }

  public void setJavascriptEnabled(boolean javascriptEnabled) {
    setCapability(SUPPORTS_JAVASCRIPT, javascriptEnabled);
  }

  public Object getCapability(String capabilityName) {
    return capabilities.get(capabilityName);
  }

  public boolean is(String capabilityName) {
    Object cap = getCapability(capabilityName);
    if (cap == null) {
      return false;
    }
    return cap instanceof Boolean ? (Boolean) cap : Boolean.parseBoolean(String.valueOf(cap));
  }

  /**
   * Merges the extra capabilities provided into this DesiredCapabilities instance. If capabilities
   * with the same name exist in this instance, they will be overridden by the values from the
   * extraCapabilities object.
   *
   * @param extraCapabilities Additional capabilities to be added.
   */

  public DesiredCapabilities merge(
      org.openqa.selenium.Capabilities extraCapabilities) {
    if (extraCapabilities != null) {
      capabilities.putAll(extraCapabilities.asMap());
    }
    return this;
  }

  public void setCapability(String capabilityName, boolean value) {
    capabilities.put(capabilityName, value);
  }

  public void setCapability(String capabilityName, String value) {
    capabilities.put(capabilityName, value);
  }

  public void setCapability(String capabilityName, Platform value) {
    capabilities.put(capabilityName, value);
  }

  public void setCapability(String key, Object value) {
    capabilities.put(key, value);
  }

  public Map<String, ?> asMap() {
    return Collections.unmodifiableMap(capabilities);
  }

  public static DesiredCapabilities android() {
    return new DesiredCapabilities(BrowserType.ANDROID, "", Platform.ANDROID);
  }

  public static DesiredCapabilities chrome() {
    return new DesiredCapabilities(BrowserType.CHROME, "", Platform.ANY);
  }

  public static DesiredCapabilities firefox() {
    return new DesiredCapabilities(BrowserType.FIREFOX, "", Platform.ANY);
  }

  public static DesiredCapabilities htmlUnit() {
    return new DesiredCapabilities(BrowserType.HTMLUNIT, "", Platform.ANY);
  }

  public static DesiredCapabilities htmlUnitWithJs() {
    DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.HTMLUNIT,
                                                               "", Platform.ANY);
    capabilities.setJavascriptEnabled(true);
    return capabilities;
  }

  public static DesiredCapabilities internetExplorer() {
    DesiredCapabilities capabilities = new DesiredCapabilities(
        BrowserType.IE, "", Platform.WINDOWS);
    capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
    return capabilities;
  }

  public static DesiredCapabilities iphone() {
    return new DesiredCapabilities(BrowserType.IPHONE, "", Platform.MAC);
  }

  public static DesiredCapabilities ipad() {
    return new DesiredCapabilities(BrowserType.IPAD, "", Platform.MAC);
  }

  public static DesiredCapabilities opera() {
    return new DesiredCapabilities(BrowserType.OPERA, "", Platform.ANY);
  }

  public static DesiredCapabilities safari() {
    return new DesiredCapabilities(BrowserType.SAFARI, "", Platform.ANY);
  }

  public static DesiredCapabilities phantomjs() {
    return new DesiredCapabilities(BrowserType.PHANTOMJS, "", Platform.ANY);
  }

    public static DesiredCapabilities qtwebkit() {
        return new DesiredCapabilities("qtwebkit", "", Platform.ANY);
    }

  @Override
  public String toString() {
    Map<String, String> map = Maps.newHashMap();

    for (Map.Entry<String, ?> entry : capabilities.entrySet()) {
      String value = String.valueOf(entry.getValue());
      if ("firefox_profile".equals(entry.getKey())) {
        if (value.length() > 32) {
          value = value.substring(0, 29) + "...";
        }
      }
      map.put(entry.getKey(), value);
    }

    return String.format("Capabilities [%s]", map);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DesiredCapabilities)) {
      return false;
    }

    DesiredCapabilities that = (DesiredCapabilities) o;

    return capabilities.equals(that.capabilities);
  }

  @Override
  public int hashCode() {
    return capabilities.hashCode();
  }
}
