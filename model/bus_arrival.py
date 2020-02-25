#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import requests
import xmltodict
import json
import numpy as np
import pandas as pd
import sys


# In[ ]:


def bus_arrival(stationId, routeId):
    url = 'http://openapi.gbis.go.kr/ws/rest/busarrivalservice'

    queryParams = '?' + 'serviceKey' + '=' + 'uNY%2BOVwL3weHrcP9MP3EjeBGgmlirGAMsaVwv%2FDRvG4i6h7StXaoWWHqTTS5g0T%2FFIf7HyW2GZCwL2%2F6lQvsvA%3D%3D'
    queryParams += '&' + 'stationId' + '=' + stationId
    queryParams += '&' + 'routeId' + '=' + routeId
    response = requests.get(url + queryParams)

    try:
        if(response.status_code == 200):
            responseData = response.text #요청받은 XML 데이터 저장
            rD = xmltodict.parse(responseData) #XML -> dict형식으로 변환
            rDJ = json.dumps(rD) #dict -> json
            rDD = json.loads(rDJ) #json-=> dict

            if rDD['response']['msgHeader']['resultCode']=='0':
                print('&', rDD['response']['msgBody']['busArrivalItem']['predictTime1'], '&')
            elif rDD['response']['msgHeader']['resultCode']=='4':
                print('& 차고지에서 출발 전이거나 운행시간이 아닙니다. &')

        else:
            print(f"& Error(url) : code {rescode} &")
    except:
        print("& Error &")


# In[ ]:


bus_arrival(sys.argv[1], sys.argv[2])

