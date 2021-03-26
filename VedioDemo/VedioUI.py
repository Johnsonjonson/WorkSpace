# coding:UTF-8
from PyQt5.QtWidgets import QMainWindow,QApplication
from PyQt5.QtGui import QPixmap,QImage
import sys
import os
import cv2
import threading

def getUrl():
    # 获取绝对路径
    url_father = os.path.dirname(os.path.abspath(__file__))

    # 因为styleSheet里正斜杠才管用，我要把反斜杠转化为正斜杠
    urlPath=""
    for i in url_father:
        if(i == "\\"):
            urlPath = urlPath + "/"
        else:
            urlPath = urlPath + i
    return urlPath

class win(QMainWindow):
    def __init__(self,resUrl):
        super().__init__()
        # self.resize(1000,800)
        from PyQt5.uic import loadUi  # 需要导入的模块
        uiUrl = getUrl() + "/res/vedioUI.ui"
        print(uiUrl)
        loadUi(uiUrl, self)  #加载UI文件
        self.isCamera = False
        self.playBtn.clicked.connect(self.Open)
        self.stopEvent = threading.Event()
        self.stopEvent.set()
        # self:Open()

    def Open(self):
        self.playFinishCount = 0
        self.playBtn.setEnabled(False)
        self.fileName = getUrl() + "/res/1.mp4"
        print(self.fileName)
        self.cap = cv2.VideoCapture(self.fileName)
        self.frameRate = self.cap.get(cv2.CAP_PROP_FPS)

        self.fileName1 = getUrl() + "/res/2.mp4"
        print(self.fileName1)
        self.cap1 = cv2.VideoCapture(self.fileName1)
        self.frameRate1 = self.cap1.get(cv2.CAP_PROP_FPS)

        self.fileName2 = getUrl() + "/res/3.mp4"
        print(self.fileName2)
        self.cap2 = cv2.VideoCapture(self.fileName2)
        self.frameRate2 = self.cap2.get(cv2.CAP_PROP_FPS)

        self.fileName3 = getUrl() + "/res/4.mp4"
        print(self.fileName3)
        self.cap3 = cv2.VideoCapture(self.fileName3)
        self.frameRate3 = self.cap3.get(cv2.CAP_PROP_FPS)

        # 创建视频显示线程
        self.th1 = threading.Thread(target=self.Display1)
        self.th1.start()
        # self.th2 = threading.Thread(target=self.Display2)
        # self.th2.start()
        # self.th3 = threading.Thread(target=self.Display3)
        # self.th3.start()
        # self.th4 = threading.Thread(target=self.Display4)
        # self.th4.start()
    def closeEvent(self,event):
        # 关闭事件设为触发，关闭视频播放
        self.th1._stop()
        # self.th2._stop()
        # self.th3._stop()
        # self.th4._stop()
        self.stopEvent.set()

    def playVideo(self,cap,vedioLable,frameRate):
        if cap:
            while cap.isOpened():
                success, frame = cap.read()
                # print('video readed')

                if success == False:
                    self.playFinishCount = self.playFinishCount + 1
                    if self.playFinishCount == 1:
                        self.playBtn.setEnabled(True)
                    print("play finished")  # 判断本地文件播放完毕
                    # cap.release()
                    break
                #视频的resize操作
                frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
                fps = cap.get(cv2.CAP_PROP_FPS) 
                newRate = int(frame.shape[0]/240) #求出resize的比值
                newHeight = int(frame.shape[0]/newRate)	#resize后的高和宽
                newWidth = int(frame.shape[1]/newRate) 
                newFrame = cv2.resize(frame,(newWidth,newHeight),interpolation=cv2.INTER_AREA)
                bytesPerLine = 3*newWidth
                temp_image = QImage(newFrame.data, newWidth, newHeight, bytesPerLine,QImage.Format_RGB888).scaled(vedioLable.width(), vedioLable.height())
                # temp_image = QImage(rgb.flatten(), width, height, QImage.Format_RGB888)
                temp_pixmap = QPixmap.fromImage(temp_image)
                vedioLable.setPixmap(temp_pixmap)
                # print(frameRate)
                cv2.waitKey(int(500 / frameRate))
                # cv2.waitKey(0)
                # 判断关闭事件是否已触发
                # if True==self.stopEvent.is_set():
                #     # self.continueEvent1.clear()
                #     self.b=1
                #     while self.b==1:
                #         if True == self.continueEvent1.is_set():
                #             # self.continueEvent1.clear()
                #             self.b=0
                # if True == self.stopEvent.is_set():
                #     # 关闭事件置为未触发，清空显示label
                #     break
            cap.release()
            self.stopEvent.clear()
    def Display1(self):
        self.playVideo(self.cap,self.vedioLable1,self.frameRate)

    def Display2(self):
        self.playVideo(self.cap1,self.vedioLable2,self.frameRate1)

    def Display3(self):
        self.playVideo(self.cap2,self.vedioLable3,self.frameRate2)

    def Display4(self):
        self.playVideo(self.cap3,self.vedioLable4,self.frameRate3)

    def Display(self):
        self.playVideo(self.cap,self.vedioLable1,self.frameRate)
        self.playVideo(self.cap1,self.vedioLable2,self.frameRate1)
        self.playVideo(self.cap2,self.vedioLable3,self.frameRate2)
        self.playVideo(self.cap3,self.vedioLable4,self.frameRate3)

if __name__=='__main__':
    app=QApplication(sys.argv)
    w=win(getUrl())
    w.show()
    sys.exit(app.exec_())