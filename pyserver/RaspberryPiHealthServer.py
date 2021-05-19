# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
healthData = {'heart':0.0,'blood':0.0,'temp':0.0}
class updateData(MethodView):
    def get(self):
        healthData['heart'] = request.args.get("heart",0)
        healthData['blood'] = request.args.get("blood",0)
        healthData['temp'] = request.args.get("temp",0)

        return "200"

class getData(MethodView):
    def get(self):
        return flask.jsonify(healthData)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))
    app.run(host='0.0.0.0', port=8553)

