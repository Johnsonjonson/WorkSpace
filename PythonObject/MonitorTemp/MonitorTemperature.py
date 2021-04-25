# coding:UTF-8
# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

import json
from matplotlib import ticker
from matplotlib.dates import DateFormatter
import SendEmail
import datetime
import urllib.request
import matplotlib.pyplot as plt
import numpy as np
import ReadData

lowest_temperature = 10

t = []
wen_du = []
shi_du = []
plt.ion()
fig = plt.figure(2, figsize=(12, 6), dpi=120)
fig.subplots_adjust(left=0.06, right=0.9)
ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[], yticks=[])
ax.set_title('Temperature and humidity monitoring', size=16, weight='bold')

ax1 = fig.add_subplot(111)
dateFormat = DateFormatter("%m/%d %H:%M:%S")
ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))

wenduline, = ax1.plot(t, wen_du, linestyle="-", color="red", label="Temperature")
ax1.legend(loc="upper right", frameon=True)
shiduline, = ax1.plot(t, shi_du, linestyle="-", color="blue", label="humidity")
ax1.legend(loc="upper right", frameon=True)

ax1.set_xlabel("Times/s")
ax1.set_ylabel("Value")

def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.
    SendEmail.send()
    
def updateValue(wendu, shidu):
    wen_du.append(wendu)
    shi_du.append(shidu)
    # z.append(c)
    timestamp = datetime.datetime.now()
    t.append(timestamp)
    ax1.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
    # timestamp = time.time()
    # ax1.set_xlim(min(t), max(t))
    # t.append(timestamp)
    wenduRange = [min(wen_du), max(wen_du)+1]
    shiduRange = [min(shi_du), max(shi_du)+1]
    xyRange = [min(wenduRange[0], shiduRange[0]), max(wenduRange[1], shiduRange[1])]
    ax1.set_ylim(xyRange[0],xyRange[1])
    wenduline.set_data(t, wen_du)
    shiduline.set_data(t, shi_du)
    plt.pause(0.001)
    ax1.figure.canvas.draw()

def startReal():
    while True:
        # url = 'http://129.204.232.210:8900/mva/getdata'
        # rs = urllib.request.urlopen(url).read()
        # i = 1 +i
        # print(rs,i)
        # pyData = json.loads(rs)
        # wenduValue = pyData["wendu"]
        # shiduValue = pyData["shidu"]
        # timeValue = pyData["timestamp"]
        wenduValue,shiduValue = ReadData.readData()
        # wenduValue =np.random.randint(100)
        # shuduValue = np.random.randint(100)
        # timeValue = datetime.datetime.now()
        print(wenduValue,shiduValue)
        if float(wenduValue) < float(lowest_temperature):
            print(u"温度过低")
            SendEmail.send()  #温度小于最低温度 发送警报
        updateValue(wenduValue, shiduValue)
        plt.pause(0.5)

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    # print_hi('PyCharm')
    startReal()
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
