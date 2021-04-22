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
        self.xValueRange = [0, 1]
        self.yValueRange = [0, 1]
        self.zValueRange = [0, 1]
        self.timestampRange = [0, 1]
        # initial the figure.
        global fig
        fig = plt.figure(figsize=(12, 6), facecolor="white")
        fig.subplots_adjust(left=0.06, right=0.9)
        ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[], yticks=[])
        ax.set_title('Motor Vibration Analysi', size=16, weight='bold')
        self.ax1 = fig.add_subplot(111)
        dateFormat = DateFormatter("%m/%d %H:%M:%S")
        self.ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
        self.lineX, = self.ax1.plot(self.timestamp, self.xValue, linestyle="-", color="red", label="x-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.lineY, = self.ax1.plot(self.timestamp, self.yValue, linestyle="-", color="blue", label="y-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.lineZ, = self.ax1.plot(self.timestamp, self.zValue, linestyle="-", color="black", label="z-Axis")
        self.ax1.legend(loc="upper right", frameon=True)
        self.ax1.set_xlabel("Times/s")
        self.ax1.set_ylabel("Acceleration(m/s^2)")
        buttonaxe = plt.axes([0.91, 0.5, 0.08, 0.08])
        button1 = Button(buttonaxe, 'History Data', color='khaki', hovercolor='yellow')
        button1.on_clicked(self.showHistory)

        
    def updateValue(self,x,y,z):
        self.xValue.append(x)
        self.yValue.append(y)
        self.zValue.append(z)
        timestamp = datetime.datetime.now()
        self.timestamp.append(timestamp)
        self.ax1.set_xlim(min(self.timestamp), max(self.timestamp)+datetime.timedelta(seconds=10))
        # timestamp = time.time()
        # ax1.set_xlim(min(t), max(t))
        # t.append(timestamp)
        xRange = [min(self.xValue), max(self.xValue)+1]
        yRange = [min(self.yValue), max(self.yValue)+1]
        zRange = [min(self.zValue), max(self.zValue)+1]
        xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
        self.ax1.set_ylim(min(xyRange[0], zRange[0])-20,
                    max(xyRange[1], zRange[1]) + 20)
        self.lineX.set_data(self.timestamp, self.xValue)
        self.lineY.set_data(self.timestamp, self.yValue)
        self.lineZ.set_data(self.timestamp, self.zValue)
        plt.pause(0.001)
        self.ax1.figure.canvas.draw()
    
    def showHistory(self):
        print("show history")
        plt.ioff()
        plt.show()