# coding:UTF-8
import matplotlib
matplotlib.use('TkAgg')
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure
import  sqlite3
import Tkinter
from History import HistoryDataView 
import matplotlib.pyplot as plt
import time
import websocket
import threading
import wx
from pubsub import pub

class RealTimeView(object):
    """
    Anomaly plot output.
    """

    # initial the figure parameters.
    def __init__(self):
        self.root = Tkinter.Tk()
        self.root.title("Motor Vibration Analysi")
        plt.ion()
        self.f = plt.figure(figsize=(18, 8))
        # self.f = Figure(figsize=(18, 8), dpi=100)
        self.f_plot = self.f.add_subplot(111)
        ax = plt.axes([0., 0., 1, 0.9], frameon=False, xticks=[],yticks=[])
        ax.set_title('Motor Vibration Analysi', size=16, weight='bold')
        self.xValues = []
        self.yValues = []
        self.zValues = []
        self.tValues = []
        self.xLine, = self.f_plot.plot(self.xValues, self.tValues, linestyle="-", color="red", label="x_value")
        self.yLine, = self.f_plot.plot(self.yValues, self.tValues, linestyle="-", color="blue", label="y_value")
        self.zLine, = self.f_plot.plot(self.zValues, self.tValues, linestyle="-", color="black", label="z_value")
        self.canvs = FigureCanvasTkAgg(self.f, self.root)
        self.canvs.get_tk_widget().pack(side=Tkinter.TOP, fill=Tkinter.BOTH, expand=1)
        # self.drawHistory()
        # Tkinter.Button(self.root, text='Real-time Data', command=self.draw_picture).pack()
        Tkinter.Button(self.root, text='History Data', command=self.showHistoryData).pack()
        # self.root.after(1, self.updateRealView)
        pub.subscribe(self.updateRealView, "update")
        self.connectSocket()
        self.root.mainloop()

    def on_message(self, message):
        print("receive Msg = ", message)
        # self.setValues(200,200,100)
        pub.sendMessage("update",data = message)

    def on_error(self, error):
        print(error)


    def on_close(self):
        print("### closed ###")


    def on_open(self):
        print("### open ###")
        pub.sendMessage("update",data = "message")
        # self.setValues(200,200,100)

    def connectSocket(self):
        # socketUrl = "ws://localhost:8080/PythonTest_war_exploded/PythonWebSocket"
        socketUrl = "ws://129.204.232.210:8080/light/PythonWebSocket"
        self.ws = websocket.WebSocketApp(socketUrl,
                                         on_message=self.on_message,
                                         on_error=self.on_error,
                                         on_close=self.on_close)
        self.ws.on_open = self.on_open
        print(type(self.ws))
        wst = threading.Thread(target=self.ws.run_forever)
        wst.daemon = True
        wst.start()

    # 显示历史数据
    def showHistoryData(self):
        # HistoryDataView()
        con=sqlite3.connect('mvaDataBase')
        cursor=con.cursor()
        selectSql = "SELECT * FROM mvaTable"
        cursor.execute(selectSql)
        result=cursor.fetchall()
        for value in result:
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            self.setValues(xvalue,yvalue,zvalue)
            # x.append(xvalue)
            # y.append(xvalue)
            # z.append(xvalue)
            print("xvalue = %s yvalue =  %s zvalue = %s" % (xvalue,yvalue,zvalue))
        print(result[1][1])
        
        print(u"显示历史数据")
    
    def updateRealView(self,data):
        # self.root.after(1, self.updateRealView)
        print("=======updateRealView==========",data)
        th=threading.Thread(target=self.setValues,args=(233, 344,673))  
        th.setDaemon(True) 
        th.start()  
        # self.setValues(100, 200,300)

    def setValues(self,x, y,z):
        self.xValues.append(x)
        self.yValues.append(y)
        self.zValues.append(z)
        self.tValues.append(time.time())
        self.f_plot.set_xlim(min(self.tValues), max(self.tValues) + 10)
        self.f_plot.set_ylim(min(self.xValues)-10, max(self.xValues) + 10)
        self.xLine.set_data(self.tValues, self.xValues)
        self.yLine.set_data(self.tValues,self.yValues)
        self.zLine.set_data(self.tValues,self.zValues)
        plt.pause(0.001)
        self.canvs.draw()

if __name__ == '__main__':
    realView = RealTimeView()
    # realView.root.after(1, realView.updateRealView)