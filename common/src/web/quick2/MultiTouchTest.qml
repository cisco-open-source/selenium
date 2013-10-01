import QtQuick 2.0

Rectangle {
    width: 400
    height: 600
    color: "black"
    Column {
        spacing: 10
        anchors.fill: parent

        Row {
            spacing: 10
            Text {
                text: "Zoom factor:"               
                color: "white"
            }
            Text {
                id: zoomFactorLbl
                objectName: "zoomLbl"
                color: "white"
                text: "0"
            }
        }

        Row {
            spacing: 10
            Text {
                text: "Rotation:"
                color: "white"
            }
            Text {
                id: rotationLbl
                objectName: "rotationLbl"
                color: "white"
                text: "0"
            }
        }

        Rectangle {
            height: 560
            width: 400
            color: "yellow"
            Rectangle
            {
                id: actionItem
                objectName: "pinchItem"
                height: 20
                width: 20
                color: "blue"
                anchors.centerIn: parent

                PinchArea {
                    anchors.fill: parent
                    pinch.target: actionItem
                    onPinchUpdated: {                  
                        rotationLbl.text = Math.round(pinch.rotation)
                        actionItem.rotation = Math.round(pinch.scale)
                        zoomFactorLbl.text = pinch.scale
                        actionItem.scale = pinch.scale
                    }
                    onPinchFinished: {
                        rotationLbl.text = Math.round(pinch.rotation)
                        actionItem.rotation = Math.round(pinch.rotation)
                        zoomFactorLbl.text = pinch.scale
                        actionItem.scale = pinch.scale
                    }
                }
            }
        }
    }
}
