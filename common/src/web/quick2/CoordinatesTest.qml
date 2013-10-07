import QtQuick 2.0

Rectangle {
    property string title
    width: 320
    height: 240

    Rectangle {
        id: zeroSizedElement
        objectName: "zeroSizedElement"
        x: 8
        y: 8
        width: 0
        height: 0
        border.color: "black"
        color: "red"
    }

    Rectangle {
        id: mainRectangle
        objectName: "mainRectangle"
        anchors.top: parent.top
        anchors.left: parent.left
        anchors.topMargin: 10
        anchors.leftMargin: 10
        width: 100
        height: 100
        border.color: "black"
        color: "green"
    }

    Rectangle {
        id: transparentRect
        objectName: "transparentRect"
        anchors.top: mainRectangle.bottom
        anchors.left: mainRectangle.left
        anchors.topMargin: 10
        width: 100
        height: 30
        border.color: "black"
        color: "red"
        opacity: 0
    }

    Rectangle {
        id: hiddenRect
        objectName: "hiddenRect"
        anchors.top: mainRectangle.top
        anchors.left: mainRectangle.right
        anchors.leftMargin: 10
        width: 30
        height: 100
        border.color: "black"
        color: "blue"
        visible: false
    }

    Item {
        id: invisibleItem
        objectName: "invisibleItem"
        x: 3
        y: 3
        width: 3
        height: 3
    }


}

