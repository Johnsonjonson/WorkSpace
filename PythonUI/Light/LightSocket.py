# coding:UTF-8
import flask
from flask import request, Flask
from flask.views import MethodView
from geventwebsocket.handler import WebSocketHandler
from geventwebsocket.server import WSGIServer
from geventwebsocket.websocket import WebSocket
import threading
import json

app = Flask(__name__)

user_socket_list = []
data = {'code': None, 'status': None}
# data = {'temp': None, 'status': None}


def temp():
    for usocket in user_socket_list:
        usocket.send("")


class UpdateData(MethodView):
    def get(self):
        status = int(request.args.get('status', None))
        print("============%s",status)
        if isinstance(status,int):
            data['status'] = True if status == 1 else False
            print("============2  %s",data['status'])
        for usocket in user_socket_list:
            try:
                sendData = {'status':'true' if data['status'] else 'false','msg':'路灯' if data['status'] else '故障'}
                usocket.send(json.dumps(sendData))
            except:
                continue
        return ('{status=%s,code=%s}' % (1 if data['status'] else 0, 200), 200)


class ChangeData(MethodView):
    def get(self):
        status = request.args.get('change', None)
        if status:
            data['status'] = True if status == 'true' else False
        return ('{"status":%s,"msg":%s}' % ('"true"' if data['status'] else '"false"', '"路灯"' if data['status'] else '"故障"'), 200)


class GetData(MethodView):
    def get(self):
        # return flask.jsonify(data)
        return ('{"status":%s,"msg":%s}' % ('"true"' if data['status'] else '"false"', '"路灯"' if data['status'] else '"故障"'), 200)


@app.route("/light/PythonWebSocket")
def my_socket():
    #
    print("链接   =======  ")
    user_socket = request.environ.get("wsgi.websocket")  # type:WebSocket
    if user_socket:
        user_socket_list.append(user_socket)
        print(len(user_socket_list), user_socket_list)
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
    app.add_url_rule('/light/update', view_func=UpdateData.as_view('update'))
    app.add_url_rule(
        '/light/python', view_func=GetData.as_view('current-location'))
    http_serv = WSGIServer(("192.168.1.114", 8080), app,
                           handler_class=WebSocketHandler)
    http_serv.serve_forever()
    # app.run(host='192.168.1.114', port=8524)
