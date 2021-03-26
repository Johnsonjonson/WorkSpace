# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
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
              'port':3306,  # 默认即为3306
              'database':'student',
              'charset':'utf8'  # 默认即为utf8
              }
    try:
        mydb = mysql.connector.connect(**config)  # connect方法加载config的配置进行数据库的连接，完成后用一个变量进行接收
    except mysql.connector.Error as e:
        print(u'----------------连接数据库  ---------------  数据库链接失败！', str(e))
    else:  # try没有异常的时候才会执行
        print(u"----------------连接数据库  ---------------  数据库连接sucessfully!")
        return mydb

class inputScore(MethodView):
    def get(self):
        try:
            scoreDB = connect()
            math = request.args.get("math",0)
            english = request.args.get("english",0)
            zhengzhi = request.args.get("zhengzhi",0)
            auto = request.args.get("auto",0)
            name = request.args.get("name", '')
            id = request.args.get("id", 0)
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            sql = "UPDATE t_student SET political=%s, math=%s, english=%s, autoctrl=%s WHERE id=%s"
            val = (zhengzhi, math,english,auto,id)
            print(u"-------------------录入分数  --------------- 数据库是否连接 " + str(scoreDB.is_connected()))
            mycursor = scoreDB.cursor()
            mycursor.execute(sql, val)
            scoreDB.commit()  # 数据表内容有更新，必须使用到该语句
            result = queryStudent(id)
            print(u"-------------录入分数-----------------提交分数成功 ")
        except:
            result = {"errorno": 0}
            print(u"---------------------录入分数报错-------------------")
        scoreDB.close()
        return flask.jsonify(result)

    def post(self):
       return self.get()

class daka(MethodView):
    def get(self):
        try:
            dakadb = connect()
            id = request.args.get("id", 0)
            if id == 0:
                return "400"
            result = queryStudent(id)
            if int(result['errorno']) == 0:
                return "400"
            dakaNum = int(result['daka_num'])
            dakaNum = dakaNum + 1
            name = result['name']
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            sql = "UPDATE t_student SET daka_num=%s WHERE id=%s"
            val = (dakaNum,id)
            print(u"---------------打卡 -------------------  数据库是否连接" + str(dakadb.is_connected()))
            mycursor = dakadb.cursor()
            mycursor.execute(sql, val)
            dakadb.commit()  # 数据表内容有更新，必须使用到该语句

            sql = "INSERT INTO t_stu_daka (id,name,time) VALUES (%s, %s,%s)"
            val = (id,name, timestamp)
            mycursor = dakadb.cursor()
            mycursor.execute(sql, val)

            dakadb.commit()  # 数据表内容有更新，必须使用到该语句

            print(u"-------------------打卡---------------------打卡成功sucessfully! ")
        except:
            print(u"---------------------打卡报错-------------------")
            return "400"
        dakadb.close()
        return "200"

    def post(self):
       return self.get()

class registerUser(MethodView):
    def get(self):
        try:
            registerDB = connect()
            pwd = request.args.get("pwd",'')
            name = request.args.get("name", '')
            tel = request.args.get("tel", '')
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            sql = "INSERT INTO t_student (name,pwd,tel) VALUES (%s, %s,%s)"
            val = (name, pwd,tel)
            print(u"---------------------注册学生------------------- 数据库是否连接" + str(registerDB.is_connected()))
            mycursor = registerDB.cursor()
            mycursor.execute(sql, val)
            id = mycursor.lastrowid
            registerDB.commit()  # 数据表内容有更新，必须使用到该语句
            # result = {'name': '', "pwd": '', 'id': 0, 'tel': '', 'political': 0, 'math': 0, 'english': 0, 'autoctrl': 0,
            #           'daka_num': 0, 'errorno': 0}
            print(u"---------------------注册学生-------------------记录插入成功。   id  = " + str(id))
            result = queryStudent(id)
        except:
            result = {"errorno": 0}
            print(u"---------------------学生注册报错-------------------")
            registerDB.close()
        return flask.jsonify(result)

    def post(self):
       return self.get()

class registerTeacher(MethodView):
    def get(self):
        registerDB = connect()
        try:
            pwd = request.args.get("pwd",'')
            name = request.args.get("name", '')
            tel = request.args.get("tel", '')
            sql = "INSERT INTO t_teacher (name,pwd,tel) VALUES (%s, %s,%s)"
            val = (name, pwd,tel)
            print(u"---------------------注册老师-------------------注册成功sucessfully! " + str(registerDB.is_connected()))
            mycursor = registerDB.cursor()
            mycursor.execute(sql, val)
            id = mycursor.lastrowid
            registerDB.commit()  # 数据表内容有更新，必须使用到该语句
            print(u"---------------------注册老师-------------------  记录插入成功。   id  = " + str(id))
            result = queryTeacher(id)
        except:
            result = {"errorno":0}
            print(u"---------------------老师注册报错-------------------")
        registerDB.close()
        return flask.jsonify(result)

    def post(self):
       return self.get()

