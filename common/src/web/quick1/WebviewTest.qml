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
import CiscoQtWebKit 1.0

Rectangle {
    width: 640
    height: 480
    color: "yellow"

    Text {
        text: webView.url
    }

    Rectangle {
        anchors.fill: parent
        anchors.bottomMargin: 40
        anchors.topMargin: 40
        anchors.leftMargin: 40
        anchors.rightMargin: 40
        border.color: "black"
        color: "blue"

        CiscoWebView {
            id: webView
            html: "<p>This is <b>HTML</b>."
            //preferredWidth: 640
            //preferredHeight: 480
            smooth: false
            anchors.fill: parent
        }
    }
}


