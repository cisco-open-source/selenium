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

package org.openqa.selenium.qtwebkit.quick_tests.interactions.touch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.AfterClass;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.touch.TouchInteractionTests;
import org.openqa.selenium.qtwebkit.quick_tests.interactions.touch.MultiTouchTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MultiTouchTests.class,
        TouchInteractionTests.class,
})
public class TouchTests {
    @AfterClass
    public static void cleanUpDriver() {
        JUnit4TestBase.removeDriver();
    }
}
