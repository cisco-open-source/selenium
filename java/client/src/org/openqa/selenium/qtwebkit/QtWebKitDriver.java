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

import org.openqa.selenium.html5.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteLocalStorage;
import org.openqa.selenium.remote.html5.RemoteSessionStorage;



public class QtWebKitDriver extends RemoteWebDriver implements TakesScreenshot, WebStorage {

    private RemoteLocalStorage localStorage;
    private RemoteSessionStorage sessionStorage;

    public QtWebKitDriver(Capabilities capabilities) {
        super(QtWebKitDriverService.getCommandExecutor(), capabilities);
        localStorage = new RemoteLocalStorage(getExecuteMethod());
        sessionStorage = new RemoteSessionStorage(getExecuteMethod());
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
}

