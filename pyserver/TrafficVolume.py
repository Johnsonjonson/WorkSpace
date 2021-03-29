# -*- coding: utf-8 -*-
import flask
from flask.views import MethodView
from flask import request
import datetime

app = flask.Flask(__name__)
temperature = 0
data = {
    "enter": 0,
    "exit": 0,
    "total": 0,
    "park": 0,
}
global mydb


class updateDatas(MethodView):
    def get(self):
        enter = request.args.get("enter", 0)
        exit = request.args.get("exit", 0)
        total = request.args.get("total", 0)
        park = request.args.get("park", 0)
        data["enter"] = enter
        data["exit"] = exit
        data["total"] = total
        data["park"] = park
        return "200"


class getDatas(MethodView):
    def get(self):
        return flask.jsonify(data)


if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateDatas.as_view('update'))
    app.add_url_rule('/get', view_func=getDatas.as_view('get'))
    app.run(host='0.0.0.0', port=8538)
