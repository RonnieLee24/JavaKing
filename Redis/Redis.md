# Redis

## 1. 基础知识

### 1.1 相关资料

#### 1.1.1 官网: https://redis.io/

#### 1.1.2 中文地址: http://redis.cn/

#### 1.1.3 下载地址: https://redis.io/download

### 1.2 为什么需要Redis

#### 1.2.1 企业需求

- 高并发
- 高可用
- 高性能
- 海量用户

#### 1.2.2 关系型数据库(如MySQL)-问题

##### 1.2.2.1 性能瓶颈：磁盘IO 性能低下

##### 1.2.2.2 扩展瓶颈：数据关系复杂，扩展性差，不便于大规模集群

#### 1.2.3 Redis 的优势

##### 1.2.3.1 内存存储-降低磁盘IO 次数

##### 1.2.3.2 不存储关系，仅存储数据-数据间关系，越简单越好

### 1.3 Redis 简介

1. 一句话: Redis (Remote DIctionary Server) 是用C 语言开发的一个开源的高性能键值对（key-value）数据库
2. 特征
   - 数据间没有必然的关联关系
   - 高性能。官方提供测试数据，50 个并发执行100000 个请求,读的速度是110000 次/s,写的速度是81000 次/s
3. 多种数据结构支持
   - 字符串类型string
   - 列表类型list
   -  散列类型hash
   -  集合类型set
   -  有序集合类型 sorted_set
4. 持久化支持。可以进行数据灾难恢复
5. 应用场景
   - 为热点数据加速查询，如热点商品、热点新闻、热点资讯、推广类等高访问量信息等
   - 任务队列，如秒杀、抢购、购票排队等
   -  即时信息查询，如排行榜、各类网站访问统计
   - 时效性信息控制，如验证码控制、投票控制等
   - 分布式数据共享，如分布式集群架构中的session 分离
   - 消息队列
   - 分布式锁

## 2. NoSQL 数据库

1. 一句话: 即Not-Only SQL（ 泛指非关系型的数据库），作为关系型数据库的补充
2. 作用：应对在海量用户和海量数据的情况下，带来的数据处理问题
3. NoSQL 的特点
   - 可扩容，可伸缩
   - 大数据量下高性能
   - 灵活的数据模型
   - 高可用
4. 常见Nosql 数据库
   - Redis
   - memcache
   - HBase
   - MongoDB

## 3. Redis 下载安装

下载地址: https://redis.io/download

在实际开发中Redis 都在Linux 下工作， Linux 版本: Redis6

解压完成后, 进入目录：cd redis-6.2.6

在redis-6.2.6 目录下, 执行make 命令（编译指令）

执行: make install, 进行安装

到此，安装OK ， 安装目录在/usr/local/bin

```bash
 cd /usr/local/bin/
```

![image-20230220000953369](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302200009438.png)

redis-benchmark:性能测试工具，可以在自己机器运行，看看自己机器性能如何
redis-check-aof：修复有问题的AOF 文件，rdb 和aof 后面讲
redis-check-dump：修复有问题的dump.rdb 文件
redis-sentinel：Redis 集群使用
redis-server：Redis 服务器启动命令
redis-cli：客户端，操作入口

## 4. Redis 后台启动&使用

1. 拷贝一份redis.conf 到其他目录, 比如/etc 目录, 注意执行保证能够定位到 redis.conf

  ```bash
  cd /opt/redis-6.2.6
  cp redis.conf /etc/redis.conf
  ```

2. 修改/etc/redis.con 后台启动设置daemonize no 改成yes, 并保存退出.

  ```bash
  # 运行服务（以 conf 配置）
  cd /usr/local/bin/
  ./redis-server /etc/redis.conf	
  ```

3. ```bash
    ps -aux | grep redis
  ```bash
## 5. 用客户端访问：redis-cli

```bash
 cd /usr/local/bin/
 redis-cli
 # 指定端口
 redis-cli -p 6380		
  ```

![image-20230220003636782](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302200036826.png)

![image-20230220003832104](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302200038139.png)

## 5. 修改 Redis 密码

1. 打开配置文件

   ```bash
   vim /etc/redis.conf
   ```

2. 找到 requirepass 值修改密码

3. 重启服务

   ```bash
   redis-cli shutdown
   cd /usr/local/bin/
   ```

4. 发现重启失败

   ```bash
   (error) ERR Errors trying to SHUTDOWN. Check logs.
   ```

