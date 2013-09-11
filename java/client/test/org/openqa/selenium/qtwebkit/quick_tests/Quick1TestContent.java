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
