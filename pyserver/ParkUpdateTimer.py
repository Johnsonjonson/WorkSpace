# -*- coding: utf-8 -*-
import math
import threading

import flask
from flask import request
from flask.views import MethodView
import time
import json
import mysql.connector
import datetime


# bookTime = 3600
# recoverTime = 3600*3

bookTime = 10
recoverTime = 15
# 创建链接数据库
def connect():
	config = {'host': 'localhost',  # 默认127.0.0.1
			  'user': 'root',
			  'password': 'zqs@@740848126',
			  'port': 3306,  # 默认即为3306
			  'database': 'parking',
			  'charset': 'utf8'  # 默认即为utf8
			  }
	try:
		mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
	except mysql.connector.Error as e:
		print(u'----------------连接数据库  ---------------  数据库链接失败！', str(e))
	else:  # try没有异常的时候才会执行
		print(u"----------------连接数据库  ---------------  数据库连接sucessfully!")
		return mydb

def initBookTime():
	initBookDB = connect()
	try:
		cursor = initBookDB.cursor()
		# SQL 查询语句
		sql = "SELECT * FROM t_user"
		print(sql)
		# 执行SQL语句
		cursor.execute(sql)
		# 获取所有记录列表
		results = cursor.fetchall()
		for row in results:
			id = row[0]
			name = str(row[1])
			pwdS = str(row[2])
			status = int(row[3] or 0)
			parkId = int(row[4] or 0)
			parkTime = str(row[5])
			bookTime = str(row[6])
			# 字符类型的时间
			# 转为时间数组
			timeArray = time.strptime(bookTime, "%Y-%m-%d %H:%M:%S")
			# 转为时间戳
			booktimeStamp = int(time.mktime(timeArray))
			curtimestamp = int(time.time())
			difTime = int(curtimestamp - booktimeStamp)
			if difTime > bookTime and status == 1:  # ,预约时，超时
				sql = "UPDATE t_user SET parkId=%s, status=%s WHERE id=%s"
				val = (0, 3, id)  # 超时
				if difTime > recoverTime:  # 超时
					val = (0, 0, id)  # 恢复正常
				mycursor1 = initBookDB.cursor()
				mycursor1.execute(sql, val)
				initBookDB.commit()  # 数据表内容有更新，必须使用到该语句
				
				sql = "UPDATE t_pack SET status=%s, userId=%s,canTakeCar=%s WHERE id=%s"
				val = (0, 0, 0, parkId)
				mycursor2 = initBookDB.cursor()
				mycursor2.execute(sql, val)
				initBookDB.commit()  # 数据表内容有更新，必须使用到该语句
				pass
	
	except:
		print
		u"---------------------查询车位信息报错-------------------"
	initBookDB.close()
	

from threading import _Timer

class RepeatingTimer(_Timer):
	def run(self):
		while not self.finished.is_set():
			self.function(*self.args, **self.kwargs)
			self.finished.wait(self.interval)
t = RepeatingTimer(5.0, initBookTime)
t.start()