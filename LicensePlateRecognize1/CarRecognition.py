# encoding:utf-8
import base64
import json

import requests
import ssl

ssl._create_default_https_context = ssl._create_unverified_context

def recognize(file):
    # client_id 为官网获取的AK， client_secret 为官网获取的SK
    host = 'https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=0lUbgRSL2wlaUW8QMfSyLyxX&client_secret=DHXf1GTWGRtbHrKDU0IC8SvzKwvtsaQa'
    response = requests.get(host)
    if response:
        print(response.json()['refresh_token'])
    '''
    车牌识别
    '''
    request_url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate"
    # 二进制方式打开图片文件
    if file:
        f = open(file, 'rb')
    else:
        f = open('test/tmp.jpg', 'rb')
    img = base64.b64encode(f.read())
    # img = base64.b64encode(resimg)

    params = {"image":img}
    access_token = response.json()['access_token']
    request_url = request_url + "?access_token=" + access_token
    headers = {'content-type': 'application/x-www-form-urlencoded'}
    response = requests.post(request_url, data=params, headers=headers)
    if response:
        print (response.json())
    return response.json()