# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import time
import json
import mysql.connector
import datetime

app = flask.Flask(__name__)
userid = 0
dakatime = ""

data = {
    '1': {
        'name': "A单位",
        'car_type': "大众",
        'car_num': "吉AA266G",
        'time': "2021-03-08 15:49:13",
    },
    '2': {
        'name': "B单位",
        'car_type': "奔驰",
        'car_num': "京AG6104",
        'time': "2021-03-08 15:49:13",
    }
}


class SetUserId(MethodView):
    def get(self):
        global userid
        userid = request.args.get("id", 0)
        global dakatime
        dakatime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        if int(userid) == 0:
            return "402"
        elif int(userid) == 1 or int(userid) ==2:
            return "200"
        else:
            return "401"


class GetUserInfo(MethodView):
    def get(self):
        temp = {}
        if int(userid) ==0:
            temp = {}
        elif int(userid) == 1 or int(userid) == 2:
            temp = data[str(userid)] or {}
            temp['time'] = dakatime
        return flask.jsonify(temp)


if __name__ == '__main__':
    app.add_url_rule('/set_id', view_func=SetUserId.as_view('set_id'))
    app.add_url_rule('/get', view_func=GetUserInfo.as_view('get'))
    app.run(host='0.0.0.0', port=8534)
