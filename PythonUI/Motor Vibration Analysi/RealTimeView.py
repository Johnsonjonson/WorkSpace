# coding:UTF-8
import matplotlib.pyplot as plt
import numpy as np

from matplotlib.widgets import Button
import threading
import urllib2
import random
import time
import datetime
import json

from matplotlib.dates import DateFormatter
import matplotlib.ticker as ticker
from History import HistoryDataView 

class RealTimeView():
    def __init__(self):
         # Turn matplotlib interactive mode on.
        plt.ion()
        # initial the plot variable.
        self.xValue = []
        self.yValue = []
        self.zValue = []
        self.timestamp = []
        # initial the figure.
        global fig
        fig = plt.figure(figsize=(12, 6), facecolor="white")
        fig.subplots_adjust(left=0.06, right=0.9)
        ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[], yticks=[])
        ax.set_title('Motor Vibration Analysis', size=16, weight='bold')
        self.ax1 = fig.add_subplot(111)
        dateFormat = DateFormatter("%m/%d %H:%M:%S")
        self.ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
        self.lineX, = self.ax1.plot(self.timestamp, self.xValue, linestyle="-", color="red", label="x-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.lineY, = self.ax1.plot(self.timestamp, self.yValue, linestyle="-", color="blue", label="y-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.lineZ, = self.ax1.plot(self.timestamp, self.zValue, linestyle="-", color="black", label="z-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.ax1.set_xlabel("Times(s)", color="white")
        self.ax1.set_ylabel("Acceleration(m/$\mathregular{s^2}$)", color="white")
        # buttonaxe = plt.axes([0.91, 0.5, 0.08, 0.08])
        # button1 = Button(buttonaxe, 'History Data', color='khaki', hovercolor='yellow')
        # button1.on_clicked(self.showHistory)
        # self.ax1.clear()
        url = 'http://129.204.232.210:8900/mva/history'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        # i = 1 +
        print(rs)
        result = json.loads(rs)
        for value in result:
            # print(value)
            tvalue = value[0]
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            timechange = datetime.datetime.fromtimestamp(tvalue)
            self.timestamp.append(timechange)
            self.xValue.append(xvalue)
            self.yValue.append(yvalue)
            self.zValue.append(zvalue)
        self.ax1.set_xlim(min(self.timestamp), max(self.timestamp)+datetime.timedelta(seconds=10))
        xRange = [min(self.xValue), max(self.xValue)+1]
        yRange = [min(self.yValue), max(self.yValue)+1]
        zRange = [min(self.zValue), max(self.zValue)+1]
        xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
        self.ax1.set_ylim(min(xyRange[0], zRange[0])-20,
                    max(xyRange[1], zRange[1]) + 20)
        self.lineX.set_data(self.timestamp, self.xValue)
        self.lineY.set_data(self.timestamp, self.yValue)
        self.lineZ.set_data(self.timestamp, self.zValue)
        self.lineX.set_data(self.timestamp, self.xValue)
        self.lineY.set_data(self.timestamp, self.yValue)
        self.lineZ.set_data(self.timestamp, self.zValue)
        plt.pause(0.001)
        self.ax1.figure.canvas.draw()
        # plt.ioff()
        # plt.show()
    
# RealTimeView()