# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
healthData = {'red':0,'fire':0,'temp':0.0}
global switch
switch = 0
class updateData(MethodView):
    def get(self):
        healthData['red'] = request.args.get("red",0)
        healthData['fire'] = request.args.get("fire",0)
        healthData['temp'] = request.args.get("temp",0)
        global switch
        return str(switch)

class getData(MethodView):
    def get(self):
        return flask.jsonify(healthData)

class updateSwitch(MethodView):
    def get(self):
        global switch
        switch = request.args.get("switch", 0)
        return "200"

class getSwitch(MethodView):
    def get(self):
        global switch
        return str(switch)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))

    app.add_url_rule('/set_switch', view_func=updateSwitch.as_view('set_switch'))
    app.add_url_rule('/get_switch', view_func=getSwitch.as_view('get_switch'))
    app.run(host='0.0.0.0', port=8554)

