# Nginx

## 1. 安装

https://www.jianshu.com/p/830eaceee167

https://blog.csdn.net/weixin_45766506/article/details/120670094

https://blog.51cto.com/u_14175560/6245704

```bash
# 安装
cd nginx-1.22.1
./configure --prefix=/usr/local/nginx
make && make install
```

```bash
# 启动 nginx
/usr/local/nginx/sbin/nginx
# 查看进程
ps -ef | grep nginx
```

解决nginx 启动报错nginx: [emerg] open() "/var/run/nginx/nginx.pid" failed (2: No suchfile or directory)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091551136.png)

## 2. Nginx 命令行参数

https://nginx.org/en/docs/switches.html

```bash
#启动nginx的命令为：
/usr/local/nginx/sbin/nginx
#带配置文件的启动：
/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf
#停止nginx的命令为 ：
/usr/local/nginx/sbin/nginx -s stop
#重启nginx的命令为：
/usr/local/nginx/sbin/nginx -s reload
# 查看版本
/usr/local/nginx/sbin/nginx -v
#查看版本、配置参数
/usr/local/nginx/sbin/nginx -V
```

## 3. nginx.conf 配置文件

1. Nginx的配置文件位置

   ```bash
   安装目录\conf\nginx.conf # 使用/usr/local/nginx/sbin/nginx 启动Nginx ，默认用的是安装目录\nginx.conf 配置文件
   ```

2. Nginx.conf 组成

   - 全局块
   - events 块
   - http 块

