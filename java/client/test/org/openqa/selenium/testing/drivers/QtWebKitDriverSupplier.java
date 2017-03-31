package org.openqa.selenium.testing.drivers;

import com.google.common.base.Supplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.qtwebkit.QtWebDriverExecutor;
import org.openqa.selenium.qtwebkit.QtWebDriverService;
import org.openqa.selenium.qtwebkit.QtWebKitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.net.MalformedURLException;

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
            return null;
        }

        if (!DesiredCapabilities.qtwebkit().getBrowserName().equals(desiredCapabilities.getBrowserName())) {
            return null;
        }

        String ip = System.getProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY);
        if (ip == null) {
            ip = "http://localhost:9517";
            System.setProperty(QtWebDriverService.QT_DRIVER_EXE_PROPERTY, ip);
        }

        QtWebDriverExecutor executor = QtWebKitDriver.createDefaultExecutor();
        RemoteWebDriver driver = new QtWebKitDriver(executor, desiredCapabilities, requiredCapabilities);
		driver.setFileDetector(new LocalFileDetector());

        return driver;
    }
}