5. kill -9 后重试

   ```bash
    cd /usr/local/bin/
    ./redis-server /etc/redis.conf
    # 进入命令行后，使用 auth + 密码来完成密码校验
   ```



## 6. 指导文档

指令文档: http://redis.cn/commands.html

Redis 命令十分丰富，包括的命令组有Cluster、Connection、Geo、Hashes、HyperLogLog、Keys、Lists、Pub/Sub、Scripting、Server、Sets、Sorted Sets、Strings、Transactions 一共14个redis 命令组两百多个redis 命令

### 6.1. 对 key 操作

keys * ： 查看当前库所有key (匹配：keys *1)

exists key：判断某个key 是否存在

- 1：存在
- 0：不存在

type key ：查看你的key 是什么类型

del【阻塞式】 key ： 删除指定的key 数据

- 1：删除成功
- 0：删除失败

unlink【非阻塞式】 key：根据value 选择非阻塞删除【仅将keys 从keyspace 元数据中删除，真正的删除会在后续异步操作】

expire key 10 ： 10 秒钟：为给定的key 设置过期时间

ttl key 查看还有多少秒过期

- -1 表示永不过期
- -2 表示已过期

### 6.2.  对 db 操作

select： 命令切换数据库

1. redis 安装后，默认有16 个库, 0-15
2. 默认操作的是redis 的 0 号库
   - dbsize：查看当前数据库的key 的数量
   - flush<font color="yellow">db</font>：清空当前库
   - flush<font color="yellow">all</font>：清空全部库

### 6.3 Redis 五大数据类型/结构

#### 操作文档

官方文档: https://redis.io/commands

中文文档: http://redisdoc.com/

#### Redis 数据存储格式

一句话: redis 自身是一个Map，其中所有的数据都是采用key : value 的形式存储

- key 是字符串
- value 是数据，数据支持多种类型/结构

#### Redis 数据类型-5 种常用

##### 1. string（set）

1. String 是Redis 最基本的类型，一个key 对应一个value。
2. String 类型是二进制安全的, Redis 的string 可以包含任何数据。比如j <font color="yellow">jpg 图片 </font>或者 <font color="yellow">**序列化**</font>
    的对象。
3. String 类型是Redis 基本的数据类型，一个Redis 中字符串value 最多可以是 <font color="red">**512M**</font>

String 常用指令

- set <key><value>添加键值对

- get <key>查询对应键值

- append <key><value>将给定的<value> 追加到原值的末尾

- strlen <key>获得值的长度

- set<font color="yellow">nx【not exists】</font> <key><value>只有在key 不存在时设置key 的值

- incr <key> 将key 中储存的数字值(字符串)增1， 只能对数字值操作，如果为空，新增值为 1

- decr <key> 将key 中储存的数字值(字符串)减1 ， 只能对数字值操作，如果为空，新增值为-1

- incr<font color="yellow">by</font> / decr<font color="yellow">by</font> <key><步长>将key 中储存的数字值增减。自定义步长

- mset <key1><value1><key2><value2> ....., 同时设置一个或多个key-value 对

  ```sql
   MSET k5 terry k6 tom
  ```

  ![image-20230510155731455](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305101557507.png)

- mget <key1><key2><key3> ..... 同时获取一个或多个value

- msetnx <key1><value1><key2><value2> ..... 同时设置一个或多个key-value 对，当且仅当所有给定key 都不存在, 原子性，有一个失败则都失败

- get<font color="yellow">range</font> <key><起始位置><结束位置> , 获得值的范围，类似 java 中的 substring

- setrange <key><起始位置><value> 用<value> 覆写<key>所储存的字符串值，从<起始位置>开始(索引从0 开始)。

- set<font color="yellow">ex【expire】</font> <key><过期时间><value> 设置键值的同时，设置过期时间，单位秒。

  ```sql
  setex k9 10 gogo # 单位：秒
  ```

  ![image-20230510160842748](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305101608794.png)

- getset <key><value> , 以新换旧，设置了新值同时获得旧值

##### 2. list（lpush / rpush）

一句话: list 类型, 保存多个数据，底层使用双向链表存储结构实现

![jiji-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302202342376.jpg)

1. Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头
    部(左边)或者尾部(右边)。
2. 底层是个双向链表，对两端的操作性能高，通过索引下标的操作中间的节点性能较差

