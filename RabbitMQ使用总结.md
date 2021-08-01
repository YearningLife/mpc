# RabbitMQ使用总结

## 一. Linux安装

创建目录:
		mkdir -p usr/rabbitmq
	切换目录:
		cd usr/rabbitmq

	安装:
		
		erlang下载路径:    https://packages.erlang-solutions.com/erlang-solutions-2.0-1.noarch.rpm
		rabbitmq:官网
		
		依次执行: 
			rpm -Uvh erlang-solutions-2.0-1.noarch.rpm 
			yum install -y erlang
			erl -v
			yum install -y socat
			rpm -Uvh rabbitmq-server-3.9.1-1.el7.noarch.rpm 
			yum install -y rabbitmq-server
			
	开机自启动
	systemctl enable rabbitmq-server
	停止
	systemctl stop rabbitmq-server
	查看状态
	systemctl status rabbitmq-server
	开启服务
	systemctl start rabbitmq-server
	开启图像化界面
	rabbitmq-plugins enable rabbitmq_management
		默认端口15672,用户名/密码: guest/guest 仅限本机访问
	创建用户
	rabbitmqctl add_user admin admin
	赋权-角色
	rabbitmqctl set_user_tags admin administrator
	具体权限
	rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
## 二.docker安装

1. ### docker升级

   [参考链接1](https://segmentfault.com/a/1190000020073249)

```
#查看centos版本
uname -a
#移除原有镜像
yum remove docker  docker-common docker-selinux docker-engine
#安装插件
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
#安装仓库
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
#安装docker
yum install docker-ce -y
#设置开机自启
systemctl enable docker
#启动docker
systemctl start docker
#查看docker版本
docker -v
#查看版本信息
docker version
#安装阿里云镜像仓库
mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://r0gd3ueb.mirror.aliyuncs.com"]
}
EOF
```

2. ### docker 安装最新版rabbitmq,含报错

   使用命令:

   ```
   docker run -d --name rabbit -e RABBITMQ_DEFAULT_PASS=admin -e RABBITMQ_DEFAULT_USER=admin -p 15672:15672  -p 5672:5672 -p 61613:61613 -p 1883:1883 rabbitmq:3-management
   ```

   ```
   
   ```

   ```
   #看到报错信息
   docker ps -a
   ```

   发现镜像为exit(1) 状态,代表启动失败

   查看日志,报错了: 

   ```
   error: RABBITMQ_DEFAULT_PASS is set but deprecated
   
   error: RABBITMQ_DEFAULT_USER is set but deprecated
   
   error: deprecated environment variables detected
   
   Please use a configuration file instead; visit https://www.rabbitmq.com/configure.html to learn more
   ```

   在[dockerhub官网][https://hub.docker.com/_/rabbitmq] 注意到,一条信息:

   ![报错信息](.\images\image-20210801174940003.png)

点击[配置文件链接](https://www.rabbitmq.com/configure.html) 可以看到:

![命令框](.\images\image-20210801175225012.png)

![查看命令](.\images\image-20210801180556637.png)

进入到这个页面:

查看对应的命令就可以:



![image-20210801180826190](.\images\image-20210801180826190.png)

3. 问题解决,正确做法:

   ```
   #部署
   docker run -d --name rabbit -p 15672:15672  -p 5672:5672 -p 61613:61613 -p 1883:1883 rabbitmq:management
   #查看
   docker ps -a
   #新增用户
   docker container exec -it rabbit01 rabbitmqctl add_user admin 123456
   #角色分配
   docker container exec -it rabbit01 rabbitmqctl set_user_tags admin administrator
   #赋权
   docker container exec -it rabbit01 rabbitmqctl  set_permissions admin  ".*"  ".*"  ".*" 
   ```

   