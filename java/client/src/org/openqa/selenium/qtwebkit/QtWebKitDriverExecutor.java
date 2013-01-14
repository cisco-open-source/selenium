/*
Copyright 2011-2012 Selenium committers
Copyright 2011-2012 Software Freedom Conservancy

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


import org.openqa.selenium.WebDriverException;

import java.net.URL;
import java.net.MalformedURLException;

/*
* A specialized {@link HttpCommandExecutor} that will use a {@link ChromeDriverService} that lives
* and dies with a single WebDriver session. The service will be restarted upon each new session
* request and shutdown after each quit command.
*/

class QtWebKitDriverExecutor {

    /**
     * System property that defines the IP of the launched WebDriver.
     */
    public static final String REMOTE_IP_PROPERTY = "RemoteIP";

    /**
     * Creates a new ChromeCommandExecutor which will communicate with the chromedriver as configured
    */
    public QtWebKitDriverExecutor() {

    }

    public static URL getCommandExecutor()
    {
        try{
            String ip = System.getProperty(REMOTE_IP_PROPERTY);
            URL url = new URL(ip);
            return url;
        }
        catch (MalformedURLException e) {
            throw new WebDriverException("The Node was run with wrong RemoteIP parameter!", e);
        }
    }
}
