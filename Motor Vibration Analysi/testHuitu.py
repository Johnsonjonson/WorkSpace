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

t = []
x = []
y = []
z = []
plt.ion()
fig = plt.figure(2, figsize=(12, 6), dpi=120)
fig.subplots_adjust(left=0.06, right=0.9)
ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[], yticks=[])
ax.set_title('Motor Vibration Analysi', size=16, weight='bold')

ax1 = fig.add_subplot(111)
dateFormat = DateFormatter("%m/%d %H:%M:%S")
ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
line, = ax1.plot(t, x, linestyle="-", color="red", label="x-Axis")
ax1.legend(loc="upper right", frameon=True)
ys = np.random.normal(100, 10, 1000)

# ax2 = fig.add_subplot(2, 1, 2)
line1, = ax1.plot(t, y, linestyle="-", color="blue", label="y-Axis")
ax1.legend(loc="upper right", frameon=True)
ys1 = np.random.normal(100, 10, 1000)

line2, = ax1.plot(t, z, linestyle="-", color="black", label="z-Axis")
ax1.legend(loc="upper right", frameon=True)
ys1 = np.random.normal(100, 10, 1000)

ax1.set_xlabel("Times/s")
ax1.set_ylabel("Acceleration(m/s^2)")

def showHistory(t):
    HistoryDataView()
    
def updateValue(a, b, c,timeValue):
    x.append(a)
    y.append(b)
    z.append(c)
    timestamp = datetime.datetime.now()
    print(timestamp)
    timechange = datetime.datetime.fromtimestamp(timeValue)
    # timechange = time.gmtime(timeValue) 
    print(timechange) 
    t.append(timechange)
    ax1.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
    # timestamp = time.time()
    # ax1.set_xlim(min(t), max(t))
    # t.append(timestamp)
    xRange = [min(x), max(x)+1]
    yRange = [min(y), max(y)+1]
    zRange = [min(z), max(z)+1]
    xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
    ax1.set_ylim(min(xyRange[0], zRange[0])-20,
                 max(xyRange[1], zRange[1]) + 20)
    line.set_data(t, x)
    line1.set_data(t, y)
    line2.set_data(t, z)
    plt.pause(0.001)
    ax1.figure.canvas.draw()

def startReal(object):
    i = 1
    while True:
        url = 'http://129.204.232.210:8900/mva/getdata'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        i = 1 +i
        print(rs,i)
        pyData = json.loads(rs)
        xValue = pyData["x"]
        yValue = pyData["y"]
        zValue = pyData["z"]
        timeValue = pyData["timestamp"]
        print(xValue,yValue,zValue,timeValue)
        updateValue(xValue, yValue, zValue,timeValue)
        plt.pause(0.5)


buttonaxe = plt.axes([0.91, 0.4, 0.08, 0.08])
button1 = Button(buttonaxe, 'History Data', color='khaki', hovercolor='yellow')
button1.on_clicked(showHistory)

buttonaxe = plt.axes([0.91, 0.6, 0.08, 0.08])
button2 = Button(buttonaxe, 'Real-time', color='khaki', hovercolor='yellow')
button2.on_clicked(startReal)

startReal(None)
plt.ioff()
plt.show()