list 指令一览：http://redisdoc.com/list/index.html

- <font color="yellow">l</font>push / <font color="yellow">r</font>push <key><value1><value2><value3> .... 从左边/右边插入一个或多个值

  ![11](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305101628980.png)

  ```bash
  lpush k10 a b c
  ```

  ![image-20230510162624575](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305101626617.png)

  ```bash
  rpush k10 a b cw'w'w'w'w'w'w'w'w'w'w'w'w'w'w'w'w'w'w
  # 预计输出 c b a a b c
  ```

  ![image-20230510163015497](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305101630542.png)

- lpop/rpop <key>从左边/右边吐出一个值

- rpoplpush <key1><key2>从<key1>列表右边吐出一个值，插到<key2>列表左边

- lrange <key><start><stop> 按照索引下标获得元素(从左到右)

- lrange mylist 0 -1 

  - 0 左边第一个，-1 右边第一个（0-1 表示获取所有）

- lindex <key><index>按照索引下标获得元素(从左到右)

- llen <key>获得列表长度

- linsert <key> before <value><newvalue>在<value>的前面插入<newvalue>插入值

- lrem <key><n><value>从左边删除n 个value(从左到右)

- lset<key><index><value>将列表key 下标为index 的值替换成value

list 最佳实践

redis 应用于具有操作 <font color="yellow">先后顺序</font> 的数据控制

- 系统通知，按照时间顺序展示，将最近的通知列在前面
- 其它，比如微信的最近转发，微博的最新关注等.

![zzz-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302202351670.jpg)

##### 3. set（sadd）【类似 list，但是会自动排重，无序】

一句话: set 提供的功能与list 类似是一个列表的功能，特殊之处在于set 是可以自动排重的, 即值是不允许重复的

set 常用指令&使用

set 指令一览：http://redisdoc.com/set/index.html

set 指令操作示意图

![888-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302202355192.jpg)

sadd <key><value1><value2> ..... 将一个或多个member 元素加入到集合key 中，已经存在的member 元素将被忽略

```sql
SADD name_key jack nono tom hsp

SMEMBERS name_key
```

![image-20230511113244769](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111132834.png)

- smembers <key>取出该集合的所有值。

- s <font color="yellow">**is** </font>member <key><value>判断集合<key>是否为含有该<value>值，有1，没有0

- scard<key>返回该集合的元素个数。

- srem <key><value1><value2> .... 删除集合中的某个元素。

- spop <key>随机从该集合中吐出一个值。

- srandmember <key><n>随机从该集合中取出n 个值。不会从集合中删除。

- smove <source><destination>value 把集合中一个值从一个集合移动到另一个集合【原子性】

  ![image-20230511113911773](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111139820.png)

  ```sql
  sadd name2_key kobe jordan
  					
  ```

  ![image-20230511114013226](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111140278.png)

- s<font color="yellow">**inter（交集）**</font> <key1><key2>返回两个集合的交集元素。

- s<font color="yellow">**union（并集）**</font> <key1><key2>返回两个集合的并集元素。

- s<font color="yellow">**diff**（**差集**）</font> <key1><key2>返回两个集合的差集元素(key1 中的，不包含key2 中的)【key1 - key2】

##### 4. hash（hset）【类似 map】

Redis hash 是一个键值对集合，hash 适合用于存储对象， 类似Java 里面的Map<String,Object>

Redis hash 存储结构简单示意图

![oopp-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302210007759.jpg)

hash 常用指令&使用

hash 指令一览：http://redisdoc.com/hash/index.html

hash 指令操作示意图

![lll-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302210020506.jpg)

hset <key><field><value>给<key>集合中的<field>键赋值<value>

- 初次添加：返回 1
- 修改：返回 0 

hget <key1><field>从<key1>集合<field>取出value

hmset <key1><field1><value1><field2><value2>... 批量设置hash 的值

```sql
hset monster:100 id 100 name jack
hset monster:200 id 200 name scott

HMGET monster:100 id name
```

![image-20230511172710329](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111727392.png)

h<font color="yellow">m</font>get <key1><field1> <field2>... 批量取出hash 的filed 值

h<font color="Yellow">exists</font><key1><field>查看哈希表key 中，给定域 field 是否存在

```sql
HEXISTS monster:100 id
# 存在：1
# 不存在：0
```

hkeys <key>列出该hash 集合的所有field

```sql
HKEYS monster:100
```

