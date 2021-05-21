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

cardType = 0  # 0 ，1有效卡 ，2 无效卡

data = {
    'cardType': 0,
    'time': ''
}


class SetCard(MethodView):
    def get(self):
        global cardType
        cardType = request.args.get("card_type", 0)
        global dakatime
        dakatime = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        data['time'] = dakatime
        data['cardType'] = cardType
        return "200"


class GetUserInfo(MethodView):
    def get(self):
        return flask.jsonify(data)


if __name__ == '__main__':
    app.add_url_rule('/set', view_func=SetCard.as_view('set'))
    app.add_url_rule('/get', view_func=GetUserInfo.as_view('get'))
    app.run(host='0.0.0.0', port=8540)
