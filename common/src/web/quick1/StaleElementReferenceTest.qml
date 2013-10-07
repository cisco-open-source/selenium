import QtQuick 1.0

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