![image-20230511173113259](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111731302.png)

hvals <key>列出该hash 集合的所有value

```sql
 HVALS monster:100
```

![image-20230511173308377](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111733419.png)

hincrby <key><field><increment>为哈希表key 中的域field 的值加上增量1 -1

hsetnx【not Exists：防止覆盖】 <key><field><value>将哈希表key 中的域field 的值设置为value ，当且仅当域field 不存在

##### 5. 有序集合Zset（zadd）【类似：Treeset】

1. Redis 有序集合zset 与普通集合set 非常相似，是一个没有重复元素的字符串集合。
2. 不同之处是有序集合的每个成员都关联了一个评分(score),这个评分(score)被用来按照
    从最低分到最高分的方式排序集合中的成员。集合的成员是唯一的，但是评分可以是重复
    了。
3. 因为元素是有序的, 所以也可以很快的根据评分(score)或者次序(position)来获取一个范
    围的元素。
4. 访问有序集合的中间元素也是非常快的, 你能够使用有序集合作为一个没有重复成员
    的列表。

sorted set 指令一览：http://redisdoc.com/sorted_set/index.html

sorted set 指令示意图- 案例蜀国五虎将

```sql
# 添加
ZADD hero 1 gy 2 zf 3 zy 4 mc 5 hz
# 按照下标取出
zrange hero2 0 -1
# 逆序
zrevrange hero2 0 -1
# 打印评分
zrange hero2 0 -1 withscores
# 查看评分
zscore hero2 gy
# 按照分数排序
ZRANGEBYSCORE hero2 1 3 withscores
ZRANGEBYSCORE hero2 3 1 withscores	# 逆序

# 修改分数
zincrby <key><increment><value>
zincrby hero2 4 zf

# 删除指定元素
zrem <key>value>

# 统计个数【分数区间】
zcount <key><min><max>

# 返回该值在集合中的排名，从 0 开始
zrank <key><value>
```

![image-20230511181738773](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305111817824.png)

![ppl](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302211106492.jpg)

zadd <key><score1><value1><score2><value2>… 将一个或多个member 元素及其score 值加入到有序集key 当中。

- 如果 value 已经存在：会更新 score【int 或者 double 类型】
- 然后再重新插入

zrange <key><start><stop> [WITHSCORES] 返回有序集key 中，下标在<start><stop>之间的元素，带WITHSCORES，可以让分数一起和值返回到结果集

zscore <key><member> 返回有序集key 中，成员member 的score 值

zrangebyscore key min max [withscores] [limit offset count] 返回有序集key 中，所有score 值介于min 和max 之间(包括等于min 或max )的成员。有序集成员按score 值递增(从小到大)次序排列。

zrevrangebyscore key max min [withscores] [limit offset count] 同上，改为从大到小排列。

zincrby <key><increment><value> 为元素的 score 加上增量

zrem <key><value>删除该集合下，指定值的元素

zcount <key><min><max>统计该集合，分数区间内的元素个数

zrank <key><value>返回该值在集合中的排名，从 0 开始

## 7. Redis 配置

参考文档: https://www.cnblogs.com/nhdlb/p/14048083.html#_label0

四个文件：

1. queryRedisProcess.sh

   ```bash
   ps -aux | grep redis
   ```

2. runRedisService.sh

   ```bash
   cd /usr/local/bin/
   ./redis-server /etc/redis.conf
   ```

3. start_redis.sh

   ```bash
    cd /usr/local/bin/
    redis-cli -p 10072 -a ****
   ```

4. updateRedisConfig.sh

   ```bash
   vim /etc/redis.conf
   ```


### 7.1. 快速查找

```bash
# 输入 / + 查找词
# n：查看下一个
# N：查看上一个
# 取消搜索高亮：noh
```

### 7.2. Units 单位

1、配置大小单位,开头定义了一些基本的度量单位，只支持bytes，不支持bit
2、不区分大小写

```bash
6 # ./redis-server /path/to/redis.conf
7
8 # Note on units: when memory size is needed, it is possible to specify
9 # it in the usual form of 1k 5GB 4M and so forth:
10 #
11 # 1k => 1000 bytes
12 # 1kb => 1024 bytes
13 # 1m => 1000000 bytes
14 # 1mb => 1024*1024 bytes
15 # 1g => 1000000000 bytes
16 # 1gb => 1024*1024*1024 bytes
17 #
18 # units are case insensitive so 1GB 1Gb 1gB are all the same.
```

