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

package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


public class CoordinatesTest extends JUnit4TestBase {

    @Before
    public void setUpWebDriver()
    {
        driver.get("qtwidget://CoordinatesTestWidget");
    }

    @Test
    public void testShouldGetCoordinatesOfAnElementInViewPort() {
        assertThat(getLocationOnScreen(By.id("scrollArea")), is(new Point(11, 11)));
    }

    @Test
    public void testShouldGetCoordinatesOfAnZeroSizeElement() {
      Point elementLocation =  getLocationOnScreen(By.id("zeroSizeLabel"));
      boolean isElementLocatedCorrectly = true;
        if(elementLocation.x > 22 || elementLocation.x < 20)
          isElementLocatedCorrectly = false;
        else if(elementLocation.y > 22 || elementLocation.y < 20)
          isElementLocatedCorrectly = false;
        assertThat(isElementLocatedCorrectly, is(true));
    }

    @Test
    public void testShouldGetCoordinatesOfATransparentElement() {
      Point elementLocation =  getLocationOnScreen(By.id("transparentLabel"));
      boolean isElementLocatedCorrectly = true;
      if(elementLocation.x > 22 || elementLocation.x < 20)
        isElementLocatedCorrectly = false;
      else if(elementLocation.y > 25 || elementLocation.y < 23)
        isElementLocatedCorrectly = false;
      assertThat(isElementLocatedCorrectly, is(true));
    }

    @Test
    public void testShouldGetCoordinatesOfAHiddenElement() {
        assertNotNull(getLocationOnScreen(By.id("hiddenLabel")));
    }

    @Test
    public void testShouldGetCoordinatesOfAnInvisibleElement() {
        assertNotNull(getLocationOnScreen(By.id("unvisibleLabel")));
    }

    private Point getLocationOnScreen(By locator) {
        WebElement element = driver.findElement(locator);
        return ((Locatable) element).getCoordinates().onScreen();
    }
}
