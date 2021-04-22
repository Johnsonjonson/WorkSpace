# coding:UTF-8
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from matplotlib.widgets import Button
from matplotlib.widgets import CheckButtons,RadioButtons
from matplotlib.widgets import TextBox
import urllib2
import datetime
import json
import time
import threading
from matplotlib.dates import DateFormatter
from matplotlib.dates import MicrosecondLocator
import matplotlib.ticker as ticker
from History import HistoryDataView 
# from RealTimeView import RealTimeView 
from concurrent.futures import ThreadPoolExecutor
executor = ThreadPoolExecutor()
from multiprocessing import Process
import numpy as np
import os
global t
global x
global y
global z
global flag
global valueTable
global line
global line1
global line2
t = []
x = []
y = []
z=  []
tableValue = []
flag = True
plt.ion()

# mpl.rcParams['font.sans-serif']=['SimHei'] #指定默认字体 SimHei为黑体
# mpl.rcParams['axes.unicode_minus']=False #用来正常显示负号

fig = plt.figure(0, figsize=(13, 6), dpi=120)
fig.subplots_adjust(left=0.06, right=0.70)
fig.canvas.set_window_title('Motor Vibration Analysis')
ax = plt.axes([0., 0., 0.74, 0.9], frameon=True, xticks=[], yticks=[],facecolor = "gray")
ax.set_title('Motor Vibration Analysis', size=16, weight='bold')

ax1 = fig.add_subplot(111,facecolor="#000000")
# dateFormat = DateFormatter("%m/%d\n%H:%M:%S")
# ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
# ax1.xaxis.set_major_locator(MicrosecondLocator(interval=10))

line, = ax1.plot(t, x, linestyle="-", color="red", label="x-Axis")
ax1.legend(loc="upper right", frameon=True)
# ax2 = fig.add_subplot(2, 1, 2)
line1, = ax1.plot(t, y, linestyle="-", color="blue", label="y-Axis")
ax1.legend(loc="upper right", frameon=True)
line2, = ax1.plot(t, z, linestyle="-", color="yellow", label="z-Axis")
ax1.legend(loc="upper right", frameon=True)
ax1.set_xlabel("Times(s)", color="white")
ax1.set_ylabel("Acceleration(m/$\mathregular{s^2}$)", color="white")


def func(label):
    if label == 'x_line':
        print("x_line")
        line.set_visible(not line.get_visible())
    elif label == 'y_line':
        print("y_line")
        line1.set_visible(not line1.get_visible())
    elif label == 'z_line':
        print("z_line")
        line2.set_visible(not line2.get_visible())
    plt.draw()
# def initCheckButtons():
ax_cb = fig.add_axes([0.76, 0.04, 0.08, 0.2], frameon=True)
ax_cb.text(0.1, 1.1, "Visibility", size=12)
proj_checks = CheckButtons(ax_cb, labels=["x_line","y_line","z_line"], actives=[True,True,True])
proj_checks.on_clicked(func)

def showHistory(t):
    HistoryDataView()
    # RealTimeView()

def drawTable():
    xRange = [min(x), max(x)]
    yRange = [min(y), max(y)]
    zRange = [min(z), max(z)]
    valueTable = fig.add_axes([0.76, 0.8, 0.2, 0.1], frameon=False)
    valueTableColumnsName = ["Axis", "Max. Value", "Min. Value"]
    valueTable.text(0.1, 1.1, "Maximum and minimum", size=12)
    valueTable.set_xticks([])
    valueTable.set_yticks([])
    tableValue = ((["x",xRange[1],xRange[0]],["y",yRange[1],yRange[0]],["z",zRange[1],zRange[0]]))
    valueTable.table(cellText=tableValue,colWidths=[0.35] * 4,colLabels=valueTableColumnsName,loc=1,cellLoc="center")
    
def updateValue(a, b, c,timeValue):
    # plt.cla()
    x.append(a)
    y.append(b)
    z.append(c)
    timechange = datetime.datetime.fromtimestamp(timeValue)
    t.append(timechange)
    # ax1.set_xlim(min(t), max(t))
    ax1.set_xlim(max(t)-datetime.timedelta(seconds=20), max(t))
    # ax1.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
    xRange = [min(x), max(x)]
    yRange = [min(y), max(y)]
    zRange = [min(z), max(z)]
    xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
    ax1.set_ylim(min(xyRange[0], zRange[0])-1,
                 max(xyRange[1], zRange[1]) + 1)
    # for link in range(len(t)):
    #     plt.text(t[link],-1.2,year,ha='center')#需要根据自己的位置调整Y的坐标即调整-1
    # ax1.set_ylim(-10,
    #              10)
    line.set_data(t, x)
    line1.set_data(t, y)
    line2.set_data(t, z)
    # drawTable()
    print(time.time())
    plt.pause(0.000001)
    # background = fig.canvas.copy_from_bbox(ax.bbox)
    # fig.canvas.restore_region(background)
    # ax1.draw_artist(line)
    # ax1.draw_artist(line1)
    # ax1.draw_artist(line2)
    # fig.canvas.blit(ax1.bbox)
    # plt.draw()
    ax1.figure.canvas.draw()


