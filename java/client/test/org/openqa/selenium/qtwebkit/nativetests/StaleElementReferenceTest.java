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
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.testing.JUnit4TestBase;

import static org.junit.Assert.fail;

public class StaleElementReferenceTest extends JUnit4TestBase {


    @Before
    public void setUp() throws Exception {
        driver.get("qtwidget://StaleElementReferenceTestWidget");
    }

    @Test
    public void testOldPage() {
        WebElement elem = driver.findElement(By.id("captionLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.click();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }

    @Test
    public void testShouldNotCrashWhenCallingGetSizeOnAnObsoleteElement() {
        WebElement elem = driver.findElement(By.id("captionLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.getSize();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }

    @Test
    public void testShouldNotCrashWhenQueryingTheAttributeTextOfAStaleElement() {
        WebElement elem = driver.findElement(By.xpath("//QLabel"));
        driver.get("qtwidget://TypingTestWidget");
        try {
            elem.getText();
            fail();
        } catch (StaleElementReferenceException e) {
            // do nothing. this is what we expected.
        }
    }
}
