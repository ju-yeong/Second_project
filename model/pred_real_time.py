#!/usr/bin/env python
# coding: utf-8

# In[ ]:


import joblib
import numpy as np
import pandas as pd

import requests as req
import xmltodict
import json
from datetime import datetime
import time

from selenium import webdriver as wd
from bs4 import BeautifulSoup as bs

import sys


# In[ ]:


def pred_time(stationId):

    if stationId == "200000188": bus_stop = '장안문'
    elif stationId == "200000186": bus_stop = '수원kt'
    elif stationId == "200000078": bus_stop = '수일중학교'
    elif stationId == "200000194": bus_stop = '경기일보'
    elif stationId == "200000271": bus_stop = '동성아울렛'
    elif stationId == "200000183": bus_stop = '장안공원'
    elif stationId == "202000220": bus_stop = '수원여고'
    elif stationId == "202000217": bus_stop = '농천교회'
        
        
    try:
        # 1. 모델 로드
        model = joblib.load("C:/Users/-/python/SecondProject/model/bus_info_model.pkl") 
        # 분석할 데이터 컬럼
        columns = np.loadtxt("C:/Users/-/python/SecondProject/model/columns.txt", dtype=object)
        # 컬럼 중 roadsectionid만 추출
        roadsectionid = columns[1:-58]
    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 로드 에러)"

    
        
    try:
        # 2. 통행속도 데이터 수집
        # 국토교통부 openAPI 이용해서 roadsectionid의 통행속도 구하기
        cols = ['roadsectionid', 'avgspeed', 'startnodeid', 'roadnametext', 'traveltime', 'endnodeid', 'generatedate']

        url = 'http://openapi.its.go.kr/api/NTrafficInfo'
        queryParams = '?' + 'key' + '=' + '1567388978624'
        queryParams += '&' + 'ReqType' + '=' + '2'
        queryParams += '&' + 'MinX' + '=' + '126.988000'
        queryParams += '&' + 'MaxX' + '=' + '127.015000'
        queryParams += '&' + 'MinY' + '=' + '37.267000'
        queryParams += '&' + 'MaxY' + '=' + '37.314000'

        response = req.get(url + queryParams)

        if (response.status_code == 200):
            responseData = response.text #요청받은 XML 데이터 저장
            rD = xmltodict.parse(responseData) #XML -> dict형식으로 변환
            rDJ = json.dumps(rD) #dict -> json
            rDD = json.loads(rDJ) #json-=> dict
            curr_data = rDD['response']['data']

            traffic = []
            for i in curr_data:
                traffic.append(list(i.values()))
            traffic = np.array(traffic)

            # 데이터프레임으로 변환
            traffic = pd.DataFrame(traffic, columns = cols)
            # roadsectionid와 통행속도만 추출
            traffic = traffic[traffic['roadsectionid'].isin(roadsectionid)][['roadsectionid','avgspeed']]
            traffic = traffic.sort_values(by='roadsectionid', ascending=True)
            # int형으로 data type 변경
            traffic['avgspeed'] = traffic['avgspeed'].astype('int')
            
    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 통행속도 API 에러)"
    
    
    
    try:
        # 3. 버스 데이터 수집
        # 경기도 제공 openAPI 이용하여 버스 도착 예상 시간과 버스 번호 수집
        url = 'http://openapi.gbis.go.kr/ws/rest/busarrivalservice'
        queryParams = '?' + 'serviceKey' + '=' + 'uNY%2BOVwL3weHrcP9MP3EjeBGgmlirGAMsaVwv%2FDRvG4i6h7StXaoWWHqTTS5g0T%2FFIf7HyW2GZCwL2%2F6lQvsvA%3D%3D'
        queryParams += '&' + 'stationId' + '=' + stationId
        queryParams += '&' + 'routeId' + '=' + '233000031'

        response = req.get(url + queryParams)
        
        if (response.status_code == 200):
            responseData = response.text
            rD = xmltodict.parse(responseData)
            rDJ = json.dumps(rD)
            rDD = json.loads(rDJ)

            if rDD['response']['msgHeader']['resultCode'] != '0':
                return "버스 도착 정보가 존재하지 않습니다. 잠시 뒤에 다시 시도해 주세요."
            else :
                pred_time = int(rDD['response']['msgBody']['busArrivalItem']['predictTime1'])

    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 버스 API 에러)"
    
    
    
    try:
        # 4. 날짜 데이터
        day = datetime.now().day
        hour = datetime.now().hour
        minute = datetime.now().minute
        dayofweek = datetime.now().weekday()
        if dayofweek==5 or dayofweek==6: weekend = 1
        else: weekend = 0
    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 날짜 에러)"

    
    
    try:
        # 5. 데이터 통합
        data_traffic = traffic.set_index('roadsectionid').T.reset_index(drop=True)
        data_bus_date = pd.DataFrame([pred_time,day, hour,minute,dayofweek,weekend]).T

        data_bus_date.columns = ['pred_time','day', 'hour', 'min', 'dayofweek', 'weekend' ]

        data = pd.DataFrame(columns=columns)
        data[data_bus_date.columns] = data_bus_date
        data[data_traffic.columns] = data_traffic

        data['bus_stop_'+bus_stop] = 1

        data = data.fillna(0)
    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 데이터 통합 에러)"

    
    
    try:
        # 6. 모델 예측
        return model.predict(data)
                
    except:
        return "죄송합니다. 관리자에게 연락주세요(Error: 모델 에러)"


# In[ ]:


print("&" , pred_time(sys.argv[1]), "&")

