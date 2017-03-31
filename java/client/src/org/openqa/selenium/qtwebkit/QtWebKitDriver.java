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

/*
Copyright 2012 Selenium committers
Copyright 2012 Software Freedom Conservancy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.openqa.selenium.qtwebkit;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.AppCacheStatus;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.HasMultiTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.MultiTouchScreen;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.RemoteMultiTouchScreen;
import org.openqa.selenium.remote.html5.RemoteLocalStorage;
import org.openqa.selenium.remote.html5.RemoteSessionStorage;

/**
 * QtWebKitDriver implements BrowserConnection with some limitation - setOnline() can change
 * connection state only for currently opened windows. If new view will be open now, it sets online
 * by default (while doesn't receive a command explicitly).
 */

public class QtWebKitDriver extends RemoteWebDriver

    implements TakesScreenshot, WebStorage, Rotatable, ApplicationCache,
               HasMultiTouchScreen, Visualizer {

    private RemoteLocalStorage localStorage;
    private RemoteSessionStorage sessionStorage;
    private MultiTouchScreen multiTouchScreen;

    /**
     * Custom capability that defines support different view and qtVersion.
     * <p>In qtVersion important only first digit .
     */
    public final static String HYBRID = "hybrid";

    /**
     * Custom capability.
     * WD checks this caps, if it is not specified,
     * in case of attempt to create second session we get exception of "one session only",
     * otherwise prev session will be terminated without closing windows
     * and new session can reuse those windows
     */
    public final static String REUSE_UI = "reuseUI";

    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities,
                          Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
        localStorage = new RemoteLocalStorage(getExecuteMethod());
        sessionStorage = new RemoteSessionStorage(getExecuteMethod());
        multiTouchScreen = new RemoteMultiTouchScreen(getExecuteMethod());
    }

    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        this(executor, desiredCapabilities, null);
    }

    public QtWebKitDriver(Capabilities desiredCapabilities) {
        this(createDefaultExecutor(), desiredCapabilities);
    }

    public QtWebKitDriver(URL remoteAddress, Capabilities desiredCapabilities,
                          Capabilities requiredCapabilities) {
        this(new QtWebDriverExecutor(remoteAddress), desiredCapabilities, requiredCapabilities);
    }

    public QtWebKitDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        this(new QtWebDriverExecutor(remoteAddress), desiredCapabilities, null);
    }

    public <X> X getScreenshotAs(OutputType<X> target) {
        // Get the screenshot as base64.
        String base64 = (String) execute(DriverCommand.SCREENSHOT).getValue();
        // ... and convert it.
        return target.convertFromBase64Png(base64);
    }

    @Override
    public LocalStorage getLocalStorage() {
        return localStorage;
    }

    @Override
    public SessionStorage getSessionStorage() {
        return sessionStorage;
    }

    @Override
    public void rotate(ScreenOrientation orientation) {
        execute(DriverCommand.SET_SCREEN_ORIENTATION, ImmutableMap.of("orientation", orientation));
    }

    @Override
    public ScreenOrientation getOrientation() {
        return ScreenOrientation.valueOf(
            (String) execute(DriverCommand.GET_SCREEN_ORIENTATION).getValue());
    }

    @Override
    public AppCacheStatus getStatus() {
        Long status = (Long) execute(DriverCommand.GET_APP_CACHE_STATUS).getValue();
        long st = status;
        return AppCacheStatus.getEnum((int)st);
    }

    @Override
    public MultiTouchScreen getMultiTouch() {
        return multiTouchScreen;
    }

    @Override
    public TouchScreen getTouch() {
        return multiTouchScreen;
    }

    @Override
    public String getVisualizerSource() {
        return (String) execute(QtWebKitDriverCommand.GET_VISUALIZER_SOURCE).getValue();
    }

    public static QtWebDriverExecutor createDefaultExecutor() {
        try{
            String ip = System.getProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY);
            if (ip == null) {
              ip = "http://localhost:9517";
              System.setProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY, ip);
            }
            URL url = new URL(ip);
            return new QtWebDriverExecutor(url);
        }
        catch (MalformedURLException e) {
            return new QtWebDriverServiceExecutor(QtWebDriverService.createDefaultService());
        }
    }

    public static QtWebDriverExecutor createExecutor(Map<String, String> environment) {
        return new QtWebDriverServiceExecutor(QtWebDriverService.createService(environment));
    }

}