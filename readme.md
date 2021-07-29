# 一.  日期更新

参考链接--->[mybatis-plus官网](https://baomidou.com/guide/)

1. 通过修改数据库时间, 日期格式为datastamp,默认为CURRENT_DATETIMEP

2. 在程序中修改: 通过new Date方式

3. 在entity实体类上面添加注解

   ![时间更新](E:\ProjectCode\test\mpc\images\image-20210720230217950.png)

时间格式是这样的:

```java
//修改日期
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
// @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@TableField(fill = FieldFill.INSERT_UPDATE) //自动更新时间
@ApiModelProperty(value = "更新时间")
private Date updateDate;
```

```java
public enum FieldFill {
    DEFAULT,  //默认
    INSERT,  //insert操作时更新时间
    UPDATE, //update操作时进行更新时间
    INSERT_UPDATE;// insert或update时都会更新时间

    private FieldFill() {
    }
}
```

4. 官方文档地址:  https://baomidou.com/guide/auto-fill-metainfo.html  参考这个做法

   ![image-20210720230710670](E:\ProjectCode\test\mpc\images\image-20210720230710670.png)

# 二. 使用RestTemplate 调用外部接口



```java
/**
 * 测试查询全部信息
 */
@Test
public void testHttp(){
    //创建RestTemplate对象
    RestTemplate template = new RestTemplate();
    //创建系统头
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    //参数赋值,非必输
    Map<String, Integer> params = new HashMap<String, Integer>();
    params.put("pageNum",1);
    params.put("pageCnt",10);
    //创建请求实体
    HttpEntity<String> entity = new HttpEntity<String>(JSONObject.toJSONString(params), headers);
    //发送post请求
    ResponseEntity<String> stringResponseEntity = template.postForEntity("HTTP地址:端口号/apis/v1/auditlogs/events", entity, String.class);
    //获取响应值
    String body = stringResponseEntity.getBody();
    //转换为json对象
    JSONObject parseObject = JSON.parseObject(body);
    //获取对应值
    String desc = parseObject.getString("desc");
    JSONObject data = parseObject.getJSONObject("data");
    System.out.println("转换为json对象------data:"+data);
}
```

# 三. 过滤器/拦截器中使用配置yaml文件中的数据,并调用接口记录日志    

点击 --->[参考链接](https://blog.csdn.net/qq_32921327/article/details/113996791)

## step1	在yaml中写入

```yaml
#审计日志
loginfo:
  logAuditlogsUrl: ip-address:port/apis/v1/auditlogs
  logQueryAllUrl: ip-address:port/apis/v1/auditlogs/events
  logQueryByEventsUrl: ip-address:port/apis/v1/auditlogs/
```

## step2	 通过实体获取

```java
package com.alimama.test.require.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description: TODO 审计日志
 * @version: 1.0
 */
@Component
@ConfigurationProperties(prefix = "loginfo" ) // yml中的前缀
@Primary
public class LogUrlConfig {

    private String logAuditlogsUrl;

    private String logQueryAllUrl;

    private String logQueryByEventsUrl;

    public String getLogAuditlogsUrl() {
        return logAuditlogsUrl;
    }

    public void setLogAuditlogsUrl(String logAuditlogsUrl) {
        this.logAuditlogsUrl = logAuditlogsUrl;
    }

    public String getLogQueryAllUrl() {
        return logQueryAllUrl;
    }

    public void setLogQueryAllUrl(String logQueryAllUrl) {
        this.logQueryAllUrl = logQueryAllUrl;
    }

    public String getLogQueryByEventsUrl() {
        return logQueryByEventsUrl;
    }

    public void setLogQueryByEventsUrl(String logQueryByEventsUrl) {
        this.logQueryByEventsUrl = logQueryByEventsUrl;
    }
}
```

## step3. 创建拦截器

```java
package com.alimama.test.requirementmarket;

import com.alibaba.fastjson.JSONObject;
import com.alimama.test.require.util.LogUrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: TODO 审计日志拦截器
 * @date: 2021/7/27
 * @version: 1.0
 */
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    //获取链接
    private LogUrlConfig logUrlConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        RestTemplate template = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //赋值
        Map<String,Object> params = new HashMap<String,Object>();
        //用户ID
        params.put("tenantId",request.getHeader("userId"));
        //操作的返回body体
        params.put("responseBody",JSONObject.toJSONString(request.getAttribute("responseData")));
        //封装请求内容
        HttpEntity<String> httpEntity = new HttpEntity<>(JSONObject.toJSONString(params), httpHeaders);
        //1. 获取yaml中的内容 2.发送post请求内容
        ResponseEntity<String> entity = template.postForEntity(logUrlConfig.getLogAuditlogsUrl(), httpEntity, String.class);
        //获取响应内容
        String responseBody = entity.getBody();
        System.out.println("添加数据后的返回对象为==================\n"+responseBody);
    }
}
```

## step4. 声明拦截器

```java
package com.alimama.test.requirementmarket.config;

import com.alimama.test.requirementmarket.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: TODO 声明拦截器
 * @version: 1.0
 */
@Configuration
public class ConfigurerAdapter implements WebMvcConfigurer {


    @Bean
    //声明之后,拦截器才可获取参数
    public HandlerInterceptor getLogInterceptor(){
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLogInterceptor()).addPathPatterns("/**");
    }
}
```