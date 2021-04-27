# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json
from iFinDPy import *
from datetime import datetime
import pandas as pd
import time as _time

app = flask.Flask(__name__)

# 登录函数
def thslogin():
    # 输入用户的帐号和密码
    thsLogin = THS_iFinDLogin("guolianan025", "410446")
    print(thsLogin)
    if thsLogin != 0:
        print('登录失败')
    else:
        print('登录成功')


def history(date_str):
    # H30184.CSI 在 date_str 日的收盘价
    data = THS_HQ('H30184.CSI', 'close', '', date_str, date_str)
    if data.errorcode != 0:
        print('error:{}'.format(data.errmsg))
        return 0
    else:
        print(data)
        try:
            close = float(data.data.close)
        except:
            close = 0
        return float(close)


def latest():
    data = THS_RQ('H30184.CSI', 'latest')
    if data.errorcode != 0:
        print('error:{}'.format(data.errmsg))
        return 0
    else:
        print(data)
        try:
            latest = float(data.data.latest)
        except:
            latest = 0
        return float(latest)


class GetLatest(MethodView):
    def get(self):
        latestValue = latest()
        return str(latestValue)
    
    def post(self):
        return self.get()


class GetHistory(MethodView):
    def get(self):
        date_str = request.args.get("date", '')
        historyValue  = history(date_str)
        return str(historyValue)
    
    def post(self):
        return self.get()


if __name__ == '__main__':
    app.add_url_rule('/latest', view_func=GetLatest.as_view('latest'))
    app.add_url_rule('/history', view_func=GetHistory.as_view('history'))
    thslogin()
    app.run(host='0.0.0.0', port=8600)
