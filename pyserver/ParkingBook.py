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


class registerUser(MethodView):
	def get(self):
		try:
			registerDB = connect()
			pwd = request.args.get("pwd", '')
			name = request.args.get("name", '')
			timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
			sql = "INSERT INTO t_user (name,pwd) VALUES (%s, %s)"
			val = (name, pwd)
			print(u"---------------------注册用户------------------- 数据库是否连接" + str(registerDB.is_connected()))
			mycursor = registerDB.cursor()
			mycursor.execute(sql, val)
			id = mycursor.lastrowid
			registerDB.commit()  # 数据表内容有更新，必须使用到该语句
			# result = {'name': '', "pwd": '', 'id': 0, 'tel': '', 'political': 0, 'math': 0, 'english': 0, 'autoctrl': 0,
			#           'daka_num': 0, 'errorno': 0}
			print(u"---------------------注册用户-------------------记录插入成功。   id  = " + str(id))
			result = queryUser(id)
		except:
			result = {"errorno": 0}
			print(u"---------------------学生注册报错-------------------")
			registerDB.close()
		return flask.jsonify(result)
	
	def post(self):
		return self.get()


def queryUser(id):
	try:
		
		stuDB = connect()
		result = {'name': '', "pwd": '', 'id': 0, 'status': 0, 'parkId': 0, 'parkTime': '', 'bookTime': '',
		          'errorno': 0}
		print(u"查询用户。   id  = " + str(id))
		sql = "SELECT * FROM t_user WHERE id=%s" % id
		print(sql)
		mycursor = stuDB.cursor()
		# 执行SQL语句
		mycursor.execute(sql)
		# 获取所有记录列表
		isCorrect = False
		results = mycursor.fetchall()
		if len(results) <= 0:
			result['errorno'] = 0
			return result
		else:
			for row in results:
				ids = int(row[0])
				names = str(row[1])
				pwds = str(row[2])
				status = int(row[3] or 0)
				parkId = int(row[4] or 0)
				parkTime = str(row[5])
				bookTime = str(row[6])
				if ids == int(id):
					# result = {'name':name,"pwd":pwd,'id':id}
					result['name'] = names
					result['id'] = ids
					result['pwd'] = pwds
					result['status'] = status
					result['parkId'] = parkId
					result['parkTime'] = parkTime
					result['bookTime'] = bookTime
					result['errorno'] = 1
					isCorrect = True
					break
				# 打印结果
				if not isCorrect:
					result['errorno'] = 2
	except:
		result = {"errorno": 0}
		print
		u"---------------------查询同学报错-------------------"
	
	# 关闭数据库连接
	stuDB.close()
	return result


class userLogin(MethodView):
	def get(self):
		try:
			
			loginDB = connect()
			pwd = request.args.get("pwd", '')
			name = request.args.get("name", '')
			timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
			# 使用cursor()方法获取操作游标
			print(name, pwd)
			cursor = loginDB.cursor()
			# SQL 查询语句
			sql = "SELECT * FROM t_user WHERE name='%s'" % name
			print(sql)
			result = {'name': '', "pwd": '', 'id': 0, 'status': 0, 'parkId': 0, 'parkTime':"", 'bookTime': "",
			          'errorno': 0}
			# 执行SQL语句
			cursor.execute(sql)
			# 获取所有记录列表
			isCorrect = False
			results = cursor.fetchall()
			if len(results) <= 0:
				print(u"未查询到用户")
				result['errorno'] = 0
				pass
			for row in results:
				id = row[0]
				name = str(row[1])
				pwdS = str(row[2])
				status = int(row[3] or 0)
				parkId = int(row[4] or 0)
				parkTime = str(row[5])
				bookTime = str(row[6])
				
				if pwd == pwdS:
					# result = {'name':name,"pwd":pwd,'id':id}
					result['name'] = name
					result['id'] = id
					result['pwd'] = pwd
					result['status'] = status
					result['parkId'] = parkId
					result['parkTime'] = parkTime
					result['bookTime'] = bookTime
					result['errorno'] = 1
					isCorrect = True
					break
			# 打印结果
			if not isCorrect:
				result['errorno'] = 2
			print(id, name, pwdS)
		except:
			result = {"errorno": 0}
			print
			u"---------------------学生登陆报错-------------------"
		# 关闭数据库连接
		loginDB.close()
		
		return flask.jsonify(result)
	
	def post(self):
		return self.get()

