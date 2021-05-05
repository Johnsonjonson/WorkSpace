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

global isOpen
isOpen = 0


# 创建链接数据库
def connect():
    config = {'host': 'localhost',  # 默认127.0.0.1
              'user': 'root',
              'password': 'zqs@@740848126',
              'port': 3306,  # 默认即为3306
              'database': 'express',
              'charset': 'utf8'  # 默认即为utf8
              }
    try:
        mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
    except mysql.connector.Error as e:
        print(u'----------------连接数据库  ---------------  数据库链接失败！', str(e))
    else:  # try没有异常的时候才会执行
        # print(u"----------------连接数据库  ---------------  数据库连接sucessfully!")
        return mydb


class CreateExpress(MethodView):
    def get(self):
        try:
            createDB = connect()
            start = request.args.get("start", '')
            end = request.args.get("end", '')
            phone = request.args.get("phone", '')
            name = request.args.get("name", '')
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            sql = "INSERT INTO t_express (start,end,phone,name,time,status) VALUES (%s, %s, %s, %s, %s,%s)"
            val = (start, end, phone, name, timestamp, 1)
            mycursor = createDB.cursor()
            mycursor.execute(sql, val)
            id = mycursor.lastrowid
            createDB.commit()  # 数据表内容有更新，必须使用到该语句
            result = {'errorno': 1}
        # result = queryUser(id)
        except:
            result = {"errorno": 0}
        createDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()


class LastExpress(MethodView):
    def get(self):
        result = {"errorno": 0, "id": 0, "start": '', "end": '', "phone": '', "name": '', "status": 0, "weight": 0,
                  "fee": 0, "time": ''}
        try:
            createDB = connect()
            mycursor = createDB.cursor()
            sql = "select * from t_express order by id desc limit 1"
            mycursor.execute(sql)
            record_last = mycursor.fetchall()
            # print('数据库中最后一条记录为：', record_last)
            if len(record_last) > 0:
                last = record_last[0]
                createDB.commit()
                result['errorno'] = 1
                result['id'] = int(last[0])
                result['start'] = str(last[1])
                result['end'] = str(last[2])
                result['phone'] = str(last[3])
                result['name'] = str(last[4])
                result['status'] = int(last[5] or 0)
                result['weight'] = int(last[6] or 0)
                result['fee'] = int(last[7] or 0)
                result['time'] = str(last[8])

        # result = queryUser(id)
        except Exception as e:
            print('报错：', str(e))
            result['errorno'] = 0
        createDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

class UpdateWeightAndFee(MethodView):
    def get(self):
        result = {"errorno": 0}
        try:
            weight = request.args.get("weight", 0)
            fee = request.args.get("fee", 0)
            id = int(request.args.get("id", 0))
            updateDB = connect()
            mycursor = updateDB.cursor()
            sql = "UPDATE t_express SET weight=%s, fee=%s,status=%s WHERE id=%s"
            val = (weight, fee, 2, id)
            mycursor.execute(sql, val)
            updateDB.commit()  # 数据表内容有更新，必须使用到该语句
            result['errorno'] = 1
        except Exception as e:
            print('报错：', str(e))
            result['errorno'] = 0
        updateDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

class Pay(MethodView):
    def get(self):
        result = {"errorno": 0}
        try:
            id = int(request.args.get("id", 0))
            updateDB = connect()
            mycursor = updateDB.cursor()
            sql = "UPDATE t_express SET status=%s WHERE id=%s"
            val = (3, id)
            mycursor.execute(sql, val)
            updateDB.commit()  # 数据表内容有更新，必须使用到该语句
            result['errorno'] = 1
        except Exception as e:
            print('报错：', str(e))
            result['errorno'] = 0
        updateDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

class UpdateStatus(MethodView):
    def get(self):
        result = {"errorno": 0}
        try:
            id = int(request.args.get("id", 0))
            status = int(request.args.get("status", 0))
            updateDB = connect()
            mycursor = updateDB.cursor()
            sql = "UPDATE t_express SET status=%s WHERE id=%s"
            val = (status, id)
            mycursor.execute(sql, val)
            updateDB.commit()  # 数据表内容有更新，必须使用到该语句
            result['errorno'] = 1
        except Exception as e:
            print('报错：', str(e))
            result['errorno'] = 0
        updateDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

class UpdateDoorStatus(MethodView):
    def get(self):
        global isOpen
        isOpen = request.args.get("isOpen", 0)
        return str(isOpen)

    def post(self):
        return self.get()


class GetDoorStatus(MethodView):
    def get(self):
        global isOpen
        return str(isOpen)

    def post(self):
        return self.get()


if __name__ == '__main__':
    app.add_url_rule('/create', view_func=CreateExpress.as_view('create'))  # 创建
    app.add_url_rule('/last', view_func=LastExpress.as_view('last'))  # 创建
    app.add_url_rule('/weight_fee', view_func=UpdateWeightAndFee.as_view('weight_fee'))  # 设置重量和费用
    app.add_url_rule('/pay', view_func=Pay.as_view('pay'))  # z支付
    app.add_url_rule('/update_status', view_func=UpdateStatus.as_view('update_status'))  # 更新快递状态
    app.add_url_rule('/update', view_func=UpdateDoorStatus.as_view('update'))  # 更新快递柜门开关状态
    app.add_url_rule('/get', view_func=GetDoorStatus.as_view('get'))  # 获取快递柜门开关状态
    app.run(host='0.0.0.0', port=8547)
