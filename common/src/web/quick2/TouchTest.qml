import QtQuick 1.1

Rectangle{
    width: 400
    height: 620

    Column
    {
        anchors.fill: parent
        anchors.margins: 10
        spacing: 10
        Row{
            spacing: 10
            Text {
                color: "black"
                text: "Result: "
            }
            Text {
                id: resultLabel
                objectName: "resultLabel"
                color: "black"
                text: ""
            }
        }


        Rectangle {
            id: touchArea
            objectName: "touchArea"
            color: "#F1F1F1"

            width: 380
            height: 280
            signal touchPress(int x, int y)
            signal touchRelease(int x, int y)
            signal touchMove(int x, int y)
            Text {
                anchors.centerIn: parent
                color: "black"
                text: "Touch Area"
            }
            MultiPointTouchArea {
                anchors.fill: parent
                touchPoints: [
                    TouchPoint { id: point1 }
                ]

                onReleased: {
                    releasePoint.visible = true
                    touchArea.touchRelease(point1.x, point1.y)
                    resultLabel.text = resultLabel.text+ "Released"
                }

                onUpdated: {
                    movePoint.visible = true
                    touchArea.touchMove(point1.x, point1.y)
                    resultLabel.text = resultLabel.text + "Moved"
                }

                onPressed: {
                    pressPoint.visible = true
                    touchArea.touchPress(point1.x, point1.y)
                    resultLabel.text = resultLabel.text + "Pressed"
                }


            }

            Rectangle {
                id: pressPoint
                objectName: "pressPoint"
                visible: false
                width: 20
                height: 20
                radius: 10
                color: "red"

                function move(newx, newy) {
                    x = newx - width/2;
                    y = newy - height/2;
                }
            }

            Rectangle {
                id: movePoint
                objectName: "movePoint"
                visible: false
                width: 20
                height: 20
                radius: 10
                color: "yellow"

                function move(newx, newy) {
                    x = newx - width/2;
                    y = newy - height/2;
                }
            }

            Rectangle {
                id: releasePoint
                objectName: "releasePoint"
                visible: false
                width: 20
                height: 20

                radius: 10
                color: "green"

                function move(newx, newy) {
                    x = newx - width/2;
                    y = newy - height/2;
                }
            }

            Component.onCompleted: {
                touchArea.touchPress.connect(pressPoint.move);
                touchArea.touchRelease.connect(releasePoint.move)
                touchArea.touchMove.connect(movePoint.move)
            }

        }

        Rectangle {
            id: mouseArea
            objectName: "mouseArea"
            color: "#E1F1A1"

            width: 380
            height: 280
            signal touchPress(int x, int y)
            signal touchRelease(int x, int y)
            signal touchMove(int x, int y)
            Text {
                anchors.centerIn: parent
                color: "black"
                text: "Mouse Area"
            }

            MouseArea {
                anchors.fill: parent
                onClicked: resultLabel.text = "Clicked"
                onDoubleClicked:  resultLabel.text = "DoubleClicked"
                onPressAndHold: resultLabel.text = "LongClicked"
                onCanceled: resultLabel.text = "Canceled"
            }
        }

     }
}
