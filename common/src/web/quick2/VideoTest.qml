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
import QtMultimedia 5.0

Rectangle {
    id: root
    width: 900
    height: 700
    MouseArea {
        anchors.fill: parent
        onClicked: {
            Qt.quit();
        }
        z: 1.0
    }

    Rectangle{
        width: 800
        height: 600
        color: "black"
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.verticalCenter: parent.verticalCenter
        transformOrigin: Item.Center

        Video {
            id: video
            objectName: "videoPlayer"
            width: metaData.resolution ? metaData.resolution.width : 0
            height: metaData.resolution ? metaData.resolution.height : 0
            transformOrigin: Item.BottomRight
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.verticalCenter: parent.verticalCenter
            source: "./TestVideo.ogv"

            MouseArea {
                anchors.fill: parent
                onClicked: {
                    video.play()
                }
                z: 3.0
            }

            focus: true
            Keys.onSpacePressed: video.playbackState == MediaPlayer.PlayingState ? video.pause() : video.play()
            Keys.onLeftPressed: video.seek(video.position - 5000)
            Keys.onRightPressed: video.seek(video.position + 5000)
        }
        z: 2.0
    }
}
