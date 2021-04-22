import matplotlib.pyplot as plt
import numpy as np
import threading
import random
import time

class MyDataClass():

    def __init__(self):

        self.XData = [0]
        self.YData = [0]


class MyPlotClass(threading.Thread):

    def __init__(self, dataClass):

        threading.Thread.__init__(self)

        self._dataClass = dataClass
        self._period = 1
        self._nextCall = time.time()

        self.hLine, = plt.plot(0, 0)

        plt.ion()

    def run(self):

        while True:
            self.hLine.set_data(self._dataClass.XData, self._dataClass.YData)
            plt.draw()
            print("updated %i datapoints" % len(self._dataClass.XData))
            # sleep until next execution
            self._nextCall = self._nextCall + self._period
            time.sleep(self._nextCall - time.time())
            plt.ioff()
            plt.show()


class MyDataFetchClass(threading.Thread):

    def __init__(self, dataClass):

        threading.Thread.__init__(self)

        self._dataClass = dataClass
        self._period = 0.25
        self._nextCall = time.time()

    def run(self):

        while True:
            # add data to data class
            self._dataClass.XData.append(self._dataClass.XData[-1] + 1)
            self._dataClass.YData.append(random.randint(0, 256))
            print("Added (%i, %i)" % (self._dataClass.XData[-1], self._dataClass.YData[-1]))
            # sleep until next execution
            self._nextCall = self._nextCall + self._period
            time.sleep(self._nextCall - time.time())


data = MyDataClass()
fetcher = MyDataFetchClass(data)
plotter = MyPlotClass(data)

fetcher.start()
plotter.start()

fetcher.join()
plotter.join()