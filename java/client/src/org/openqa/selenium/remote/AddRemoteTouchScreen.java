/****************************************************************************
**
** Copyright © 1992-2014 Cisco and/or its affiliates. All rights reserved.
** All rights reserved.
** 
** $CISCO_BEGIN_LICENSE:APACHE$
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
** http://www.apache.org/licenses/LICENSE-2.0
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
**
** $CISCO_END_LICENSE$
**
****************************************************************************/

package org.openqa.selenium.remote;

import org.openqa.selenium.interactions.HasTouchScreen;

import java.lang.reflect.Method;

/** Provides the RemoteTouchScreen for getTouch method to the proxy. */
public class AddRemoteTouchScreen implements AugmenterProvider {

  @Override
  public Class<?> getDescribedInterface() {
    return HasTouchScreen.class;
  }

  @Override
  public InterfaceImplementation getImplementation(Object value) {
    return new InterfaceImplementation() {

      @Override
      public Object invoke(ExecuteMethod executeMethod, Object self,
          Method method, Object... args) {
        if ("getTouch".equals(method.getName())) {
          return new RemoteTouchScreen(executeMethod);
        }
        return null;
      }
    };
  }
}
