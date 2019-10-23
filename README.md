
**请求参数**：

| 参数名称         | 说明     |     in |  是否必须      |  类型   |  schema  |
| ------------ | -------------------------------- |-----------|--------|----|--- |
| name         |      用户名   |     query        |       false      | string   |      |
            | password         |      密码   |     query        |       false      | string   |      |
            




**响应数据**:


```json
{
    "age": 0,
    "name": "",
    "password": "",
    "properties": {
        "basePackage": "",
        "description": "",
        "termsOfServiceUrl": "",
        "title": "",
        "version": ""
    }
}
```



**响应参数说明**:

| 参数名称         | 说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| age     |年龄      |    int32   |       |
            | name     |名字      |    string   |       |
            | password     |密码      |    string   |       |
            | properties     |配置对象      |    string   |   SwaggerProperties    |
            



**schema属性说明**
  
**SwaggerProperties**

| 参数名称         |  说明          |   类型  |  schema |
| ------------ | ------------------|--------|----------- |
| basePackage         |           |  string   |      |
            | description         |           |  string   |      |
            | termsOfServiceUrl         |           |  string   |      |
            | title         |           |  string   |      |
            | version         |           |  string   |      |
            




**响应状态码说明**:

| 状态码         | 说明                             |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200         | OK                        |User                          |
| 201         | Created                        |                          |
| 401         | Unauthorized                        |                          |
| 403         | Forbidden                        |                          |
| 404         | Not Found                        |                          |




# swagger-spring-boot-starter