def requestData():
    # url = 'http://www.ftmsccc.com/ceshi/api.php?a=pulldata'
    # rq = urllib2.Request(url)
    # rs = urllib2.urlopen(rq).read()
    # # i = 1 +i
    # print(rs)
    # pyData = json.loads(rs)
    # xyzData = pyData['data']
    # xData = xyzData['xzhou']
    # yData = xyzData['yzhou']
    # zData = xyzData['zzhou']
    xData = np.random.normal(1, 1, 10)
    yData = np.random.normal(1, 1, 10)
    zData = np.random.normal(1, 1, 10)
    print(xData)
    for i in range(len(xData)):
        # print(xData[i])
        updateValue(xData[i], yData[i], zData[i],time.time())
    

def startReal(object):
    print("============start-real")
    global flag
    flag = True
    while flag == True:
        # requestData()
        # executor.submit(requestData)
        # head_process=Process(target=requestData)
        # gaze_process=Process(target=show_heat_map,args=(x_gaze,y_gaze,2,))
        # head_process.start()
        # head_process.join()
        plt.pause(0.001)
        maxTryNum=10  
        for tries in range(maxTryNum):  
            try: 
                user_agent ='"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"'  
                headers = { 'User-Agent' : user_agent }  
                url = 'http://129.204.232.210:8901/mva/getdata'
                rq = urllib2.Request(url, headers = headers)
                rs = urllib2.urlopen(rq).read()
                pyData = json.loads(rs)
                break  
            except:  
                if tries <(maxTryNum-1):  
                    continue  
                else: 
                    print("error")
                    break  
        # url = 'http://129.204.232.210:8901/mva/getdata'
        # # url = 'http://www.ftmsccc.com/ceshi/api.php?a=pulldata'
        # rq = urllib2.Request(url)
        # rs = urllib2.urlopen(rq).read()
        # pyData = json.loads(rs)
        # i = 1 +i
        # print(rs)
        for value in pyData:
            # print(value)
            tvalue = value[0]
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            updateValue(xvalue, yvalue,zvalue,time.time())
        # for i in range(len(pyData)):
        #     xValue = float(pyData[i][1])
        #     yValue = float(pyData[i][2])
        #     zValue = float(pyData[i][3])
        #     timeValue = float(pyData[i][0])
        #     print(xValue,yValue,zValue,timeValue)
        #     updateValue(xValue, yValue, zValue,timeValue)

def stopReal(object):
    global flag
    flag = False

def closeWin(event):
    plt.close()
    global flag
    flag = False

def clearView(object):
    global t
    global x
    global y
    global z
    t = []
    x = []
    y = []
    z = []
    line.set_data(t, x)
    line1.set_data(t, y)
    line2.set_data(t, z)
    ax1.figure.canvas.draw()

def saveData(object):
    path = os.getcwd()
    otherStyleTime0 = t[0].strftime("%Y%m%d_%H%M%S")
    otherStyleTime1 = t[len(t)-1].strftime("%Y%m%d_%H%M%S")
    pathEx = otherStyleTime1 + "-" + otherStyleTime0
    with open(path+"./" + pathEx + ".txt",'w') as f:
        for index in range(len(x)):
            f.write("%s %s %s %s \r\n" % (t[index],x[index],y[index],z[index]))
        f.close()

buttonaxe1 = plt.axes([0.914, 0.1, 0.08, 0.08])
button1 = Button(buttonaxe1, 'History Data', color='khaki', hovercolor='yellow')
button1.on_clicked(showHistory)

buttonaxe4 = plt.axes([0.86, 0.05, 0.05, 0.05])
button4 = Button(buttonaxe4, 'Save', color='khaki', hovercolor='yellow')
button4.on_clicked(saveData)

buttonaxe3 = plt.axes([0.86, 0.12, 0.05, 0.05])
button3 = Button(buttonaxe3, 'Stop', color='khaki', hovercolor='yellow')
button3.on_clicked(stopReal)

buttonaxe2 = plt.axes([0.86, 0.19, 0.05, 0.05])
button2 = Button(buttonaxe2, 'Start', color='khaki', hovercolor='yellow')
button2.on_clicked(startReal)

buttonaxe5 = plt.axes([0.86, 0.26, 0.05, 0.05])
button5 = Button(buttonaxe5, 'Clear', color='khaki', hovercolor='yellow')
button5.on_clicked(clearView)

# buttonaxe6 = plt.axes([0.86, 0.26, 0.05, 0.05])
# button6 = Button(buttonaxe6, 'Clear', color='khaki', hovercolor='yellow')
# button6.on_clicked(clearView)

fig.canvas.mpl_connect('close_event', closeWin)
# freeze_support()
# startReal(None)
# executor.submit(startReal)
# plt.ioff()
# plt.show()

if __name__ == '__main__':
    ani = animation.FuncAnimation(fig, startReal, xrange(1, 200), 
                              interval=0, blit=True)
    # startReal(None)
    plt.ioff()
    plt.show()