# coding:UTF-8
import matplotlib
import matplotlib.pyplot as plt
import urllib2
import json
from matplotlib.dates import DateFormatter
import matplotlib.ticker as ticker
import datetime

class HistoryDataView():
    """
    Anomaly plot output.
    """

    # initial the figure parameters.
    def __init__(self):
        url = 'http://129.204.232.210:8901/mva/history'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        result = json.loads(rs)
        t = []
        x = []
        y = []
        z = []
        xZhou = []
        yZhou = []
        yTemp = []
        # print(result)
        for value in result:
            # print(value)
            tvalue = value[0]
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            timechange = datetime.datetime.fromtimestamp(tvalue)
            t.append(timechange)
            x.append(xvalue)
            y.append(yvalue)
            z.append(zvalue)
            yTemp.append(xvalue)
            yTemp.append(yvalue)
            yTemp.append(zvalue)
        # plt.figure()
        fig = plt.figure(3, figsize=(13, 6))
        # fig.subplots_adjust(left=0.2, right=0.8)
        # ax1 = fig.add_subplot(111,facecolor="#000000")
        fig.canvas.set_window_title('History Data')
        ax = plt.axes([0.07, 0.07, 0.86, 0.86], frameon=True, xticks=[], yticks=[])
        ax.set_xlabel("Times(s)")
        ax.set_ylabel("Acceleration(m/$\mathregular{s^2}$)")
        ax.set_title('History Data', size=16, weight='bold')
        dateFormat = DateFormatter("%m/%d %H:%M:%S")
        ax.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
        x_data = t
        y_data = x
        y_data2 = y
        y_data3 = z
        line,  = plt.plot(x_data,y_data,color='red',linestyle='-', label="x-Axis")
        ax.legend(loc="upper right", frameon=True)
        line1,  =plt.plot(x_data,y_data2,color='blue',linestyle='-', label="y-Axis")
        ax.legend(loc="upper right", frameon=True)
        line2, = plt.plot(x_data,y_data3,color='yellow',linestyle='-', label="z-Axis")
        ax.legend(loc="upper right", frameon=True)
        # xRange = [min(x), max(x)+1]
        # yRange = [min(y), max(y)+1]
        # zRange = [min(z), max(z)+1]
        # xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
        # ax.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
        # ax.set_ylim(min(xyRange[0], zRange[0])-20,
        #          max(xyRange[1], zRange[1]) + 20)
        # plt.xticks(t)
        yZhou.append(min(yTemp))
        yZhou.append(max(yTemp))
        yZhou.append(yTemp[len(yTemp)/4])
        yZhou.append(yTemp[len(yTemp)/2])
        yZhou.append(yTemp[len(yTemp)/2+len(yTemp)/4])

        xZhou.append(min(t))
        xZhou.append(max(t))
        xZhou.append(t[len(t)/4])
        xZhou.append(t[len(t)/2])
        xZhou.append(t[len(t)/2+len(t)/4])


        plt.xticks(xZhou) #横坐标日期范围及间隔
        plt.yticks(yZhou)

        # plt.ioff()
        plt.show() 

        # drawHistory
# HistoryDataView()