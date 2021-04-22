# coding:UTF-8
import matplotlib
matplotlib.use('TkAgg')
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure
import  sqlite3
import Tkinter
import urllib2
import json

class HistoryDataView(object):
    """
    Anomaly plot output.
    """

    # initial the figure parameters.
    def __init__(self):
        self.root = Tkinter.Tk()
        self.root.title("HistoryData")
        self.f = Figure(figsize=(18, 8), dpi=100)
        self.f_plot = self.f.add_subplot(111)
        self.canvs = FigureCanvasTkAgg(self.f, self.root)
        self.canvs.get_tk_widget().pack(side=Tkinter.TOP, fill=Tkinter.BOTH, expand=1)
        self.drawHistory()
        # Tkinter.Button(self.root, text='Real-time Data', command=self.draw_picture).pack()
        # Tkinter.Button(self.root, text='History Data', command=self.drawHistory).pack()
        self.root.mainloop()

    def other_picture_alg(self): #数据相关的算法应该与plot分离开
        x = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        y = [3, 6, 9, 12, 15, 18, 15, 12, 15, 18]
        return x, y

    def draw_picture(self):
        self.f_plot.clear()
        x = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] #关于数据的部分可以提取出来
        y = [3, 6, 9, 12, 15, 18, 21, 24, 27, 30]
        self.f_plot.plot(x, y)
        self.canvs.draw()

    def drawHistory(self):
        self.f_plot.clear()
        url = 'http://129.204.232.210:8900/mva/history'
        rq = urllib2.Request(url)
        rs = urllib2.urlopen(rq).read()
        # i = 1 +
        print(rs)
        # con=sqlite3.connect('mvaDataBase')
        # cursor=con.cursor()
        # selectSql = "SELECT * FROM mvaTable"
        # cursor.execute(selectSql)
        # result=cursor.fetchall()
        result = list(rs)
        t = []
        x = []
        y = []
        z = []
        for value in result:
            tvalue = value[0]
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            t.append(tvalue)
            x.append(xvalue)
            y.append(yvalue)
            z.append(zvalue)
        self.f_plot.plot(t, x, linestyle="-", color="red", label="x_value")
        self.f_plot.plot(t, y, linestyle="-", color="blue", label="y_value")
        self.f_plot.plot(t, z, linestyle="-", color="black", label="z_value")
        self.f_plot.legend(loc="upper right", frameon=True)
        self.canvs.draw()