# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json

app = flask.Flask(__name__)
# 116.403838,39.915046
data = {
    'location1':{
        'lat': 37.915046,#纬度
        'lng':116.403838,#经度
    },
    'location2':{
        'lat': 36.915046,#纬度
        'lng':116.403838,#经度
    },
    'id':0,
}
class updateLocation1(MethodView):
    def get(self):
        data['location1']['lat'] = request.args.get("lat",0)
        data['location1']['lng'] = request.args.get("lng",0)
        data['id'] = request.args.get("id", 0)
        return "200"

class updateLocation2(MethodView):
    def get(self):
        data['location2']['lat'] = request.args.get("lat", 0)
        data['location2']['lng'] = request.args.get("lng", 0)
        return "200"

class getLocation(MethodView):
    def get(self):
        return flask.jsonify(data)

if __name__ == '__main__':
    app.add_url_rule('/location1', view_func=updateLocation1.as_view('location1'))
    app.add_url_rule('/location2', view_func=updateLocation2.as_view('location2'))
    app.add_url_rule('/get', view_func=getLocation.as_view('get'))
    app.run(host='0.0.0.0', port=8546)

