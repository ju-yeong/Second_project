#!/usr/bin/env python
# coding: utf-8

# In[19]:


# 한글 깨짐 방지 코드
from matplotlib import font_manager, rc
font_name = font_manager.FontProperties(fname="c:/Windows/Fonts/malgun.ttf").get_name()
rc('font', family=font_name)


# In[20]:


import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.model_selection import train_test_split
from sklearn import metrics
from sklearn.model_selection import cross_val_score
from sklearn.model_selection import GridSearchCV
from sklearn.feature_selection import SelectFromModel

from sklearn.linear_model import LinearRegression
from sklearn.linear_model import Ridge
from sklearn.linear_model import Lasso
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.ensemble import GradientBoostingRegressor
from xgboost import XGBRegressor


from sklearn.neighbors import KNeighborsClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.svm import LinearSVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import GradientBoostingClassifier
from xgboost import XGBClassifier

from sklearn.pipeline import Pipeline


# In[381]:


data = pd.read_csv('data/구역_최종/final_data(0908).csv', engine='python')


# ### 버스정류장 컬럼 : 원핫인코딩

# In[382]:


one_hot = pd.get_dummies(data["정류장"], prefix="정류장")
data = pd.concat([data, one_hot], axis=1)
data.drop(["정류장"], axis=1, inplace=True)


# ### 오차값 이상치 제거 (±10분 초과)

# In[383]:


data['오차값'].value_counts()


# In[384]:


data = data[data["오차값"].apply(lambda x: np.abs(x)<=10)]


# In[385]:


data['오차값'].value_counts()


# ### 문제와 답 분리

# In[386]:


X = data.iloc[:,1:]
y = data.iloc[:,0]


# # 회귀

# ### train/test 분리

# In[366]:


X_train, X_test, y_train, y_test = train_test_split(X,
                                                   y,
                                                   test_size = 0.3,
                                                   random_state = 3)


# ### 스케일링

# In[367]:


# Robust
from sklearn.preprocessing import RobustScaler
scaler_r = RobustScaler()
scaler_r.fit(X_train)
X_train_r = scaler_r.transform(X_train)
X_test_r = scaler_r.transform(X_test)

# MinMax
from sklearn.preprocessing import MinMaxScaler
scaler_m = MinMaxScaler()
scaler_m.fit(X_train)
X_train_m = scaler_m.transform(X_train)
X_test_m = scaler_m.transform(X_test)

# Standard
from sklearn.preprocessing import StandardScaler
scaler_s = StandardScaler()
scaler_s.fit(X_train)
X_train_s = scaler_s.transform(X_train)
X_test_s = scaler_s.transform(X_test)

# Normalizer
from sklearn.preprocessing import Normalizer
scaler_n = StandardScaler()
scaler_n.fit(X_train)
X_train_n = scaler_n.transform(X_train)
X_test_n = scaler_n.transform(X_test)


# ### 선형회귀모델

# In[368]:


line_model = LinearRegression()
line_model.fit(X_train, y_train)
print("train score : ", line_model.score(X_train, y_train))
print("test score : ", line_model.score(X_test, y_test))

y_pre = line_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### 리지

# In[38]:


ridge = Ridge(alpha=20).fit(X_train, y_train)
print("훈련 세트 점수 : ", ridge.score(X_train, y_train))
print("테스트 세트 점수 : ", ridge.score(X_test, y_test))

y_pre = ridge.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### 라소

# In[370]:


# minmax 스케일러
lasso = Lasso(alpha=0.01, max_iter=100000).fit(X_train_m, y_train)
print("훈련 세트 점수 : ", lasso.score(X_train_m, y_train))
print("테스트 세트 점수 : ", lasso.score(X_test_m, y_test))
print("사용한 특성의 개수 : ", np.sum(lasso.coef_ != 0))

y_pre = lasso.predict(X_test_m)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### 결정트리

# In[371]:


tree_model = DecisionTreeRegressor(max_depth=3).fit(X_train, y_train)

print("train score : ", tree_model.score(X_train, y_train))
print("test score : ", tree_model.score(X_test, y_test))

y_pre = tree_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### 랜덤포레스트

# In[372]:


random_model = RandomForestRegressor(n_estimators=300, max_depth=5).fit(X_train, y_train)

print("train score : ", random_model.score(X_train, y_train))
print("test score : ", random_model.score(X_test, y_test))

y_pre = random_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### XGBoost

# In[374]:


xgb_model = XGBRegressor(max_depth=10,
                         min_child_weight = 100,
                         gamma=0.1, 
                         learning_rate=0.1, 
                         random_state=7, 
                         seed=1, 
                         objective ='reg:linear',
                         silent = False).fit(X_train, y_train)

print("train score : ", xgb_model.score(X_train, y_train))
print("test score : ", xgb_model.score(X_test, y_test))

y_pre = xgb_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# ### 결정트리를 이용한 특성선택

# In[319]:


fi = tree_model.feature_importances_
importances = pd.DataFrame(fi,
                          index = X_train.columns)
feature = importances.sort_values(by=0, ascending = False)


# In[320]:


feature = list(feature[feature.iloc[:,0]>0].index)


# ### 특성 선택 이후 재학습 : 결과가 크게 다르지 않았음

# In[321]:


X_feature = X[feature]


# In[322]:


X_train, X_test, y_train, y_test = train_test_split(X_feature,
                                                   y,
                                                   test_size = 0.3,
                                                   random_state = 3)


# In[323]:


"""선형회귀모델"""
line_model = LinearRegression()
line_model.fit(X_train, y_train)
print("train score : ", line_model.score(X_train, y_train))
print("test score : ", line_model.score(X_test, y_test))

y_pre = line_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# In[324]:


"""리지 + minmax 스케일러"""
ridge = Ridge(alpha=50).fit(X_train_m, y_train)
print("훈련 세트 점수 : ", ridge.score(X_train_m, y_train))
print("테스트 세트 점수 : ", ridge.score(X_test_m, y_test))

y_pre = ridge.predict(X_test_m)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# In[325]:


"""라소 + minmax 스케일러"""
lasso = Lasso(alpha=0.01, max_iter=100000).fit(X_train_m, y_train)
print("훈련 세트 점수 : ", lasso.score(X_train_m, y_train))
print("테스트 세트 점수 : ", lasso.score(X_test_m, y_test))
print("사용한 특성의 개수 : ", np.sum(lasso.coef_ != 0))

y_pre = lasso.predict(X_test_m)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# In[326]:


"""결정트리"""
tree_model = DecisionTreeRegressor(max_depth=3).fit(X_train, y_train)

print("train score : ", tree_model.score(X_train, y_train))
print("test score : ", tree_model.score(X_test, y_test))

y_pre = tree_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# In[327]:


"""랜덤포레스트"""
random_model = RandomForestRegressor(n_estimators=200, max_depth=5).fit(X_train, y_train)

print("train score : ", random_model.score(X_train, y_train))
print("test score : ", random_model.score(X_test, y_test))

y_pre = random_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# In[330]:


"""XGBoost"""
xgb_model = XGBRegressor(max_depth=10,
                         min_child_weight = 100,
                         gamma=0.1, 
                         learning_rate=0.1, 
                         random_state=7, 
                         seed=1, 
                         objective ='reg:linear',
                         silent = False).fit(X_train, y_train)

print("train score : ", xgb_model.score(X_train, y_train))
print("test score : ", xgb_model.score(X_test, y_test))

y_pre = xgb_model.predict(X_test)
mse = metrics.mean_squared_error(y_test, y_pre)
rmse = np.sqrt(mse)
print("mse : ", mse)
print("rmse : ", rmse)


# # 분류

# ### 오차값 카테고리화

# In[387]:


bins = [-np.inf, -6, -2, 1, 5, np.inf]
bins_names = ['-6', '-2~-5','-1~1','2~5','6']
y_cate = pd.cut(y, bins, labels=bins_names)
y_cate.value_counts().sort_index()


# ### train/test 분리

# In[388]:


X_train, X_test, y_train, y_test = train_test_split(X,
                                                   y_cate,
                                                   test_size = 0.3,
                                                   random_state = 3,
                                                   stratify = y_cate)


# ### 스케일링

# In[377]:


