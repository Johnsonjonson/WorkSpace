# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json

app = flask.Flask(__name__)
control = {'1':"-1"} # 1 上， 2 下， 3 左 ，4 右
status = 0
temperature  = 0
data = {
    'status':0,
    'temp':0.0
}
class updateStatus(MethodView):
    def get(self):
        global status
        global temperature
        # params = request.args.get("params","")
        status = request.args.get("status",0)
        temperature = request.args.get("temp", 0)

        data['status'] = status
        data['temp'] = temperature
        return "200"

class getStatus(MethodView):
    def get(self):
        return flask.jsonify(data)

class updateTemperature(MethodView):
    def get(self):
        global temperature
        # params = request.args.get("params","")
        temperature = request.args.get("temp", 0)

        return "200"

class getTemperature(MethodView):
    def get(self):
        global temperature
        return str(temperature)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateStatus.as_view('update'))
    app.add_url_rule('/get', view_func=getStatus.as_view('get'))
    app.add_url_rule('/updateTemp', view_func=updateTemperature.as_view('updateTemp'))
    app.add_url_rule('/getTemp', view_func=getTemperature.as_view('getTemp'))
    app.run(host='0.0.0.0', port=8530)

