import flask
from flask import request
from flask.views import MethodView
import logging

app = flask.Flask(__name__)
data = {'temp': None, 'status': None}


class UpdateData(MethodView):
	def get(self):
		temp = request.args.get('temp', None)
		needChange = request.args.get('change', None)
		if temp:
			data['temp'] = float(temp)
		if needChange:
			data['status'] = True if needChange == 'true' else False
		return ('%s' % (1 if data['status'] else 0), 200)

class ChangeData(MethodView):
	def get(self):
		status = request.args.get('change', None)
		logging.info('debug status %s',status)
		if status:
			data['status'] = True if status == 'true' else False
		return ('{"status":%s,"temp":%s}' % ('true' if data['status'] else 'false',data['temp'] if data['temp'] else 0), 200)

class GetData(MethodView):
	def get(self):
		return flask.jsonify(data)

if __name__ == '__main__':
	app.add_url_rule('/update', view_func=UpdateData.as_view('update'))
	app.add_url_rule('/change', view_func=ChangeData.as_view('change'))
	app.add_url_rule('/data', view_func=GetData.as_view('current-location'))
	app.run(host='172.16.0.16', port=8524)