class UpdatePwd(MethodView):
	def get(self):
		try:
			
			loginDB = connect()
			pwd = request.args.get("pwd", '')
			newpwd = request.args.get("newpwd", '')
			id = request.args.get("id", 0)
			
			result = queryUser(id)
			print(newpwd, pwd,id,result['pwd'])
			if result['errorno'] == 1:
				if result['pwd'] == pwd:
					sql = "UPDATE t_user SET pwd=%s WHERE id=%s"
					val = (newpwd, id)
					print(u"-------------------update password --------------- ",newpwd, id);
					mycursor = loginDB.cursor()
					mycursor.execute(sql, val)
					loginDB.commit()  # 数据表内容有更新，必须使用到该语句
					result['pwd'] = newpwd
				else:
					result['errorno'] = 2 #密码不正确
			else:
				result = {"errorno": 3} #未查询到用户
		except:
			result = {"errorno": 0}
			print
			u"---------------------修改密码报错-------------------"
		# 关闭数据库连接
		loginDB.close()
		
		return flask.jsonify(result)
	
	def post(self):
		return self.get()

def QueryParkById(parkId):
	try:
		queryParkDB = connect()
		result = {'id': '', "status": '', 'time': "", 'userId': 0, 'name': "", 'canTakeCar': 0, 'errorno': 0}
		print(u"查询停车位。   id  = " + str(parkId))
		sql = "SELECT * FROM t_pack where id=%s" %parkId
		print(sql)
		cursor = queryParkDB.cursor()
		# 执行SQL语句
		cursor.execute(sql)
		# 获取所有记录列表
		isCorrect = False
		parkresults = cursor.fetchall()
		if len(parkresults) <= 0:
			result['errorno'] = 0
			return result
		else:
			for row in parkresults:
				ids = int(row[0])
				if ids == int(parkId):
					# result = {'name':name,"pwd":pwd,'id':id}
					result["id"] = ids
					result["status"] =row[1]
					result["time"] = str(row[2])
					result["userId"] = int(row[3] or 0)
					result["name"] = str(row[4])
					result["canTakeCar"] = row[5] or 0
					result['errorno'] = 1
					isCorrect = True
					break
				# 打印结果
			if not isCorrect:
				result['errorno'] = 2
	except:
		result = {"errorno": 0}
		print
		u"---------------------查询停车位报错-------------------"
	# 关闭数据库连接
	queryParkDB.close()
	return result


# 查询所有停车位
class QueryPark(MethodView):
	def get(self):
		try:
			queryDB = connect()
			cursor = queryDB.cursor()
			# SQL 查询语句
			sql = "SELECT * FROM t_pack"
			print(sql)
			result = []
			# 执行SQL语句
			cursor.execute(sql)
			# 获取所有记录列表
			results = cursor.fetchall()
			i = 0
			for row in results:
				temp = {}
				temp["id"] = row[0]
				temp["status"] = str(row[1])
				temp["time"] = str(row[2])
				temp["userId"] = int(row[3] or 0)
				temp["name"] = str(row[4])
				result.append(temp)
				# result[i] = temp
				i = i + 1
		except:
			result = {"errorno": 0}
			print
			u"---------------------查询车位信息报错-------------------"
		queryDB.close()
		return flask.jsonify(result)
	
	def post(self):
		return self.get()


class QueryUser(MethodView):
	def get(self):
		try:
			id = request.args.get("id", 0)
			result = queryUser(id)
		except:
			result = {"errorno": 0}
		return flask.jsonify(result)
	
	def post(self):
		return self.get()


# 预约
class BookPark(MethodView):
	def get(self):
		try:
			bookDB = connect()
			userId = request.args.get("userId", 0)
			parkId = request.args.get("parkId", 0)
			
			# bookTime = math.floor(time.time())
			bookTime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
			sql = "UPDATE t_user SET parkId=%s, status=%s, bookTime=%s WHERE id=%s"
			val = (parkId, 1, bookTime, userId)
			print(u"-------------------预约 停车 --------------- ",userId,parkId,bookTime)
			mycursor = bookDB.cursor()
			mycursor.execute(sql, val)
			bookDB.commit()  # 数据表内容有更新，必须使用到该语句
			
			sql1 = "UPDATE t_pack SET status=%s, userId=%s WHERE id=%s"
			val1 = (1, userId, parkId)
			mycursor1 = bookDB.cursor()
			mycursor1.execute(sql1, val1)
			bookDB.commit()  # 数据表内容有更新，必须使用到该语句
			result = {
				"errorno": 1,
				"bookTime": bookTime,  # 预约时间
			}
		except:
			result = {"errorno": 0}
		bookDB.close()
		return flask.jsonify(result)
	
	def post(self):
		return self.get()


