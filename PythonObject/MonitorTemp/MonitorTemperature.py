# coding:UTF-8
from matplotlib import ticker
from matplotlib.dates import DateFormatter
from matplotlib.widgets import TextBox

import SendEmail
import datetime
import matplotlib.pyplot as plt
import ReadData

lowest_temperature = 0  #最低温度阈值 请结合实际情况设定
highest_humidity = 100  #最高湿度阈值 请结合实际情况设定

t = []
wen_du = []
shi_du = []
plt.ion()
plt.rcParams['font.sans-serif']=['SimHei']     #正常显示中文标签
fig = plt.figure(2, figsize=(12, 6), dpi=120)
fig.subplots_adjust(left=0.06, right=0.9)
ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[], yticks=[])
ax.set_title('Temperature and humidity monitoring', size=16, weight='bold')
# ax.set_title(u'温湿度监测', size=16, weight='bold')


ax1 = fig.add_subplot(111)
dateFormat = DateFormatter("%m/%d %H:%M:%S")
ax1.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))

wenduline, = ax1.plot(t, wen_du, linestyle="-", color="red", label="Temperature")
ax1.legend(loc="upper right", frameon=True)
shiduline, = ax1.plot(t, shi_du, linestyle="-", color="blue", label="humidity")
ax1.legend(loc="upper right", frameon=True)

ax1.set_xlabel("Times/s")
ax1.set_ylabel("Value")

wendu_tips_axe = plt.axes([0.91, 0.4, 0.08, 0.08])
shudu_tips_axe = plt.axes([0.91, 0.6, 0.08, 0.08])
wendualertTips = TextBox(wendu_tips_axe, '', color='red')
shidualertTips = TextBox(shudu_tips_axe, '', color='red')

def updateValue(wendu, shidu):
    wen_du.append(wendu)
    shi_du.append(shidu)
    timestamp = datetime.datetime.now()
    t.append(timestamp)
    ax1.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
    wenduRange = [min(wen_du), max(wen_du)+1]
    shiduRange = [min(shi_du), max(shi_du)+1]
    xyRange = [min(wenduRange[0], shiduRange[0]), max(wenduRange[1], shiduRange[1])]
    ax1.set_ylim(xyRange[0],xyRange[1])
    wenduline.set_data(t, wen_du)
    shiduline.set_data(t, shi_du)
    plt.pause(0.001)
    ax1.figure.canvas.draw()

def startReal():
    curWendu, curShidu = -1,-1
    while True:
        wenduValue,shiduValue = ReadData.readData()
        # wendualertTips.set_val("温度没变")
        if curWendu !=-1 and curShidu!=-1:
            if wenduValue > curWendu:
                wendualertTips.set_val("温度升高   ↑")
            else:
                wendualertTips.set_val("温度降低   ↓")
            if shiduValue > curShidu:
                shidualertTips.set_val("湿度升高   ↑")
            else:
                shidualertTips.set_val("湿度降低   ↓")
        curWendu, curShidu = wenduValue, shiduValue
        print(wenduValue,shiduValue)
        if float(wenduValue) < float(lowest_temperature):
            print(u"温度过低")
            SendEmail.send('警报：温度过低')  #温度小于最低温度 发送警报
        if float(shiduValue) > float(highest_humidity):
            print(u"湿度过高")
            SendEmail.send('警报：湿度过高')  # 湿度大于最高湿度 发送警报
        updateValue(wenduValue, shiduValue)
        plt.pause(0.5)

if __name__ == '__main__':
    startReal()
