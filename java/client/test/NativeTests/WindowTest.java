package NativeTests;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.testing.JUnit4TestBase;

import java.util.logging.Logger;

import org.junit.Test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 4/1/13
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class WindowTest extends JUnit4TestBase {

    private static Logger log = Logger.getLogger(WindowTest.class.getName());

    @Test
    public void testGetsTheSizeOfTheCurrentWindow() {
        Dimension size = driver.manage().window().getSize();
        log.log(log.getLevel(), "Driver: " + driver);

        assertThat(size.width, is(greaterThan(0)));
        assertThat(size.height, is(greaterThan(0)));
    }
}