3. 详细文档

   ```bash
    
   #Nginx用户及组：用户 组。window下不指定
   #user  nobody;
    
   #工作进程：数目。根据硬件调整，通常等于CPU数量或者2倍于CPU。
   worker_processes  1;
    
   #错误日志：存放路径。
   #error_log  logs/error.log;
   #error_log  logs/error.log  notice;
   #error_log  logs/error.log  info;
    
   #pid(进程标识符)：存放路径
   pid       /usr/local/nginx/logs/nginx.pid;
    
   #一个进程能打开的文件描述符最大值，理论上该值因该是最多能打开的文件数除以进程数。
   #但是由于nginx负载并不是完全均衡的，所以这个值最好等于最多能打开的文件数。
   #LINUX系统可以执行 sysctl -a | grep fs.file 可以看到linux文件描述符。
   worker_rlimit_nofile 65535;
    
    
   events {
   	#使用epoll的I/O 模型。linux建议epoll，FreeBSD建议采用kqueue，window下不指定。
   	use epoll;
   	
   	#单个进程最大连接数（最大连接数=连接数*进程数）
       worker_connections  1024;
   	
   	#客户端请求头部的缓冲区大小。这个可以根据你的系统分页大小来设置，
   	#一般一个请求头的大小不会超过1k，不过由于一般系统分页都要大于1k，所以这里设置为分页大小。
   	#client_header_buffer_size 4k;
   }
    
    
   http {
   	#设定mime类型,类型由mime.type文件定义
       include       mime.types;
   	
       default_type  application/octet-stream;
   	
   	#日志格式设置
       #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
       #                  '$status $body_bytes_sent "$http_referer" '
       #                  '"$http_user_agent" "$http_x_forwarded_for"';
   	
   		
   	#用了log_format指令设置了日志格式之后，需要用access_log指令指定日志文件的存放路径
   	#记录了哪些用户，哪些页面以及用户浏览器、ip和其他的访问信息
   	#access_log  logs/host.access.log  main;
   	#access_log  logs/host.access.404.log  log404;
   	
   	#服务器名字的hash表大小
       server_names_hash_bucket_size 128;
   	
   	#客户端请求头缓冲大小。
       #nginx默认会用client_header_buffer_size这个buffer来读取header值，
       #如果header过大，它会使用large_client_header_buffers来读取。
       #如果设置过小HTTP头/Cookie过大 会报400 错误 nginx 400 bad request
       #如果超过buffer，就会报HTTP 414错误(URI Too Long)
       #nginx接受最长的HTTP头部大小必须比其中一个buffer大
       #否则就会报400的HTTP错误(Bad Request)
       #client_header_buffer_size 32k;
       #large_client_header_buffers 4 32k;
   	
   	
   	#隐藏ngnix版本号
       #server_tokens off;
   	
   	#忽略不合法的请求头
       #ignore_invalid_headers   on;
   	
   	#让 nginx 在处理自己内部重定向时不默认使用  server_name设置中的第一个域名
       #server_name_in_redirect off;
   	
   	
   	#客户端请求体的大小
       #client_body_buffer_size    8m;
      
     
       #开启文件传输，一般应用都应设置为on；若是有下载的应用，则可以设置成off来平衡网络I/O和磁盘的I/O来降低系统负载
       sendfile        on;
   	
   	
   	#告诉nginx在一个数据包里发送所有头文件，而不一个接一个的发送。
       #tcp_nopush     on;
   	
   	#tcp_nodelay off 会增加通信的延时，但是会提高带宽利用率。在高延时、数据量大的通信场景中应该会有不错的效果
       #tcp_nodelay on，会增加小包的数量，但是可以提高响应速度。在及时性高的通信场景中应该会有不错的效果
   	tcp_nodelay on;
   	
    
       #长连接超时时间，单位是秒
       keepalive_timeout  65;
    
       #gzip模块设置，使用 gzip 压缩可以降低网站带宽消耗，同时提升访问速度。
       #gzip  on;                     #开启gzip
       #gzip_min_length  1k;          #最小压缩大小
       #gzip_buffers     4 16k;       #压缩缓冲区
       #gzip_http_version 1.0;        #压缩版本
       #gzip_comp_level 2;            #压缩等级
       #gzip_types   text/plain text/css text/xml text/javascript application/json application/x-javascript application/xml application/xml+rss;#压缩类型
       
   	
   	
   	
       #负载均衡
   	#max_fails为允许请求失败的次数，默认为1
   	#weight为轮询权重，根据不同的权重分配可以用来平衡服务器的访问率。
       # upstream myServer{
       #   server  192.168.247.129:8080 max_fails=3 weight=2;
       #   server  192.168.247.129:8081 max_fails=3 weight=4;	
       # }
   	
   	
   	
       #server {
       #    listen       80;
   	#	
   	#	#IP/域名可以有多个，用空格隔开
   	#	server_name  192.168.247.129;
   	#	#server_name  www.test.com;
   	#
       #    #charset koi8-r;
   	#
       #    #access_log  logs/host.access.log  main;
   	#	
   	#   #反向代理配置，
       #   #将所有请求为www.test.com的请求全部转发到upstream中定义的目标服务器中。
       #   location / {
   	#   			
   	#	    #此处配置的域名必须与upstream的域名一致，才能转发。
   	#	    proxy_pass http://myServer;
   	#	    #proxy_pass http://192.168.247.129:8080;
   	#		
   	#		 proxy_connect_timeout 20;          #nginx跟后端服务器连接超时时间(代理连接超时)
   	#		
       #        #client_max_body_size       10m;   #允许客户端请求的最大单文件字节数
       #        #client_body_buffer_size    128k;  #缓冲区代理缓冲用户端请求的最大字节数
   	#		 #proxy_send_timeout         300;   #后端服务器数据回传时间(代理发送超时)
       #        #proxy_read_timeout         300;   #连接成功后，后端服务器响应时间(代理接收超时)
       #        #proxy_buffer_size          4k;    #设置代理服务器（nginx）保存用户头信息的缓冲区大小
       #        #proxy_buffers              4 32k; #proxy_buffers缓冲区，网页平均在32k以下的话，这样设置
       #        #proxy_busy_buffers_size    64k;   #高负荷下缓冲大小（proxy_buffers*2）
       #        #proxy_temp_file_write_size 64k;   #设定缓存文件夹大小，大于这个值，将从upstream服务器传    		
   	#		
   	#		root   html;
   	#		
   	#		#定义首页索引文件的名称
   	#		index  index.html index.htm;
       #    }
   	#
       #   #动静分离 静态资源走linux 动态资源走tomcat
       #   # 注意 /source/image/下面寻找资源
       #   location /image/ {
       #       root /source/;
   	#       autoindex on;
       #   } 		
   	#
   	#
   	#    # 出现50x错误时，使用/50x.html页返回给客户端
       #    error_page   500 502 503 504  /50x.html;
       #    location = /50x.html {
       #        root   html;
       #    }
       #}
   		
   		
   		
   	#下面是配置生产环境中既支持HTTP又支持HTTPS,保证用户在浏览器中输入HTTP也能正常访问
   	
   	# SSL证书 配置                                 
   	ssl_certificate     	cert/yphtoy.com.pem;   #加密证书路径
   	ssl_certificate_key	cert/yphtoy.com.key;       #加密私钥路径
   	ssl_protocols		TLSv1 TLSv1.1 TLSv1.2;     #加密协议
   	ssl_session_cache	shared:SSL:1m;             #加密访问缓存设置,可以大大提高访问速度
   	ssl_session_timeout	10m;                       #加密访问缓存过期时间
   	ssl_ciphers		HIGH:!aNULL:!MD5;              #加密算法
   	ssl_prefer_server_ciphers on;	               #是否由服务器决定采用哪种加密算法
   	
   	# 负载均衡
   	upstream api_upstream
   	{
   	    server 127.0.0.1:8080 max_fails=3 weight=1;
   		server 127.0.0.1:8081 max_fails=3 weight=1;
   	}
   	
   	#api 接口(兼容HTTP)
   	server{
   	    listen 80;
   		server_name api.test.com;
   		# 301重定向跳转到HTTPS接口
   		return 301 https://$server_name$request_uri;
   		error_page   500 502 503 504  /50x.html;
           location = /50x.html {
               root   html;
           }
   	}
   	
   	#api 接口(兼容HTTPS)
   	server{
   	    listen 443 ssl;
   		server_name api.test.com;
   		location / {
   		   root html;
   		   index  index.html index.htm;
   		   proxy_pass http://api_upstream;
   		   
   		   #语法： proxy_cookie_path oldpath replacepath;
   		   #oldpath就是你要替换的路径 replacepath 就是要替换的值
   		   #作用：同一个web服务器下面多个应用之间能获取到cookie
   		   proxy_cookie_path /api/ /;
   		   
   		   #服务端接收的请求头Cooke值不变
   		   proxy_set_header Cookie $http_cookie;
   		}
   	}
   	
   	#管理后台端(兼容HTTP)
   	server{
   	    listen 80;
   		server_name manage.test.com;
   		# 301重定向跳转到HTTPS接口
   		return 301 https://$server_name/$request_uri;
   		error_page 500 502 503 504 /50x.html;
   		location = /50x.html{
   			 root html	
   		}
   	}
   	
   	#管理后台端(兼容HTTPS)
   	server{
   	    listen 443 ssl;
   		server_name manage.test.com;
   		location / {
   		    root /home/test/web/dist
   			
   			index /index.html;
   			
   			
   			#语法：try_files 【$uri】 【 $uri/】 【参数】
   			#当用户请求https://manage.test.com/login时，
   			#一.如果配置了上面的默认index,会依次请求
   			#1./home/test/web/dist/login       查找有没有login这个文件，没有的话
   			#2./home/test/web/dist/index.html  有就直接返回
   			
   			#二.如果没有配置了上面的默认index或者配置了没有找到对应的资源,会依次请求
   			#1./home/test/web/dist/login        查找有没有login这个文件，没有的话
   			#2./home/test/web/dist/login/       查找有没有login这个目录，没有的话
   		    #3.请求https://manage.test.com/index.html  nginx内部做了一个子请求
   			
   			#三.总的来说,index的优先级比try_files高,请求会先去找index配置,这里最后一个参数必须存在
   			try_files $uri $uri/ /index.html;	
   			
   			
   			
   			#解决跨域问题
               #允许跨域请求地址(*表示全部,但是无法满足带cookie请求,因为cookie只能在当前域请求)
               add_header Access-Control-Allow-Origin $http_origin;
               #允许接收cookie和发送cookie
               add_header Access-Control-Allow-Credentials 'true';
               #允许请求的方法
               add_header Access-Control-Allow-Methods 'GET,POST,DELETE,PUT,OPTIONS';
               #允许请求头（Content-Type:请求数据/媒体类型 x-requested-with:判断请求是异步还是同步 自定义header 比如 token）
               add_header Access-Control-Allow-Headers $http_access_control_request_headers;
               #浏览器缓存请求头信息,1800秒内,只会有1次请求，不会出现"OPTIONS"预请求,节约资源
               #add_header Access-Control-Max-Age '1800';
   		    if ($request_method = 'OPTIONS') {
                       return 204;
               }
   			
   			
   			#服务端HttpServletRequest可以获得用户的真实ip
   		    proxy_set_header X-Real-IP $remote_addr;
   			
   			#服务端HttpServletRequest可以获得用户的真实ip和经过的每一层代理服务器的ip
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
   			
   			#服务端接收的请求头Host值不变
               proxy_set_header Host  $http_host;
   			
               proxy_set_header X-Nginx-Proxy true;
   		}
   	}
   }
   ```