### 7.3. #INCLUDES#

多实例的情况可以把公用的配置文件提取出来, 然后include

```bash
0 ################################## INCLUDES ###################################
21
22 # Include one or more other config files here.  This is useful if you
23 # have a standard template that goes to all Redis servers but also need
24 # to customize a few per-server settings.  Include files can include
25 # other files, so use this wisely.
26 #
27 # Notice option "include" won't be rewritten by command "CONFIG REWRITE"
28 # from admin or Redis Sentinel. Since Redis always uses the last processed
29 # line as value of a configuration directive, you'd better put includes
30 # at the beginning of this file to avoid overwriting config change at runtime.
31 #
32 # If instead you are interested in using includes to override configuration
33 # options, it is better to use include as the last line.
34 #
35 # include /path/to/local.conf
36 # include /path/to/other.conf
```

### 7.4. #NETWORK#

#### bind

![image-20230512205627977](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122056054.png)

默认情况bind=127.0.0.1 只能接受本机的访问请求

如果服务器是需要远程访问的，需要将改为：0.0.0.0

#### protected-mode

默认是保护模式

![image-20230512210227161](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122102224.png)

如果服务器是需要远程访问的, 需要将yes 设置为 no

#### port

Redis 服务默认端口6379

![image-20230512210308221](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122103284.png)

#### timeout

一个 <font color="red">**空闲的客户端**</font> 维持多少秒会关闭，0 表示关闭该功能, 即永不超时

![image-20230512210334889](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122103946.png)

#### tcp-keepalive【TCP的保活探测机制】

tcp-keepalive 是对访问客户端的一种心跳检测，每隔n 秒检测一次, 单位为秒

![image-20230512210445948](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122104004.png)

如果设置为0，则不会进行Keepalive 检测，建议设置成60

为什么需要心跳检测机制？

TCP 协议中有长连接和短连接之分

- 短连接环境下，数据交互完毕后，主动释放连接
- 长连接的环境下，进行一次数据交互后，很长一段时间内无数据交互时，客户端可能意外断开，这些TCP 连接并未来得及正常释放，那么，连接的另一方并不知道对端的情况，它会一直维护这个连接，长时间的积累会导致非常多的半打开连接，造成端系统资源的消
  耗和浪费，且有可能导致在一个无效的数据链路层面发送业务数据，结果就是发送失败。所以服务器端要做到快速感知失败，减少无效链接操作，这就有了TCP 的Keepalive(保活探测)机制

### 7.5. #GENERAL 通用#

#### daemonize

是否为后台进程，设置为yes

设置为yes 后, 表示守护进程, 后台启动

![image-20230512210805489](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122108552.png)

#### pidfile【记录 redis的进程号】

存放pid 文件的位置，每个实例会产生一个不同的pid 文件, 记录redis 的进程号

![image-20230512210939517](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122109582.png)

![image-20230514150153021](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141501100.png)

```bash
cat /var/run/redis_6379.pid
```

![image-20230514150318258](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141503326.png)

![image-20230514150333370](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141503431.png)

#### loglevel

![image-20230512211039314](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122110376.png)

redis 日志分为4 个级别，默认的设置为notice, 开发测试阶段可以用debug(日志内容较多,不建议生产环境使用)，生产模式一般选用notice

| redis日志4个级别  | 说明                                         |
| ----------------- | -------------------------------------------- |
| debug             | 会打印出很多信息，适用于开发和测试阶段       |
| verbose【冗长的】 | 包含很多不太有用的信息，但比debug 要清爽一些 |
| notice            | 适用于生产模式                               |
| warning           | 警告信息                                     |



#### logfile

logfile "" 就是说，默认为控制台打印，并没有日志文件生成

![image-20230512211357475](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122113534.png)

可以为redis.conf 的logfile 指定配置项

```bash
logfile /var/log/redis/redis.log
```

如果提示日志文件redis.log 不存在，创建一个该文件即可

查看 redis.log

```bash
cat /var/log/redis/redis.log
```

#### databases 16

设定库的数量默认16，默认数据库为0 号

![image-20230512211741262](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122117331.png)

可以使用SELECT \<dbid>命令在连接上指定数据库id

![image-20230512211840075](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305122118134.png)

### 7.6. #SECURITY 安全#

#### 设置密码

##### redis.conf 中设置密码

