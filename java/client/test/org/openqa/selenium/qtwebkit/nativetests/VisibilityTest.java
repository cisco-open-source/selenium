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
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.testing.JUnit4TestBase;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VisibilityTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://VisibilityTestWidget");
    }

    @Test
    public void testShouldAllowTheUserToTellIfAnElementIsDisplayedOrNot() {

        assertThat(driver.findElement(By.id("displayedLabel")).isDisplayed(),
                is(true));
        assertThat(driver.findElement(By.id("notDisplayedLabel")).isDisplayed(), is(false));

    }

    @Test
    public void testVisibilityShouldTakeIntoWidgetParentVisibility() {

        WebElement element = driver.findElement(By.id("hiddenScrollArea"));
        WebElement externalElement = driver.findElement(By.id("buttonInScrollArea"));

        assertFalse(element.isDisplayed());
        assertFalse(externalElement.isDisplayed());
    }

    @Test
    public void testShouldModifyTheVisibilityOfAnElementDynamically() {

        WebElement element = driver.findElement(By.id("buttonCanHide"));
        assertTrue(element.isDisplayed());

        element.click();
        wait.until(elementNotToDisplayed(element));
        assertFalse(element.isDisplayed());
    }

    @Test
    public void testHiddenInputElementsAreNeverVisible() {

        WebElement shown = driver.findElement(By.id("buttonHidden"));

        assertFalse(shown.isDisplayed());
    }

    @Test
    public void testShouldNotBeAbleToClickOnAnElementThatIsNotDisplayed() {
        WebElement element = driver.findElement(By.id("buttonHidden"));

        try {
            element.click();
            fail("You should not be able to click on an invisible element");
        } catch (ElementNotVisibleException e) {
            // This is expected
        }
    }

    @Test
    public void testShouldNotBeAbleToTypeAnElementThatIsNotDisplayed() {
        WebElement element = driver.findElement(By.id("inputHidden"));

        try {
            element.sendKeys("You don't see me");
            fail("You should not be able to send keyboard input to an invisible element");
        } catch (ElementNotVisibleException e) {
            // This is expected
        }

        assertThat(element.getText(), is(not("You don't see me")));
    }

    @Test
    public void testZeroSizedDivIsShownIfDescendantHasSize() {

        WebElement element = driver.findElement(By.id("zeroLabel"));
        Dimension size = element.getSize();

        assertEquals("Should have 0 width", 0, size.width);
        assertEquals("Should have 0 height", 0, size.height);
        assertTrue(element.isDisplayed());
    }

    private ExpectedCondition<Boolean> elementNotToDisplayed(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !element.isDisplayed();
            }
        };
    }

    @Test
    public void parentNodeVisibleWhenAllChildrenAreAbsolutelyPositionedAndOverflowIsHidden() {
        String url = "qtwidget://TypingTestWidget";
        driver.get(url);

        WebElement element = driver.findElement(By.id("result"));
        assertTrue(element.isDisplayed());
    }

    @Test
    public void tooSmallAWindowWithOverflowHiddenIsNotAProblem() {
        WebDriver.Window window = driver.manage().window();
        Dimension originalSize = window.getSize();

        try {
            // Short in the Y dimension
            window.setSize(new Dimension(1024, 500));

            String url = "qtwidget://TypingTestWidget";
            driver.get(url);

            WebElement element = driver.findElement(By.id("result"));
            assertTrue(element.isDisplayed());
        } finally {
            window.setSize(originalSize);
        }
    }

}