4. nginx.conf 讲解

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091603538.png)

我们来看下 /usr/local/nginx/conf/nginx.conf

```bash
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}
}
```

### 3.1 全局块

1. 从配置文件开始到events 块之间的内容
2. 主要会设置一些影响nginx 服务器整体运行的配置指令，主要包括配置运行Nginx服务器的用户(组)、允许生成的worker process 数，进程PID 存放路径、日志存放路径和类型以及配置文件的引入等

分析：

这是Nginx 服务器并发处理服务的关键配置，worker_processes 值越大，可以支持的并发处理量也越多，但是会受到硬件、软件等设备的制约

```bash
# 配置举例
worker_processes 1;
```

### 3.2  events 块

1. events 块涉及的指令主要影响Nginx 服务器与用户的网络连接
2. 常用的设置包括是否开启对多work process 下的网络连接进行序列化，是否允许同时接收多个网络连接，选取哪种事件驱动模型来处理连接请求，每个work process 可以同时支持的最大连接数等

分析：上述例子就表示每个work process 支持的最大连接数为1024, 这部分的配置对Nginx 的性能影响较大，在实际中应根据实际情况配置

```bash
# 配置举例
events {
  worker_connections 1024;
}
```

### 3.3 http块（⭐）

1. 这是Nginx 服务器配置中最复杂的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里
2. http 块也可以包括http 全局块、server 块

#### 1. http 全局块

http 全局块配置的指令包括文件引入、MIME-TYPE 定义、日志自定义、连接超时时间、单连接请求数上限等

```bash
 http {
   include mime.types;
   default_type application/octet-stream;
   #开启文件传输
   sendfile on;
   #tcp_nopush on;
   #keepalive_timeout 0;
   keepalive_timeout 65;
 }
```

#### 2. server 块

1. 这块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的，该技术的产生是为了节省互联网服务器硬件成本。
2. 每个http 块可以包括多个server 块，而每个server 块就相当于一个虚拟主机。
3. 每个server 块也分为全局server 块，以及可以同时包含多个location 块
4. 小结: 这块的主要作用是基于Nginx 服务器接收到的请求字符串（ 例如server_name/uri-string），对虚拟主机名称(也可以是IP 别名) 之外的字符串（例如前面的/uri-string）进行匹配，对特定的请求进行处理。比如地址定向、数据缓存和应答控制等功能，还有许多第三方模块的配置也在这里进行。

##### 2.1 全局 server 块

最常见的配置是本虚拟机主机的监听配置和本虚拟主机的名称或IP 配置。

##### 2.2 location 块

一个server 块可以配置多个location 块

```bash
 server {
     listen 80;
     server_name localhost;
     #charset koi8-r;
     #access_log logs/host.access.log main;
     location / {
         root html;
         index index.html index.htm;
     }
     #error_page 404 /404.html;
     # redirect server error pages to the static page /50x.html
     #
     error_page 500 502 503 504 /50x.html;
     location = /50x.html {
         root html;
     }
 }
```

## 4. 反向代理快速入门

需求说明

1. 在浏览器输入www.llq.com (windows), 可以访问到tomcat
2. 使用Nginx 反向代理功能, 完成需求

![image-20230709164134316](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091641396.png)

思路分析：

![image-20230709164157201](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091641262.png)

配置本机 hosts

```bash
# Nginx服务器 ip
192.168.8.228 www.llq.com	
```

![image-20230709165136081](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091651153.png)

![image-20230709165149533](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091651605.png)

小技巧：如何查看 nginx.conf 的配置错误

```bash
# 指定检测配置文件
./sbin/nginx -t -c
```