![image-20230514131618170](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141316303.png)

##### 命令行设置密码

![image-20230514131711248](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141317321.png)

在命令中设置密码，是临时的, 重启redis 服务器，密码就还原了

### 7.7. #LIMITS 限制#

#### maxclients

![image-20230514131751876](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141317934.png)

设置redis 同时可以与多少个客户端进行连接。

默认情况下为10000 个客户端

如果达到了此限制，redis 会拒绝新的连接请求，并且向这些连接请求方发出"max number of clients reached"

#### maxmemory

![image-20230514131946557](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141319621.png)

- 在默认情况下, 对32 位实例会限制在3 GB, 因为32 位的机器最大只支持4GB 的内存，而系统本身就需要一定的内存资源来支持运行，所以32 位机器限制最大3 GB 的可用内存是非常合理的，这样可以避免因为内存不足而导致Redis 实例崩溃
- 在默认情况下, 对于64 位实例是没有限制
- 当用户开启了redis.conf 配置文件的maxmemory 选项，那么Redis 将限制选项的值不能小于1 MB

老韩对maxmemory 设置的建议

1. Redis 的maxmemory 设置取决于使用情况, 有些网站只需要32MB，有些可能需要12GB。
2. maxmemory 只能根据具体的生产环境来调试，不要预设一个定值，从小到大测试，基本标准是不干扰正常程序的运行。
3. Redis 的最大使用内存跟搭配方式有关，如果只是用Redis 做纯缓存, 64-128M 对一般小型网站就足够了
4. 如果使用Redis 做数据库的话，设置到物理内存的1/2 到 3/4 左右都可以
5. 如果使用了快照功能的话，最好用到50%以下，因为快照复制更新需要双倍内存空间，如果没有使用快照而设置redis 缓存数据库，可以用到内存的80%左右，只要能保证Java、NGINX 等其它程序可以正常运行就行了

#### maxmemory-policy

![image-20230514140857631](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141408693.png)

policy 一览

1. volatile-lru：使用LRU 算法移除key，只对设置了过期时间的键；(最近最少使用)
2. allkeys-lru：在所有集合key 中，使用LRU 算法移除key
3. volatile-random：在过期集合中移除随机的key，只对设置了过期时间的键
4. allkeys-random：在所有集合key 中，移除随机的key
5. volatile-ttl：移除那些TTL 值最小的key，即那些最近要过期的key
6. noeviction：不进行移除。针对写操作，只是返回错误信息

#### maxmemory-samples

![image-20230514141133264](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141411328.png)

- 设置样本数量，LRU 算法和最小TTL 算法都并非是精确的算法，而是估算值，所以你可以设置样本的大小，redis 默认会检查这么多个key 并选择其中LRU 的那个
- 一般设置3 到 7 的数字，数值越小样本越不准确，但性能消耗越小

## 8. 发布【publish】和订阅【subscribe】

### 1. 发布和订阅是什么

一句话：Redis 发布订阅(pub/sub) 是一种消息通信模式：

- 发送者(pub) 发送消息
- 订阅者(sub) 接收消息

Redis 客户端可以订阅任意数量的频道

#### 客户端订阅频道示意图

![jiji-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261843897.jpg)

#### 当给这个频道发布消息后，消息就会发送给订阅的客户端

![2211-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261844023.jpg)

### 2. 如何理解发布和订阅模式

#### 2.1 任务队列

1. 顾名思义，就是"传递消息的队列"
2. 与任务队列进行交互的实体有两类：
   - 一类是生产者（producer）
   - 另一类则是消费者（consumer）。生产者将需要处理的任务放入任务队列中，而消费者则不断地从任务队列
     中读入任务信息并执行

#### 2.2 如何理解

可以这么简单的理解：

1. Subscriber：收音机，可以收到多个频道，并以队列方式显示f
2. Publisher：电台，可以往不同的FM 频道中发消息
3. Channel：不同频率的 FM 频道

从 Pub/Sub 的机制来看，它更像是一个广播系统，多个订阅者（Subscriber）可以订阅多个频道（Channel），多个发布者（Publisher）可以往多个频道（Channel）中发布消息。

### 3. 发布订阅模式分类

#### 1. 一个发布者，多个订阅者【通知、公告】

主要应用：通知、公告

可以作为 消息 <font color="yellow">**队列**</font> 或者 消息 <font color="yellow">**管道**</font>

