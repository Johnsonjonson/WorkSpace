# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json
import mysql.connector
import datetime

app = flask.Flask(__name__)
userid = 0

data = {
    '1': {
        'name': "张三",
        'tel': "18273645533",
        'car_num': "京AG6104"
    },
    '2': {
        'name': "李四",
        'tel': "18088888888",
        'car_num': "京AG6104"
    }
}


class SetUserId(MethodView):
    def get(self):
        global userid
        userid = request.args.get("id", 0)
        return "200"


class GetUserInfo(MethodView):
    def get(self):
        if userid ==0:
            temp = {}
        else:
            temp = data[str(userid)] or {}
        return flask.jsonify(temp)


if __name__ == '__main__':
    app.add_url_rule('/set_id', view_func=SetUserId.as_view('set_id'))
    app.add_url_rule('/get', view_func=GetUserInfo.as_view('get'))
    app.run(host='0.0.0.0', port=8534)
