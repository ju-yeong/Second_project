#!/usr/bin/env python
# coding: utf-8

# In[1]:


from selenium import webdriver as wd 
from bs4 import BeautifulSoup as bs
import pandas as pd
import time 
from datetime import datetime
import requests as req


# In[2]:


#장안문
url='http://www.gbis.go.kr/gbis2014/schBusRealtime.action?cmd=stationInfoPopReal&GDecorator=no&stationId=200000188&stationNm=%EC%9E%A5%EC%95%88%EB%AC%B8.%EB%8A%90%EB%A6%BC%EB%B3%B4%ED%83%80%EC%9A%B4&routeId=233000031&staNo=%2001139&staTP=0&staType=0'
driver = wd.Chrome()
driver.get(url)
#수원kt
url_1='http://www.gbis.go.kr/gbis2014/schBusRealtime.action?cmd=stationInfoPopReal&GDecorator=no&stationId=200000186&stationNm=%EC%88%98%EC%9B%90KT%EC%9C%84%EC%A6%88%ED%8C%8C%ED%81%AC&routeId=233000031&staNo=%2001131&staTP=0&staType=0'
driver_1 = wd.Chrome()
driver_1.get(url_1)


# In[3]:


#time.sleep(7300)
switch=True

while(switch):
    minute = datetime.now().minute
    if minute%10==0:
        print('현재시간',datetime.now().hour,":",datetime.now().minute)
        
    try:

        if(datetime.now().hour>=5): #시작시간
            bus_list=[]
            clock=[]
            bus_remain = []

            bus_list_1=[]
            clock_1=[]
            bus_remain_1 = []

            driver.refresh()
            driver_1.refresh()

            time.sleep(3)

            soup = bs(driver.page_source, 'lxml')
            soup_1 = bs(driver_1.page_source, 'lxml')

            cnt = 0
            bus = soup.select_one('body > div > div.popup_scroll > div > div > dl > dd > strong').text
            bus_1 = soup_1.select_one('body > div > div.popup_scroll > div > div > dl > dd > strong').text

            while(datetime.now().hour<21):#끝시간
                driver.refresh()
                driver_1.refresh()
                time.sleep(30)
                soup = bs(driver.page_source, 'lxml')
                soup_1 = bs(driver_1.page_source, 'lxml')

                bus_real_time = soup.select_one('body > div > div.popup_scroll > div > div > dl > dd > strong')
                bus_real_time_1 = soup_1.select_one('body > div > div.popup_scroll > div > div > dl > dd > strong')

                if not bus_real_time == None:        
                    if bus != bus_real_time.text:
                        bus=bus_real_time.text
                        bus_list.append(bus)
                        clock.append(datetime.now())
                        bus_remain.append(soup.select('body > div > div.popup_scroll > div > div > dl > dd > strong')[1].text)
                else :
                    print("장안문에러:",datetime.now().hour,":",datetime.now().minute) #마지막 시간

                if not bus_real_time_1 == None:        
                    if bus_1 != bus_real_time_1.text:
                        bus_1=bus_real_time_1.text
                        bus_list_1.append(bus_1)
                        clock_1.append(datetime.now())
                        bus_remain_1.append(soup_1.select('body > div > div.popup_scroll > div > div > dl > dd > strong')[1].text)
                else :
                    print("수원kt에러",datetime.now().hour,":",datetime.now().minute) #마지막 시간


                cnt+=1
                print('장안문',str(cnt)+'/'+bus+'/'+bus_real_time.text)
                print('수원kt',str(cnt)+'/'+bus_1+'/'+bus_real_time_1.text)


            bus_list=pd.DataFrame(bus_list)
            bus_remain=pd.DataFrame(bus_remain)
            bus_list['now']=clock
            result = pd.concat([bus_list, bus_remain],axis=1)
            result.to_csv('0809_14_장안문.csv', encoding="")

            bus_list_1=pd.DataFrame(bus_list_1)
            bus_remain_1=pd.DataFrame(bus_remain_1)
            bus_list_1['now']=clock_1
            result_1 = pd.concat([bus_list_1, bus_remain_1],axis=1)
            result_1.to_csv('0809_14_수원kt.csv', encoding="")

            switch=False
            
    except:
            print('에러시간', datetime.now())
    else:
        time.sleep(60)

