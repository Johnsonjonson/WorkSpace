# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
healthData = {'jingdu':0.0,'weidu':0.0}
controlData = {'fengming':0,'deng':0}
class updateData(MethodView):
    def get(self):
        healthData['jingdu'] = request.args.get("jingdu",0)
        healthData['weidu'] = request.args.get("weidu",0)

        return str(controlData['fengming']) + "," + str(controlData['deng'])

class getData(MethodView):
    def get(self):
        return flask.jsonify(healthData)

class updateControl(MethodView):
    def get(self):
        controlData['fengming'] = request.args.get("fengming",0)
        controlData['deng'] = request.args.get("deng",0)

        return "200"

class getControl(MethodView):
    def get(self):
        return str(controlData['fengming']) + "," + str(controlData['deng'])

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))
    app.add_url_rule('/updateCtrl', view_func=updateControl.as_view('updateCtrl'))
    app.add_url_rule('/getCtrl', view_func=getControl.as_view('getCtrl'))
    app.run(host='0.0.0.0', port=8541)

