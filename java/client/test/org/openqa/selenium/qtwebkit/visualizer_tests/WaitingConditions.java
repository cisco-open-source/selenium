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

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.concurrent.Callable;

public class WaitingConditions {

  public static ExpectedCondition<String> pageUrlToBe(
      final String expectedUrl) {
    if (expectedUrl == null)
      throw new IllegalArgumentException("expectedUrl");

    return new ExpectedCondition<String>() {
      private String actualUrl;

      @Override
      public String apply(WebDriver driver) {
        actualUrl = driver.getCurrentUrl();

        if (expectedUrl.equals(actualUrl)) {
          return actualUrl;
        }

        return null;
      }

      @Override
      public String toString() {
        return "page url to be [" + expectedUrl + "] while actual value is [" + actualUrl + "]";
      }
    };
  }

  public static ExpectedCondition<String> elementAttributeToEqual(
      final WebElement element, final String attributeName, final String expectedValue) {
    if (expectedValue == null)
      throw new IllegalArgumentException("expectedValue");

    return new ExpectedCondition<String>() {

      @Override
      public String apply(WebDriver driver) {
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

  public static ExpectedCondition<Boolean> elementToBeEnabled(final WebElement element) {
    return new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver driver) {
        try {
          return element.isEnabled();
        } catch (StaleElementReferenceException e) {
          return true;
        }
      }
    };
  }

  public static ExpectedCondition<Boolean> elementToBeDisabled(final WebElement element) {
    return new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver driver) {
        try {
          return !element.isEnabled();
        } catch (StaleElementReferenceException e) {
          return true;
        }
      }
    };
  }

  public static ExpectedCondition<Boolean> elementToBeDisplayed(final WebElement element) {
    return new ExpectedCondition<Boolean>() {

      @Override
      public Boolean apply(WebDriver driver) {
        try {
          return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
          return true;
        }
      }
    };
  }

  public static ExpectedCondition<WebElement> activeElementToBe(
      final WebElement expectedActiveElement) {
    return new ExpectedCondition<WebElement>() {

      @Override
      public WebElement apply(WebDriver driver) {
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

  public static ExpectedCondition<String> activeWindowToBe(
      final String expectedActiveWindow) {
    return new ExpectedCondition<String>() {

      @Override
      public String apply(WebDriver driver) {
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
