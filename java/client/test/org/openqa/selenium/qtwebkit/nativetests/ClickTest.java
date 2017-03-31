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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import org.openqa.selenium.*;
import org.openqa.selenium.testing.JUnit4TestBase;

public class ClickTest extends JUnit4TestBase {


    @Before
     public void setUp() throws Exception {
        driver.get("qtwidget://ClickTestWidget");
    }

    @Test
    public void testCanClickOnAPushButton() {
        driver.findElement(By.id("pushBtn")).click();

        wait.until(titleIs("CLick Test Window"));
    }

    @Test
    public void testCanClickOnACheckBox() {
        driver.findElement(By.id("checkBox")).click();

        wait.until(titleIs("CLick Test Window"));
    }

    @Test
    public void testClickingOnButtonInScrollArea() {
        driver.findElement(By.id("btnOnScroll")).click();

        wait.until(titleIs("CLick Test Window"));
    }
}
