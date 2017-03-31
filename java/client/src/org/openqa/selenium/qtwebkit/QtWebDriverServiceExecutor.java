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

package org.openqa.selenium.qtwebkit;

import com.google.common.base.Throwables;

import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.WebDriverException;


import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;


public class QtWebDriverServiceExecutor extends QtWebDriverExecutor {
    private DriverService service;

    public QtWebDriverServiceExecutor(DriverService service){
        super(service.getUrl());
        this.service = service;
    }
    /**
   * Sends the {@code command} to the driver server for execution. The server will be started
   * if requesting a new session. Likewise, if terminating a session, the server will be shutdown
   * once a response is received.
   *
   * @param command The command to execute.
   * @return The command response.
   * @throws IOException If an I/O error occurs while sending the command.
   */
  @Override
  public Response execute(Command command) throws IOException {
    if (DriverCommand.NEW_SESSION.equals(command.getName())) {
      service.start();
    }

    try {
      return super.execute(command);
    } catch (Throwable t) {
      Throwable rootCause = Throwables.getRootCause(t);
      if (rootCause instanceof ConnectException &&
          "Connection refused".equals(rootCause.getMessage()) &&
          !service.isRunning()) {
        throw new WebDriverException("The driver server has unexpectedly died!", t);
      }
      Throwables.propagateIfPossible(t);
      throw new WebDriverException(t);
    } finally {
      if (DriverCommand.QUIT.equals(command.getName())) {
        service.stop();
      }
    }
  }

}
