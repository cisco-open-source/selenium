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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.InteractionTests;
import org.openqa.selenium.testing.JUnit4TestBase;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        WindowTest.class,
        ElementEqualityTest.class,
        ClickTest.class,
        FindingTest.class,
        CoordinatesTest.class,
        TakesScreenshotTest.class,
        ElementAttributeTest.class,
        TypingTest.class,
        FormHandlingTest.class,
        MiscTest.class,
        XPathElementFindingTest.class,
        StaleElementReferenceTest.class,
        VisibilityTest.class,
        InteractionTests.class,
        ExecutingJavascriptTest.class
})
public class Quick1WebDriverTests {
    @BeforeClass
    public static void prepareCommonEnvironment() {
        InProcessTestEnvironment environment = GlobalTestEnvironment.get(InProcessTestEnvironment.class);
        environment.setTestContent(new Quick1TestContent(environment.getAppServer()));
    }

    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
        String testsName = Quick1WebDriverTests.class.getName();
        GlobalTestEnvironment.get(InProcessTestEnvironment.class).getStatisticCommands().writeToXml(testsName);
    }

}
