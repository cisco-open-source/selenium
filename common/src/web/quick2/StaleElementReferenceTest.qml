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

import QtQuick 2.0

Rectangle {
    property string title
    width: 320
    height: 200

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
            objectName: "captionLabel"
            text: "This Is Caption"
            width: parent.width
        }

        Rectangle {
            objectName: "clickButton"
            border.color: "black"
            color: "green"
            width: parent.width
            height: parent.height / 2
            Text {
                text: "Click me"
                anchors.fill: parent
            }
        }
    }
}

