# 资源异步抓取

## 简介
下载文件列表的资源到本地。  

## 配置文件
**操作需指定数据源，请先[配置数据源](datasource.md)**  

### 功能配置参数
```
process=download
domain=
protocol=
indexes=
url-index=
queries=
add-prefix=
rm-prefix=
host=
pre-down=
download-timeout=
```  
|参数名|参数值及类型 | 含义|  
|-----|-------|-----|  
|process| 下载资源时设置为download | 表示资源下载操作|  
|domain| 域名字符串| 当数据源数据的资源为文件名列表时，需要设置进行访问的域名，当指定 url-index 时无需设置|  
|protocol| http/https| 使用 http 还是 https 访问资源进行下载（默认 http）|  
|indexes|字符串| 设置输入行中 key 字段的下标（有默认值），参考[数据源 indexes 设置](datasource.md#1-公共参数)|  
|url-index| 字符串| 通过 url 操作时需要设置的 [url 索引（下标）](#关于-url-index)，需要手动指定才会进行解析，支持[需要私有签名的情况](#url-需要私有签名访问)|  
|queries| 字符串| url 的 query 参数或样式后缀，如 `-w480` 或 `?v=1.1&time=1565171107845`（这种形式请务必带上 ? 号，否则无效）[关于 queries 参数](#关于-queries-参数)|  
|add-prefix| 字符串| 表示为保存的文件名添加指定前缀|  
|rm-prefix| 字符串| 表示将得到的目标文件名去除存在的指定前缀后再作为保存的文件名|  
|host| 域名字符串| 下载源资源时指定 host|  
|pre-down| true/false|为 true 时表示预下载，即下载的内容不保存为文件，为 false 表示保存成本地文件，默认为 false|  
|download-timeout| 时间，单位秒|设置下载文件的超时时间，默认 1200s，下载大文件可根据需要调整|  

### 关于 queries 参数
queries 参数用于设置 url 的后缀或 ?+参数部分，资源下载可能需要下载不同格式或尺寸的如图片文件，因此可以通过一些图片处理样式或参数来设置对处理之后的
图片进行下载。当设置 private（私有签名）的情况下，该参数会使用在 privateurl 操作中（因为 privateurl 操作在前，当前操作在后）。  

### 关于 url-index
当使用 file 源且 parse=tab/csv 时 [xx-]index(ex) 设置的下标必须为整数。url-index 表示输入行含 url 形式的源文件地址，未设置的情况下则使用 
key 字段加上 domain 的方式访问源文件地址，key 下标用 indexes 参数设置。  

### url 需要私有签名访问
当进行图片审核的 url 需要通过私有鉴权访问时（资源来自于存储私有权限的空间），本工具支持串联操作，即先进行对应的私有签名再提交审核，使用 private 参
数设置，如不需要进行私有访问则不设置，目前支持四类签名：
`private=qiniu` [七牛云私有签名](privateurl.md#七牛配置参数)  
`private=tencent` [腾讯云私有签名](privateurl.md#其他存储配置参数)  
`private=aliyun` [阿里云私有签名](privateurl.md#其他存储配置参数)  
`private=s3` [AWS S3 私有签名](privateurl.md#其他存储配置参数)  

## 命令行参数方式
```
-process=download -ak= -sk= -to-bucket= -add-prefix= -domain= -protocol= -host= ...
```

