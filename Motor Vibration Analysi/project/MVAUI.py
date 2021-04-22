# coding:UTF-8
import urllib2
import json
import wx
import websocket
import threading
from pubsub import pub
from distutils.util import strtobool
import  sqlite3
import datetime
import time
from MVADraw import streamDetectionPlot 
from History import HistoryDataView 
import Tkinter
import random
def on_message(ws, message):
    print("receive Msg = ", message)
    wx.CallAfter(pub.sendMessage, "update",data = message)

def on_error(ws, error):
    print(error)


def on_close(ws):
    print("### closed ###")


def on_open(ws):
    print("### open ###")
    wx.CallAfter(pub.sendMessage, "update",data = "message")

class Example(wx.Frame):
    def __init__(self, parent, title):
        super(Example, self).__init__(parent, title=title, size=(500, 180))
        self.InitUI()
        self.Centre()
        self.Show()
        # self.requestData(None)
        # self.connectSocket()

    def InitUI(self):
        panel = wx.Panel(self)
        font = wx.Font(40, wx.MODERN, wx.NORMAL, wx.NORMAL, False, 'Consolas')
        vbox = wx.BoxSizer(wx.VERTICAL)
        hbox1 = wx.BoxSizer(wx.HORIZONTAL)
        self.st1 = wx.StaticText(panel, label='None')
        self.st1.SetFont(font)
        hbox1.Add(self.st1, flag=wx.ALIGN_CENTER | wx.CENTER, border=8)
        vbox.Add(hbox1, flag=wx.ALIGN_CENTER | wx.CENTER | wx.TOP, border=10)
        vbox.Add((-1, 25))
        hbox5 = wx.BoxSizer(wx.HORIZONTAL)
        btn1 = wx.Button(panel, label='Real-time Data', size=(100, 30))
        self.Bind(wx.EVT_BUTTON, self.showRealTimeData, btn1)
        hbox5.Add(btn1)
        btn2 = wx.Button(panel, label='History Data', size=(100, 30))
        self.Bind(wx.EVT_BUTTON, self.showHistoryData, btn2)
        self.Bind(wx.EVT_CLOSE, self.OnCloseWindow) 
        hbox5.Add(btn2, flag=wx.LEFT | wx.BOTTOM, border=5)
        vbox.Add(hbox5, flag=wx.ALIGN_CENTER | wx.CENTER, border=10)
        panel.SetSizer(vbox)
        pub.subscribe(self.updateStatus, "update")

    def OnCloseWindow(self, event):
        # if self.ws:
        #     self.ws.close()
        #     self.ws = None
        self.Destroy()

    # 显示历史数据
    def showHistoryData(self, event):
        con=sqlite3.connect('mvaDataBase')
        cursor=con.cursor()
        selectSql = "SELECT * FROM mvaTable"
        cursor.execute(selectSql)
        result=cursor.fetchall()
        x = []
        y = []
        z = []
        for value in result:
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            x.append(xvalue)
            y.append(xvalue)
            z.append(xvalue)
            print("xvalue = %s yvalue =  %s zvalue = %s" % (xvalue,yvalue,zvalue))
        print(result[1][1])
        HistoryDataView()
        print(u"显示历史数据")

    # 显示实时数据
    def showRealTimeData(self, event):
        print(u"显示实时数据")
        self.graph = streamDetectionPlot()
        self.graph.initPlot()
        # self.graph.anomalyDetectionPlot(datetime.datetime.now(),random.randint(0,100),random.randint(0,100),random.randint(0,100))
        self.connectSocket()
        # graph.close()

    def updateStatus(self,data):
        print("===============updateStatus=============",data)
        timestamp = datetime.datetime.now()
        self.graph.anomalyDetectionPlot(timestamp,random.randint(0,100),random.randint(0,100),random.randint(0,100))

    def connectSocket(self):
        while True:
            url = 'http://129.204.232.210:8900/mva/getdata'
            rq = urllib2.Request(url)
            rs = urllib2.urlopen(rq).read()
            # i = 1 +i
            print(rs)
            pyData = json.loads(rs)
            xValue = pyData["x"]
            yValue = pyData["y"]
            zValue = pyData["z"]
            timeValue = pyData["timestamp"]
            print(xValue,yValue,zValue,timeValue)
            self.graph.anomalyDetectionPlot(datetime.datetime.now(),xValue,yValue,zValue)


if __name__ == '__main__':
    app = wx.App()
    frame = Example(None, title="Motor Vibration Analysi")
    app.MainLoop()
