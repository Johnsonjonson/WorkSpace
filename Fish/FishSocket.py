import flask
from flask import request, Flask
from flask.views import MethodView
from geventwebsocket.handler import WebSocketHandler
from geventwebsocket.server import WSGIServer
from geventwebsocket.websocket import WebSocket

app = Flask(__name__)

user_socket_list = []
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
		if status:
			data['status'] = True if status == 'true' else False
		return ('{"status":%s,"temp":%s}' % ('true' if data['status'] else 'false',data['temp'] if data['temp'] else 0), 200)

class GetData(MethodView):
	def get(self):
		return flask.jsonify(data)


@app.route("/fish/fishsocket")
def my_socket():
    # 
    user_socket = request.environ.get("wsgi.websocket")  # type:WebSocket
    if user_socket:
            user_socket_list.append(user_socket)
            print(len(user_socket_list),user_socket_list)
        # 1 [<geventwebsocket.websocket.WebSocket object at 0x000001D0D70E1458>]
        # print(user_socket,"OK")
    while 1:
        # 
        msg = user_socket.receive()
        print(msg)

        for usocket in user_socket_list:
            try:
                usocket.send(msg)
            except:
                continue

if __name__ == '__main__':
    app.add_url_rule('/update', view_func=UpdateData.as_view('update'))
    app.add_url_rule('/change', view_func=ChangeData.as_view('change'))
    app.add_url_rule('/data', view_func=GetData.as_view('current-location'))
    http_serv = WSGIServer(("192.168.1.114",8524),app,handler_class=WebSocketHandler) # 
    http_serv.serve_forever()
    # app.run(host='192.168.1.114', port=8524)