class AddStudent(MethodView):
    def get(self):
        try:
            addDB = connect()
            name = request.args.get("name", '')
            tel = request.args.get("tel", '')
            sql = "INSERT INTO t_student (name,pwd,tel) VALUES (%s,%s,%s)"
            val = (name,"", tel)
            print(u"---------------------添加学生-------------------添加成功sucessfully! " + str(addDB.is_connected()))
            mycursor = addDB.cursor()
            mycursor.execute(sql, val)
            id = mycursor.lastrowid
            addDB.commit()  # 数据表内容有更新，必须使用到该语句
            print(u"---------------------添加学生-------------------记录插入成功。   id  = " + str(id))
            result = queryStudent(id)
        except:
            result = {"errorno":0}
            print(u"---------------------添加学生报错-------------------")
        addDB.close()
        return flask.jsonify(result)

    def post(self):
        return self.get()

def queryStudent(id):
    try:

        stuDB = connect()
        result = {'name': '', "pwd": '', 'id': id, 'tel': '', 'political': 0, 'math': 0, 'english': 0, 'autoctrl': 0,
                  'daka_num': 0, 'errorno': 2}
        print(u"查询学生。   id  = " + str(id))
        sql = "SELECT * FROM t_student WHERE id=%s" % id
        print(sql)
        mycursor = stuDB.cursor()
        # 执行SQL语句
        mycursor.execute(sql)
        # 获取所有记录列表
        isCorrect = False
        results = mycursor.fetchall()
        if len(results) <= 0:
            result['errorno'] = 0
            return flask.jsonify(result)
        else:
            for row in results:
                ids = int(row[0])
                names = str(row[1])
                pwds = str(row[2])
                tels = str(row[3])
                political = float(row[4] or 0)
                math = float(row[5] or 0)
                english = float(row[6] or 0)
                autoctrl = float(row[7] or 0)
                daka_num = int(row[8] or 0)
                if ids == int(id):
                    # result = {'name':name,"pwd":pwd,'id':id}
                    result['name'] = names
                    result['id'] = ids
                    result['pwd'] = pwds
                    result['tel'] = tels
                    result['political'] = political
                    result['math'] = math
                    result['english'] = english
                    result['autoctrl'] = autoctrl
                    result['daka_num'] = daka_num
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

def queryTeacher(id):
    try:

        queryDB = connect()
        result = {'name': '', "pwd": '', 'id': id, 'tel': '','errorno': 2}
        print(u"---------------------查询老师-------------------查询老师。   id  = " + str(id))
        sql = "SELECT * FROM t_teacher WHERE id=%s" % id
        print(sql)
        mycursor = queryDB.cursor()
        # 执行SQL语句
        mycursor.execute(sql)
        # 获取所有记录列表
        isCorrect = False
        results = mycursor.fetchall()
        if len(results) <= 0:
            result['errorno'] = 0
            return flask.jsonify(result)
        else:
            for row in results:
                ids = int(row[0])
                names = str(row[1])
                pwds = str(row[2])
                tels = str(row[3])
                if ids == int(id):
                    # result = {'name':name,"pwd":pwd,'id':id}
                    result['name'] = names
                    result['id'] = ids
                    result['pwd'] = pwds
                    result['tel'] = tels
                    result['errorno'] = 1
                    isCorrect = True
                    break
                # 打印结果
                if not isCorrect:
                    result['errorno'] = 2
    except:
        result = {"errorno": 0}
        print
        u"---------------------查询老师报错-------------------"

    # 关闭数据库连接
    queryDB.close()
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
            sql = "SELECT * FROM t_student WHERE name='%s'" % name
            print(sql)
            result = {'name':'',"pwd":'','id':0,'tel':'','political':0,'math':0,'english':0,'autoctrl':0,'daka_num':0,'errorno':0}
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            isCorrect = False
            results = cursor.fetchall()
            if len(results) <=0:
                result['errorno'] = 0
                pass
            for row in results:
                id = row[0]
                name = str(row[1])
                pwdS = str(row[2])
                tel = str(row[3])
                political = float(row[4] or 0)
                math = float(row[5] or 0)
                english = float(row[6] or 0)
                autoctrl = float(row[7] or 0)
                daka_num = int(row[8] or 0)

                if pwd == pwdS:
                    # result = {'name':name,"pwd":pwd,'id':id}
                    result['name'] = name
                    result['id'] = id
                    result['pwd'] = pwd
                    result['tel'] = tel
                    result['political'] = political
                    result['math'] = math
                    result['english'] = english
                    result['autoctrl'] = autoctrl
                    result['daka_num'] = daka_num
                    result['errorno'] = 1
                    isCorrect = True
                    break
            # 打印结果
            if not isCorrect:
                result['errorno'] = 2
            print(id,name,pwdS)
        except:
            result = {"errorno": 0}
            print
            u"---------------------学生登陆报错-------------------"

        print("---------------------学生登陆-------------------成功sucessfully! " + str(loginDB.is_connected()))
        # 关闭数据库连接
        loginDB.close()


        return flask.jsonify(result)
    def post(self):
       return self.get()

