package org.openqa.selenium.environment;
//package org.openqa.selenium.testing;

import org.openqa.selenium.remote.CommandInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class StatisticCommands {//implements Serializable {

//  private HashMap<String, ArrayList<String[]>> executedCommandMap = new HashMap<String, ArrayList<String[]>>();
  private HashMap<String, CommandData> executedCommandMap;// = new HashMap<String, CommandData>();
  public StatisticCommands() {
    executedCommandMap = new HashMap<String, CommandData>();
  }

  public void addCommand(String commandName, String url, String method, String[] test) {
    if (!executedCommandMap.containsKey(commandName)) {
      executedCommandMap.put(commandName, new CommandData(commandName, url, method,test));
    } else {
    executedCommandMap.get(commandName).addTestCases(test);
//    testsList.add(test);
    }
  }

  public void writeToXml(String testsName) {
    try {
      File outFile = new File("build/test_logs/" + testsName + "_CommandReport.xml");
      outFile.mkdirs();
      if (outFile.exists())
        outFile.delete();

      if (outFile.createNewFile()) {
        FileOutputStream outStream = new FileOutputStream(outFile);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(generateXmlReport()), new StreamResult(outStream));
        outStream.flush();
        outStream.close();
      }
    } catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } catch (TransformerConfigurationException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } catch (TransformerException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }


  private Document generateXmlReport() {
    try {
      Document finalReport = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      String props = "type=\"text/xsl\" href=\"" + "transform.xsl" + "\"";
      ProcessingInstruction
          instruction = finalReport.createProcessingInstruction("xml-stylesheet", props);
      finalReport.appendChild(instruction);

      Element tableElement = finalReport.createElement("table");
      finalReport.appendChild(tableElement);

//      Map<String, CommandInfo> commandNameToUrl = GlobalTestEnvironment
//          .get(InProcessTestEnvironment.class).getCommandNameToUrl();
//
//      HashMap<String, ArrayList<String[]>> executedCommandsMap = GlobalTestEnvironment
//          .get(InProcessTestEnvironment.class).getExecutedCommandMap();
//      Iterator it = executedCommandsMap.keySet().iterator();
      Iterator it = executedCommandMap.keySet().iterator();

      // process all commands
      while (it.hasNext()) {
        String key = (String)it.next();
//        CommandInfo commandInfo = commandNameToUrl.get(key);
//        String path = commandInfo.getUrl();
//        String method = commandInfo.getVerb().toString();
        CommandData commandData = executedCommandMap.get(key);
//        String path = command.getUrl();
//        String method = command.getMethod();

//        if (null != path && null != method) {
          Element command = finalReport.createElement("command");
          command.setAttribute("name", key);
          command.setAttribute("path", commandData.getUrl());
          command.setAttribute("method", commandData.getMethod());
//
//          // add command tests
//          for(String[] testInfo: executedCommandsMap.get(key)) {

        Element tests = finalReport.createElement("tests");
        // add tests
        for(String[] testInfo: commandData.getTestCases()) {
            Element test = finalReport.createElement("testcase");
            test.setAttribute("classname", testInfo[0]);
            test.setAttribute("name", testInfo[1]);
            tests.appendChild(test);
          }
        command.appendChild(tests);
          tableElement.appendChild(command);
 //       }

      } // while (it.hasNext())

      return finalReport;

    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public HashMap<String, CommandData> getExecutedCommand() {
    return executedCommandMap;
  }

  class CommandData {
    String commandName;
    String url;
    String method;
    ArrayList<String[]> testCases;

    CommandData(String commandName, String url, String method, String[] test) {
      this.commandName = commandName;
      this.url = url;
      this.method = method;
      this.testCases = new ArrayList<String[]>();
      testCases.add(test);
    }

    void addTestCases(String[] test) {
      testCases.add(test);
    }

    String getUrl() {
      return url;
    }

    String getMethod() {
      return method;
    }

    ArrayList<String[]> getTestCases() {
      return testCases;
    }

  }

}
