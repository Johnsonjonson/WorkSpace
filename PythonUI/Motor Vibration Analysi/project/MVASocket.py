# coding:UTF-8
from SocketServer import BaseRequestHandler, TCPServer, ThreadingTCPServer
import threading
import sqlite3
import time
from threading import Timer
import os
data = {'x': 0, 'y': 0, 'z': 0, 'timestamp': 0}
global datas
datas =[]


class MVASocket():
    def startSocket(self):
        http_serv = TCPServer(("192.168.1.114", 8902),EchoHandler)
        http_serv.serve_forever()

    def getData(self):
        return data,datas

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

# 继承BaseRequestHandler这个base class，并重定义handle()
class EchoHandler(BaseRequestHandler):
    def handle(self):
        print('Got connection from', self.client_address)
        # self.request is the TCP socket connected to the client
        while True:
            # 8192代表每次读取8192字节
            msg = self.request.recv(8192)
            print(msg)
            # "B:121.78655,22.36897"
            if not msg:
                break
            print("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            print("receive Data:", msg)
            strlist1 = msg.split(',')
            print(strlist1)
            if len(strlist1) >= 3:
                timestamp = time.time()
                data['x'] = float(strlist1[0])
                data['y'] =  float(strlist1[1])
                data['z'] =  float(strlist1[2])
                data['timestamp'] = timestamp
                global datas
                datas.append(data)
                t = threading.Thread(target=insertData, args=(timestamp, float(strlist1[0]), float(strlist1[1]), float(strlist1[2])))
                t.setDaemon(True)  # 把子进程设置为守护线程，必须在start()之前设置
                t.start()
                path = os.getcwd()
                with open(path+"./" + "pathEx" + ".txt",'w') as f:
                    f.write("%s,%s,%s,%s" % (data['timestamp'],data['x'],data['y'],data['z']))
                f.close()
                # print(datas)
            self.request.send("result")

MVASocket().startSocket()