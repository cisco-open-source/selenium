package org.openqa.selenium.qtwebkit.quick_tests;

import org.openqa.selenium.environment.Pages;
import org.openqa.selenium.environment.webserver.AppServer;

public class Quick2TestContent extends Pages {


    public Quick2TestContent(AppServer appServer) {
        super(appServer);

        windowTest = appServer.whereIs("quick2/WindowTest.qml");
        findingTest = appServer.whereIs("quick2/FindingTest.qml");
        clickTest = appServer.whereIs("quick2/ClickTest.qml");
        coordinatesTest = appServer.whereIs("quick2/CoordinatesTest.qml");
        elementAttributeTest = appServer.whereIs("quick2/ElementAttributeTest.qml");
        typingTest = appServer.whereIs("quick2/TypingTest.qml");
        formHandlingTest = appServer.whereIs("quick2/FormHandlingTest.qml");
        xpathElementFindingTest = appServer.whereIs("quick2/XPathElementFindingTest.qml");
        staleElementReferenceTest = appServer.whereIs("quick2/StaleElementReferenceTest.qml");
        visibilityTest = appServer.whereIs("quick2/VisibilityTest.qml");
        javascriptTest = appServer.whereIs("quick2/JavascriptTest.qml");
        basicKeyboardInterfaceTest = appServer.whereIs("quick2/BasicKeyboardInterfaceTest.qml");
        basicMouseInterfaceTest = appServer.whereIs("quick2/BasicMouseInterfaceTest.qml");
        videoTest = appServer.whereIs("quick2/VideoTest.qml");
    }
}
