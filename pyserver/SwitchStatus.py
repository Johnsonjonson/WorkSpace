# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView
import math
import json

app = flask.Flask(__name__)
data = {
    'status':0
}
status = 0
class updateStatus(MethodView):
    def get(self):
        global status
        status = request.args.get("status",0)
        data['status'] = request.args.get("status",0)
        return status

class getStatus(MethodView):
    def get(self):
        return status

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateStatus.as_view('update'))
    app.add_url_rule('/get', view_func=getStatus.as_view('get'))
    app.run(host='0.0.0.0', port=8543)

