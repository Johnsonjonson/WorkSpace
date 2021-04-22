# coding:UTF-8
import matplotlib
matplotlib.use('TkAgg')
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure
import  sqlite3
import Tkinter
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
        self.root = Tkinter.Tk()
        self.root.title("HistoryData")
        self.f = Figure(figsize=(12, 6), dpi=100)
        self.f_plot = self.f.add_subplot(111)
        dateFormat = DateFormatter("%m/%d %H:%M:%S")
        self.f_plot.xaxis.set_major_formatter(ticker.FuncFormatter(dateFormat))
        self.canvs = FigureCanvasTkAgg(self.f, self.root)
        self.canvs.get_tk_widget().pack(side=Tkinter.TOP, fill=Tkinter.BOTH, expand=1)
        self.drawHistory()
        # Tkinter.Button(self.root, text='Real-time Data', command=self.draw_picture).pack()
        # Tkinter.Button(self.root, text='History Data', command=self.drawHistory).pack()
        self.root.mainloop()

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
        # print(result)
        result = json.loads(rs)
        t = []
        x = []
        y = []
        z = []
        print(result)
        for value in result:
            print(value)
            tvalue = value[0]
            xvalue = value[1]
            yvalue = value[2]
            zvalue = value[3]
            timechange = datetime.datetime.fromtimestamp(tvalue)
            t.append(timechange)
            x.append(xvalue)
            y.append(yvalue)
            z.append(zvalue)

        self.f_plot.set_xlim(min(t), max(t)+datetime.timedelta(seconds=10))
        # timestamp = time.time()
        # ax1.set_xlim(min(t), max(t))
        # t.append(timestamp)
        xRange = [min(x), max(x)+1]
        yRange = [min(y), max(y)+1]
        zRange = [min(z), max(z)+1]
        xyRange = [min(xRange[0], yRange[0]), max(xRange[1], yRange[1])]
        self.f_plot.set_ylim(min(xyRange[0], zRange[0])-20,
                 max(xyRange[1], zRange[1]) + 20)
        self.f_plot.plot(t, x, linestyle="-", color="red", label="x_value")
        self.f_plot.plot(t, y, linestyle="-", color="blue", label="y_value")
        self.f_plot.plot(t, z, linestyle="-", color="black", label="z_value")
        self.f_plot.legend(loc="upper right", frameon=True)
        self.canvs.draw()

        # drawHistory