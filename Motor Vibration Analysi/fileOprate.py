# -*- coding: utf-8 -*-
# f = None
# try:
#     f = open('test.txt', 'r')
#     print(f.read())
# finally:
#     if f:
#         f.close()
with open('F:/WorkSpace/Motor Vibration Analysi/test.txt','w') as f:
    f.write("-----------------")
    f.write("-----------------")
    f.close()

with open('F:/WorkSpace/Motor Vibration Analysi/test.txt',"r") as f:
    str1 = f.read()
    print(str1)

import  sqlite3
import datetime
import time
con=sqlite3.connect('mvaDataBase')
cursor=con.cursor()
cursor.execute('CREATE TABLE IF NOT EXISTS mvaTable(timestamp double,x_value double, y_value double, z_value double)')

con.commit()

#添加数据
timestr= time.time()
print(timestr)
# for 1,10 do:

sqlStr = 'INSERT INTO mvaTable(timestamp,x_value,y_value,z_value)VALUES ({timestamp},{x_value},{y_value},{z_value})'.format(timestamp = timestr,x_value = 500,y_value = 400,z_value = 300)
cursor.execute(sqlStr)
con.commit()

selectSql = "SELECT * FROM mvaTable"
cursor.execute(selectSql)
result=cursor.fetchall()
print(result)
