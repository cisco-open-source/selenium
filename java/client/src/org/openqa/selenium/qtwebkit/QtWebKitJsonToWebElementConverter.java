package org.openqa.selenium.qtwebkit;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.internal.JsonToWebElementConverter;

import java.util.Collection;
import java.util.Map;

public class QtWebKitJsonToWebElementConverter extends JsonToWebElementConverter {

  public QtWebKitJsonToWebElementConverter(QtWebKitDriver driver) {
    super(driver);
  }

  public Object apply(Object result) {
    if (result instanceof Collection<?>) {
      Collection<?> results = (Collection<?>) result;
      return Lists.newArrayList(Iterables.transform(results, this));
    }

    if (result instanceof Map<?, ?>) {
      Map<?, ?> resultAsMap = (Map<?, ?>) result;
      if (resultAsMap.containsKey("ELEMENT")) {
        RemoteWebElement element = newRemoteWebElement();
        element.setId(String.valueOf(resultAsMap.get("ELEMENT")));
        element.setFileDetector(driver.getFileDetector());

        RemotePlayer player = newRemotePlayerElement();
        player.setId(String.valueOf(resultAsMap.get("ELEMENT")));
        player.setFileDetector(driver.getFileDetector());
        if(player.getState() != null){
          return player;
        }
        return element;
      } else {
        return Maps.transformValues(resultAsMap, this);
      }
    }

    if (result instanceof Number) {
      if (result instanceof Float || result instanceof Double) {
        return ((Number) result).doubleValue();
      }
      return ((Number) result).longValue();
    }

    return result;
  }


  protected RemotePlayer newRemotePlayerElement() {
    RemotePlayer toReturn = new RemotePlayer();
    toReturn.setParent(driver);
    return toReturn;
  }

}
