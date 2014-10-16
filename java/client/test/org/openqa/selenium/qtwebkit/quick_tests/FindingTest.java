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

package org.openqa.selenium.qtwebkit.quick_tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.logging.Logger;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class FindingTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(FindingTest.class.getName());

    @Before
    public void createWebDriver() {
        driver.get(pages.findingTest);
    }

    @Test
    public void testCanFindSimpleControl() {
        assertTrue(driver.findElement(By.id("inputElement")).isDisplayed());
    }

    @Test
    public void testCanFindHiddenControl() {
        assertNotNull(driver.findElement(By.id("mouseHotSpot")));
    }

    @Test
    public void testCanFindControlByClassName() {
        assertNotNull(driver.findElement(By.className("TextInput")));
    }

    @Test
    public void testCanFindHiddenControlByClassName() {
        assertNotNull(driver.findElement(By.className("MouseArea")));
    }

    @Test
    public void testCanNotFindNonExistElementByClassName() {
        try {
            assertNull(driver.findElement(By.className("NonExistedElement")));
        } catch (RuntimeException e) {
            //  this is expected
            assertThat(e, is(instanceOf(NoSuchElementException.class)));
        }
    }

    @Test
    public void testCanNotFindNonExistElementById() {
        try {
            driver.findElement(By.id("wrongId"));
        } catch (RuntimeException e) {
            //  this is expected
            assertThat(e, is(instanceOf(NoSuchElementException.class)));
        }
    }
}
