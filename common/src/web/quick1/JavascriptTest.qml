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

import QtQuick 1.0

Rectangle {
    id: mainItem
    property string title: "Javascript Test Page"
    width: 320
    height: 400

    function sayHello(toWhom) {
        return "Hello " + toWhom + " !";
    }

    Column {
        anchors.left: parent.left
        anchors.leftMargin: 10

        anchors.top: parent.top
        anchors.topMargin: 10

        anchors.right: parent.right
        anchors.rightMargin: 10

        anchors.bottom: parent.bottom
        anchors.bottomMargin: 10

        spacing: 20

        Text {
            id: result
            objectName: "result"
            text: "default"
            color: "darkgrey"
            width: parent.width
        }

        PushButton {
            objectName: "changeLabelButton"
            text: "Say hello"
        }

        TextInput {
            objectName: "inputElement"
            width: parent.width
            text: "Example text"
            fillColor: "darkgrey"
        }

        TextInput {
            objectName: "emptyInput"
            width: parent.width
            fillColor: "darkgrey"
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            text: "Example text"
            fillColor: "darkgrey"
        }
    }
}