![1ko-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261901432.jpg)

#### 2. 多个发布者，一个订阅者【投票】

各应用程序作为Publisher 向Channel 中发送消息，Subscriber 端收到消息后执行相应的业务逻辑，比如写数据库，显示..

主要应用：

- 排行榜
- 投票
- 计数

![qqq-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261903350.jpg)

#### 3. 多个发布者，多个订阅者【群聊】

可以向不同的Channel 中发送消息，由不同的Subscriber 接收

主要应用：群聊、聊天

![mimi-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261904666.jpg)

### 4. 命令行实现发布和订阅

#### 发布订阅操作

1. PUBLISH channel msg
   - 将信息 message 发送到指定的频道 channel
2. SUBSCRIBE channel [channel ...]
   - 订阅频道，可以同时订阅多个频道
3. UNSUBSCRIBE [channel ...]
   - 取消订阅指定的频道, 如果不指定频道，则会取消订阅所有频道
4. P【Pattern】SUBSCRIBE pattern [pattern ...]
   - 订阅一个或多个符合给定模式的频道，每个模式以* 作为匹配符，比如
     - it* 匹配所有以it 开头的频道( it.news 、it.blog 、it.tweets 等等)
     - news.* 匹配所有以news. 开头的频道( news.it 、news.global.today 等等)，诸如此类
5. P【Pattern】UNSUBSCRIBE [pattern [pattern ...]]
   - 退订指定的规则, 如果没有参数则会退订所有规则

#### 快速入门

##### 1. 一个发布者，两个订阅者

![image-20230514192740969](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141927058.png)

1. 先操作订阅者

   ```bash
   subscribe channel1 channel2
   ```

   ![image-20230514193000605](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141930658.png)

2. 操作发布者

   ```bash
   PUBLISH channel1 hello,ACLQ	
   ```

   ![image-20230514193935224](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141939279.png)

3. 观察订阅者，看到可以收到消息

   ![image-20230514193314250](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141933317.png)

##### 2. 多个发布者，一个订阅者

1. 再开一个发布者，发布 hi
2. 观察到2个订阅者都可以收到消息

​	

##### 3. 多个发布者，多个订阅者

原理类似

#### 注意事项

1. 发布的消息没有持久化【关闭就没有了】

2. 订阅的客户端，只能收到订阅后发布的消息]

   ![image-20230514194153334](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305141941389.png)

## 9. Jedis

### API文档

在线文档: https://www.mklab.cn/onlineapi/jedis/

### 介绍

Jedis 工作示意图

![kkqq-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261912683.jpg)

### Jedis操作 Reids 数据

1. 创建 Maven 项目

2. 修改pom.xml , 增加依赖

   ```xml
   <!--引入 jedis 依赖-->
   <dependencies>
       <dependency>
           <groupId>redis.clients</groupId>
           <artifactId>jedis</artifactId>
           <version>3.2.0</version>
       </dependency>
   </dependencies>
   ```

3. ![image-20230226201632869](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302262016931.png)

   中文 16进制存放



