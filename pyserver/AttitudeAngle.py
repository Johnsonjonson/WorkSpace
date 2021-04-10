# -*- coding: utf-8 -*-
import flask
from flask import request
from flask.views import MethodView

app = flask.Flask(__name__)
angleData = {'pitch':0.0, 'yaw':0.0, 'roll':0.0}
class updateData(MethodView):
    def get(self):
        angleData['pitch'] = request.args.get("pitch", 0)
        angleData['yaw'] = request.args.get("yaw", 0)
        angleData['roll'] = request.args.get("roll", 0)

        return "200"

class getData(MethodView):
    def get(self):
        return flask.jsonify(angleData)


if __name__ == '__main__':
    app.add_url_rule('/update', view_func=updateData.as_view('update'))
    app.add_url_rule('/get', view_func=getData.as_view('get'))
    app.run(host='0.0.0.0', port=8539)

