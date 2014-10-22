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

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

public class ClickTest extends JUnit4TestBase {


    @Before
    public void setUp() throws Exception {
        driver.get(pages.clickTest);
    }

    @Test
    public void testCanClickOnAPushButton() {
        driver.findElement(By.id("button1")).click();

        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked button1"));
    }

    @Test
    public void testCanClickOnMouseArea() {
        driver.findElement(By.id("mouseHotSpotArea2")).click();

        waitFor(WaitingConditions.elementTextToEqual(driver, By.id("clickDisplay"), "clicked area 2"));
    }
}
