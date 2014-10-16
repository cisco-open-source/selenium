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

import org.junit.Test;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ElementAttributeTest extends JUnit4TestBase {

    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://ElementAttributeTestWidget");
    }

    @Test
    public void testElementsDisabledOrEnabled() {
        WebElement inputElement = driver.findElement(By.id("notWorkingButton"));
        assertThat(inputElement.getAttribute("disabled"), equalTo(null));
        assertThat(inputElement.isEnabled(), is(false));

        inputElement = driver.findElement(By.id("workingButton"));
        assertThat(inputElement.getAttribute("disabled"), equalTo(null));
        assertThat(inputElement.isEnabled(), is(true));
    }

    @Test
    public void testElementsDisplayedOrNo() {

        WebElement inputElement = driver.findElement(By.id("notWorkingButton"));
        assertThat(inputElement.isDisplayed(), is(false));

        inputElement = driver.findElement(By.id("workingButton"));
        assertThat(inputElement.isDisplayed(), is(true));
    }

    @Test
    public void testGetTextFromPushButton()
    {
        WebElement element = driver.findElement(By.id("workingButton"));
        assertThat(element.getAttribute("text"), is("Working Button"));
        assertThat(element.getText(), is("Working Button"));
    }

    @Test
    public void testGetTextFromRadioButton()
    {
        WebElement element = driver.findElement(By.id("defaultSelected"));
        assertThat(element.getAttribute("text"), is("Blue"));
        assertThat(element.getText(), is("Blue"));
    }

    @Test
    public void testShouldThrowExceptionIfSendingKeysToElementDisabled() {
        WebElement disabledTextElement1 = driver.findElement(By.id("disabledTextElement1"));
        assertThat(disabledTextElement1.isEnabled(),is(false));
        try {
            disabledTextElement1.sendKeys("foo");
            fail("Should have thrown exception");
        } catch (InvalidElementStateException e) {
            e.printStackTrace();
            // Expected
        }
        assertThat(disabledTextElement1.getText(), is(""));

        WebElement disabledTextElement2 = driver.findElement(By.id("disabledTextElement2"));
        assertThat(disabledTextElement2.isEnabled(),is(false));
        try {
            disabledTextElement2.sendKeys("bar");
            fail("Should have thrown exception");
        } catch (InvalidElementStateException e) {
            // Expected
        }
        assertThat(disabledTextElement2.getText(), is(""));
    }

    @Test
    public void testShouldIndicateWhenRadioButtonSelect() throws InterruptedException {

        WebElement radioBtnSelected = driver.findElement(By.id("defaultSelected"));
        WebElement radioBtnNoSelected = driver.findElement(By.id("noSelected"));

        assertTrue(radioBtnSelected.isSelected());
        assertFalse(radioBtnNoSelected.isSelected());
        radioBtnNoSelected.click();
        assertTrue(radioBtnNoSelected.isSelected());
    }

    @Test
    public void testShouldOnlyReturnTheValueOfCheckedForRadioButtonsIfItIsSet() {
        WebElement noSelected = driver.findElement(By.id("noSelected"));
        WebElement initiallySelected = driver.findElement(By.id("defaultSelected"));

        assertThat(noSelected.getAttribute("checked"), equalTo("false"));
        assertThat(initiallySelected.getAttribute("checked"), equalTo("true"));
    }

    @Test
    public void testShouldIndicateWhenCheckButtonSelect() {

        WebElement check1 = driver.findElement(By.id("checkBoxWithText"));
        assertFalse(check1.isSelected());
        check1.click();
        assertTrue(check1.isSelected());

        WebElement check2 = driver.findElement(By.id("checkBoxWithoutText"));
        assertFalse(check2.isSelected());
        check2.click();
        assertTrue(check2.isSelected());
    }

    @Test
    public void testShouldIndicateWhenATextAreaIsDisabled() {
        WebElement textArea= driver.findElement(By.id("areaWithDefaultText"));
        assertThat(textArea.isEnabled(), is(false));
    }

    @Test
    public void testShouldReturnTheContentsOfTextArea() {

        WebElement textArea= driver.findElement(By.id("areaWithDefaultText"));
        assertThat(textArea.getText(), is("Example text"));
    }

     @Test
     public void testCanRetrieveTheCurrentTextFormTextArea() {
         WebElement textArea = driver.findElement(By.id("workingArea"));
         assertEquals("", textArea.getText());
         textArea.sendKeys("hello world");
         assertEquals("hello world", textArea.getText());
     }

    @Test
    public void testCanRetrieveTheCurrentTextFormLineEdit() {
        WebElement txt = driver.findElement(By.id("enabeledTextElement"));
        txt.sendKeys("Hello");
        assertEquals("Hello", txt.getText());
        assertThat(txt.getText(), is("Hello"));
        txt.clear();
        assertEquals("", txt.getText());
        txt.sendKeys("hello world");

        assertEquals("hello world", txt.getText());
        assertThat(txt.getText(), is("hello world"));
    }

    @Test
    public void testCanRetrieveTheCurrentValueOfATextFormFieldAsEmailInput() {
        WebElement element = driver.findElement(By.id("enabeledTextElement"));
        assertEquals("", element.getText());
        element.sendKeys("hello@example.com");
        assertEquals("hello@example.com", element.getText());
    }

    @Test
    public void testIsVisibleLabel() {
        WebElement element = driver.findElement(By.id("displayedLabel"));
        assertThat(element.isDisplayed(), is(true));

        element = driver.findElement(By.id("notDisplayedLabel"));
        assertThat(element.isDisplayed(), is(false));
    }

    @Test
    public void testReturnTextOfLabel() {
        WebElement element = driver.findElement(By.id("displayedLabel"));
        assertThat(element.getAttribute("text"), is("Visible Label"));
        assertThat(element.getText(), is("Visible Label"));
    }

}
