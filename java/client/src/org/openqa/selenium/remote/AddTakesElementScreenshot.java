/*
Copyright 2010 Selenium committers

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


package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.lang.reflect.Method;
import java.util.Map;

public class AddTakesElementScreenshot implements AugmenterProvider {
    public Class<?> getDescribedInterface() {
        return TakesScreenshot.class;
    }

    public InterfaceImplementation getImplementation(Object value) {
        return new InterfaceImplementation() {
            public Object invoke(ExecuteMethod executeMethod, Object self, Method method, Object... args) {
                Object id = ((RemoteWebElement) self).getId();
                OutputType<?> outputType = ((OutputType<?>) args[0]);

                Map<String, ?> commandArgs = ImmutableMap.of("id", id);

                Object result = executeMethod.execute(DriverCommand.ELEMENT_SCREENSHOT, commandArgs);
                if (result instanceof String) {
                    String base64EncodedPng = (String) result;
                    return outputType.convertFromBase64Png(base64EncodedPng);
                } else if (result instanceof byte[]) {
                    String base64EncodedPng = new String((byte[]) result);
                    return outputType.convertFromBase64Png(base64EncodedPng);
                } else {
                    throw new RuntimeException("Unexpected result for " + DriverCommand.ELEMENT_SCREENSHOT +
                                               " command: " + (result == null ? "null" : result.getClass().getName() + " instance"));
                }
            }
        };
    }
}
