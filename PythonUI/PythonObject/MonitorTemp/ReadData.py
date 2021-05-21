# -*- coding: utf-8 -*-
"""
Created on Sun Jan 26 14:24:43 2014

@author: pi
"""

import Adafruit_DHT
import time
import numpy as np


def readData():
	# time.sleep(1)
	# temperature = np.random.randint(100)
	# humidity = np.random.randint(100)
	humidity, temperature = Adafruit_DHT.read_retry(11, 4)
	return temperature, humidity
