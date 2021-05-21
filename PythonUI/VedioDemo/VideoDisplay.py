import cv2
import threading
from PyQt5.QtCore import QFile
from PyQt5.QtWidgets import QFileDialog, QMessageBox
from PyQt5.QtGui import QImage, QPixmap

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
class Display:
    def __init__(self, ui, mainWnd):
        self.ui = ui
        self.mainWnd = mainWnd

        # 默认视频源为相机
        # self.ui.radioButtonCam.setChecked(True)
        # self.isCamera = True

        # 信号槽设置
        # ui.Open.clicked.connect(self.Open)
        # ui.Close.clicked.connect(self.Close)
        # ui.radioButtonCam.clicked.connect(self.radioButtonCam)
        # ui.radioButtonFile.clicked.connect(self.radioButtonFile)

        # 创建一个关闭事件并设为未触发
        # self.stopEvent = threading.Event()
        # self.stopEvent.clear()

    # def radioButtonCam(self):
    #     self.isCamera = True

    # def radioButtonFile(self):
    #     self.isCamera = False

    def Open(self):
        self.fileName = getUrl() + "res/1.mp4"
        self.cap = cv2.VideoCapture(self.fileName)
        self.frameRate = self.cap.get(cv2.CAP_PROP_FPS)
        # 创建视频显示线程
        th = threading.Thread(target=self.Display)
        th.start()

    def Close(self):
        # 关闭事件设为触发，关闭视频播放
        self.stopEvent.set()

    def Display(self):
        while self.cap.isOpened():
            success, frame = self.cap.read()
            # RGB转BGR
            frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
            img = QImage(frame.data, frame.shape[1], frame.shape[0], QImage.Format_RGB888)
            self.ui.vedioLabel1.setPixmap(QPixmap.fromImage(img))

            if self.isCamera:
                cv2.waitKey(1)
            else:
                cv2.waitKey(int(1000 / self.frameRate))

            # 判断关闭事件是否已触发
            if True == self.stopEvent.is_set():
                # 关闭事件置为未触发，清空显示label
                self.stopEvent.clear()
                self.ui.DispalyLabel.clear()
                self.ui.Close.setEnabled(False)
                self.ui.Open.setEnabled(True)
                break