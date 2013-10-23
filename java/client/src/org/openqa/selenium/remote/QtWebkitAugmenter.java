package org.openqa.selenium.remote;

import org.openqa.selenium.WebElement;

import java.util.Map;

import static org.openqa.selenium.remote.CapabilityType.TAKES_ELEMENT_SCREENSHOT;
import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_PLAYER_API;

public class QtWebkitAugmenter extends Augmenter {

  public QtWebkitAugmenter() {
    super();
    addElementAugmentation(TAKES_ELEMENT_SCREENSHOT, new AddTakesElementScreenshot());
    addElementAugmentation(SUPPORTS_PLAYER_API, new AddRemotePlayerApi());
  }
}
