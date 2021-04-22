# coding:UTF-8
import flask
from flask import request, Flask
from flask.views import MethodView
from SocketServer import BaseRequestHandler, TCPServer, ThreadingTCPServer
# from wsgiref.simple_server import make_server
# from geventwebsocket.handler import WebSocketHandler
# from geventwebsocket.server import WSGIServer
# from geventwebsocket.websocket import WebSocket
# from multiprocessing import Process
import json
import datetime
import time
import threading
import sqlite3
from threading import Timer
import random
import os
from concurrent.futures import ThreadPoolExecutor
executor = ThreadPoolExecutor()
app = Flask(__name__)

user_socket_list = []
data = {'x': 0, 'y': 0, 'z': 0, 'timestamp': 0}
global datas
datas =[]


def temp():
    for usocket in user_socket_list:
        usocket.send("")


def insertData(timestamp, x, y, z):
    con = sqlite3.connect('mvaDataBase')
    cursor = con.cursor()
    cursor.execute(
        'CREATE TABLE IF NOT EXISTS mvaTable(timestamp double,x_value double, y_value double, z_value double)')
    con.commit()
    sqlStr = 'INSERT INTO mvaTable(timestamp,x_value,y_value,z_value)VALUES ({timestamp},{x_value},{y_value},{z_value})'.format(
        timestamp=timestamp, x_value=x, y_value=y, z_value=z)
    cursor.execute(sqlStr)
    con.commit()


def queryAllData():
    con = sqlite3.connect('mvaDataBase')
    cursor = con.cursor()
    cursor.execute(
        'CREATE TABLE IF NOT EXISTS mvaTable(timestamp double,x_value double, y_value double, z_value double)')
    con.commit()
    # selectSql = "SELECT * FROM mvaTable where create_time >%s" % datetime.datetime('now','-1 hour','localtime')
    selectSql = "SELECT * FROM mvaTable"
    cursor.execute(selectSql)
    result = cursor.fetchall()
    return result

def genData(timestamp, x, y, z):
    for i in range(1,50): 
        tempTime = timestamp + round(i/1000.0,3)
        xoffset = round(random.uniform(-1, 1),3)
        yoffset = round(random.uniform(-1, 1),3)
        zoffset = round(random.uniform(-1, 1),3)
        tempx = x + xoffset
        tempy = y + yoffset
        tempz = z + zoffset
        print(i,tempx,tempy,tempz,tempTime)
        tempData = {'x': 0, 'y': 0, 'z': 0, 'timestamp': 0}
        tempData['x'] = tempx
        tempData['y'] = tempy
        tempData['z'] = tempz
        tempData['timestamp'] = tempTime
        # global datas
        datas.append(tempData)
        # t = threading.Thread(target=insertData, args=(timestamp, float(strlist1[0]), float(strlist1[1]), float(strlist1[2])))
        # t.setDaemon(True)  # 把子进程设置为守护线程，必须在start()之前设置
        # t.start()
        executor.submit(insertData,(tempTime, tempx, tempy, tempz))
    # pass

class UpdateData(MethodView):
    def get(self):
        x = int(request.args.get('x', 0))
        y = int(request.args.get('y', 0))
        z = int(request.args.get('z', 0))
        timestamp = time.time()
        data['x'] = x
        data['y'] = y
        data['z'] = z
        data['timestamp'] = timestamp
        global datas
        datas.append(data)
        genData(timestamp, x, y, z)
        executor.submit(insertData,(timestamp, x, y, z))
        executor.submit(genData,(timestamp, x, y, z))
        # t = threading.Thread(target=insertData, args=(timestamp, x, y, z))
        # t.setDaemon(True)  # 把子进程设置为守护线程，必须在start()之前设置
        # t.start()
        print("============%s", x, y, z, timestamp)
        return ('{x=%s,y=%s,z=%s,timestamp=%s}' % (data['x'], data['y'], data['z'], data['timestamp']), 200)


class QueryListData(MethodView):
    def get(self):
        result = queryAllData()
        resultstr = json.dumps(result)
        return (resultstr, 200)


class GetData(MethodView):
    def get(self):
        # global datas
        # temp = datas
        # datas = []
        con = sqlite3.connect('mvaDataBase.db')
        cursor = con.cursor()
        cursor.execute(
            'CREATE TABLE IF NOT EXISTS mvaTable(timestamp double,x_value double, y_value double, z_value double)')
        con.commit()
        # selectSql = "SELECT * FROM mvaTable where create_time >%s" % datetime.datetime('now','-1 hour','localtime')
        selectSql = "select * from mvaTable order by timestamp desc limit 1"
        cursor.execute(selectSql)
        result = cursor.fetchall()
        resultstr = json.dumps(result)
        # path = os.getcwd()
        # resultstr = ""
        # with open(path+"./" + "temp" + ".txt",'r') as f:
        #     resultstr = f.read()
        #     reslist = resultstr.split(',')
        #     if len(reslist)>3:
        #         data['timestamp'] = reslist[0]
        #         data['x'] = reslist[1]
        #         data['y'] = reslist[2]
        #         data['z'] = reslist[3]
        #     f.close()
        return flask.jsonify(data)

def deleteDB(inc):
    t = Timer(inc, deleteDB, (inc,))
    t.start()
    con=sqlite3.connect('mvaDataBase')
    cursor=con.cursor()
    cursor.execute('CREATE TABLE IF NOT EXISTS mvaTable(timestamp double,x_value double, y_value double, z_value double)')
    con.commit()
    cursor.execute('drop table mvaTable')
    con.commit()
    print(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))

if __name__ == '__main__':
    # 5s
    deleteDB(60*60*36)
    app.add_url_rule('/mva/update', view_func=UpdateData.as_view('update')) 
    app.add_url_rule('/mva/history', view_func=QueryListData.as_view('history')) 
    app.add_url_rule(
        '/mva/getdata', view_func=GetData.as_view('current-location'))
    # app.run(host='192.168.1.114', port=8900)
    app.run(host='172.16.0.16', port=8900)
