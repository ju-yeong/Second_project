#!/usr/bin/env python
# coding: utf-8

# In[1]:


#!pip install xmltodict  
import requests
import xmltodict
import json
import numpy as np
import pandas as pd
from datetime import datetime
import time


# In[2]:


cols = ['roadsectionid', 'avgspeed', 'startnodeid', 'roadnametext', 'traveltime', 'endnodeid', 'generatedate']
final_data = pd.DataFrame(columns=cols)

switch = True

#time.sleep(7300)

while(switch):
    if(datetime.now().hour>=5): #시작 조건 : 새벽 5시 시작
        try:
            while(switch):
                print('동작중')
                url = 'http://openapi.its.go.kr/api/NTrafficInfo'

                queryParams = '?' + 'key' + '=' + '1567388978624'
                queryParams += '&' + 'ReqType' + '=' + '2'
                queryParams += '&' + 'MinX' + '=' + '126.988000'
                queryParams += '&' + 'MaxX' + '=' + '127.010000'
                queryParams += '&' + 'MinY' + '=' + '37.301000'
                queryParams += '&' + 'MaxY' + '=' + '37.314000'

                response = requests.get(url + queryParams)

                time.sleep(5)

                if(response.status_code == 200):
                    responseData = response.text       #요청받은 XML 데이터 저장
                    rD = xmltodict.parse(responseData) #XML -> dict형식으로 변환
                    rDJ = json.dumps(rD)               #dict -> json
                    rDD = json.loads(rDJ)              #json-=> dict
                else:
                    print("Error Code:" , rescode)

                curr_data = rDD['response']['data']

                # 데이터
                curr_data_df = []
                for i in curr_data:
                    curr_data_df.append(list(i.values()))
                curr_data_df = np.array(curr_data_df)

                # 데이터프레임으로 변환
                curr_data_df = pd.DataFrame(curr_data_df, columns = cols)

                # 데이터프레임에 현재 시간 추가하기
                curr_data_df['now'] = datetime.now()

                # 누적하기
                final_data = final_data.append(curr_data_df)

                # 1분 후 재수집
                time.sleep(53)

                if(datetime.now().hour>=21): #시작 조건 : 14시 종료
                    switch=False
        except:
            print('에러발생!!!!! 시간 : ', datetime.now())
            
    else:
        time.sleep(600)

final_data.to_csv("0809__14__교통량1.csv", encoding='')

