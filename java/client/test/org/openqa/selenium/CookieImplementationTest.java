/*
Copyright 2007-2009 Selenium committers

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
package org.openqa.selenium;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.environment.DomainHelper;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.openqa.selenium.testing.JavascriptEnabled;

import java.net.URI;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.openqa.selenium.testing.Ignore.Driver.ALL;
import static org.openqa.selenium.testing.Ignore.Driver.ANDROID;
import static org.openqa.selenium.testing.Ignore.Driver.CHROME;
import static org.openqa.selenium.testing.Ignore.Driver.FIREFOX;
import static org.openqa.selenium.testing.Ignore.Driver.HTMLUNIT;
import static org.openqa.selenium.testing.Ignore.Driver.IE;
import static org.openqa.selenium.testing.Ignore.Driver.IPHONE;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA;
import static org.openqa.selenium.testing.Ignore.Driver.OPERA_MOBILE;
import static org.openqa.selenium.testing.Ignore.Driver.PHANTOMJS;
import static org.openqa.selenium.testing.Ignore.Driver.QTWEBKIT;
import static org.openqa.selenium.testing.Ignore.Driver.REMOTE;
import static org.openqa.selenium.testing.Ignore.Driver.SAFARI;

public class CookieImplementationTest extends JUnit4TestBase {

  private DomainHelper domainHelper;
  private String cookiePage;
  private static final Random random = new Random();

  @Before
  public void setUp() throws Exception {
    domainHelper = new DomainHelper(appServer);
    assumeTrue(domainHelper.checkIsOnValidHostname());
    cookiePage = domainHelper.getUrlForFirstValidHostname("/common/cookie");

    deleteAllCookiesOnServerSide();

    // This page is the deepest page we go to in the cookie tests
    // We go to it to ensure that cookies with /common/... paths are deleted
    // Do not write test in this class which use pages other than under /common
    // without ensuring that cookies are deleted on those pages as required
    try {
      driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));
    } catch (IllegalArgumentException e) {
      // Ideally we would throw an IgnoredTestError or something here,
      // but our test runner doesn't pay attention to those.
      // Rely on the tests skipping themselves if they need to be on a useful page.
      return;
    }

    driver.manage().deleteAllCookies();
    assertNoCookiesArePresent();
  }

  @JavascriptEnabled
  @Test
  public void testShouldGetCookieByName() {
    String key = generateUniqueKey();
    String value = "set";
    assertCookieIsNotPresentWithName(key);

    addCookieOnServerSide(new Cookie(key, value));

    Cookie cookie = driver.manage().getCookieNamed(key);
    assertEquals(value, cookie.getValue());
  }

  @JavascriptEnabled
  @Test
  public void testShouldBeAbleToAddCookie() {
    String key = generateUniqueKey();
    String value = "foo";
    Cookie cookie = new Cookie.Builder(key, value).build();
    assertCookieIsNotPresentWithName(key);

    driver.manage().addCookie(cookie);

    assertCookieHasValue(key, value);

    openAnotherPage();
    assertCookieHasValue(key, value);
  }

  @Test
  public void testGetAllCookies() {
    String key1 = generateUniqueKey();
    String key2 = generateUniqueKey();

    assertCookieIsNotPresentWithName(key1);
    assertCookieIsNotPresentWithName(key2);

    Set<Cookie> cookies = driver.manage().getCookies();
    int countBefore = cookies.size();

    Cookie one = new Cookie.Builder(key1, "value").build();
    Cookie two = new Cookie.Builder(key2, "value").build();

    driver.manage().addCookie(one);
    driver.manage().addCookie(two);

    openAnotherPage();
    cookies = driver.manage().getCookies();
    assertEquals(countBefore + 2, cookies.size());

    assertTrue(cookies.contains(one));
    assertTrue(cookies.contains(two));
  }

  @JavascriptEnabled
  @Test
  public void testDeleteAllCookies() {
    addCookieOnServerSide(new Cookie("foo", "set"));
    assertSomeCookiesArePresent();

    driver.manage().deleteAllCookies();

    assertNoCookiesArePresent();

    openAnotherPage();
    assertNoCookiesArePresent();
  }

  @JavascriptEnabled
  @Test
  public void testDeleteCookieWithName() {
    String key1 = generateUniqueKey();
    String key2 = generateUniqueKey();

    addCookieOnServerSide(new Cookie(key1, "set"));
    addCookieOnServerSide(new Cookie(key2, "set"));

    assertCookieIsPresentWithName(key1);
    assertCookieIsPresentWithName(key2);

    driver.manage().deleteCookieNamed(key1);

    assertCookieIsNotPresentWithName(key1);
    assertCookieIsPresentWithName(key2);

    openAnotherPage();
    assertCookieIsNotPresentWithName(key1);
    assertCookieIsPresentWithName(key2);
  }

  @Test
  public void testShouldNotDeleteCookiesWithASimilarName() {
    String cookieOneName = "fish";
    Cookie cookie1 = new Cookie.Builder(cookieOneName, "cod").build();
    Cookie cookie2 = new Cookie.Builder(cookieOneName + "x", "earth").build();
    WebDriver.Options options = driver.manage();
    assertCookieIsNotPresentWithName(cookie1.getName());

    options.addCookie(cookie1);
    options.addCookie(cookie2);

    assertCookieIsPresentWithName(cookie1.getName());

    options.deleteCookieNamed(cookieOneName);
    Set<Cookie> cookies = options.getCookies();

    assertFalse(cookies.toString(), cookies.contains(cookie1));
    assertTrue(cookies.toString(), cookies.contains(cookie2));
  }

  @Ignore(OPERA)
  @Test
  public void testAddCookiesWithDifferentPathsThatAreRelatedToOurs() {
    driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));
    Cookie cookie1 = new Cookie.Builder("fish", "cod").path("/common/animals").build();
    Cookie cookie2 = new Cookie.Builder("planet", "earth").path("/common/").build();
    WebDriver.Options options = driver.manage();
    options.addCookie(cookie1);
    options.addCookie(cookie2);

    driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));

    assertCookieIsPresentWithName(cookie1.getName());
    assertCookieIsPresentWithName(cookie2.getName());

    driver.get(domainHelper.getUrlForFirstValidHostname("/common/simplePage.html"));
    assertCookieIsNotPresentWithName(cookie1.getName());
  }

  @Ignore(value = {ANDROID, CHROME, OPERA, OPERA_MOBILE, PHANTOMJS, SAFARI})
  @Test
  public void testGetCookiesInAFrame() {
    driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));
    Cookie cookie1 = new Cookie.Builder("fish", "cod").path("/common/animals").build();
    driver.manage().addCookie(cookie1);

    driver.get(domainHelper.getUrlForFirstValidHostname("frameWithAnimals.html"));
    assertCookieIsNotPresentWithName(cookie1.getName());

    driver.switchTo().frame("iframe1");
    assertCookieIsPresentWithName(cookie1.getName());
  }

  @Ignore({CHROME, OPERA})
  @Test
  public void testCannotGetCookiesWithPathDifferingOnlyInCase() {
    String cookieName = "fish";
    Cookie cookie = new Cookie.Builder(cookieName, "cod").path("/Common/animals").build();
    driver.manage().addCookie(cookie);

    driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));
    assertNull(driver.manage().getCookieNamed(cookieName));
  }

  @Test
  //TODO: Fails because of secon hostname isn't tranformed from domain name to ipv4 adrres,
  public void testShouldNotGetCookieOnDifferentDomain() {
    assumeTrue(domainHelper.checkHasValidAlternateHostname());

    String cookieName = "fish";
    driver.manage().addCookie(new Cookie.Builder(cookieName, "cod").build());
    assertCookieIsPresentWithName(cookieName);

    driver.get(domainHelper.getUrlForSecondValidHostname("simpleTest.html"));

    assertCookieIsNotPresentWithName(cookieName);
  }

  @Ignore(value = {ANDROID, CHROME, HTMLUNIT, IE, IPHONE, OPERA},
        reason = "Untested browsers.")
  @Test
  public void testShouldBeAbleToAddToADomainWhichIsRelatedToTheCurrentDomain() {
    String cookieName = "name";
    assertCookieIsNotPresentWithName(cookieName);

    String shorter = domainHelper.getHostName().replaceFirst(".*?\\.", ".");
    Cookie cookie = new Cookie.Builder(cookieName, "value").domain(shorter).build();
    driver.manage().addCookie(cookie);

    assertCookieIsPresentWithName(cookieName);
  }

  @Ignore(value = {ALL})
  @Test
  public void testsShouldNotGetCookiesRelatedToCurrentDomainWithoutLeadingPeriod() {
    String cookieName = "name";
    assertCookieIsNotPresentWithName(cookieName);

    String shorter = domainHelper.getHostName().replaceFirst(".*?\\.", "");
    Cookie cookie = new Cookie.Builder(cookieName, "value").domain(shorter).build();
    driver.manage().addCookie(cookie);
    assertCookieIsNotPresentWithName(cookieName);
  }

  @Ignore({REMOTE, IE})
  @Test
  public void testShouldBeAbleToIncludeLeadingPeriodInDomainName() throws Exception {
    String cookieName = "name";
    assertCookieIsNotPresentWithName(cookieName);

    String shorter = domainHelper.getHostName().replaceFirst(".*?\\.", ".");
    Cookie cookie = new Cookie.Builder("name", "value").domain(shorter).build();

    driver.manage().addCookie(cookie);

    assertCookieIsPresentWithName(cookieName);
  }

  @Ignore(IE)
  @Test
  public void testShouldBeAbleToSetDomainToTheCurrentDomain() throws Exception {
    URI url = new URI(driver.getCurrentUrl());
    String host = url.getHost() + ":" + url.getPort();

    Cookie cookie = new Cookie.Builder("fish", "cod").domain(host).build();
    driver.manage().addCookie(cookie);

    driver.get(domainHelper.getUrlForFirstValidHostname("javascriptPage.html"));
    Set<Cookie> cookies = driver.manage().getCookies();
    assertTrue(cookies.contains(cookie));
  }

  @Test
  public void testShouldWalkThePathToDeleteACookie() {
    Cookie cookie1 = new Cookie.Builder("fish", "cod").build();
    driver.manage().addCookie(cookie1);

    driver.get(domainHelper.getUrlForFirstValidHostname("child/childPage.html"));
    Cookie cookie2 = new Cookie("rodent", "hamster", "/common/child");
    driver.manage().addCookie(cookie2);

    driver.get(domainHelper.getUrlForFirstValidHostname("child/grandchild/grandchildPage.html"));
    Cookie cookie3 = new Cookie("dog", "dalmation", "/common/child/grandchild/");
    driver.manage().addCookie(cookie3);

    driver.get(domainHelper.getUrlForFirstValidHostname("child/grandchild/grandchildPage.html"));
    driver.manage().deleteCookieNamed("rodent");

    assertNull(driver.manage().getCookies().toString(), driver.manage().getCookieNamed("rodent"));

    Set<Cookie> cookies = driver.manage().getCookies();
    assertEquals(2, cookies.size());
    assertTrue(cookies.contains(cookie1));
    assertTrue(cookies.contains(cookie3));

    driver.manage().deleteAllCookies();
    driver.get(domainHelper.getUrlForFirstValidHostname("child/grandchild/grandchildPage.html"));
    assertNoCookiesArePresent();
  }

  @Ignore(IE)
  @Test
  public void testShouldIgnoreThePortNumberOfTheHostWhenSettingTheCookie() throws Exception {
    URI uri = new URI(driver.getCurrentUrl());
    String host = String.format("%s:%d", uri.getHost(), uri.getPort());

    String cookieName = "name";
    assertCookieIsNotPresentWithName(cookieName);

    Cookie cookie = new Cookie.Builder(cookieName, "value").domain(host).build();
    driver.manage().addCookie(cookie);

    assertCookieIsPresentWithName(cookieName);
  }

  @Ignore(OPERA)
  @Test
  public void testCookieEqualityAfterSetAndGet() {
    driver.get(domainHelper.getUrlForFirstValidHostname("animals"));

    driver.manage().deleteAllCookies();

    Cookie addedCookie =
        new Cookie.Builder("fish", "cod")
            .path("/common/animals")
            .expiresOn(someTimeInTheFuture())
            .build();
    driver.manage().addCookie(addedCookie);

    Set<Cookie> cookies = driver.manage().getCookies();
    Cookie retrievedCookie = null;
    for (Cookie temp : cookies) {
      if (addedCookie.equals(temp)) {
        retrievedCookie = temp;
        break;
      }
    }

    assertNotNull("Cookie was null", retrievedCookie);
    // Cookie.equals only compares name, domain and path
    assertEquals(addedCookie, retrievedCookie);
  }

  @Ignore(value = {ANDROID, IE, OPERA}, reason =
      "Selenium, which use JavaScript to retrieve cookies, cannot return expiry info; " +
          "Other suppressed browsers have not been tested.")
  @Test
  public void testRetainsCookieExpiry() {
    Cookie addedCookie =
        new Cookie.Builder("fish", "cod")
            .path("/common/animals")
            .expiresOn(someTimeInTheFuture())
            .build();
    driver.manage().addCookie(addedCookie);

    Cookie retrieved = driver.manage().getCookieNamed("fish");
    assertNotNull(retrieved);
    assertEquals(addedCookie.getExpiry(), retrieved.getExpiry());
  }

  @Ignore(value = {ANDROID, IE, OPERA, OPERA_MOBILE, PHANTOMJS, SAFARI})
  @Test
  public void testRetainsCookieSecure() {
    driver.get(domainHelper.getSecureUrlForFirstValidHostname("animals"));

    Cookie addedCookie =
        new Cookie.Builder("fish", "cod")
            .path("/common/animals")
            .isSecure(true)
            .build();
    driver.manage().addCookie(addedCookie);

    driver.navigate().refresh();

    Cookie retrieved = driver.manage().getCookieNamed("fish");
    assertNotNull(retrieved);
    assertTrue(retrieved.isSecure());
  }

  @Ignore(value = {ANDROID, CHROME, FIREFOX, HTMLUNIT, IE, OPERA, OPERA_MOBILE, PHANTOMJS, SAFARI})
  @Test
  public void testRetainsHttpOnlyFlag() {
    Cookie addedCookie =
        new Cookie.Builder("fish", "cod")
            .path("/common/animals")
            .isHttpOnly(true)
            .build();

    addCookieOnServerSide(addedCookie);

    Cookie retrieved = driver.manage().getCookieNamed("fish");
    assertNotNull(retrieved);
    assertTrue(retrieved.isHttpOnly());
  }

  @Ignore(value = {ANDROID, QTWEBKIT},
          reason = "QTWEBKIT: looks like browser itself doesn't support setting expired cookie")
  @Test
  public void testSettingACookieThatExpiredInThePast() {
    long expires = System.currentTimeMillis() - 1000;
    Cookie cookie = new Cookie.Builder("expired", "yes").expiresOn(new Date(expires)).build();
    driver.manage().addCookie(cookie);

    cookie = driver.manage().getCookieNamed("fish");
    assertNull(
        "Cookie expired before it was set, so nothing should be returned: " + cookie, cookie);
  }

  @Test
  public void testCanSetCookieWithoutOptionalFieldsSet() {
    String key = generateUniqueKey();
    String value = "foo";
    Cookie cookie = new Cookie(key, value);
    assertCookieIsNotPresentWithName(key);

    driver.manage().addCookie(cookie);

    assertCookieHasValue(key, value);
  }

  @Test
  public void testDeleteNotExistedCookie() {
    String key = generateUniqueKey();
    assertCookieIsNotPresentWithName(key);

    driver.manage().deleteCookieNamed(key);
  }

  @Ignore(value = {ANDROID, CHROME, FIREFOX, IE, OPERA, OPERA_MOBILE, PHANTOMJS, SAFARI})
  @Test
  public void testShouldDeleteOneOfTheCookiesWithTheSameName() {
    driver.get(domainHelper.getUrlForFirstValidHostname("/common/animals"));
    Cookie cookie1 = new Cookie.Builder("fish", "cod")
        .domain(domainHelper.getHostName()).path("/common/animals").build();
    Cookie cookie2 = new Cookie.Builder("fish", "tune")
        .domain(domainHelper.getHostName()).path("/common/").build();
    WebDriver.Options options = driver.manage();
    options.addCookie(cookie1);
    options.addCookie(cookie2);
    assertEquals(driver.manage().getCookies().size(), 2);

    driver.manage().deleteCookie(cookie1);

    assertEquals(driver.manage().getCookies().size(), 1);
    Cookie retrieved = driver.manage().getCookieNamed("fish");
    assertNotNull("Cookie was null", retrieved);
    assertEquals(cookie2, retrieved);
  }

  private String generateUniqueKey() {
    return String.format("key_%d", random.nextInt());
  }

  private void assertNoCookiesArePresent() {
    Set<Cookie> cookies = driver.manage().getCookies();
    assertTrue("Cookies were not empty, present: " + cookies,
               cookies.isEmpty());
    String documentCookie = getDocumentCookieOrNull();
    if (documentCookie != null) {
      assertEquals("Cookies were not empty", "", documentCookie);
    }
  }

  private void assertSomeCookiesArePresent() {
    assertFalse("Cookies were empty",
                driver.manage().getCookies().isEmpty());
    String documentCookie = getDocumentCookieOrNull();
    if (documentCookie != null) {
      assertNotSame("Cookies were empty", "", documentCookie);
    }
  }

  private void assertCookieIsNotPresentWithName(final String key) {
    assertNull("Cookie was present with name " + key, driver.manage().getCookieNamed(key));
    String documentCookie = getDocumentCookieOrNull();
    if (documentCookie != null) {
      assertThat("Cookie was present with name " + key,
                 documentCookie,
                 not(containsString(key + "=")));
    }
  }

  private void assertCookieIsPresentWithName(final String key) {
    assertNotNull("Cookie was not present with name " + key, driver.manage().getCookieNamed(key));
    String documentCookie = getDocumentCookieOrNull();
    if (documentCookie != null) {
      assertThat("Cookie was not present with name " + key + ", got: " + documentCookie,
          documentCookie,
          containsString(key + "="));
    }
  }

  private void assertCookieHasValue(final String key, final String value) {
    assertEquals("Cookie had wrong value",
                 value,
                 driver.manage().getCookieNamed(key).getValue());
    String documentCookie = getDocumentCookieOrNull();
    if (documentCookie != null) {
      assertThat("Cookie was present with name " + key,
          documentCookie,
          containsString(key + "=" + value));
    }
  }

  private String getDocumentCookieOrNull() {
    if (!(driver instanceof JavascriptExecutor)) {
      return null;
    }
    try {
      return (String) ((JavascriptExecutor) driver).executeScript("return document.cookie");
    } catch (UnsupportedOperationException e) {
      return null;
    }
  }

  private Date someTimeInTheFuture() {
    return new Date(System.currentTimeMillis() + 100000);
  }

  private void openAnotherPage() {
    driver.get(domainHelper.getUrlForFirstValidHostname("simpleTest.html"));
  }

  private void deleteAllCookiesOnServerSide() {
    driver.get(cookiePage + "?action=deleteAll");
  }

  private void addCookieOnServerSide(Cookie cookie) {
    StringBuilder url = new StringBuilder(cookiePage);
    url.append("?action=add");
    url.append("&name=").append(cookie.getName());
    url.append("&value=").append(cookie.getValue());
    if (cookie.getDomain() != null) {
      url.append("&domain=").append(cookie.getDomain());
    }
    if (cookie.getPath() != null) {
      url.append("&path=").append(cookie.getPath());
    }
    if (cookie.getExpiry() != null) {
      url.append("&expiry=").append(cookie.getExpiry().getTime());
    }
    if (cookie.isSecure()) {
      url.append("&secure=").append(cookie.isSecure());
    }
    if (cookie.isHttpOnly()) {
      url.append("&httpOnly=").append(cookie.isHttpOnly());
    }
    driver.get(url.toString());
  }
}
