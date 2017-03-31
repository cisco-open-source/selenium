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

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.MouseAction;
import org.openqa.selenium.interactions.multitouch.MultiTouchActions;

import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.WaitingConditions.*;
import static org.openqa.selenium.qtwebkit.visualizer_tests.WaitingConditions.activeElementToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class QtWebDriverVisualizerTest extends QtWebDriverJsBaseTest {

  private void rotateElement(String elementId, int angle) {
    WebElement toRotate = driver.findElement(By.id(elementId));
    Action rotate = new MultiTouchActions(driver).pinchRotate(toRotate, angle).build();
    rotate.perform();
  }

  private void zoomElement(String elementId, double scale) {
    WebElement toZoom = driver.findElement(By.id(elementId));
    Action zoom = new MultiTouchActions(driver).pinchZoom(toZoom, scale).build();
    zoom.perform();
  }

  @Test
  public void canOpenLinkAndTypeText() {
    Set<String> originalWindowHandles = driver.getWindowHandles();
    page.setWebPage(pages.clicksPage);
    page.clickSource();
    wait.until(newWindowIsOpened(originalWindowHandles));

    targetDriver.findElement(By.id("normal")).click();
    targetWait.until(titleIs("XHTML Test Page"));

    driver.switchTo().window(page.clickSource());

    String typingText = "TheTypingText";
    String expectedText = "change" + typingText;

    wait.until(presenceOfElementLocated(By.id("username")));
    WebElement inputField = driver.findElement(By.id("username"));
    WebElement inputField2 = targetDriver.findElement(By.id("username"));

    inputField.click();
    wait.until(activeElementToBe(inputField));
    targetWait.until(activeElementToBe(inputField2));

    inputField.sendKeys(typingText);

    wait.until(elementValueToEqual(inputField2, expectedText));
    assertThat(inputField2.getAttribute("value"), equalTo(expectedText));
  }

  @Test
  public void isClickOnLinkCorrect() {
    page.setWebPage(pages.clicksPage);
    driver.switchTo().window(page.clickSource());

    String visualizerTitle = driver.getTitle();

    driver.findElement(By.id("normal")).click();
    targetWait.until(titleIs("XHTML Test Page"));

    assertEquals("We do not proceed by links in visualizer", visualizerTitle, driver.getTitle());
  }

  @Test
  public void canRightClick() {
    page.setWebPage(pages.javascriptPage);
    driver.switchTo().window(page.clickSource());

    new Actions(driver).contextClick(driver.findElement(By.id("doubleClickField"))).perform();
    targetWait.until(elementValueToEqual(targetDriver.findElement(By.id("doubleClickField")), "ContextClicked"));
  }

  @Test
  public void canPinchRotate() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    rotateElement("picture", 35);
    WebElement result = targetDriver.findElement(By.id("result_rotate"));
    targetWait.until(elementTextToEqual(result, "35"));
  }

  @Test
  public void canPinchRotateNegativeDegree() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    rotateElement("picture", -35);
    WebElement result = targetDriver.findElement(By.id("result_rotate"));
    targetWait.until(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "-35"));
  }

  @Test
  public void canPinchZoomOut() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    zoomElement("picture", 2.5);
    WebElement result = targetDriver.findElement(By.id("result_scale"));
    targetWait.until(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "2.5"));
  }

  @Test
  public void canPinchZoomIn() {
    page.setWebPage(pages.pinchTouchTest);
    driver.switchTo().window(page.clickSource());

    zoomElement("picture", 0.5);
    WebElement result = targetDriver.findElement(By.id("result_scale"));
    targetWait.until(org.openqa.selenium.WaitingConditions.elementTextToEqual(result, "0.5"));
  }
}