![image-20230709165344453](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091653502.png)

![image-20230709181823598](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307091818672.png)

干，之前开了梯子，总是跑到外网去了

![image-20230710113824993](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307101138123.png)

## 5. 反向代理配置-Location 实例

```bash
# 请求一个商品页面
www.aclq.com:10000/product/hi.html
# 访问会员服务页面
www.aclq.com:10000/member/hi.html
```

![image-20230710151714841](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307101517932.png)

### 5.1 Location 语法规则

语法规则： location 【= | ~| ~* | ^~】 /uri/ {… }

首先匹配 =，其次匹配^~,其次是按文件中顺序的 <font color="yellow">**正则匹配**</font>，最后是交给 / 通用匹配。当有匹配成功时候，停止匹配，按当前匹配规则处理请求。

| 符号                  | 含义                                                         |
| --------------------- | ------------------------------------------------------------ |
| =                     | = 开头表示精确匹配                                           |
| ^~                    | ^~开头表示uri以某个常规字符串开头，理解为匹配 url路径即可。nginx不对url做编码，因此请求为/static/20%/aa，可以被规则^~ /static/ /aa匹配到（注意是空格） |
| ~                     | ~ 开头表示 <font color="red">**区分大小写** </font>的正则匹配 |
| ~*                    | ~* 开头表示不区分大小写的正则匹配                            |
| !~和!~*               | !~和!~*分别为区分大小写不匹配及不区分大小写不匹配的正则      |
| /                     | 用户所使用的代理（一般为浏览器）                             |
| $http_x_forwarded_for | 可以记录客户端IP，通过代理服务器来记录客户端的ip地址         |
| $http_referer         | 可以记录用户是从哪个链接访问过来的                           |

匹配规则示例：

```bash
location = / {
	#规则A
}

location = /login {
	#规则B
}

location ^~ /static/ {
	#规则C
}

location ~ \.(gif|jpg|png|js|css)$ {
	#规则D
}

location ~* \.(gif|jpg|png|js|css)$ {
	#规则E
}

location !~ \.xhtml$ {
	#规则F
}

location !~* \.xhtml$ {
	#规则G
}

location / {
	#规则H
}
```

那么产生的效果如下：

1. 访问根目录/，比如 http://localhost/ 将匹配规则A 【保证已经和 server 的 server_name 匹配上了】
2. 访问 http://localhost/login 将匹配规则B，http://localhost/register 则匹配规则H
3. 访问 http://localhost/static/a.html 将匹配规则C
4. 访问 http://localhost/a.gif,http://localhost/b.jpg 将匹配规则D和规则E，但是规则D顺序优先，规则E不起作用，而 http://localhost/static/c.png 则优先匹配到规则C
5. 访问 http://localhost/a.PNG 则匹配规则E，而不会匹配规则D，因为规则E不区分大小写。
6. 访问 http://localhost/a.xhtml 不会匹配规则F和规则G，http://localhost/a.XHTML不会匹配规则G，因为不区分大小写。规则F，规则G属于排除法，符合匹配规则但是不会匹配到，所以想想看实际应用中哪里会用到。
7. 访问 http://localhost/category/id/1111 则最终匹配到规则H，因为以上规则都不匹配，这个时候应该是nginx转发请求给后端应用服务器，比如FastCGI（[PHP](http://lib.csdn.net/base/php)），tomcat（jsp），nginx作为反向代理服务器存在。

### 5.2 实际常用规则【^~ 优先级高于正则】

#### 1. 首页

直接匹配网站根目录，通过域名访问网站首页比较频繁，使用这个会加速处理。

这里是直接转发给后端应用服务器了，也可以是一个静态首页

```bash
# 第一个必选规则
location = / {
	proxy_pass http://tomcat:8080/index
}
```

#### 2. 静态文件请求

第二个必选规则是处理静态文件请求，这是nginx作为http服务器的强项

```bash
# 第二个必须规则是处理静态文件请求，这是 nginx 作为 http 服务器的强项
# 有 2 种配置模式，目录匹配或后缀匹配，任选其一或搭配使用

# 方式1：
location ^~ /static/ {
       # 请求/static/a.txt 将被映射到实际目录文件:/webroot/res/static/a.txt
       root /webroot/res/;
}

# 方式2: 不区分大小写【后缀】
location ~* \.(gif|jpg|jpeg|png|css|js|html|ico)${	
       root /webroot/res/;
}
```

#### 3. 转发动态请求到后端应用服务器【通用配置】

第三个规则就是通用规则，用来转发动态请求到后端应用服务器

非静态文件请求就默认是动态请求，自己根据实际把握

毕竟目前的一些框架的流行，带.php,.jsp后缀的情况很少了 

```bash
#	第 3 个 必选规则：通用规则，用来转发动态请求到后端应用服务器
location / {
	proxy_pass http://tomcat:8080/
}
```

### 5.3 nginx 的 location 解析过程

