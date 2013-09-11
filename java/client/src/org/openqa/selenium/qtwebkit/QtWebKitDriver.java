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
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteLocalStorage;
import org.openqa.selenium.remote.html5.RemoteSessionStorage;


public class QtWebKitDriver extends RemoteWebDriver implements TakesScreenshot, WebStorage, HasTouchScreen, Rotatable {

    private RemoteLocalStorage localStorage;
    private RemoteSessionStorage sessionStorage;
    private TouchScreen touchScreen;

    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
        localStorage = new RemoteLocalStorage(getExecuteMethod());
        sessionStorage = new RemoteSessionStorage(getExecuteMethod());
        touchScreen = new RemoteTouchScreen(getExecuteMethod());
      setElementConverter(new QtWebKitJsonToWebElementConverter(this));
    }

    public QtWebKitDriver(CommandExecutor executor, Capabilities desiredCapabilities) {
	    this(executor, desiredCapabilities, null);
    }

    public QtWebKitDriver(Capabilities desiredCapabilities) {
	    this((URL) null, desiredCapabilities);
    }

    public QtWebKitDriver(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
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
}