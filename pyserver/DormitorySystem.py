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

app = flask.Flask(__name__)

# global mydb

# 创建链接数据库
def connect():
	config = {'host': 'localhost',  # 默认127.0.0.1
	          'user': 'root',
	          'password': 'zqs@@740848126',
	          'port': 3306,  # 默认即为3306
	          'database': 'dormitory',
	          'charset': 'utf8'  # 默认即为utf8
	          }
	try:
		mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
	except mysql.connector.Error as e:
		print(u'----------------连接数据库  ---------------  数据库链接失败！', str(e))
	else:  # try没有异常的时候才会执行
		print(u"----------------连接数据库  ---------------  数据库连接sucessfully!")
		return mydb

class Update(MethodView):
	def get(self):
		try:
			bookDB = connect()
			card_id = request.args.get("cardId", 0) # 0 未刷卡，1，2
			temp = request.args.get("temp", 0)
			humdity = request.args.get("humdity", 0)
			num = request.args.get("num", 0)
			if int(card_id) != 0:
				timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
				sql1 = "UPDATE t_user SET isSwipe=%s,swipeTime=%s WHERE cardId=%s"
				val1 = (1, timestamp,card_id)
				mycursor = bookDB.cursor()
				mycursor.execute(sql1, val1)
				bookDB.commit()  # 数据表内容有更新，必须使用到该语句
			sql = "UPDATE t_data SET temp=%s, humdity=%s,realTimeNum=%s"
			val = (temp, humdity, num)
			mycursor1 = bookDB.cursor()
			mycursor1.execute(sql, val)
			bookDB.commit()  # 数据表内容有更新，必须使用到该语句
		except:
			return "400"
		bookDB.close()
		return "200"
	
	def post(self):
		return self.get()


if __name__ == '__main__':
	app.add_url_rule('/update', view_func=Update.as_view('update'))  # 更新停车位取车状态
	# connect()
	# initBookTime()
	app.run(host='0.0.0.0', port=8536)
