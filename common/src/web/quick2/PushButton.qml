import QtQuick 2.0

Rectangle {
    id: button
    color: "red"
    property alias text: caption.text
    signal buttonClick()
    width: 96; height: 24;

    Text {
        id: caption;
        anchors.fill: parent;
        verticalAlignment: Text.AlignVCenter;
        horizontalAlignment:  Text.AlignHCenter;
    }

    MouseArea {
        id: hotSpot
        anchors.fill: parent;
        onClicked: buttonClick()
    }
}
