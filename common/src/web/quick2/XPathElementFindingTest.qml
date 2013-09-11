import QtQuick 2.0

Rectangle {
    property string title
    width: 320
    height: 500

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
        }

        Row {
            spacing: 10
            width: parent.width

            Rectangle {
                property alias text: captionText1.text
                objectName: "defaultSelected"
                height: 50
                width: (parent.width - parent.spacing)/2
                Text { id: captionText1; text: "First"; anchors.fill: parent; verticalAlignment: Text.AlignVCenter; horizontalAlignment:  Text.AlignHCenter; }
                border.color: "black"
                color: "green"
            }

            Rectangle {
                property alias text: captionText2.text
                objectName: "noSelected"
                height: 50
                width: (parent.width - parent.spacing)/2
                Text { id: captionText2; text: "Second"; anchors.fill: parent; verticalAlignment: Text.AlignVCenter; horizontalAlignment:  Text.AlignHCenter; }
                border.color: "black"
                color: "yellow"
            }
        }

        TextInput {
            objectName: "inputElement"
            width: parent.width
            text: "Example text"
        }

        TextInput {
            objectName: "inputEmpty"
            width: parent.width
        }

        TextInput {
            objectName: "inputDisabled"
            width: parent.width
            text: "Read Only"
            enabled: false
            readOnly: false
        }

        TextEdit {
            objectName: "workingArea"
            width: parent.width
            height: 100
            text: "Example text"
        }

        Item {
            objectName: "scrollArea"
            width: parent.width
            height: 100

            PushButton {
                objectName: "buttonInScrollArea"
                text: "Click here"
            }
        }
    }
}

