# 验证单点登录后台

## 文件夹介绍
security 文件夹中存放验证登录的信息

APIUserDetailsService 是jsp直接登录的时候，使用的。

PreUserDetailsService 是直接调用接口使用的

SpringSecurityConfig 的configure是配置拦截规则的

TokenPreAuthenticationFilter 中的SSO_TOKEN是调用接口时，使用的headers的key。

`默认除了登录接口，其他接口都进行拦截；通过拦截需要带token`

## 接口

### 请求域名:

- http://localhost:19002

#### 返回参数格式说明:

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|success |boolean   |true表示调用成功，false标识调用失败  |
|data |object |成功时返回的对象 |
|errorCode |string |错误码 |
|errorMsg |string |错误原因 |

### 登陆接口

#### 请求URL:

POST:/login

#### 参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|username |是  |string |用户名   |
|password |是  |string | 密码    |

#### 返回示例:

**正确时返回:**

```

{
    "success": true,
    "errorCode": null,
    "errorMsg": null,
    "data": {
        "token": "token_api_d17ee587f3aa9d9de5f4707f48e74ce0"
    }
}
```

**错误时返回:**


```
{
    "success": false,
    "errorCode": "000002",
    "errorMsg": "未知错误",
    "data": null
}
```

#### 返回参数说明:

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|token |string   |有效凭证  |


#### 备注:

- 

### 登出接口

#### 请求URL:

GET:/to/logout

#### header参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|X-API-TOKEN |是  |string |token   |


#### 返回示例:

**正确时返回:**

```
{
    "success": true,
    "errorCode": null,
    "errorMsg": null,
    "data": "登出成功"
}
```

**错误时返回:**


```
{
    "timestamp": 1545358337952,
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource",
    "path": "/to/logout"
}
```

#### 返回参数说明:

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|        |      |      |

#### 备注:

- 

### 新增用户接口

#### 请求URL:

POST:/add

#### header参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|X-API-TOKEN |是  |string |token   |


#### 参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|username |是  |string |用户名   |
|password |是  |string | 密码    |

#### 返回示例:

**正确时返回:**

```
{
    "success": true,
    "errorCode": null,
    "errorMsg": null,
    "data": 1
}
```

**错误时返回:**


```
{
    "timestamp": 1545358337952,
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource",
    "path": "/to/logout"
}
```

#### 返回参数说明:

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|        |      |      |

#### 备注:

- 

### 查询用户信息接口

#### 请求URL:

GET:/cur/user

#### header参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|X-API-TOKEN |是  |string |token   |


#### 参数:

|参数名|是否必须|类型|说明|
|:----    |:---|:----- |-----   |
|username |是  |string |用户名   |
|password |是  |string | 密码    |

#### 返回示例:

**正确时返回:**

```
{
    "success": true,
    "errorCode": null,
    "errorMsg": null,
    "data": {
        "id": "7463ecb6836111e88cba7cd30ae015b0",
        "username": "zhangxb",
        "password": ""
    }
}
```

**错误时返回:**


```
{
    "timestamp": 1545358337952,
    "status": 401,
    "error": "Unauthorized",
    "message": "Full authentication is required to access this resource",
    "path": "/to/logout"
}
```

#### 返回参数说明:

|参数名|类型|说明|
|:-----  |:-----|-----                           |
| id       | string | id           |
| username | string | 用户名       |
| password | string | 密码，返回空 |

#### 备注:

- 



## 测试工具

### postman工具

#### 登陆
![login](doc\login.png)

#### 获取用户信息
![login](doc\login.png)
