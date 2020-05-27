# coding:UTF-8
import urllib2
import json
import wx
from threading import Timer
from distutils.util import strtobool

class Example(wx.Frame):
    def __init__(self, parent, title):
        super(Example, self).__init__(parent, title=title, size=(500, 150))
        self.InitUI()
        self.Centre()
        self.Show()
        self.t = None
        self.requestData(None)

    def InitUI(self):
        panel = wx.Panel(self)
        font = wx.SystemSettings.GetFont(wx.SYS_SYSTEM_FONT)
        font.SetPointSize(40)
        vbox = wx.BoxSizer(wx.VERTICAL)
        hbox1 = wx.BoxSizer(wx.HORIZONTAL)
        self.lightStatus = wx.StaticText(panel, label='None')
        self.lightStatus.SetFont(font)
        hbox1.Add(self.lightStatus, flag=wx.ALIGN_CENTER | wx.CENTER, border=8)
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

    def OnCloseWindow(self, event):
        if self.t:
            self.t.cancel()
            self.t = None
        self.Destroy()

    def requestData(self, event):
        if self.t:
            self.t.cancel()
            self.t = None
        url = 'http://129.204.232.210:8080/light/python'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        json_to_python = json.loads(rs)
        status = json_to_python["status"].encode('gbk')
        msg = json_to_python["msg"]
        print strtobool(status)
        if self.lightStatus: 
            if strtobool(status):
                self.lightStatus.SetForegroundColour("green")
            else:
                self.lightStatus.SetForegroundColour("red")
            self.lightStatus.SetLabelText(msg)
        self.t = Timer(1, self.requestData, (1,))
        self.t.start()

if __name__ == '__main__':
    app = wx.App()
    frame = Example(None, title="路灯监测")
    app.MainLoop()
