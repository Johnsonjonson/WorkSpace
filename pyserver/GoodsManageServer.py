# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json
import mysql.connector
import datetime

app = flask.Flask(__name__)
global mydb
global goodsId
goodsId = 0;
global status  # 1 入库,2 购买,3
status = 0
# 创建链接数据库
def connect():
    config = {'host': 'localhost',  # 默认127.0.0.1
              'user': 'root',
              'password': 'zqs@@740848126',
              'port':3306,  # 默认即为3306
              'database':'goods_data',
              'charset':'utf8',  # 默认即为utf8
              }
    try:
        mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
    except mysql.connector.Error as e:
        print('数据库链接失败！', str(e))
    else:  # try没有异常的时候才会执行
        print("数据库连接sucessfully!")
        return mydb



def CheckIsExitGoods(id):
    mydb = connect()
    sql = "select * from t_goods where id="+str(id)
    mycursor = mydb.cursor()
    mycursor.execute(sql)
    results = mycursor.fetchall()
    mydb.close()

    if len(results) > 0:
        num = results[0][3]
        return num
    return -1

def UpdateGood(id,name,price,num):
    print('---UpdateGood  UpdateGood---',id,name,price,num)
    updateDB = connect()
    mycursor = updateDB.cursor()
    sql = "UPDATE t_goods SET name=%s,price=%s,num=%s WHERE id=%s"
    val = (name,price,num, id)
    mycursor.execute(sql, val)
    updateDB.commit()  # 数据表内容有更新，必须使用到该语句
    updateDB.close()

def InsertGood(id,name,price,num):
    print('---InsertGood  InsertGood---', id, name, price, num)
    mydb = connect()
    sql = "INSERT INTO t_goods (id,name,price,num) VALUES (%s,%s,%s,%s)"
    val = (id,name,price,num)
    print("InsertGood   sucessfully! " + str(mydb.is_connected()))
    mycursor = mydb.cursor()
    mycursor.execute(sql, val)
    mydb.commit()    # 数据表内容有更新，必须使用到该语句
    mydb.close()

def AddGood(id):
    name = ''
    price = 0
    id = int(id)
    print('AddGoods ----  id =  ',id)
    if  id == 1:
        id = 1001
        name = '篮球'
        price = 120
    elif id == 2:
        id = 1002
        name = '足球'
        price = 50
    elif id == 3:
        id = 1003
        name = '棒球'
        price = 30
    if price > 0:
        num = int(CheckIsExitGoods(id))
        print("-----------num = ",num)
        if num >= 0:
            UpdateGood(id, name, price, num+1)
        else:
            InsertGood(id, name, price, 1)

class SendRequest(MethodView):
    def get(self):
        id = int(request.args.get("id", 0))
        global goodsId
        if int(id) == 1:
            goodsId = 1001
        elif int(id) ==2:
            goodsId =1002
        elif int(id) ==3:
            goodsId = 1003
        print("-------goodsId = " + str(goodsId))
        global status
        print("-------status = "+str(status))
        if int(status) == 1: # 添加商品
            AddGood(int(id))
        elif int(status) == 2: # 购买商品
            num = int(CheckIsExitGoods(id))
            if num > 0:
                UpdateGood(id, name, price, num - 1)

        return "200"
    def post(self):
        return self.get()

class updateStatus(MethodView):
    def get(self):
        goodsData = {'id':0,'name':'','price':0,'num':0}
        global status
        global goodsId
        status = request.args.get("status", 0)
        callback = request.args.get("callback"),'';
        print("-------goodsId = " + str(goodsId))
        print("-------status = " + str(status))
        if int(goodsId) > 0:
            createDB = connect();
            mycursor = createDB.cursor()
            sql = "select * from t_goods where id=" + str(goodsId)
            mycursor.execute(sql)
            record_last = mycursor.fetchall()
            # print('数据库中最后一条记录为：', record_last)
            createDB.commit()
            if len(record_last) > 0:
                last = record_last[0]
                print('====record_last======',last)
                goodsData['id'] = int(last[0])
                goodsData['name'] = str(last[1])
                goodsData['price'] = float(last[2] or 0)
                goodsData['num'] = int(last[3] or 0)
            goodsId = 0  # 重置
            createDB.close()
        else:
            pass
        result = "callback("+str(goodsData)+")"
        print(result)
        return result
    def post(self):
        return self.get()



if __name__ == '__main__':
    app.add_url_rule('/api', view_func=SendRequest.as_view('api'))
    app.add_url_rule('/status', view_func=updateStatus.as_view('status'))
    app.run(host='0.0.0.0', port=8555)
    # app.run(host='192.168.1.255', port=8555)


