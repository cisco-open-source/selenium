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

package org.openqa.selenium.qtwebkit.nativetests.interactions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StubDriver;

public class StubInputDeviceDriver extends StubDriver implements JavascriptExecutor {
  public Object executeScript(String script, Object... args) {
    return null;
  }

  public Object executeAsyncScript(String script, Object... args) {
    return null;
  }
}
