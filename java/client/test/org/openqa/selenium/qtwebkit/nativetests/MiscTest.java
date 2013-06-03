/*
Copyright 2012 Software Freedom Conservancy
Copyright 2007-2012 Selenium committers

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

package org.openqa.selenium.qtwebkit.nativetests;

import org.junit.Test;
import org.openqa.selenium.testing.JUnit4TestBase;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MiscTest extends JUnit4TestBase {


    public class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println(e.getMessage());
        }

        public void error(SAXParseException e) throws SAXException {
            System.out.println(e.getMessage());
        }

        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println(e.getMessage());
        }
    }

  @Test
  public void testShouldReturnTheSourceOfAPage() {
    driver.get("ClickScrollingTest");

    String source = driver.getPageSource().toLowerCase();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);

// the "parse" method also validates XML, will throw an exception if misformatted
      try{
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        org.w3c.dom.Document document = builder.parse(new InputSource(new ByteArrayInputStream(source.getBytes("utf-8"))));
      } catch(Exception e){
          fail("Exception " + e + " was throwed, when no exception expected!");
          e.printStackTrace();
      }
  }
}