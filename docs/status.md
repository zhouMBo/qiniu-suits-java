# 资源更新状态

## 简介
对空间中的资源修改状态。参考：[七牛空间资源更新状态](https://developer.qiniu.com/kodo/api/4173/modify-the-file-status)/[批量更新状态](https://developer.qiniu.com/kodo/api/1250/batch)

## 配置文件
**操作通常需要指定数据源，默认表示从七牛空间列举文件执行操作，如非默认或需更多条件，请先[配置数据源](datasource.md)**  

### 可选参数
```
process=status
ak=
sk=
bucket=
indexes=
status=
```  
|参数名|参数值及类型 | 含义|  
|-----|-------|-----|  
|process=status| 更新资源状态时设置为status| 表示更新状态操作|  
|ak、sk|长度40的字符串|七牛账号的ak、sk，通过七牛控制台个人中心获取，当数据源为 qiniu 时无需再设置|  
|bucket| 字符串| 操作的资源原空间，当数据源为 qiniu 时无需再设置|  
|indexes|字符串| 设置输入行中 key 字段的下标（有默认值），参考[数据源 indexes 设置](datasource.md#1-公共参数)|  
|status| 0/1| 设置资源的状态为 type，0表示文件启用，1 表示文件禁用|  

## 命令行方式
```
-process=status -ak= -sk= -bucket= -status=  
```