class TeacherLogin(MethodView):
    def get(self):
        loginDB = connect()
        try:
            pwd = request.args.get("pwd", '')
            name = request.args.get("name", '')
            # 使用cursor()方法获取操作游标
            print(name, pwd)
            cursor = loginDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_teacher WHERE name='%s'" % name
            print(sql)
            result = {'name':'',"pwd":'','id':0,'tel':'','errorno':0}
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            isCorrect = False
            results = cursor.fetchall()
            if len(results) <=0:
                result['errorno'] = 0
                pass
            for row in results:
                id = row[0]
                name = str(row[1])
                pwdS = str(row[2])
                tel = str(row[3])

                if pwd == pwdS:
                    # result = {'name':name,"pwd":pwd,'id':id}
                    result['name'] = name
                    result['id'] = id
                    result['pwd'] = pwd
                    result['tel'] = tel
                    result['errorno'] = 1
                    isCorrect = True
                    break
            # 打印结果
            if not isCorrect:
                result['errorno'] = 2
            print(id,name,pwdS)
        except:
            result = {"errorno": 0}
            print
            u"---------------------老师登陆报错-------------------"

        # 关闭数据库连接
        loginDB.close()


        print(u"---------------------老师登陆------------------- 登陆成功sucessfully! " + str(loginDB.is_connected()))
        return flask.jsonify(result)
    def post(self):
       return self.get()

# 查询打卡记录
class QueryDaka(MethodView):
    def get(self):
        try:
            dakaDB = connect()
            id = request.args.get("id", '')
            cursor = dakaDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_stu_daka WHERE id='%s'" % id
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
                temp["name"] = str(row[1])
                temp["time"] = str(row[2])
                result.append(temp)
                # result[i] = temp
                i = i+1
        except:
            result = {"errorno": 0}
            print
            u"---------------------查询打卡信息报错-------------------"
        dakaDB.close()
        return flask.jsonify(result)
    def post(self):
       return self.get()

class QueryUser(MethodView):
    def get(self):
        try:
            id = request.args.get("id",0)
            result = queryStudent(id)
        except:
            result = {"errorno": 0}
        return flask.jsonify(result)

    def post(self):
       return self.get()


class GetAllUser(MethodView):
    def get(self):
        result = []
        try:
            userDB = connect()
            cursor = userDB.cursor()
            # SQL 查询语句
            sql = "SELECT * FROM t_student"
            print(sql)
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            results = cursor.fetchall()
            for row in results:
                temp = {}
                temp["id"] = row[0]
                temp["name"] = str(row[1])
                temp["time"] = str(row[2])

                temp["id"] = row[0]
                temp["name"] = str(row[1])
                pwdS = str(row[2])
                temp["tel"] = str(row[3])
                temp["political"] = float(row[4] or 0)
                temp["math"] = float(row[5] or 0)
                temp["english"] = float(row[6] or 0)
                temp["autoctrl"] = float(row[7] or 0)
                temp["daka_num"] = int(row[8] or 0)
                result.append(temp)
        except:
            print
            u"---------------------查询所有学生报错-------------------"

        userDB.close()
        return flask.jsonify(result)

    def post(self):
       return self.get()

if __name__ == '__main__':
    app.add_url_rule('/login', view_func=userLogin.as_view('login')) #学生登陆
    app.add_url_rule('/regist', view_func=registerUser.as_view('regist')) #学生注册
    app.add_url_rule('/login_teacher', view_func=TeacherLogin.as_view('login_teacher'))  # 老师登陆
    app.add_url_rule('/regist_teacher', view_func=registerTeacher.as_view('regist_teacher')) # 老师注册
    app.add_url_rule('/score', view_func=inputScore.as_view('score'))  # 录入分数
    app.add_url_rule('/daka', view_func=daka.as_view('daka')) #学生打卡
    app.add_url_rule('/query', view_func=QueryDaka.as_view('query'))  #查询打卡记录
    app.add_url_rule('/query_user', view_func=QueryUser.as_view('query_user'))#查询用户
    app.add_url_rule('/getAllUser', view_func=GetAllUser.as_view('getAllUser'))#查询所有用户
    app.add_url_rule('/addStudent', view_func=AddStudent.as_view('addStudent'))#添加学生
    # connect()
    app.run(host='0.0.0.0', port=8533)

