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

import org.openqa.selenium.environment.Pages;
import org.openqa.selenium.environment.webserver.AppServer;

public class Quick1TestContent extends Pages {


    public Quick1TestContent(AppServer appServer) {
        super(appServer);

        windowTest = appServer.whereIs("quick1/WindowTest.qml");
        findingTest = appServer.whereIs("quick1/FindingTest.qml");
        clickTest = appServer.whereIs("quick1/ClickTest.qml");
        coordinatesTest = appServer.whereIs("quick1/CoordinatesTest.qml");
        elementAttributeTest = appServer.whereIs("quick1/ElementAttributeTest.qml");
        typingTest = appServer.whereIs("quick1/TypingTest.qml");
        formHandlingTest = appServer.whereIs("quick1/FormHandlingTest.qml");
        xpathElementFindingTest = appServer.whereIs("quick1/XPathElementFindingTest.qml");
        staleElementReferenceTest = appServer.whereIs("quick1/StaleElementReferenceTest.qml");
        visibilityTest = appServer.whereIs("quick1/VisibilityTest.qml");
        javascriptTest = appServer.whereIs("quick1/JavascriptTest.qml");
        basicKeyboardInterfaceTest = appServer.whereIs("quick1/BasicKeyboardInterfaceTest.qml");
        basicMouseInterfaceTest = appServer.whereIs("quick1/BasicMouseInterfaceTest.qml");
    }
}
