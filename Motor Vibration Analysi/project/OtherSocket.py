# import websocket
# import threading
# import time

# def on_message(ws, message):
#     print("receive Msg = ", message)

# def on_error(ws, error):
#     print(error)


# def on_close(ws):
#     print("### closed ###")


# def on_open(ws):
#     print("### open ###")
#     def run(*args):
#         ws.send("hello1")
#         time.sleep(0.001)
#         ws.close()
#     wst = threading.Thread(target=run)
#     wst.daemon = True
#     wst.start()


# def connectSocket():
#     # socketUrl = "ws://localhost:8080/PythonTest_war_exploded/PythonWebSocket"
#     socketUrl = "ws://129.204.232.210:8902"
#     ws = websocket.WebSocketApp(socketUrl,
#                                     on_message=on_message,
#                                     on_error=on_error,
#                                     on_close=on_close)
#     ws.on_open = on_open
#     # print(type(ws))
#     ws.run_forever(ping_interval=60,ping_timeout=5)
#     # wst = threading.Thread(target=ws.run_forever)
#     # wst.daemon = True
#     # wst.start()

# connectSocket()
from websocket import create_connection
ws = create_connection("ws://129.204.232.210:8902")
print("Sending 'Hello, World'...")
ws.send("1,2,3")
print("Sent")
print("Receiving...")
result =  ws.recv()
print("Received '%s'" % result)
ws.close()