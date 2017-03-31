/*
Copyright 2012 Software Freedom Conservancy
Copyright 2007-2012 WebDriver committers
Copyright 2007-2009 Google Inc.

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

package org.openqa.selenium.environment.webserver;

import static org.openqa.selenium.net.PortProber.findFreePort;
import static org.openqa.selenium.testing.InProject.locate;

import org.openqa.selenium.net.NetworkUtils;
import org.openqa.selenium.testing.InProject;
import org.seleniumhq.jetty7.server.Server;
import org.seleniumhq.jetty7.server.handler.ContextHandlerCollection;
import org.seleniumhq.jetty7.server.nio.SelectChannelConnector;
import org.seleniumhq.jetty7.server.ssl.SslSocketConnector;
import org.seleniumhq.jetty7.servlet.DefaultServlet;
import org.seleniumhq.jetty7.servlet.ServletContextHandler;
import org.seleniumhq.jetty7.servlet.ServletHolder;
import org.seleniumhq.jetty7.servlets.MultiPartFilter;
import org.seleniumhq.jetty7.util.ssl.SslContextFactory;

import java.io.File;

import javax.servlet.Filter;
import javax.servlet.Servlet;

public class Jetty7AppServer implements AppServer {

  private static final String HOSTNAME_FOR_TEST_ENV_NAME = "HOSTNAME";
  private static final String ALTERNATIVE_HOSTNAME_FOR_TEST_ENV_NAME = "ALTERNATIVE_HOSTNAME";
  private static final String FIXED_HTTP_PORT_ENV_NAME = "TEST_HTTP_PORT";
  private static final String FIXED_HTTPS_PORT_ENV_NAME = "TEST_HTTPS_PORT";

  private static final int DEFAULT_HTTP_PORT = 2310;
  private static final int DEFAULT_HTTPS_PORT = 2410;
  private static final String DEFAULT_CONTEXT_PATH = "/common";
  private static final String JS_SRC_CONTEXT_PATH = "/javascript";
  private static final String CLOSURE_CONTEXT_PATH = "/third_party/closure/goog";
  private static final String THIRD_PARTY_JS_CONTEXT_PATH = "/third_party/js";

  private static final NetworkUtils networkUtils = new NetworkUtils();

  private int port;
  private int securePort;
  private final Server server;

  private ContextHandlerCollection handlers;
  private final String hostName;

  public Jetty7AppServer() {
    this(detectHostname());
  }

  public static String detectHostname() {
    String hostnameFromProperty = System.getenv(HOSTNAME_FOR_TEST_ENV_NAME);
    return hostnameFromProperty == null ? "localhost" : hostnameFromProperty;
  }

  public Jetty7AppServer(String hostName) {
    this.hostName = hostName;
    // Be quiet. Unless we want things to be chatty
    if (!Boolean.getBoolean("webdriver.debug")) {
      new NullLogger().disableLogging();
    }

    server = new Server();

    handlers = new ContextHandlerCollection();

    ServletContextHandler defaultContext = addResourceHandler(
        DEFAULT_CONTEXT_PATH, locate("common/src/web"));
    ServletContextHandler jsContext = addResourceHandler(
        JS_SRC_CONTEXT_PATH, locate("javascript"));
    addResourceHandler(CLOSURE_CONTEXT_PATH, locate("third_party/closure/goog"));
    addResourceHandler(THIRD_PARTY_JS_CONTEXT_PATH, locate("third_party/js"));

    server.setHandler(handlers);

    addServlet(defaultContext, "/redirect", RedirectServlet.class);
    addServlet(defaultContext, "/page/*", PageServlet.class);

    addServlet(defaultContext, "/manifest/*", ManifestServlet.class);
    addServlet(defaultContext, "*.appcache", ManifestServlet.class);
    addServlet(jsContext, "*.appcache", ManifestServlet.class);
    // Serves every file under DEFAULT_CONTEXT_PATH/utf8 as UTF-8 to the browser
    addServlet(defaultContext, "/utf8/*", Utf8Servlet.class);

    addServlet(defaultContext, "/upload", UploadServlet.class);
    addServlet(defaultContext, "/encoding", EncodingServlet.class);
    addServlet(defaultContext, "/sleep", SleepingServlet.class);
    addServlet(defaultContext, "/cookie", CookieServlet.class);
    addServlet(defaultContext, "/quitquitquit", KillSwitchServlet.class);
    addServlet(defaultContext, "/basicAuth", BasicAuth.class);
    addServlet(defaultContext, "/generated/*", GeneratedJsTestServlet.class);

    addFilter(defaultContext, MultiPartFilter.class, "/upload", 0 /* DEFAULT dispatches */);

    listenOn(getHttpPort());
    listenSecurelyOn(getHttpsPort());
  }

  private int getHttpPort() {
    String port = System.getenv(FIXED_HTTP_PORT_ENV_NAME);
    return port == null ? findFreePort() : Integer.parseInt(port);
  }

  private int getHttpsPort() {
    String port = System.getenv(FIXED_HTTPS_PORT_ENV_NAME);
    return port == null ? findFreePort() : Integer.parseInt(port);
  }

  @Override
  public String getHostName() {
    return hostName;
  }

  @Override
  public String getAlternateHostName() {
    String alternativeHostnameFromProperty = System.getenv(ALTERNATIVE_HOSTNAME_FOR_TEST_ENV_NAME);
    return alternativeHostnameFromProperty == null ?
           networkUtils.getPrivateLocalAddress() : alternativeHostnameFromProperty;
  }

  @Override
  public String whereIs(String relativeUrl) {
    relativeUrl = getMainContextPath(relativeUrl);
    return "http://" + getHostName() + ":" + port + relativeUrl;
  }

  @Override
  public String whereElseIs(String relativeUrl) {
    relativeUrl = getMainContextPath(relativeUrl);
    return "http://" + getAlternateHostName() + ":" + port + relativeUrl;
  }

  @Override
  public String whereIsSecure(String relativeUrl) {
    relativeUrl = getMainContextPath(relativeUrl);
    return "https://" + getHostName() + ":" + securePort + relativeUrl;
  }

  @Override
  public String whereIsWithCredentials(String relativeUrl, String user, String pass) {
    relativeUrl = getMainContextPath(relativeUrl);
    return "http://" + user + ":" + pass + "@" + getHostName() + ":" + port + relativeUrl;
  }

  protected String getMainContextPath(String relativeUrl) {
    if (!relativeUrl.startsWith("/")) {
      relativeUrl = DEFAULT_CONTEXT_PATH + "/" + relativeUrl;
    }
    return relativeUrl;
  }

  @Override
  public void start() {
    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setPort(port);
    server.addConnector(connector);

    File keyStore = getKeyStore();
    if (!keyStore.exists()) {
      throw new RuntimeException(
          "Cannot find keystore for SSL cert: " + keyStore.getAbsolutePath());
    }

    SslContextFactory sslContextFactory = new SslContextFactory();
    sslContextFactory.setKeyStorePath(keyStore.getAbsolutePath());
    sslContextFactory.setKeyStorePassword("password");
    sslContextFactory.setKeyManagerPassword("password");
    sslContextFactory.setTrustStore(keyStore.getAbsolutePath());
    sslContextFactory.setTrustStorePassword("password");

    SslSocketConnector secureSocket = new SslSocketConnector(sslContextFactory);
    secureSocket.setPort(securePort);
    server.addConnector(secureSocket);

    try {
      server.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected File getKeyStore() {
    return InProject.locate("java/client/test/org/openqa/selenium/environment/webserver/keystore");
  }

  @Override
  public void listenOn(int port) {
    this.port = port;
  }

  @Override
  public void listenSecurelyOn(int port) {
    this.securePort = port;
  }

  @Override
  public void stop() {
    try {
      server.stop();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void addServlet(
      ServletContextHandler context, String url, Class<? extends Servlet> servletClass) {
    try {
      context.addServlet(new ServletHolder(servletClass), url);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void addFilter(
      ServletContextHandler context, Class<? extends Filter> filter, String path, int dispatches) {
    context.addFilter(filter, path, dispatches);
  }

  protected ServletContextHandler addResourceHandler(String contextPath, File resourceBase) {
    ServletContextHandler context = new ServletContextHandler();
    context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
    context.setInitParameter("org.eclipse.jetty.servlet.Default.aliases", "true");
    context.setInitParameter("org.eclipse.jetty.servlet.Default.pathInfoOnly", "true");

    context.setContextPath(contextPath);
    context.setResourceBase(resourceBase.getAbsolutePath());
    context.setAliases(true);
    context.getMimeTypes().addMimeMapping("qml", "text/x-qml");
    context.addServlet(new ServletHolder(new DefaultServlet()), "/*");

    handlers.addHandler(context);
    return context;
  }

  protected static int getHttpPortFromEnv() {
    String port = System.getenv(FIXED_HTTP_PORT_ENV_NAME);
    return port == null ? DEFAULT_HTTP_PORT : Integer.parseInt(port);
  }

  protected static int getHttpsPortFromEnv() {
    String port = System.getenv(FIXED_HTTPS_PORT_ENV_NAME);
    return port == null ? DEFAULT_HTTPS_PORT : Integer.parseInt(port);
  }

  public static void main(String[] args) {
    Jetty7AppServer server = new Jetty7AppServer(detectHostname());

    server.listenOn(getHttpPortFromEnv());
    System.out.println(String.format("Starting server on port %d", getHttpPortFromEnv()));

    server.listenSecurelyOn(getHttpsPortFromEnv());
    System.out.println(String.format("HTTPS on %d", getHttpsPortFromEnv()));

    server.start();
  }

}
