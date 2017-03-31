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

package org.openqa.selenium.qtwebkit.visualizer_tests;

import org.openqa.selenium.Dimension;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisualizerUtils {

  /**
   * Get dimension from title like 'be2a1340-3141-4d5b-a829-8d77cc57add4 (800x512 pixels)'
   */
  public static Dimension getDimensionFromTitle(String title) {
    Matcher m = Pattern.compile(".*\\((?<width>\\d+)x(?<height>\\d+) pixels\\)").matcher(title);
    if (!m.matches())
      return null;
    return new Dimension(Integer.valueOf(m.group("width")), Integer.valueOf(m.group("height")));
  }

  public static String findNotEqualsIgnoreCase(Collection<String> collection, String value) {
    for (String item : collection) {
      if ((item == null || value == null) && item != value)
        return item;

      if (item != null && !item.equalsIgnoreCase(value))
        return item;
    }

    return null;
  }
}
