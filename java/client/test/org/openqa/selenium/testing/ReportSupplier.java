package org.openqa.selenium.testing;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: andrii
 * Date: 5/29/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportSupplier {
    private static HashMap<String, HashMap<String, Boolean>> commandMap = new HashMap<String, HashMap<String, Boolean>>();

    public static void addCommand(String command)
    {
        if (!commandMap.containsKey(command))
            commandMap.put(command, new HashMap<String, Boolean>());
    }

    public static void addTestToCommand(String command, String test, Boolean result)
    {
        if (!commandMap.containsKey(command))
            commandMap.put(command, new HashMap<String, Boolean>());

        HashMap<String, Boolean> tests = commandMap.get(command);
        tests.put(test, result);
    }

    public static HashMap<String, HashMap<String, Boolean>> getCommandMap()
    {
        return commandMap;
    }
}
