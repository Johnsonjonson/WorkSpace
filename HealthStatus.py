# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json
import mysql.connector
import datetime

app = flask.Flask(__name__)
control = {'1':"-1"} # 1 上， 2 下， 3 左 ，4 右
status = 0
temperature  = 0
data = {
    'status':0,
    'temp':0.0
}
global mydb

# 创建链接数据库
def connect():
    config = {'host': 'localhost',  # 默认127.0.0.1
              'user': 'root',
              'password': 'zqs@@740848126',
              'port':3306,  # 默认即为3306
              'database':'petHospital',
              'charset':'utf8'  # 默认即为utf8
              }
    try:
        mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
    except mysql.connector.Error as e:
        print('数据库链接失败！', str(e))
    else:  # try没有异常的时候才会执行
        print("数据库连接sucessfully!")
        return mydb

def add(mydb,sql,val):
    mycursor = mydb.cursor()
    mycursor.execute(sql, val)
    mydb.commit()    # 数据表内容有更新，必须使用到该语句
    print(mycursor.rowcount, "记录插入成功。")

class updateDatas(MethodView):
    def get(self):
        global status
        global temperature
        # params = request.args.get("params","")
        blood = request.args.get("blood",0)
        heart = request.args.get("heart", 0)
        timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        id  = 1
        name = "张三"
        sql = "INSERT INTO t_user_data (name, id,blood,heart,time) VALUES (%s, %s,%s,%s,%s)"
        val = (name, id,blood,heart,timestamp)
        print("数据库连接sucessfully! " + str(mydb.is_connected()))
        add(mydb, sql, val)
        return "200"

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateDatas.as_view('update'))
    global mydb
    mydb = connect()
    app.run(host='0.0.0.0', port=8532)

