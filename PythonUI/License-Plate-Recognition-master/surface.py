import json
import tkinter as tk
from tkinter import ttk
from tkinter.filedialog import *
from tkinter import *
import predict
import cv2
from PIL import Image, ImageTk
import threading
import time
import CarRecognition
import urllib.request


class Surface(ttk.Frame):
    pic_path = ""
    viewhigh = 600
    viewwide = 600
    update_time = 0
    thread = None
    thread_run = False
    camera = None
    color_transform = {"green": ("绿牌", "#55FF55"), "yellow": ("黄牌", "#FFFF00"), "blue": ("蓝牌", "#6666FF")}
    recognize_car_num = ""
    check_car_num = ""

    def __init__(self, win):
        ttk.Frame.__init__(self, win)
        frame_left = ttk.Frame(self)
        frame_right1 = ttk.Frame(self)
        frame_right2 = ttk.Frame(self)
        frame_right3 = ttk.Frame(self)
        frame_right4 = ttk.Frame(self)
        win.title("车牌识别")
        # win.state("zoomed")

        win.geometry('820x500')
        self.pack(fill=tk.BOTH, expand=tk.YES, padx="5", pady="5")
        frame_left.pack(side=tk.LEFT, expand=1, fill=BOTH)
        frame_right1.pack(side=TOP, expand=1, fill=tk.Y)
        frame_right3.pack(side=TOP, expand=1,fill=tk.Y)
        frame_right4.pack(side=TOP, expand=1,fill=tk.Y)
        frame_right2.pack(side=RIGHT, expand=1)
        ttk.Label(frame_left, text='原图：').pack(anchor="nw")
        ttk.Label(frame_right1, text='车牌位置：').grid(column=0, row=0, sticky=tk.W)

        from_pic_ctl = ttk.Button(frame_right2, text="来自图片", width=20, command=self.from_pic)
        from_vedio_ctl = ttk.Button(frame_right2, text="来自摄像头", width=20, command=self.from_vedio)
        start_recognize = ttk.Button(frame_right2, text="开始识别", width=20, command=self.start_recognize)
        self.image_ctl = ttk.Label(frame_left)
        self.image_ctl.pack(anchor="nw")

        ttk.Label(frame_right3, text='用户信息',font =('',15)).grid(column=0, row=1, sticky=tk.W)
        # self.user_name = ttk.Label(frame_right3, text='用户姓名：',font =('',18))
        self.user_name = ttk.Label(frame_right3, text='用户姓名：')
        self.user_name.grid(column=0, row=2, sticky=tk.W)
        self.user_car_num = ttk.Label(frame_right3, text="用户车牌：")
        self.user_car_num.grid(column=0, row=4, sticky=tk.W)
        self.user_tel = ttk.Label(frame_right3, text="用户电话：")
        self.user_tel.grid(column=0, row=5, sticky=tk.W)

        self.roi_ctl = ttk.Label(frame_right1)
        self.roi_ctl.grid(column=0, row=1, sticky=tk.W)
        ttk.Label(frame_right1, text='识别结果：').grid(column=0, row=2, sticky=tk.W)
        self.car_num = ttk.Label(frame_right1, text="")
        self.car_num.grid(column=0, row=3, sticky=tk.W)
        self.r_ctl = ttk.Label(frame_right1, text="")
        self.r_ctl.grid(column=0, row=3, sticky=tk.W)
        self.color_ctl = ttk.Label(frame_right1, text="", width="20")
        self.color_ctl.grid(column=0, row=4, sticky=tk.W)
        self.result = ttk.Label(frame_right4, text="", width="20",font = ("",15))
        self.result.grid(column=0, row=1, sticky=tk.W)
        from_vedio_ctl.pack(anchor="se", pady="5")
        from_pic_ctl.pack(anchor="se", pady="5")
        start_recognize.pack(anchor="se", pady="5")
        self.predictor = predict.CardPredictor()
        self.predictor.train_svm()

        self.request_user_info()

        self.thread = threading.Thread(target=self.vedio_thread, args=(self,))
        self.thread.setDaemon(True)
        self.thread.start()
        self.thread_run = True

    def get_imgtk(self, img_bgr):
        img = cv2.cvtColor(img_bgr, cv2.COLOR_BGR2RGB)
        im = Image.fromarray(img)
        imgtk = ImageTk.PhotoImage(image=im)
        wide = imgtk.width()
        high = imgtk.height()
        if wide > self.viewwide or high > self.viewhigh:
            wide_factor = self.viewwide / wide
            high_factor = self.viewhigh / high
            factor = min(wide_factor, high_factor)

            wide = int(wide * factor)
            if wide <= 0: wide = 1
            high = int(high * factor)
            if high <= 0: high = 1
            im = im.resize((wide, high), Image.ANTIALIAS)
            imgtk = ImageTk.PhotoImage(image=im)
        return imgtk

    def show_roi(self, r, roi, color):
        if r:
            roi = cv2.cvtColor(roi, cv2.COLOR_BGR2RGB)
            roi = Image.fromarray(roi)
            self.imgtk_roi = ImageTk.PhotoImage(image=roi)
            self.roi_ctl.configure(image=self.imgtk_roi, state='enable')
            self.r_ctl.configure(text=str(r))
            self.update_time = time.time()
            try:
                c = self.color_transform[color]
                self.color_ctl.configure(text=c[0], background=c[1], state='enable')
            except:
                self.color_ctl.configure(state='disabled')
        elif self.update_time + 8 < time.time():
            self.roi_ctl.configure(state='disabled')
            self.r_ctl.configure(text="")
            self.color_ctl.configure(state='disabled')

    def from_vedio(self):
        if self.thread_run:
            return
        if self.camera is None:
            video = "http://admin:admin@172.30.18.149:8081/video"
            self.camera = cv2.VideoCapture(video)
            # self.camera = cv2.VideoCapture(0)
            if not self.camera.isOpened():
                # mBox.showwarning('警告', '摄像头打开失败！')
                self.camera = None
                return
        self.thread = threading.Thread(target=self.vedio_thread, args=(self,))
        self.thread.setDaemon(True)
        self.thread.start()
        self.thread_run = True

    def parse_resrlt(self,result,file):
        # result = {'words_result': {'number': '京AG6104',
        #                            'vertexes_location': [{'x': 257, 'y': 284}, {'x': 334, 'y': 281},
        #                                                  {'x': 334, 'y': 307},
        #                                                  {'x': 257, 'y': 310}], 'color': 'blue',
        #                            'probability': [0.9995086193084717, 0.9999552965164185, 0.9999735355377197,
        #                                            0.9999620914459229, 0.9999897480010986, 0.9999547004699707,
        #                                            0.9999920129776001]}, 'log_id': 1367414422082945024}
        self.r_ctl.configure(text=str(result['words_result']['number']))
        color = str(result['words_result']['color'])
        location = result['words_result']['vertexes_location']
        print(location, location[0], int(location[0]['y']), int(location[2]['y']), int(location[0]['x']),
              int(location[2]['x']))
        c = self.color_transform[color]
        print(file)
        self.color_ctl.configure(text=c[0], background=c[1], state='enable')
        self.recognize_car_num = str(result['words_result']['number'])
        if self.recognize_car_num != "" and self.check_car_num != "" and self.recognize_car_num == self.check_car_num:
            self.result.configure(text="比对成功，通过", background="#00FF00", state='enable')
        else:
            self.result.configure(text="比对失败,不通过", background="#FF0000", state='enable')
        try:
            img = cv2.imread(file)
            crop_img = img[int(location[0]['y']):int(location[2]['y']), int(location[0]['x']):int(location[2]['x'])]
            roi = cv2.cvtColor(crop_img, cv2.COLOR_BGR2RGB)
            roi = Image.fromarray(roi)
            self.imgtk_roi = ImageTk.PhotoImage(image=roi)
            self.roi_ctl.configure(image=self.imgtk_roi, background='#FFFFFF',state='enable')
        except:
            self.roi_ctl.configure(text="车牌截取失败", background='#FF0000',state='enable')

    def start_recognize(self):
        if self.camera is None:
            # result = CarRecognition.recognize()
            result = {'words_result': {'number': '京AG6104',
                                       'vertexes_location': [{'x': 257, 'y': 284}, {'x': 334, 'y': 281},
                                                             {'x': 334, 'y': 307},
                                                             {'x': 257, 'y': 310}], 'color': 'blue',
                                       'probability': [0.9995086193084717, 0.9999552965164185, 0.9999735355377197,
                                                       0.9999620914459229, 0.9999897480010986, 0.9999547004699707,
                                                       0.9999920129776001]}, 'log_id': 1367414422082945024}
            self.parse_resrlt(result,"test/tmp.jpg")
            return
        print(u"-----------------------开始识别-----------------")
        _, img_bgr = self.camera.read()
        cv2.imwrite("test/tmp.jpg", img_bgr)
        result = CarRecognition.recognize("test/tmp.jpg")
        self.parse_resrlt(result, "test/tmp.jpg")

    def from_pic(self):
        # self.thread_run = False
        self.pic_path = askopenfilename(title="选择识别图片", filetypes=[("jpg图片", "*.jpg")])
        if self.pic_path:
            img_bgr = predict.imreadex(self.pic_path)
            self.imgtk = self.get_imgtk(img_bgr)
            self.image_ctl.configure(image=self.imgtk)
            result = CarRecognition.recognize(self.pic_path)
            self.parse_resrlt(result,self.pic_path)
            # resize_rates = (1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4)
            # for resize_rate in resize_rates:
            #     print("resize_rate:", resize_rate)
            #     r, roi, color = self.predictor.predict(img_bgr, resize_rate)
            #     if r:
            #         break
            # # r, roi, color = self.predictor.predict(img_bgr, 1)
            # print(r, roi, color)
            # self.show_roi(r, roi, color)

    def update_user_info(self,data):
        self.user_name.configure(text="用户名称："+data['name'], state='enable')
        self.user_car_num.configure(text="用户车牌："+data['car_num'], state='enable')
        self.user_tel.configure(text="用户电话："+data['tel'], state='enable')

    @staticmethod
    def vedio_thread(self):
        self.thread_run = True
        predict_time = time.time()
        while self.thread_run:
            # _, img_bgr = self.camera.read()
            # cv2.imwrite("test/tmp.jpg", img_bgr)
            # self.imgtk = self.get_imgtk(img_bgr)
            # self.image_ctl.configure(image=self.imgtk)
            if time.time() - predict_time > 5:
                # CarRecognition.recognize(self.imgtk)
                # r, roi, color = self.predictor.predict(img_bgr)
                # self.show_roi(r, roi, color)

                # 更新用户信息

                self.request_user_info()
                predict_time = time.time()
        print("run end")

    def request_user_info(self):
        url = 'http://129.204.232.210:8534/get'
        rs = urllib.request.urlopen(url).read()
        pyData = json.loads(rs)
        print(pyData['name'], pyData['car_num'], pyData['tel'])
        self.check_car_num = pyData['car_num']
        self.update_user_info(pyData)


def close_window():
    print("destroy")
    if surface.thread_run:
        surface.thread_run = False
        surface.thread.join(2.0)
    win.destroy()


if __name__ == '__main__':
    win = tk.Tk()

    surface = Surface(win)
    win.protocol('WM_DELETE_WINDOW', close_window)
    win.mainloop()
