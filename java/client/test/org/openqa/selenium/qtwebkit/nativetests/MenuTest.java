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

import org.openqa.selenium.*;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Before;
import org.openqa.selenium.testing.TestUtilities;
import org.openqa.selenium.testing.drivers.SauceDriver;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;

public class MenuTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(MenuTest.class.getName());

    @Before
    public void setUp()
    {
        driver.get("qtwidget://MenuTestWidget");
    }

    @Test
    public void testFindMenuItem() {
        WebElement menuItem = driver.findElement(By.xpath("//QAction[@text='Action_1_1']"));

        assertThat(menuItem.getText(), is(equalTo("Action_1_1")));
    }

    @Test
    public void testFindNestedMenuItem() {
        WebElement menuItem = driver.findElement(By.xpath("//QAction[@text='Action_2_1_1']"));

        assertThat(menuItem.getText(), is(equalTo("Action_2_1_1")));
    }

    @Test
    public void testActivateMenuItem() {
        WebElement menuItem = driver.findElement(By.xpath("//QAction[@text='Action_1_2']"));
        WebElement statusEl = driver.findElement(By.id("statusLabel"));

        menuItem.click();

        assertThat(statusEl.getText(), is(equalTo("Menu 1, Action 2")));
    }

    @Test
    public void testActivateNestedMenuItem() {
        WebElement menuItem = driver.findElement(By.xpath("//QAction[@text='Action_2_1_1']"));
        WebElement statusEl = driver.findElement(By.id("statusLabel"));

        menuItem.click();

        assertThat(statusEl.getText(), is(equalTo("Menu 2-1, Action 1")));
    }

}
