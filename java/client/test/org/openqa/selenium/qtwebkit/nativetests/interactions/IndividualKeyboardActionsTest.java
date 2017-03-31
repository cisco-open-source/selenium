/*
Copyright 2007-2010 Selenium committers

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.KeyDownAction;
import org.openqa.selenium.interactions.KeyUpAction;
import org.openqa.selenium.interactions.SendKeysAction;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Unit test for all simple keyboard actions.
 * 
 */
public class IndividualKeyboardActionsTest {

  @Mock private Keyboard mockKeyboard;
  @Mock private Mouse mockMouse;
  @Mock private Coordinates mockCoordinates;
  @Mock private Locatable stubLocatable;
  final String keysToSend = "hello";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(stubLocatable.getCoordinates()).thenReturn(mockCoordinates);
  }

  @Test
  public void keyDownActionWithoutProvidedElement() {
    final Keys keyToPress = Keys.SHIFT;

    KeyDownAction keyDown = new KeyDownAction(mockKeyboard, mockMouse, keyToPress);
    keyDown.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockKeyboard).pressKey(keyToPress);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void keyDownActionOnAnElement() {
    final Keys keyToPress = Keys.SHIFT;

    KeyDownAction keyDown = new KeyDownAction(
        mockKeyboard, mockMouse, stubLocatable, keyToPress);

    keyDown.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockMouse).click(mockCoordinates);
    order.verify(mockKeyboard).pressKey(keyToPress);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void keyUpActionWithoutProvidedElement() {
    final Keys keyToRelease = Keys.CONTROL;

    KeyUpAction keyUp = new KeyUpAction(mockKeyboard, mockMouse, keyToRelease);
    keyUp.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockKeyboard).releaseKey(keyToRelease);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void keyUpOnAnAnElement() {
    final Keys keyToRelease = Keys.SHIFT;

    KeyUpAction upAction = new KeyUpAction(
        mockKeyboard, mockMouse, stubLocatable, keyToRelease);
    upAction.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockMouse).click(mockCoordinates);
    order.verify(mockKeyboard).releaseKey(keyToRelease);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void sendKeysActionWithoutProvidedElement() {
    SendKeysAction sendKeys = new SendKeysAction(mockKeyboard, mockMouse, keysToSend);
    sendKeys.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockKeyboard).sendKeys(keysToSend);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void sendKeysActionOnAnElement() {
    SendKeysAction sendKeys = new SendKeysAction(
        mockKeyboard, mockMouse, stubLocatable, keysToSend);
    sendKeys.perform();

    InOrder order = Mockito.inOrder(mockKeyboard, mockMouse, mockCoordinates);
    order.verify(mockMouse).click(mockCoordinates);
    order.verify(mockKeyboard).sendKeys(keysToSend);
    order.verifyNoMoreInteractions();
  }

  @Test
  public void keyDownActionFailsOnNonModifier() {
    final Keys keyToPress = Keys.BACK_SPACE;

    try {
      new KeyDownAction(mockKeyboard, mockMouse, stubLocatable, keyToPress);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("modifier keys"));
    }
  }
}
