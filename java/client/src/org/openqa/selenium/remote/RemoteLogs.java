/*
Copyright 2007-2011 Selenium committers
Portions copyright 2011 Software Freedom Conservancy

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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import org.openqa.selenium.Beta;
import org.openqa.selenium.logging.LocalLogs;
import org.openqa.selenium.logging.LogCombiner;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogLevelMapping;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Beta
public class RemoteLogs implements Logs {
  private static final String LEVEL = "level";
  private static final String TIMESTAMP= "timestamp";
  private static final String MESSAGE = "message";

  protected ExecuteMethod executeMethod;

  @VisibleForTesting public static final String TYPE_KEY = "type";
  private final LocalLogs localLogs;

  public RemoteLogs(ExecuteMethod executeMethod, LocalLogs localLogs) {
    this.executeMethod = executeMethod;
    this.localLogs = localLogs;
  }

  public LogEntries get(String logType) {
    if (LogType.PROFILER.equals(logType)) {
      if (getAvailableDriverLogs().contains("profiler"))
        return LogCombiner.combine(getRemoteEntries(logType), getLocalEntries(logType));
      return getLocalEntries(logType);
    }
    if (LogType.CLIENT.equals(logType)) {
      return getLocalEntries(logType);
    }
    return getRemoteEntries(logType);
  }

  private LogEntries getRemoteEntries(String logType) {
    Object raw = executeMethod.execute(DriverCommand.GET_LOG, ImmutableMap.of(TYPE_KEY, logType));
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> rawList = (List<Map<String, Object>>) raw;
    List<LogEntry> remoteEntries = Lists.newArrayListWithCapacity(rawList.size());

    for (Map<String, Object> obj : rawList) {
      remoteEntries.add(new LogEntry(LogLevelMapping.toLevel((String)obj.get(LEVEL)),
          (Long) obj.get(TIMESTAMP),
          (String) obj.get(MESSAGE)));
    }
    return new LogEntries(remoteEntries);
  }

  private LogEntries getLocalEntries(String logType) {
    return localLogs.get(logType);
  }

  private Set<String> getAvailableLocalLogs() {
    return localLogs.getAvailableLogTypes();
  }

  private Set<String> getAvailableDriverLogs() {
    Object raw = executeMethod.execute(DriverCommand.GET_AVAILABLE_LOG_TYPES, null);
    @SuppressWarnings("unchecked")
    List<String> rawList = (List<String>) raw;
    ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<String>();
    for (String logType : rawList) {
      builder.add(logType);
    }
    return builder.build();
  }

  public Set<String> getAvailableLogTypes() {
    ImmutableSet.Builder<String> builder = new ImmutableSet.Builder<String>();
    builder.addAll(getAvailableDriverLogs());
    builder.addAll(getAvailableLocalLogs());
    return builder.build();
  }
}