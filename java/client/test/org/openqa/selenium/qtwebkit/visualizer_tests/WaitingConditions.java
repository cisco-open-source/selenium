package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Callable;

public class WaitingConditions {

  public static Callable<String> elementAttributeToEqual(
      final WebElement element, final String attributeName, final String expectedValue) {
    if (expectedValue == null)
      throw new IllegalArgumentException("expectedValue");

    return new Callable<String>() {

      @Override
      public String call() throws Exception {
        String actualValue = element.getAttribute(attributeName);

        if (expectedValue.equals(actualValue)) {
          return actualValue;
        }

        return null;
      }

      @Override
      public String toString() {
        return "element " + element + " attribute '" + attributeName + "' to equal '" + expectedValue + "'";
      }
    };
  }

  public static Callable<WebElement> activeElementToBe(
      final WebDriver driver, final WebElement expectedActiveElement) {
    return new Callable<WebElement>() {

      public WebElement call() throws Exception {
        WebElement activeElement = driver.switchTo().activeElement();

        if (expectedActiveElement.equals(activeElement)) {
          return activeElement;
        }

        return null;
      }

      @Override
      public String toString() {
        return "active element to be: " + expectedActiveElement;
      }
    };
  }
}
