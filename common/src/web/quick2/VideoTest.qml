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
