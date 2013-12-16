package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Callable;

public class WaitingConditions {

  public static Callable<String> pageUrlToBe(
      final WebDriver driver, final String expectedUrl) {
    if (expectedUrl == null)
      throw new IllegalArgumentException("expectedUrl");

    return new Callable<String>() {
      @Override
      public String call() throws Exception {
        String actualUrl = driver.getCurrentUrl();

        if (expectedUrl.equals(actualUrl)) {
          return actualUrl;
        }

        return null;
      }

      @Override
      public String toString() {
        return "page url to be: " + expectedUrl;
      }
    };
  }

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
        return "expected element " + element + " attribute '" + attributeName + "' " +
               "to equal '" + expectedValue + "' while actual value is '" + element.getAttribute(attributeName) + "'";
      }
    };
  }

  public static Callable<Boolean> elementToBeEnabled(final WebElement element) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        try {
          return element.isEnabled();
        } catch (StaleElementReferenceException e) {
          return true;
        }
      }
    };
  }

  public static Callable<Boolean> elementToBeDisabled(final WebElement element) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        try {
          return !element.isEnabled();
        } catch (StaleElementReferenceException e) {
          return true;
        }
      }
    };
  }

  public static Callable<Boolean> elementToBeDisplayed(final WebElement element) {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        try {
          return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
          return true;
        }
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

  public static Callable<String> activeWindowToBe(
      final WebDriver driver, final String expectedActiveWindow) {
    return new Callable<String>() {

      public String call() throws Exception {
        String actualActiveWindow = driver.getWindowHandle();

        if (expectedActiveWindow.equalsIgnoreCase(actualActiveWindow)) {
          return actualActiveWindow;
        }

        return null;
      }

      @Override
      public String toString() {
        return "active window to be: " + expectedActiveWindow;
      }
    };
  }
}