class UpdateParkTakeStatus(MethodView):
	def get(self):
		try:
			bookDB = connect()
			status = request.args.get("status", 0) # 0 不可以取车，1可以取车
			parkId = request.args.get("parkId", 0)
			parkStatus = 0
			if int(status) == 0:
				parkStatus = 2 # 2是已占用 0是空闲
			sql1 = "UPDATE t_pack SET canTakeCar=%s WHERE id=%s"
			print("-----------parkId = "+str(parkId)+"   parkStatus = "+str(parkStatus))
			val1 = (status, parkId)
			if parkStatus == 2:  # 2表示停车
				park = QueryParkById(parkId)
				print(park)
				if int(park['errorno']) == 1:
					userId = park['userId']
					timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
					sql = "UPDATE t_user SET parkId=%s, status=%s,parkTime=%s WHERE id=%s"
					val = (parkId, 2, timestamp,userId)
					print(u"-------------------停车  --------------- ", userId, parkId)
					mycursor1 = bookDB.cursor()
					mycursor1.execute(sql, val)
					bookDB.commit()  # 数据表内容有更新，必须使用到该语句
				sql1 = "UPDATE t_pack SET canTakeCar=%s,status=%s WHERE id=%s"
				val1 = (status,parkStatus,parkId)
			print(u"-------------------更新停车位的取车状态  --------------- ",status,parkId)
			mycursor = bookDB.cursor()
			mycursor.execute(sql1, val1)
			bookDB.commit()  # 数据表内容有更新，必须使用到该语句
		except:
			return "400"
		bookDB.close()
		return "200"
	
	def post(self):
		return self.get()

# 取车
class TakeCar(MethodView):
	def get(self):
		result = {
			"errorno": 0,
		}
		try:
			
			userId = request.args.get("userId", 0)
			parkId = request.args.get("parkId", 0)
			park = QueryParkById(parkId)
			print(park)
			bookDB = connect()
			if int(park['errorno']) == 1:
				if int(park['canTakeCar']) == 1:  # 标志位为可以取车
					bookTime = "1917-1-1 00:00:00"
					sql = "UPDATE t_user SET parkId=%s, status=%s WHERE id=%s"
					val = (0, 0, userId)
					print(u"-------------------取车  --------------- ",userId,parkId)
					mycursor1 = bookDB.cursor()
					mycursor1.execute(sql, val)
					bookDB.commit()  # 数据表内容有更新，必须使用到该语句
					
					sql = "UPDATE t_pack SET status=%s, userId=%s,canTakeCar=%s WHERE id=%s"
					val = (0, 0,0, parkId)
					mycursor2 = bookDB.cursor()
					mycursor2.execute(sql, val)
					bookDB.commit()  # 数据表内容有更新，必须使用到该语句
					result = {
						"errorno": 1,  # 取车成功
					}
				else:
					result = {
						"errorno": 2,  # 取车失败 暂时不能取车
					}
		except:
			result = {"errorno": 0}
		bookDB.close()
		return flask.jsonify(result)
	
	def post(self):
		return self.get()

@staticmethod
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
			if difTime > 3600 and status==1: #,预约时，超时
				sql = "UPDATE t_user SET parkId=%s, status=%s WHERE id=%s"
				val = (0, 3, id)  #超时
				if difTime > (3600*3): #超时
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


if __name__ == '__main__':
	app.add_url_rule('/login', view_func=userLogin.as_view('login'))  # 登陆
	app.add_url_rule('/regist', view_func=registerUser.as_view('regist'))  # 注册
	app.add_url_rule('/query_user', view_func=QueryUser.as_view('query_user'))  # 查询用户
	app.add_url_rule('/modify_pwd', view_func=UpdatePwd.as_view('modify_pwd'))  # 修改密码
	app.add_url_rule('/query_park', view_func=QueryPark.as_view('query_park'))  # 查询停车位
	app.add_url_rule('/book_park', view_func=BookPark.as_view('book_park'))  # 预约停车
	app.add_url_rule('/take_car', view_func=TakeCar.as_view('take_car'))  # 取车
	
	
	app.add_url_rule('/update_park', view_func=UpdateParkTakeStatus.as_view('update_park'))  # 更新停车位取车状态
	# connect()
	# initBookTime()
	app.run(host='0.0.0.0', port=8535)
