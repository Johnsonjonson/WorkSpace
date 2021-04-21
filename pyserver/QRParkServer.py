# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
data = {'index':0,'phone':'','chepai':''}
class updateData(MethodView):
    def get(self):
        data['index'] = request.args.get("index",0)
        data['phone'] = request.args.get("phone",'')
        data['chepai'] = request.args.get("chepai",'')
        return flask.jsonify(data)

class getIndex(MethodView):
    def get(self):
        return data['index']

class getData(MethodView):
    def get(self):
        return flask.jsonify(data)

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get_index', view_func=getIndex.as_view('get_index'))
    app.add_url_rule('/get_data', view_func=getData.as_view('get_data'))
    app.run(host='0.0.0.0', port=8542)

