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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openqa.selenium.Platform.LINUX;
import static org.openqa.selenium.Platform.WINDOWS;
import static org.openqa.selenium.testing.Ignore.Driver.ALL;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.FIREFOX;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.MARIONETTE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;
import static org.openqa.selenium.testing.Ignore.Driver.REMOTE;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;
import static org.openqa.selenium.testing.drivers.Browser.chrome;
import static org.openqa.selenium.testing.drivers.Browser.htmlunit;
import static org.openqa.selenium.testing.drivers.Browser.htmlunit_js;
import static org.openqa.selenium.testing.drivers.Browser.ie;
import static org.openqa.selenium.testing.drivers.Browser.opera;
import static org.openqa.selenium.testing.drivers.Browser.phantomjs;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.Platform;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JavascriptEnabled;
import org.openqa.selenium.testing.NativeEventsRequired;
import org.openqa.selenium.testing.NeedsLocalEnvironment;

import java.util.Arrays;
import java.util.Set;

/**
 * Class that decides whether a test class or method should be ignored.
 */
public class TestIgnorance {

  private Set<Browser> alwaysNativeEvents = ImmutableSet.of(chrome, ie, opera);
  private Set<Browser> neverNativeEvents = ImmutableSet.of(
      htmlunit, htmlunit_js, phantomjs);
  private IgnoreComparator ignoreComparator = new IgnoreComparator();
  private Set<String> methods = Sets.newHashSet();
  private Set<String> only = Sets.newHashSet();
  private Set<String> ignoreMethods = Sets.newHashSet();
  private Browser browser;

  public TestIgnorance(Browser browser) {
    setBrowser(browser);

    String onlyRun = System.getProperty("only_run");
    if (onlyRun != null) {
      only.addAll(Arrays.asList(onlyRun.split(",")));
    }

    String method = System.getProperty("method");
    if (method != null) {
      methods.addAll(Arrays.asList(method.split(",")));
    }

    String skip = System.getProperty("ignore_method");
    if (skip != null) {
      ignoreMethods.addAll(Arrays.asList(skip.split(",")));
    }
  }

  // JUnit 4
  public boolean isIgnored(FrameworkMethod method, Object test) {
    boolean ignored = ignoreComparator.shouldIgnore(test.getClass().getAnnotation(Ignore.class)) ||
                      ignoreComparator.shouldIgnore(method.getMethod().getAnnotation(Ignore.class));

    ignored |= isIgnoredBecauseOfJUnit4Ignore(test.getClass().getAnnotation(org.junit.Ignore.class));
    ignored |= isIgnoredBecauseOfJUnit4Ignore(method.getMethod().getAnnotation(org.junit.Ignore.class));
    if (Boolean.getBoolean("ignored_only")) {
      ignored = !ignored;
    }

    ignored |= isIgnoredDueToJavascript(test.getClass().getAnnotation(JavascriptEnabled.class));
    ignored |= isIgnoredDueToJavascript(method.getMethod().getAnnotation(JavascriptEnabled.class));

    ignored |= isIgnoredBecauseOfNativeEvents(test.getClass().getAnnotation(NativeEventsRequired.class));
    ignored |= isIgnoredBecauseOfNativeEvents(method.getMethod().getAnnotation(NativeEventsRequired.class));

    ignored |= isIgnoredDueToEnvironmentVariables(method, test);

    ignored |= isIgnoredDueToBeingOnSauce(method, test);

    return ignored;
  }

  private boolean isIgnoredBecauseOfJUnit4Ignore(org.junit.Ignore annotation) {
    return annotation != null;
  }

  private boolean isIgnoredBecauseOfNativeEvents(NativeEventsRequired annotation) {
    if (annotation == null) {
      return false;
    }

    if (neverNativeEvents.contains(browser)) {
      return true;
    }

    if (alwaysNativeEvents.contains(browser)) {
      return false;
    }

    if (!Boolean.getBoolean("selenium.browser.native_events")) {
      return true;
    }

    // We only have native events on Linux and Windows.
    Platform platform = getEffectivePlatform();
    return !(platform.is(LINUX) || platform.is(WINDOWS));
  }

  private static Platform getEffectivePlatform() {
    if (SauceDriver.shouldUseSauce()) {
      return SauceDriver.getEffectivePlatform();
    }

    return Platform.getCurrent();
  }

  private boolean isIgnoredDueToBeingOnSauce(FrameworkMethod method, Object test) {
    return SauceDriver.shouldUseSauce() &&
           (method.getMethod().getAnnotation(NeedsLocalEnvironment.class) != null ||
            test.getClass().getAnnotation(NeedsLocalEnvironment.class) != null);
  }

  private boolean isIgnoredDueToJavascript(JavascriptEnabled enabled) {
    return enabled != null && !browser.isJavascriptEnabled();

  }

  private boolean isIgnoredDueToEnvironmentVariables(FrameworkMethod method, Object test) {
    return (!only.isEmpty() && !only.contains(test.getClass().getSimpleName())) ||
           (!methods.isEmpty() && !methods.contains(method.getName())) ||
           ignoreMethods.contains(method.getName());
  }

  public void setBrowser(Browser browser) {
    this.browser = checkNotNull(browser, "Browser to use must be set");
    addIgnoresForBrowser(browser, ignoreComparator);
  }

  private void addIgnoresForBrowser(Browser browser, IgnoreComparator comparator) {
    if (Boolean.getBoolean("selenium.browser.remote") || SauceDriver.shouldUseSauce()) {
      comparator.addDriver(REMOTE);
    }

    switch (browser) {
      case chrome:
        comparator.addDriver(CHROME);
        break;

      case ff:
        if (Boolean.getBoolean("webdriver.firefox.marionette")) {
          comparator.addDriver(MARIONETTE);
        } else {
          comparator.addDriver(FIREFOX);
        }
        break;

      case htmlunit:
      case htmlunit_js:
        comparator.addDriver(HTMLUNIT);
        break;

      case ie:
        comparator.addDriver(IE);
        break;

      case none:
        comparator.addDriver(ALL);
        break;

      case opera:
        comparator.addDriver(OPERA);
        break;

      case opera_mobile:
        comparator.addDriver(OPERA_MOBILE);
        comparator.addDriver(REMOTE);
        break;

      case phantomjs:
        comparator.addDriver(PHANTOMJS);
        break;

      case safari:
        comparator.addDriver(SAFARI);
        break;

      case qtwebkit:
          comparator.addDriver(QTWEBKIT);
          break;

      default:
        throw new RuntimeException("Cannot determine which ignore to add ignores rules for");
    }
  }

}
