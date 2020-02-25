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


def bus_location(routeId):
    url = 'http://openapi.gbis.go.kr/ws/rest/buslocationservice'

    queryParams = '?' + 'serviceKey' + '=' + 'uNY%2BOVwL3weHrcP9MP3EjeBGgmlirGAMsaVwv%2FDRvG4i6h7StXaoWWHqTTS5g0T%2FFIf7HyW2GZCwL2%2F6lQvsvA%3D%3D'
    queryParams += '&' + 'routeId' + '=' + routeId
    response = requests.get(url + queryParams)

    try:
        if(response.status_code == 200):
            responseData = response.text #요청받은 XML 데이터 저장
            rD = xmltodict.parse(responseData) #XML -> dict형식으로 변환
            rDJ = json.dumps(rD) #dict -> json
            rDD = json.loads(rDJ) #json-=> dict

            if rDD['response']['msgHeader']['resultCode']=='0':
                print('&')
                cols = rDD['response']['msgBody']['busLocationList'][0].keys()
                data = np.array(list(rDD['response']['msgBody']['busLocationList'][0].values()))
                for temp in rDD['response']['msgBody']['busLocationList'][1:]:
                    data = np.concatenate([data, np.array(list(temp.values()))], axis=0)
                data = pd.DataFrame(data.reshape(-1,len(cols)), columns=cols)    
                data['stationSeq'] = data['stationSeq'].astype(int)
                data.sort_values(by=['stationSeq'], inplace=True)
                for x in data['stationId']: 
                    print(x+"#")
                print('&')
            elif rDD['response']['msgHeader']['resultCode']=='4':
                print('& 버스 운행 시간이 아닙니다. &')

        else:
            print(f"& Error(url) : code {rescode} &")
    except:
        print("& Error &")


# In[ ]:


bus_location(sys.argv[1])

