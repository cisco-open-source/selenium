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

package org.openqa.selenium.qtwebkit.hybridtests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewTransitionsTest  extends JUnit4TestBase {

    String[] viewUrls;

    @Before
    public void setUp() {
        String current = "";
        try {
            current = new java.io.File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        viewUrls = new String[] {
            "qtwidget://TextHandlingTestWidget",                        // widget
            pages.colorPage,                                            // remote html
            "file://"+current+"/common/src/web/banner.gif",             // local html
            appServer.whereIs("quick1/FindingTest.qml"),                // remote qml
            "file://"+current+"/common/src/web/quick1/FindingTest.qml"  // local qml
        };
    }

    @Test
    public void testViewTransitions() {
        int srcLength = viewUrls.length;
        int firstView = 0;
        int secondView = 1;

        while(firstView < srcLength) {
            driver.get(viewUrls[firstView]);
            assertEquals(viewUrls[firstView], driver.getCurrentUrl());

            driver.get(viewUrls[secondView]);
            assertEquals(viewUrls[secondView], driver.getCurrentUrl());

            // get next pair
            secondView++;
            if (secondView == firstView) secondView++;
            if (secondView >= srcLength) {
                firstView++;
                secondView = 0;
            }
        }
    }

}
