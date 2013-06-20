/**
 * Created with IntelliJ IDEA.
 * User: liberus
 * Date: 3/25/13
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
package org.openqa.selenium.testing.drivers;

import com.google.common.base.Supplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;


public class QtWebKitDriverSupplier implements Supplier<WebDriver>{

    private Capabilities desiredCapabilities;
    private Capabilities requiredCapabilities;

    public QtWebKitDriverSupplier(Capabilities desiredCapabilities,
                          Capabilities requiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
        this.requiredCapabilities = requiredCapabilities;
    }

    public WebDriver get() {
        if (desiredCapabilities == null ){
            System.out.println("Wrong capability was set!");
            Thread.dumpStack();
            return null;
        }

        if (!DesiredCapabilities.qtwebkit().getBrowserName().equals(desiredCapabilities.getBrowserName())) {
            System.out.println("Wrong capability was set!");
            Thread.dumpStack();
            return null;
        }

        String remoteIpProperty = "RemoteIP";
        String ip = System.getProperty(remoteIpProperty);
        if (ip == null)
            System.setProperty(remoteIpProperty, "http://localhost:9517");

        RemoteWebDriver driver = new QtWebKitDriver(desiredCapabilities);
		driver.setFileDetector(new LocalFileDetector());

        return driver;
    }
}
