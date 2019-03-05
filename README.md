# Getting Started

##### spring boot quartz starter 基于spring boot 1.5.6-RELEASE
````
quartz.enabled=true
````

    
**简要描述：** 

- 新增任务

**请求URL：** 
- ` http://localhost:8081/quartz `
  
**请求方式：**
- POST 

**参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|jobName |是  |string |a   |
|jobGroup |是  |string | a    |
|cronExpression     |是  |string | a    |
|jobDescription     |是  |string |  a   |
 **请求示例**

``` 
  {
	"jobName":"cn.com.servision.lq.sbom.job.TestJob",
	"jobGroup":"testGroup",
	"cronExpression":"* * * * * ?",
	"jobDescription":"c"
}
```



- 修改任务

**请求URL：** 
- ` http://localhost:8081/quartz `
  
**请求方式：**
- PUT 

**参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|jobName |是  |string |   |
|jobGroup |是  |string |     |
|cronExpression     |是  |string |     |
|jobDescription     |是  |string |  -   |
 **请求示例**

``` 
  {
	"jobName":"cn.com.servision.lq.sbom.job.TestJob",
	"jobGroup":"testGroup",
	"cronExpression":"* * * * * ?",
	"jobDescription":"c"
}
```


- 暂停任务

**请求URL：** 
- ` http://localhost:8081/quartz/pause `
  
**请求方式：**
- POST 

**参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|jobName |是  |string |   |
|jobGroup |是  |string |     |

 **请求示例**

``` 
  {
  	"jobName":"cn.com.servision.lq.sbom.job.TestJob",
  	"jobGroup":"testGroup"
  }
```

- 重启任务

**请求URL：** 
- ` http://localhost:8081/quartz/resume `
  
**请求方式：**
- POST 

**参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|jobName |是  |string |   |
|jobGroup |是  |string |     |

 **请求示例**

``` 
  {
  	"jobName":"cn.com.servision.lq.sbom.job.TestJob",
  	"jobGroup":"testGroup"
  }
```


- 立即执行任务

**请求URL：** 
- ` http://localhost:8081/quartz/trigger `
  
**请求方式：**
- POST 

**参数：** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|jobName |是  |string |   |
|jobGroup |是  |string |     |

 **请求示例**

``` 
  {
  	"jobName":"cn.com.servision.lq.sbom.job.TestJob",
  	"jobGroup":"testGroup"
  }
```


- 查询所有

**请求URL：** 
- ` http://localhost:8081/quartz `
  
**请求方式：**
- GET 

**参数：** 