# Robust
from sklearn.preprocessing import RobustScaler
scaler_r = RobustScaler()
scaler_r.fit(X_train)
X_train_r = scaler_r.transform(X_train)
X_test_r = scaler_r.transform(X_test)

# MinMax
from sklearn.preprocessing import MinMaxScaler
scaler_m = MinMaxScaler()
scaler_m.fit(X_train)
X_train_m = scaler_m.transform(X_train)
X_test_m = scaler_m.transform(X_test)

# Standard
from sklearn.preprocessing import StandardScaler
scaler_s = StandardScaler()
scaler_s.fit(X_train)
X_train_s = scaler_s.transform(X_train)
X_test_s = scaler_s.transform(X_test)

# Normalizer
from sklearn.preprocessing import Normalizer
scaler_n = StandardScaler()
scaler_n.fit(X_train)
X_train_n = scaler_n.transform(X_train)
X_test_n = scaler_n.transform(X_test)


# ### 로지스틱 : c=1000, minmax

# In[378]:


logi_model = LogisticRegression(C=1000).fit(X_train_m, y_train)

print("train score : ", logi_model.score(X_train_m, y_train))
print("test score : ", logi_model.score(X_test_m, y_test))
print(metrics.classification_report(y_train, logi_model.predict(X_train_m)))
print(metrics.classification_report(y_test, logi_model.predict(X_test_m)))


# ### SVM : c=0.1, robust

# In[379]:


svm_model = LinearSVC(C=0.1).fit(X_train_r, y_train)

print("train score : ", svm_model.score(X_train_r, y_train))
print("test score : ", svm_model.score(X_test_r, y_test))
print(metrics.classification_report(y_train, svm_model.predict(X_train_r)))
print(metrics.classification_report(y_test, svm_model.predict(X_test_r)))


# ### 결정트리

# In[390]:


tree_model = DecisionTreeClassifier(max_depth=5).fit(X_train, y_train)

print("train score : ", tree_model.score(X_train, y_train))
print("test score : ", tree_model.score(X_test, y_test))
print(metrics.classification_report(y_train, tree_model.predict(X_train)))
print(metrics.classification_report(y_test, tree_model.predict(X_test)))


# ### 랜덤포레스트

# In[391]:


random_model = RandomForestClassifier(n_estimators=300, max_depth=10, random_state=7).fit(X_train, y_train)

print("train score : ", random_model.score(X_train, y_train))
print("test score : ", random_model.score(X_test, y_test))
print(metrics.classification_report(y_train, random_model.predict(X_train)))
print(metrics.classification_report(y_test, random_model.predict(X_test)))


# ### XGBoost

# In[407]:


xgb_model = XGBClassifier(max_depth=5, min_child_weight = 100, gamma=0.1, learning_rate=0.01, random_state=7, seed=1, silent = False).fit(X_train, y_train)

print("train score : ", xgb_model.score(X_train, y_train))
print("test score : ", xgb_model.score(X_test, y_test))
print(metrics.classification_report(y_train, xgb_model.predict(X_train)))
print(metrics.classification_report(y_test, xgb_model.predict(X_test)))


# ### 결정트리를 이용한 특성선택

# In[341]:


fi = xgb_model.feature_importances_
importances = pd.DataFrame(fi,
                          index = X_train.columns)
feature = importances.sort_values(by=0, ascending = False)


# In[342]:


feature = list(feature[feature.iloc[:,0]>0].index)


# In[343]:


X_feature = X[feature]


# In[344]:


X_train, X_test, y_train, y_test = train_test_split(X_feature,
                                                   y_cate,
                                                   test_size = 0.3,
                                                   random_state = 3)


# ### 특성 선택 이후 재학습 : 결과가 크게 다르지 않았음

# In[345]:


xgb_model = XGBClassifier(max_depth=5, min_child_weight = 100, gamma=0.1, learning_rate=0.05, random_state=7, seed=1, silent = False).fit(X_train, y_train)

print("train score : ", xgb_model.score(X_train, y_train))
print("test score : ", xgb_model.score(X_test, y_test))
print(metrics.classification_report(y_train, xgb_model.predict(X_train)))
print(metrics.classification_report(y_test, xgb_model.predict(X_test)))

