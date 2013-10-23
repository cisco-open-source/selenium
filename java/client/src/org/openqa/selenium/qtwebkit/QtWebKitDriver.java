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

import java.net.URL;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.*;
import org.openqa.selenium.html5.AppCacheStatus;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.BrowserConnection;
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

    implements TakesScreenshot, WebStorage, HasTouchScreen, Rotatable, ApplicationCache,
               BrowserConnection, HasMultiTouchScreen {

    private RemoteLocalStorage localStorage;
    private RemoteSessionStorage sessionStorage;
    private TouchScreen touchScreen;
    private MultiTouchScreen multiTouchScreen;

    /**
     * Custom capability that defines support different view and qtVersion. .
     */
    public final static String HYBRID = "hybrid";


    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities,
                          Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
        localStorage = new RemoteLocalStorage(getExecuteMethod());
        sessionStorage = new RemoteSessionStorage(getExecuteMethod());
        touchScreen = new RemoteTouchScreen(getExecuteMethod());
        multiTouchScreen = new RemoteMultiTouchScreen(getExecuteMethod());
        setElementConverter(new QtWebKitJsonToWebElementConverter(this));
    }

    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
        this(executor, desiredCapabilities, null);
    }

    public QtWebKitDriver(Capabilities desiredCapabilities) {
        this(QtWebKitDriverService.getCommandExecutor(), desiredCapabilities);
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
    public TouchScreen getTouch() {
        return touchScreen;
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
    public boolean isOnline() {
        return ((Boolean) execute(DriverCommand.IS_BROWSER_ONLINE).getValue());
    }

    @Override
    public void setOnline(boolean online) throws WebDriverException {
        execute(DriverCommand.SET_BROWSER_ONLINE, ImmutableMap.of("state", online));
    }
	
	@Override
    public MultiTouchScreen getMultiTouch() {
        return multiTouchScreen;
    }

}