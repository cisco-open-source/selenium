package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CapabilitiesTest.class,
    ViewTransitionsTest.class,
    InternalWebViewTest.class,
    WidgetAndWebViewTest.class,
    ProxySettingTest.class,
    WindowWithDeclarativeViewTest.class,
    WindowWithSeparatedDeclarativeAndWebViewsTest.class,
    CustomCapabilitiesQt4Test.class,
    EmbeddedWebViewTest.class
})
public class HybridWebDriverQt4Tests {

  @BeforeClass
  public static void prepareCommonEnvironment() {
    GlobalTestEnvironment.get(InProcessTestEnvironment.class);
  }

  @AfterClass
  public static void cleanUpDriver() {
    JUnit4TestBase.removeDriver();
  }
}