[nginx的location解析过程介绍 (huati365.com)](https://blog.huati365.com/89af5ae5a56d1b96)

![image-20230710154045699](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307101540787.png)

流程梳理：

1.  先判断精准命中，如果命中，立即返回结果并结束解析过程
2. 判断普通命中，如果有多个命中，“记录”下来“最长”的命中结果（记录但不结束，最长的为准）[一会还要梳理]。
3. 继续判断正则表达式的解析结果，按配置里的正则表达式顺序为准，由上至下开始匹配，一旦匹配成功1个，立即返回结果，并结束解析过程。
4. 普通命中顺序无所谓，是因为按命中的长短来确定。正则命中，顺序有所谓，因为是从前往后命中的。

实战：简化一下

- 只配置 2个服务
- Wiindows一个，LInux一个

修改 nginx.conf 配置文件

![image-20230711210242268](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307112102338.png)

我们再去新增一个 Server 块

```bash
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

pid        /usr/local/nginx/logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen   80;
        server_name 192.168.8.230;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
	    proxy_pass http://127.0.0.1:8080;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }
    
     server {
        listen   10000;
        server_name 192.168.8.230;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location ~ /product/ {	# 区分大小写（~ 和 / 之间有空格）
	    proxy_pass http://127.0.0.1:8080;
        }
        
         location ~ /member/ {	# 转发到 windows的 tomcat的 8080端口
	    proxy_pass http://192.168.8.238:8080;
        } 

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }
    

    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}
}
```



在Linux 的Tomcat 创建webapps\product\hi.html

在windows 的Tomcat 创建webapps\member\hi.html

![image-20230711202851098](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307112028824.png)



现在测试下：【关掉梯子，要不会返回 502，机制就是找不到的话，就用梯子去找了】

可以看到可以直接找到 linux 下的 tomcat

![image-20230711204449836](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307112044926.png)

同样也能找到 Windows 本地的服务

![image-20230711205712090](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307112057159.png)

## 6. 负载均衡（upstream）

![image-20230712004701042](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120047149.png)

分析示意图：

![image-20230712005648528](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120056653.png)

### 1. 配置规则

1. 负载均衡就是将负载分摊到不同的服务单元，既保证服务的可用性，又保证响应足够快

2. linux 下有Nginx、LVS、Haproxy 等等服务可以提供负载均衡服务， Nginx 提供了几种分配方式(策略)：

   - 轮询(默认)

     每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down 掉，能自动剔除

   - weight【认为哪个服务器应该多负载一点】

     weight 代表权,重默认为1,权重越高被分配的客户端越多

     指定轮询几率，weight 和访问比率成正比，用于后端服务器性能不均的情况。例如

     ```bash
     upstream hspservers{	# 上游服务
     server 192.168.12.134:8080 weight=1;
     server 192.168.12.134:8081 weight=2;
     }
     ```

3. ip_hash

   每个请求按访问ip 的hash 结果分配，这样每个访客固定访问一个后端服务器，可以解决session 的问题。

   ```bash
   upstream hspservers{
   ip_hash;
   server 192.168.12.134:8081;
   server 192.168.12.134:8080;
   }
   ```

4. fair(第三方)

   按后端服务器的响应时间来分配请求，响应时间短的优先分配

   ```bash
   upstream hspservers{
   server 192.168.12.134:8080;
   server 192.168.12.134:8081;
   fair;
   }
   ```


### 2. 实现步骤

配置需要进行均衡的服务器地址 + 端口【配置在 http 全局块】

![image-20230712084714280](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120847944.png)

![image-20230712085936005](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120859099.png)

```bash
# 复制当前文件夹里面的东西 到另外一个文件夹
cp -rf pathA pathB
# 再去修改配置文件
Connector prot=8081
redirectPort=8445
shoutdown=8055
```

测试下：

![image-20230712093537191](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120935263.png)

![image-20230712093546725](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307120935793.png)

### 3. 注意事项和避免踩坑

1. nginx.conf 的upstream 不能带下划线, 否则会失败, 但是 <font color="yellow">语法检测不到</font>
2. 如果你的浏览器是无痕上网, 负载均衡可能失效, 因为Nginx 无法采集到相关信息, 老师就遇到这个情况. 改用其它浏览器即可(比如chrome)
3. 老韩提示: 如果某tomcat 没有监听对应端口, 说明启动失败了, 可以尝试先执行shutdown.sh 再执行startup.sh 解决

 ### 4. 几个小实现-多测试

基本介绍

- Nginx 是一个反向代理软件，大部分的网站都采用Nginx 作为网站/平台的服务器软件。Nginx 除了可以直接作为web 服务器使用外，更多的情况是通过反向代理将请求转发给上游服务器
- 配置上游服务器可以使用upstream 进行设置，通过upstream 可以实现服务的负载均衡规则，可以提高服务器的高可用性。

#### max_fails

max_fails是最多出错数量，可以为每一个server设置一个max_fails，如果请求server发生了错误则max_fails会加一，如果请求server错误次数达到了max_fails后，Nginx会标记这个server为故障状态，后面就不会再去请求它了。

```bash
# 默认情况下，max_fails的次数是1次
upstream default {
    server  tflinux_php-fpm-tfphp_1:9000 max_fails=5;
    server  tflinux_php-fpm-tfphp_2:9000 max_fails=3;
}
```

#### fail_timeout

fail_timeout是故障等待超时时间，前面说过了max_fails是请求server错误次数，如果达到了max_fails次数之后server会被标记为故障状态，那么多长时间会重新尝试呢？这个fail_timeout就是这个时间了，在达到max_fails次数之后server进入故障状态，而后在fail_timeout时间之后会被重新标记为正常状态。

```bash
# fail_timeout的时间是10秒
upstream default {
    server  tflinux_php-fpm-tfphp_1:9000 max_fails=5 fail_timeout=100;
    server  tflinux_php-fpm-tfphp_2:9000 max_fails=3 fail_timeout=60;
}
```

#### proxy_connect_timeout

这个proxy_connect_timeout是连接超时时间，如果连接不到就会报错了。

```bash
proxy_connect_timeout 3s;
```

#### proxy_next_upstream_tries

这个proxy_next_upstream_tries是一个upstream反向代理的重试次数，简单说就是如果请求server出错的次数达到了proxy_next_upstream_tries的次数的话，即使没有达到max_fails的次数，即使后面还有没有尝试过的server，都不会再继续尝试了，而是直接报错。

```bash
# 一个upstream反向代理的重试次数，
proxy_next_upstream_tries 3;
```

#### proxy_next_upstream_timeout

这个proxy_next_upstream_timeout是一个upstream反向代理的故障等待时间，简单说就是无论upstream内部如何进行重试，所有花费的时间加在一起达到了proxy_next_upstream_timeout时间的话，就会直接报错，不会再继续尝试了。

```bash
proxy_next_upstream_timeout 60s;
```

#### backup

这个是备用服务器参数，可以为一个upstream设置一个backup的server，在生产server全部都出问题之后，可以自动切换到备用server上，为回复服务争取时间。

backup的server不同于其他server，平时是不承载请求的，所以它应该是比较空闲的状态，应急再合适不过了~~

```bash
upstream default {
    server  tflinux_php-fpm-tfphp_1:9000 max_fails=5 fail_timeout=100;
    server  tflinux_php-fpm-tfphp_2:9000 max_fails=3 fail_timeout=60;
    server  tflinux_php-fpm-tfphp_3:9000 backup;
}
```

给出总和示例：

```bash
proxy_connect_timeout 3s;
proxy_next_upstream_timeout 60s;
proxy_next_upstream_tries 3;

upstream default {
    server  tflinux_php-fpm-tfphp_1:9000 max_fails=5 fail_timeout=100;
    server  tflinux_php-fpm-tfphp_2:9000 max_fails=3 fail_timeout=60;
    server  tflinux_php-fpm-tfphp_3:9000 backup;
}
```

## 7. 动静分离

1. Nginx 动静分离简单来说就是把动态跟静态请求分开
   - 使用Nginx 处理静态页面/资源，
   - Tomcat 处理动态页面/资源。
2. 动静分离可以减轻Tomcat 压力，静态请求由Nginx 处理，提供系统整体性能.

![image-20230712100712612](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121007715.png)

需求说明

![image-20230712100747871](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121007998.png)

在 2 个 tomcat 中新建 tomcat\webapps\search\cal.jsp 文件

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>hello, jsp</title>
    </head>
    <body>
        <img src="image/cal.jpg"/>
        <h1>JSP, 计算器</h1>
        <%
	        int i = 20;
	        int j = 70;
    	    int res = i + j;
	        out.println(i + " + " + j + " = " + res);
        %>
    </body>
</html>
```

拷贝静态资源 cal.jpg 到 tomcat\webapps\search\image 目录，因为图片路径其实是 ip/search/image

使用动静分离优化

![image-20230712103642038](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121036144.png)

```bash
location /search/image/ {
	root html; # 代表在 nginx 安装目录的 html 目录下去找
}
```

![image-20230712111352164](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121113254.png)

将Linux 的两个Tomcat\webapps\search\image 目录删除， 在 /usr/local/nginx/html/search/image 目录下放入图片

![image-20230712112332027](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121123113.png)

![image-20230712112757353](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121127453.png)

如果有css js 文件需要动静分离，按照规则配置即可

![image-20230712112955117](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121129205.png)

也可以简写成 return html 

## 8. Nginx 工作机制&参数设置

### 1. master-worker 机制

工作原理图【一个 master 管理多个 worker】

![image-20230712113846971](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121138057.png)

```bash
ps -ef | grep nginx
```

![image-20230712192958104](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121929171.png)

![image-20230712192803429](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121928778.png)

### 2. 争抢机制

![image-20230712193826574](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121938653.png)

1. 一个master Process 管理多个worker process, 也就是说Nginx 采用的是 <font color="yellow">**多进程结构**</font>, 而不是多线程结构.
2. 当client 发出请求(任务)时，master Process 会通知管理的 worker process
3. worker process 开始争抢任务, 争抢到的worker process 会开启连接,完成任务
4. 每个worker 都是一个独立的进程，每个进程里只有一个主线程
5. Nginx 采用了IO 多路复用机制(需要在Linux 环境), 使用IO 多路复用机制, 是Nginx 在使用为数不多的worker process 就可以实现高并发的关键

![image-20230712193932885](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121939975.png)

1. Nginx 在启动后，会有一个master 进程和多个相互独立的worker 进程。
2. Master 进程接收来自外界的信号，向各worker 进程发送信号，每个进程都有可能来处理这个连接。
3. Master 进程能监控Worker 进程的运行状态，当worker 进程退出后(异常情况下)，会自动启动新的worker 进程。

### 3. accept_mutes 解决 "惊群现象"

1. 所有子进程都继承了父进程的sockfd，当连接进来时，所有子进程都将收到通知并“争着”与它建立连接，这就叫“惊群现象”。
2. 大量的进程被激活又挂起，只有一个进程可以accept() 到这个连接，会消耗系统资源
3. Nginx 提供了一个accept_mutex ，这是一个 <font color="yellow">加在accept 上的一把共享锁</font>。即每个worker 进程在执行accept 之前都需要先获取锁，获取不到就放弃执行accept()。有了这把锁之后【进了门之后才能吃鱼食（执行 accept）】，同一时刻，就只会有一个进程去accpet()，就不会有惊群问题了。
4. 当一个worker 进程在accept() 这个连接之后，就开始读取请求，解析请求，处理请求，产生数据后，再返回给客户端，最后才断开连接，完成一个完整的请求。
5. 一个请求，完全由worker 进程来处理，而且只能在一个worker 进程中处理。

### 4. 用多进程结构而不用多线程结构的好处 / 理论

1. 节省锁带来的开销, 每个worker 进程都是独立的进程，不共享资源，不需要加锁。在编程以及问题排查时，会方便
2. 独立进程，减少风险。采用独立的进程，可以让互相之间不会影响，一个进程退出后，其它进程还在工作，服务不会中断，master 进程则很快重新启动新的worker 进程
3. IO 多路复用：
   - 对于Nginx 来讲，一个进程只有一个主线程，那么它是怎么实现高并发的呢？
   - 采用了IO 多路复用的原理，通过 <font color="yellow">异步非阻塞的事件处理机制</font>，<font color="yellow">epoll 模型</font>，实现了轻量级和高并发
   - nginx 是如何具体实现的呢，举例来说：每进来一个request，会有一个worker 进程去处理。但不是全程的处理，处理到什么程度呢？处理到 <font color="yellow">**可能发生阻塞的地方**</font>，比如向上游(后端)服务器转发request，并等待请求返回。那么，这个处理的worker 不会这么傻等着，他会在发送完请求后，注册 <font color="yellow">一个事件</font>："如果upstream 返回了，告诉我一声，我再接着干"。于是他就休息去了。
     - 此时，如果再有request 进来，他就可以很快再按这种方式处理。
     - 而一旦上游服务器返回了，就会触发这个事件，worker 才会来接手，这个request 才会接着往下走。
     - 由于web server 的工作性质决定了每个request 的大部份生命都是在 <font color="red">**网络传输** </font>中，实际上花费在server 机器上的时间片不多，这就是几个进程就能解决高并发的秘密所在

### 5. Nginx的 master-worker 工作机制的优势

1. 支持nginx -s reload 热部署, 这个特征在前面我们使用过
2. 对于每个worker 进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销，同时在编程以及问题查找时，也会方便很多
3. 每个worker 都是一个独立的进程，但每个进程里只有一个主线程，通过异步非阻塞的方式/IO 多路复用来处理请求， 即使是高并发请求也能应对.
4. 采用独立的进程，互相之间不会影响，一个worker 进程退出后，其它worker 进程还在工作，服务不会中断，master 进程则很快启动新的worker 进程
5. 一个worker 分配一个CPU ， 那么worker 的线程可以把一个cpu 的性能发挥到极致

### 6. 参数设置

#### worker_processes

worker 数 = 服务器的 CPU 数

Nginx 默认没有开启利用多核cpu，可以通过增加 worker_cpu_affinity 配置参数来充分利用多核cpu 的性能

```bash
#2 核cpu，开启2 个进程
worker_processes 2;
worker_cpu_affinity 01 10;
#2 核cpu，开启4 个进程，发【1、3进程用的是同一个 CPU】
worker_processes 4;
worker_cpu_affinity 01 10 01 10;
#4 核cpu，开启2 个进程，0101 表示开启第一个和第三个内核，1010 表示开启第二个和 第四个内核；
worker_processes 2;
worker_cpu_affinity 0101 1010;
#4 个cpu，开启4 个进程
worker_processes 4;
worker_cpu_affinity 0001 0010 0100 1000;
#8 核cpu，开启8 个进程
worker_processes 8;
worker_cpu_affinity 00000001 00000010 00000100 00001000 00010000 00100000 01000000 10000000; # 顺序：从右向左
```

worker_cpu_affinity 理解

![image-20230712202712639](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307122027708.png)

修改配置文件，开启多核 CPU

设置worker 数量, Nginx 默认没有开启利用多核cpu，可以通过增加worker_cpu_affinity配置参数来充分利用多核cpu 的性能

![image-20230712203623762](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307122036822.png)

![image-20230712203810153](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307122038209.png)

#### worker_connection

1. worker_connection 表示 <font color="yellow">每个worker 进程所能建立连接的最大值</font>，所以，一个nginx 能建立的最大连接数，应该是worker_connections * worker_processes

   ```bash
   # 默认
   worker_connections: 1024
   # 调大（60000 连接）
   worker_connections: 60000
   # 同时要根据系统的最大打开文件数来调整
   系统的最大打开文件数>= worker_connections*worker_process
   ```

   ```bash
   # 查看系统的最大打开文件数
   ulimit -a|grep "open files"
   ```

   ![image-20230713003021791](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307130030873.png)

2. 根据最大连接数计算最大并发数：

   - 如果是支持http1.1 的浏览器每次访问要占两个连接，所以普通的静态访问最大并发数是： worker_connections * worker_processes / 2

   - 而如果是HTTP 作为反向代理来说， 最大并发数量应该是worker_connections *worker_processes/4。因为作为反向代理服务器，每个并发会建立与客户端的连接和与后端服务的连接，会占用两个连接， 看一个示意图

     ![image-20230713003243437](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307130032529.png)

#### 配置Linux 最大打开文件数

1. 使用ulimit -a 可以查看当前系统的所有限制值，使用ulimit -n 可以查看当前的最大打开文件数。
2. 新装的linux 默认只有1024，当作负载较大的服务器时，很容易遇到error: too many openfiles。因此，需要将其改大。
3. 使用ulimit -n 65535 可即时修改，但重启后就无效了。(注ulimit -SHn 65535 等效ulimit -n 65535，-S 指soft，-H 指hard)
4. 有如下 3 种修改方式：
   - 在/etc/rc.local 中增加一行ulimit -SHn 65535
   - 在/etc/profile 中增加一行ulimit -SHn 65535 【Debian】
   - 在/etc/security/limits.conf 最后增加如下两行记录 【Ubuntu】
     * soft nofile 65535
     * hard nofile 65535

[在Linux中修改打开文件数量和进程数量限制的3种方法_linux打开文件数量限制_请叫我常总裁的博客-CSDN博客](https://blog.csdn.net/weixin_43055250/article/details/124980838)

![image-20230713004809714](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307130048778.png)

ubuntu系统下，一定要把*改为你自己的用户名。好像是识别不了通配符。centos就没问题。

然后，在下面的两文件中加入：DefaultLimitNOFILE=65535 sudo vim /etc/systemd/user.conf  sudo vim /etc/systemd/system.conf 

## 9. 搭建高可用集群

Keepalived+Nginx 高可用集群(主从模式)

### 1. 集群架构图

1. 准备两台nginx 服务器, 一台做主服务器, 一台做备份服务器
2. 克隆虚拟机后，修改 ip、uuid、mac
3. 安装keepalived , 保证主从之间的通讯
4. 对外提供统一的访问IP(虚拟IP-VIP)

示意图：

![image-20230712114240990](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121142081.png)



### 2. 在两台Linux 服务器, 安装keepalived

下载 keepalived-2.0.20.tar.gz

上传到两台Linux /root 目录下

```bash
mkdir /root/keepalived
# 解压文件到指定目录:
tar -zxvf keepalived-2.0.20.tar.gz -C ./keepalived

cd /root/keepalived/keepalived-2.0.20
# 将配置文件放在/etc 目录下, 安装路径在/usr/local
./configure --sysconf=/etc --prefix=/usr/local\

# 编译并安装
```

说明：

- 说明: keepalived 的配置目录在/etc/keepalived/keepalived.conf
- keepalived 的启动指令在/usr/local/sbin/keepalived

### 3. 配置 keepalived【启动时候，要切换管理员模式】

指定一台 Linux 为 Master

```bash
vim /etc/keepalived/keepalived.conf
```

![image-20230712115158022](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121151128.png)

![image-20230712115208805](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121152909.png)

将另一台 Linux 指定为：Backup（备份服务器）

```bash
vim /etc/keepalived/keepalived.conf
```

![image-20230712115503540](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121155633.png)

![image-20230712115532726](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121155817.png)

启动 2台 Linux的 keeplived 指令：/usr/local/sbin/keepalived

观察 2 台 linux 的 ens33 是否已经绑定到 虚拟 IP（virtual_ipaddress）

```bash
ip a
```

![image-20230713112147148](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307131121648.png)

### 4. 注意事项和细节

1. keepalived 启动后无法ping 通VIP，提示ping: sendmsg: Operation not permitted

   [keepalived启动后无法ping通VIP，提示ping: sendmsg: Operation not permitted_天芝兰的博客-CSDN博客](https://blog.csdn.net/xjuniao/article/details/101793935)

2. nginx+keepalived 配置说明和需要避开的坑

   [nginx+keepalived配置踩过的坑_nginx集群_steven在学习的博客-CSDN博客](https://blog.csdn.net/qq_42921396/article/details/123074780)

### 5. 自动检测 Nginx 异常，终止 keepalived

1. 编写 shell 脚本

   ```bash
   vim /etc/keepalived/ch_nginx.sh
   ```

   简单说明: 下面的脚本就是去统计 ps -C nginx --no-header 的行数, 如果为0 , 说明 nginx 已经异常终止了, 就执行killall keepalived

   ```bash
   #!/bin/bash
   num=`ps -C nginx --no-header | wc -l`
   if [ $num -eq 0 ];then
   killall keepalived
   fi
   ```

2. 修改 ch_nginx.sh 权限

   ```bash
   chmod 755 ch_nginx.sh
   ```

3. 修改 主 Master 配置文件 ==> 改完配置后要重启 keepalived

   ```bash
   vim /etc/keepalived/keepalived.conf
   ```

   ![image-20230712120458961](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307121204064.png)

4. 重新启动 Master 的 keepalived，这时因为 Master 优先级高，会争夺 VIP 优先绑定

5. 手动关闭 Master 的 Nginx，注意观察 keepalived 也终止了

   ```bash
   ps -ef | grep keepalived
   ```

6. 再次访问nginx , 发现 虚拟IP 又和 backup 服务器绑定了.

### 6. 注意事项

1. keepalived vrrp_script 脚本不执行解决办法

   打开日志观察

   ```bash
   tail -f /var/log/messages
   ```

   重启 keepalived

   ```bash
   systemctl restart keepalived.service
   ```

2. 如果配置有定时检查Nginx 异常的脚本, 需要先启动nginx ,在启动keepalived ,否则 keepalived 一起动就被 killall 了

### 6. 配置文件 keepalived.conf 详解

```bash
#这里只注释要修改的地方
global_defs {
notification_email {
test@foxmail.com #接收通知的邮件地址
}
notification_email_from Alexandre.Cassen@firewall.loc #发送邮件的邮箱
smtp_server 192.168.200.1 #smtp server 地址
韩顺平Java 工程师
smtp_connect_timeout 30
router_id Node132 #Node132 为主机标识
vrrp_skip_check_adv_addr
#vrrp_strict #这里需要注释，避免虚拟ip 无法ping 通
vrrp_garp_interval 0
vrrp_gna_interval 0
}
vrrp_instance VI_1 {
state MASTER #主节点MASTER 备用节点为BACKUP
interface ens33 #网卡名称
virtual_router_id 51 #VRRP 组名，两个节点的设置必须一样，指明属于同一VRRP 组 【集群】
priority 100 #主节点的优先级（1-254 之间），备用节点必须比主节点优先级低
advert_int 1 #组播信息发送间隔，两个节点设置必须一样
authentication { #设置验证信息，两个节点必须一致
auth_type PASS
auth_pass 1111
}
virtual_ipaddress { #指定虚拟IP, 两个节点设置必须一样
192.168.200.16
}
}
```



