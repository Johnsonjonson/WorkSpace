# coding:UTF-8
import urllib2
import json
import wx
import websocket
import threading
from pubsub import pub
from distutils.util import strtobool

def on_message(ws, message):
    print("receive Msg = ", message)
    wx.CallAfter(pub.sendMessage, "update",data = message)

def on_error(ws, error):
    print(error)


def on_close(ws):
    print("### closed ###")


def on_open(ws):
    print("### open ###")

class Example(wx.Frame):
    def __init__(self, parent, title):
        super(Example, self).__init__(parent, title=title, size=(500, 180))
        self.InitUI()
        self.Centre()
        self.Show()
        self.requestData(None)
        self.connectSocket()

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
        btn1 = wx.Button(panel, label='Refresh', size=(70, 30))
        self.Bind(wx.EVT_BUTTON, self.requestData, btn1)
        hbox5.Add(btn1)
        btn2 = wx.Button(panel, label='Close', size=(70, 30))
        self.Bind(wx.EVT_BUTTON, self.OnCloseWindow, btn2)
        self.Bind(wx.EVT_CLOSE, self.OnCloseWindow) 
        hbox5.Add(btn2, flag=wx.LEFT | wx.BOTTOM, border=5)
        vbox.Add(hbox5, flag=wx.ALIGN_CENTER | wx.CENTER, border=10)
        panel.SetSizer(vbox)
        pub.subscribe(self.updateStatus, "update")

    def OnCloseWindow(self, event):
        if self.ws:
            self.ws.close()
            self.ws = None
        self.Destroy()

    def requestData(self, event):
        url = 'http://129.204.232.210:8080/light/python'
        # url = 'http://localhost:8080/PythonTest_war_exploded/python'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        self.updateStatus(rs)

    def updateStatus(self,data):
        pyData = json.loads(data)
        status = pyData["status"]
        msg = pyData["msg"]
        if self.st1:
            if strtobool(status):
                self.st1.SetForegroundColour("green")
            else:
                self.st1.SetForegroundColour("red")
            self.st1.SetLabelText(msg)

    def connectSocket(self):
        # socketUrl = "ws://localhost:8080/PythonTest_war_exploded/PythonWebSocket"
        socketUrl = "ws://129.204.232.210:8080/light/PythonWebSocket"
        self.ws = websocket.WebSocketApp(socketUrl,
                                         on_message=on_message,
                                         on_error=on_error,
                                         on_close=on_close)
        self.ws.on_open = on_open
        print(type(self.ws))
        wst = threading.Thread(target=self.ws.run_forever)
        wst.daemon = True
        wst.start()


if __name__ == '__main__':
    app = wx.App()
    frame = Example(None, title="路灯监测")
    app.MainLoop()