```java
package com.llq.jedis;

public class jedis_ {

    //  连接 Redis
    @Test
    public void con(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        String res = jedis.ping();
        System.out.println("连接成功 ping 返回结果 " + res);
        jedis.close();  //  关闭当前连接
    }

    //  key 操作
    @Test
    public void key(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        jedis.set("k1", "v1");
        jedis.set("k2", "v2");
        jedis.set("k3", "v3");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys.size());
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println(jedis.exists("k1"));
        System.out.println(jedis.ttl("k2"));    //  默认 -1
        System.out.println(jedis.get("k3"));
        //  关闭连接
        jedis.close();
    }

    @Test
    public void string(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        jedis.mset("k1", "jack", "k2", "marry", "k3", "smith");
        System.out.println(jedis.mget("k1", "k2", "k3"));   //  返回 list
        jedis.close();
    }

    @Test
    public void list(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        jedis.lpush("nameList", "jack", "tom", "mary");
        List<String> list = jedis.lrange("nameList", 0, -1);
        for (String element : list) {
            System.out.println(element);
        }
        jedis.close();
    }

    @Test
    public void set(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        jedis.sadd("city", "北京", "上海");
        jedis.sadd("city", "广州");
        jedis.sadd("city", "深圳");
        Set<String> city = jedis.smembers("city");
        for (String s : city) {
            System.out.println(s);
        }
        jedis.close();
    }

    @Test
    public void hash(){ //  很像 java 的 map
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        //  设置
        jedis.hset("hash01", "name", "李白");
        jedis.hset("hash01", "age", "30");

        String name = jedis.hget("hash01", "name");
        System.out.println("name=>" + name);

        Map<String, String> map = new HashMap<>();
        map.put("job", "java 工程师");
        map.put("address", "北京");
        map.put("email", "hello@sohu.com");
        jedis.hset("hash02", map);

        //  取出看看
        List<String> person = jedis.hmget("hash02", "job", "address", "email");
        System.out.println(person);
        jedis.close();
    }

    //  zset 有序集合
    @Test
    public void zest(){
        Jedis jedis = new Jedis("8.129.52.105", 6380);
        //  redis 设置密码后，则需要进行身份校验
        jedis.auth("352420kobe24llq");
        //  根据 score 来评分
        jedis.zadd("hero", 2, "张飞");
        jedis.zadd("hero", 9, "马超");
        jedis.zadd("hero", 7, "赵云");
        jedis.zadd("hero", 1, "关羽");
        jedis.zadd("hero", 6, "黄忠");
        //  取出
        Set<String> heros = jedis.zrange("hero", 0, -1);   //  0：第一个，-1：最后一个
        Set<String> heros1 = jedis.zrevrange("hero", 0, -1);   //   逆序输出！！！
        for (String hero : heros) {
            System.out.println(hero);
        }
        jedis.close();
    }
}
```

### 防火墙查看端口是否开放

```bash
firewall-cmd --list-all
# 开放端口
firewall-cmd --add-port=6379/tcp --permanent
# 然后重启防火墙
firewall-cmd --reload
```

## 10. SpringBoot2 整合 Redis

需求分析：

1. 在springboot 中, 整合redis
2. 可以通过RedisTemplate 完成对redis 的操作, 包括设置数据/获取数据
3. 比如添加和读取数据

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.6</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.hspedu</groupId>
    <artifactId>redis_springboot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>redis_springboot</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <!-- 说明: 如果这里是spring-boot-start 就改成如下
spring-boot-start-web-->
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- spring2.X 集成redis 所需common-pool-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <!--不要带版本号,防止冲突-->
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.2.2</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

配置：application.properties

完成 redis 的基本配置

```bash
#Redis 服务器地址
spring.redis.host=192.168.198.130
#Redis 服务器连接端口
spring.redis.port=6379
#Redis 如果有密码,需要配置, 没有密码就不要写
spring.redis.password=hspedu
#Redis 数据库索引（默认为0）
spring.redis.database=0
#连接超时时间（毫秒）
spring.redis.timeout=1800000
#连接池最大连接数（使用负值表示没有限制）
spring.redis.lettuce.pool.max-active=20
#最大阻塞等待时间(负数表示没限制)
spring.redis.lettuce.pool.max-wait=-1
#连接池中的最大空闲连接
spring.redis.lettuce.pool.max-idle=5
#连接池中的最小空闲连接
spring.redis.lettuce.pool.min-idle=0
```

Redis 配置类

1. 是对要使用的RedisTemplate bean 对象的配置, 可以理解成是一个常规配置.
2. 同学们想一想我们以前学习过一个JdbcTemplate,设计理念类似
3. 如果不配置, springboot 会使用默认配置, 这个默认配置, 会出现一些问题, 比如:redisTemplate 的key 序列化等, 问题所以通常我们需要配置

注意事项和细节：

1. 如果没有提供RedisConfig 配置类, springboot 会使用默认配置, 也可以使用(先说,后面写RedisTestController 再测试)
2. 如果没有提供RedisConfig 配置类, springboot 会使用默认配置, 但是会存在问题,比如redisTemplate 模糊查找key 数据为空
3. Unrecognized token 'beijing': was expecting ('true', 'false' or 'null')
   - 看报错，是jason 转换异常，实际上是因为redisTemplate 在做数据存储的时候会把存
     储的内容序列化，所以，redisTemplate 读取的时候也会反序列化，而在redis 客户端
     set 的时候并不会做序列化，因此set 的进去的值在用redisTemplate 读的时候就会报类
     型转换异常了
4. 解决方案: 最简单的就是用程序重新set 一遍即可

## 11. Redis 持久化 RDB

在线文档：[Redis persistence | Redis](https://redis.io/docs/management/persistence/)





