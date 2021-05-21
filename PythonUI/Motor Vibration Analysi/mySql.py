import mysql.connector
# import MySQLdb

mydb = mysql.connector.connect(
  host="localhost",
  user="root",
  passwd="zqs@@740848126",
  auth_plugin='mysql_native_password',
  db="mydatabase")

data = '''
    create table data(
        timeStamp varchar(100) primary key not null,
        xvalue int not null,
        yvalue int not null,
        zvalue int not null, 
    )
'''
mycursor = mydb.cursor()

# mycursor.execute("CREATE DATABASE mydatabase")

mycursor.execute(data)
mycursor.close()
mydb.commit()

# def connect_mysql():
#     db_config = dict(host="localhost", port=3306, db="mydatabase", charset="utf8", user="root", passwd="zqs@@740848126")
#     try:
#         cnx = MySQLdb.connect(**db_config)
#         cnx.execute(data)
#     except Exception as err:
#         raise err
#     return cnx

# if __name__ == "__main__":
#     sql = "create table test(id int not null);"
#     cnx = connect_mysql()
#     cus = cnx.cursor() 
#     try:
#         cus.execute(data)
#         cus.close()
#         cnx.commit()
#     except Exception as err:
#         cnx.rollback()
#         raise err
#     finally:
#         cnx.close()