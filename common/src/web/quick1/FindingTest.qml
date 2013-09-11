import QtQuick 1.0

Rectangle {
    property string title
    width: 320
    height: 240

    Column {
        spacing: 2

        TextInput {
            id: inputElement
            objectName: "inputElement"
            text: { "<b>" + "(^ ^)" + "</b>" }
            font.pixelSize: 18
        }

        Rectangle { objectName: "hiddenRect"; color: "red"; width: 50; height: 50; visible: false }
        Rectangle {
            color: "green"; width: 20; height: 50;
            MouseArea {
                objectName: "mouseHotSpot"
                anchors.fill: parent
                onClicked: { parent.color = 'red' }
            }
        }
        Rectangle { color: "blue"; width: 50; height: 20 }
    }
}

