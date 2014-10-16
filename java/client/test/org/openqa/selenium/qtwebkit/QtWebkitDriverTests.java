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

package org.openqa.selenium.qtwebkit;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.StandardSeleniumTests;
import org.openqa.selenium.environment.GlobalTestEnvironment;
import org.openqa.selenium.environment.InProcessTestEnvironment;
import org.openqa.selenium.qtwebkit.touch.TouchTests;
import java.util.logging.Logger;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StandardSeleniumTests.class,
        GraphicsWebSanityTest.class,
        TouchTests.class,
        QtWebDriverSwitchesTest.class,
        HTML5VideoTagTest.class,
        HTML5AudioTagTest.class,
})

public class QtWebkitDriverTests {

    private static final String testsName = QtWebkitDriverTests.class.getName();
    private static final Logger logger = Logger.getLogger(testsName);

    @AfterClass
    public static void flushReport() {
      GlobalTestEnvironment.get(InProcessTestEnvironment.class).getStatisticCommands().writeToXml(testsName);
    }
}
