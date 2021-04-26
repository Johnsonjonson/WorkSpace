# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json

app = flask.Flask(__name__)
# 116.403838,39.915046
data = {
    'lat': 39.915046,#纬度
    'lng':116.403838,#经度
}
class updateLocation(MethodView):
    def get(self):
        data['lat'] = request.args.get("lat",0)
        data['lng'] = request.args.get("lng",0)
        return "200"

class getLocation(MethodView):
    def get(self):
        return flask.jsonify(data)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateLocation.as_view('update'))
    app.add_url_rule('/get', view_func=getLocation.as_view('get'))
    app.run(host='0.0.0.0', port=8544)

