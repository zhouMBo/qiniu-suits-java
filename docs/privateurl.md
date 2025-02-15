# 私有空间资源签名

## 简介
对私有存储空间中的资源进行签名。支持：  
- [七牛](#七牛配置参数)参考：[私有空间资源签名](https://developer.qiniu.com/kodo/manual/1656/download-private)  
- [腾讯](#七牛配置参数)参考：[私有空间资源签名](https://cloud.tencent.com/document/product/436/35217)  
- [阿里](#七牛配置参数)参考：[私有空间资源签名](https://help.aliyun.com/document_detail/31857.html)  
- [AWS](#七牛配置参数)参考：[私有空间资源签名](https://docs.aws.amazon.com/zh_cn/general/latest/gr/signing_aws_api_requests.html#rest-and-query-requests)  

## 配置文件
**操作需指定数据源，请先[配置数据源](datasource.md)**  

### 七牛配置参数
```
process=privateurl
ak=
sk=
domain=
protocol=
indexes=
url-index=
expires=
queries=
```  
|参数名|参数值及类型 | 含义|  
|-----|-------|-----|  
|process| 私有资源签名时设置为privateurl | 表示私有资源生成签名链接操作|  
|ak、sk|长度40的字符串|七牛账号的ak、sk，通过七牛控制台个人中心获取，当数据源为 qiniu 时无需再设置|  
|domain| 域名字符串| 用于拼接文件名生成链接的域名，当指定 url-index 时无需设置|  
|protocol| http/https| 使用 http 还是 https 访问资源进行抓取（默认 http）|  
|indexes|字符串| 设置输入行中 key 字段的下标（有默认值），参考[数据源 indexes 设置](datasource.md#1-公共参数)|  
|url-index| 字符串| 通过 url 操作时需要设置的 url 索引（下标），需要手动指定才会进行解析|  
|expires| 整型| url 签名的过期时间，单位 s，默认 3600s|  
|queries| 字符串| url 的 query 参数或样式后缀，如 `?v=1.1&time=1565171107845` 或 `-w480`，[关于 queries 参数](#关于-queries-参数)|  

#### 关于 url-index
当 parse=tab/csv 时 [xx-]index(ex) 设置的下标必须为整数。url-index 表示输入行中存在 url 形式的源文件地址，未设置的情况下则默认从 key 字段
加上 domain 的方式访问源文件地址，key 下标用 indexes 参数设置。  

### 其他存储配置参数
```
process=tenprivate/aliprivate/awsprivate
<密钥配置>
region=
bucket=
expires=
queries=
``` 
|参数名|参数值及类型 | 含义|  
|-----|-------|-----|  
|process| 私有资源签名 process name | 表示私有资源生成签名链接操作|  
|bucket|长度40的字符串|七牛账号的ak、sk，通过七牛控制台个人中心获取，当数据源为 qiniu 时无需再设置|  
|expires| 整型| url 签名的过期时间，单位 s，默认 3600s|  
|queries| 字符串| url 的 query 参数或样式后缀，如 `?v=1.1&time=1565171107845`，[关于 queries 参数](#关于-queries-参数)|  

|其他存储|             密钥和 region 字段         |                  对应关系和描述                |  
|------|---------------------------------------|---------------------------------------------|  
|tencent|`ten-id=`<br>`ten-secret=`<br>`region=ap-beijing/...`| 密钥对应腾讯云账号的 SecretId 和 SecretKey<br>region使用简称(可不设置)，参考[腾讯 Region](https://cloud.tencent.com/document/product/436/6224)|  
|aliyun|`ali-id=`<br>`ali-secret=`<br>`region=oss-cn-hangzhou/...`| 密钥对应阿里云账号的 AccessKeyId 和 AccessKeySecret<br>region使用简称(可不设置)，参考[阿里 Region](https://help.aliyun.com/document_detail/31837.html)|  
|aws/s3|`s3-id=`<br>`s3-secret=`<br>`region=ap-east-1/...`| 密钥对应 aws/s3 api 账号的 AccessKeyId 和 SecretKey<br>region使用简称(可不设置)，参考[AWS Region](https://docs.aws.amazon.com/zh_cn/general/latest/gr/rande.html)|  

### 关于 queries 参数
queries 参数用于设置 ?+参数部分（或 url 的后缀），希望在私有签名之前加上参数。  

## 命令行方式
```
-process=avinfo -ak= -sk= -domain= -protocol= 
```