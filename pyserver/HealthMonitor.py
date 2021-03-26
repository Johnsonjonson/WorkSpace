# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
healthData = {'heartRate':0.0,'bloodOxygen':0.0}
controlData = {'anmo':0,'dianliao':0}
class updateData(MethodView):
    def get(self):
        healthData['heartRate'] = request.args.get("heart",0)
        healthData['bloodOxygen'] = request.args.get("blood",0)

        return str(controlData['anmo']) + "," + str(controlData['dianliao'])

class getData(MethodView):
    def get(self):
        return flask.jsonify(healthData)

class updateControl(MethodView):
    def get(self):
        controlData['anmo'] = request.args.get("anmo",0)
        controlData['dianliao'] = request.args.get("dianliao",0)

        return "200"

class getControl(MethodView):
    def get(self):
        return str(controlData['anmo']) + "," + str(controlData['dianliao'])

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))
    app.add_url_rule('/updateCtrl', view_func=updateControl.as_view('updateCtrl'))
    app.add_url_rule('/getCtrl', view_func=getControl.as_view('getCtrl'))
    app.run(host='0.0.0.0', port=8531)

