# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
chargeData = {'voltage':0.0,'current':0.0,'temp':0.0,'status':0}
class updateData(MethodView):
    def get(self):
        chargeData['voltage'] = request.args.get("voltage",0)
        chargeData['current'] = request.args.get("current",0)
        chargeData['temp'] = request.args.get("temp",0)
        chargeData['status'] = request.args.get("status",0)

        return "200"

class getData(MethodView):
    def get(self):
        return flask.jsonify(chargeData)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))
    app.run(host='0.0.0.0', port=8553)

