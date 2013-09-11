import QtQuick 1.0

Rectangle {
    property string title
    width: 320
    height: 240

    Column {
        anchors.left: parent.left
        anchors.leftMargin: 10

        anchors.top: parent.top
        anchors.topMargin: 10

        spacing: 20
        width: 180

        Text {
            id: clickDisplay
            objectName: "clickDisplay"
            text: "no action"
            font.pixelSize: 18
        }

        Rectangle {
            id: button1
            objectName: "button1"

            width: parent.width
            height: 40
            color: "green"
            Text {
                //anchors.fill: parent
                text: "push me"
            }

            MouseArea {
                objectName: "mouseHotSpotButton1"
                anchors.fill: parent
                onClicked: { clickDisplay.text = "clicked button1" }
            }
        }

        MouseArea {
            objectName: "mouseHotSpotArea2"
            width: 40
            height: 40
            onClicked: { clickDisplay.text = "clicked area 2" }
        }
    }
}

