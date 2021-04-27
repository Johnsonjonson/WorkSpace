# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json

app = flask.Flask(__name__)
data = {
    'index':0
}
index = 0
class updateIndex(MethodView):
    def get(self):
        global index
        index = request.args.get("index",0)
        data['index'] = index
        return '200'
    def post(self):
        return self.get()

class getIndex(MethodView):
    def get(self):
        return flask.jsonify(data)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateIndex.as_view('update'))
    app.add_url_rule('/get', view_func=getIndex.as_view('get'))
    app.run(host='0.0.0.0', port=8545)

