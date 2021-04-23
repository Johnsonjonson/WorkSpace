# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import json
import mysql.connector

app = flask.Flask(__name__)
data = {'index': 0, 'phone': '', 'chepai': ''}


# 创建链接数据库
def connect():
    config = {'host': 'localhost',  # 默认127.0.0.1
              'user': 'root',
              'password': 'zqs@@740848126',
              'port': 3306,  # 默认即为3306
              'database': 'qr_parking',
              'charset': 'utf8'  # 默认即为utf8
              }
    try:
        mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
    except mysql.connector.Error as e:
        print(u'----------------连接数据库  ---------------  数据库链接失败！', str(e))
    else:  # try没有异常的时候才会执行
        print(u"----------------连接数据库  ---------------  数据库连接sucessfully!")
        return mydb
    
def isChepaiExit(chepai):
        isExit = False
        try:
            queryDB = connect()
            cursor = queryDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_park where chepai=%s" % chepai
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            results = cursor.fetchall()
            if len(results) <= 0:
                isExit = False
            else:
                isExit= True
        except:
            isExit = False
        queryDB.close()
        return isExit


def isParkBooked(id):
    try:
        isBooked = False
        queryDB = connect()
        cursor = queryDB.cursor()
        # SQL 查询语句
        sql = "SELECT * FROM t_park where id=%s" % id
        # val = (status, chepai, phone, id)
        # 执行SQL语句
        # cursor.execute(sql, val)
        cursor.execute(sql)
        # 获取所有记录列表
        results = cursor.fetchall()
        for row in results:
            status = int(row[1])
            if status != 0:
                isBooked = True
                break
    except:
        isBooked = False
    queryDB.close()
    return isBooked

# 预约
class BookPark(MethodView):
    def get(self):
        try:
            bookDB = connect()
            id = request.args.get("index", 0)
            status = 1
            phone = request.args.get("phone", '')
            chepai = request.args.get("chepai", '')
            if isChepaiExit(chepai):
                result = {
                    "errorno": -1,  #车牌已预约
                }
                return flask.jsonify(result)
            if isParkBooked(id):
                result = {
                    "errorno": -2,  #车位已预约
                }
                return flask.jsonify(result)
            sql = "UPDATE t_park SET status=%s,chepai=%s, phone=%s WHERE id=%s"
            val = (status, chepai, phone,id)
            mycursor = bookDB.cursor()
            mycursor.execute(sql, val)
            bookDB.commit()  # 数据表内容有更新，必须使用到该语句
            result = {
                "errorno": 1,
            }
        except:
            result = {"errorno": 0}
        bookDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()


# 查询所有停车位
class QueryPark(MethodView):
    def get(self):
        try:
            queryDB = connect()
            cursor = queryDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_park"
            # print(sql)
            result = []
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            results = cursor.fetchall()
            for row in results:
                temp = {}
                temp["id"] = int(row[0])
                temp["status"] = int(row[1])
                temp["chepai"] = str(row[2])
                temp["phone"] = str(row[3])
                result.append(temp)
        except:
            result = {"errorno": 0}
        queryDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

class updateData(MethodView):
    def get(self):
        data['index'] = request.args.get("index", 0)
        data['status'] = request.args.get("status", 0)
        data['phone'] = request.args.get("phone", '')
        data['chepai'] = request.args.get("chepai", '')
        return flask.jsonify(data)

class TakeCar(MethodView):
    def get(self):
        try:
            id = request.args.get("index", 0)
            queryDB = connect()
            cursor = queryDB.cursor()
            # SQL 查询语句
            sql = "UPDATE t_park SET status=%s,chepai=%s,phone=%s WHERE id=%s"
            val = (0, "", "",id)
            cursor.execute(sql, val)
            queryDB.commit()  # 数据表内容有更新，必须使用到该语句
            result = {"errorno": 1}
        except Exception as err:
            print(err)
            result = {"errorno": 0}
        queryDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()


class getIndex(MethodView):
    def get(self):
        return data['index']


class getData(MethodView):
    def get(self):
        return flask.jsonify(data)

# 查询所有停车位
class QueryParkByChepai(MethodView):
    def get(self):
        result = {'id': 0, "status": 0,'chepai': '', 'phone': '','errorno': 0}
        try:
            chepai = request.args.get("chepai", '')
            queryDB = connect()
            cursor = queryDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_park where chepai='%s'" % chepai
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            results = cursor.fetchall()
            for row in results:
                result["id"] = int(row[0])
                result["status"] = int(row[1])
                result["chepai"] = str(row[2])
                result["phone"] = str(row[3])
                result["errorno"] = 1
        except:
            result = {"errorno": 0}
        queryDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get_index', view_func=getIndex.as_view('get_index'))
    app.add_url_rule('/get_data', view_func=getData.as_view('get_data'))
    app.add_url_rule('/query', view_func=QueryPark.as_view('query'))
    app.add_url_rule('/book', view_func=BookPark.as_view('book'))
    app.add_url_rule('/take', view_func=TakeCar.as_view('take'))
    app.add_url_rule('/query_chepai', view_func=QueryParkByChepai.as_view('query_chepai'))
    app.run(host='0.0.0.0', port=8542)
