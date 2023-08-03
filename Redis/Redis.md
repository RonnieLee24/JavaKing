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

1. 拷贝一份redis.conf 到其他目录, 比如 /etc 目录, 注意执行保证能够定位到 redis.conf

  ```bash
  cd /opt/redis-6.2.6
  cp redis.conf /etc/redis.conf
  ```

2. 修改/etc/redis.conf 后台启动设置daemonize no 改成yes, 并保存退出.

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
  rpush k10 a b c
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
             <!--不要带版本号,防止冲突-->
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
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

```bash
main
├─ java
│    └─ com
│           └─ llq
│                  └─ redis
│                         ├─ RedisSpringBoot.java
│                         ├─ config	# 2. Redis 配置类
│                         └─ controller
└─ resources
       └─ application.properties	# 1. 配置 application.properties
```

配置：application.properties

完成 redis 的基本配置

```bash
#Redis 服务器地址
spring.redis.host=8.129.52.105
#Redis 服务器连接端口
spring.redis.port=10072
#Redis 如果有密码,需要配置, 没有密码就不要写
spring.redis.password=352420kobe24llq
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

```java
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template =
                new RedisTemplate<>();
        System.out.println("template=>" + template);
        RedisSerializer<String> redisSerializer =
                new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(factory);
        //key 序列化方式
        template.setKeySerializer(redisSerializer);
        //value 序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //value hashmap 序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer =
                new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置序列化（解决乱码的问题）,过期时间600 秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(600))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config).build();
        return cacheManager;
    }
}
```

注意事项和细节：

1. 如果没有提供RedisConfig 配置类, springboot 会使用默认配置, 也可以使用(先说,后面写RedisTestController 再测试)
2. 如果没有提供RedisConfig 配置类, springboot 会使用默认配置, 但是会存在问题,比如 redisTemplate 模糊查找key 数据为空
3. Unrecognized token 'beijing': was expecting ('true', 'false' or 'null')
   - 看报错，是jason 转换异常，实际上是因为redisTemplate 在做数据存储的时候会把存
     储的内容序列化，所以，redisTemplate 读取的时候也会反序列化，而在redis 客户端
     set 的时候并不会做序列化，因此set 的进去的值在用redisTemplate 读的时候就会报类
     型转换异常了
4. 解决方案: 最简单的就是用程序重新set 一遍即可 【都用 redisTemplate 操作即可】

控制器-提供API 接口

```java
@RestController
@RequestMapping("/redisTest")
public class RedisTestController {

    //  装配 RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //  编写第一个测试方法
    //  演示设置数据和读取数据
    @GetMapping("/t1")
    public String t1(){
        redisTemplate.opsForValue().set("book", "三国演义");    //  设置
        String bookName = (String)redisTemplate.opsForValue().get("book");
        return bookName;
    }

    @GetMapping("/t2")
    public String testRedis2() {
      redisTemplate.opsForList().leftPush("books", "笑傲江湖");
      redisTemplate.opsForList().leftPush("books", "hello, java");

        List books = redisTemplate.opsForList().range("books", 0, -1);
        StringBuilder sb = new StringBuilder();
        for (Object book : books) {
            sb.append(book.toString() + " ");
            System.out.println(book.toString());
        }
        return sb.toString();
    }
}
```

编写主启动类-完成测试

```java
@SpringBootApplication
public class RedisSpringBoot {
    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(RedisSpringBoot.class, args);
    }
}
```

## 11. Redis 持久化 RDB

在线文档：[Redis persistence | Redis](https://redis.io/docs/management/persistence/)

持久化方案：

- RDB（Reids DataBase）
- AOF（Append Of File）

### 1. RDB（Redis DataBase）

在指定的时间间隔内将内存中的数据集快照写入磁盘， 也就Snapshot 快照，恢复时将快照文件读到内存

![image-20230715171001796](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151710010.png)

具体流程如下：

1. redis 客户端执行 bgsave 命令或者自动触发bgsave 命令
2. 主进程判断当前是否已经存在正在执行的子进程，如果存在，那么主进程直接返回
3. 如果不存在正在执行的子进程，那么就fork 一个新的子进程进行持久化数据，fork 过程是阻塞的，fork 操作完成后主进程即可执行其他操作；
4. 子进程先将数据写入到 <font color="yellow">临时的rdb 文件 </font>中，待快照数据写入完成后再原子替换旧的rdb文件
5. 同时发送信号给主进程，通知主进程rdb 持久化完成，主进程更新相关的统计信息

总结：

- 整个过程中，主进程是不进行任何IO 操作的，这就确保了极高的性能
- 如果需要进行大规模数据的恢复, 且对于数据恢复的完整性不是非常敏感，那RDB 方式要比AOF 方式更加的高效
- RDB 的缺点是最后一次持久化后的数据可能丢失

解决：

- 如果你是正常关闭Redis , 仍然会进行持久化, 不会造成数据丢失
- 如果是Redis <font color="red">**异常终止** </font>/宕机, 就可能造成数据丢失
- 后面在讲解快照配置, 还会举例说明

#### dump.rdp 文件

在 redis.conf 中配置文件名称, 默认为dump.rdb

![image-20230715172152224](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151721305.png)

默认是启动环境下

![image-20230715172221662](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151722745.png)

#### 相关配置 & 参数 & 操作

默认配置如下：

![image-20230715172356702](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151723795.png)

注意理解这个时间段的概念

![image-20230715172430767](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151724841.png)

如果我们没有 开启 save 注释，那么在退出 Redis 时，也会进行备份，更新 dump.db

#### save VS bgsave(back)

1. save ：save 时只管保存，其它不管，全部阻塞。手动保存, 不建议。
2. bgsave：Redis 会在后台异步进行快照操作， 快照同时还可以响应客户端请求。
3. 可以通过lastsave 命令获取最后一次成功执行快照的时间(unix 时间戳) , 可以使用工具转换

![image-20230715173102544](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151731614.png)

![image-20230715173207500](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151732577.png)

#### flushall

1. 执行flushall 命令，也会产生dump.rdb 文件, 数据为空.
2. Redis Flushall 命令用于清空整个Redis 服务器的数据(删除所有数据库的所有key)

#### Save

1. 格式：save 秒钟 写操作次数，如图
2. RDB 是整个内存的压缩过的Snapshot，RDB 的数据结构，可以配置复合的快照触发条件
3. 禁用: 给save 传入空字符串, 可以看文档

#### stop-writes-on-bgsave-error

当Redis 无法写入磁盘的话(比如磁盘满了), 直接关掉Redis 的写操作。推荐 yes

#### rdbcompression

对于存储到磁盘中的快照，可以设置是否进行压缩存储。如果是的话，redis 会采用LZF 算法进行压缩。

如果你不想消耗CPU 来进行压缩的话，可以设置为关闭此功能, 默认yes

#### rdbchecksum

在存储快照后, 还可以让redis 使用CRC64 算法来进行数据校验，保证文件是完整的

但是这样做会增加大约10%的性能消耗，如果希望获取到最大的性能提升，可以关闭此功能, 推荐yes

#### 动态停止RDB

1. 动态停止RDB：redis-cli config set save ""
2. 说明: save 后给空值，表示禁用保存策略



实例演示：

需求: 如果Redis 的key 在30 秒内, 有5 个key 变化, 就自动进行RDB 备份【修改完配置后，要 shutdown 服务！！！】

![image-20230716192537745](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307161925834.png)

记录下之前的 dump 文件信息

![image-20230716192713535](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307161927611.png)

修改 5 个 key 之后，再来查看 dump 文件，发现确实生效了

![image-20230716193514348](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307161935414.png)

### 2. RDB 备份&恢复

1. Redis 可以充当缓存, 对项目进行优化, 因此重要/敏感的数据建议在Mysql要保存一份
2. 从设计层面来说, Redis 的内存数据, 都是可以重新获取的(可能来自程序, 也可能来自Mysql)
3. 因此我们这里说的备份&恢复主要是给大家说明一下Redis 启动时, 初始化数据是从dump.rdb 来的, 这个机制.

```bash
# 查询 rdb 文件的目录
config get dir
```

将dump.rdb 进行备份, 如果有必要可以写shell 脚本来定时备份[参考Linux 课程定时备份Mysql 数据库, 视频地址https://www.bilibili.com/video/BV1Sv411r7vd?p=105 ] , 这里老师简单处理

### 3. RDB持久化小结

优势：

- 适合大规模的数据恢复
- 对数据完整性和一致性要求不高更适合使用
- 节省磁盘空间
- 恢复速度快

![image-20230715173919994](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151739066.png)

劣势：

1. 虽然Redis 在fork 时使用了写时拷贝技术(Copy-On-Write), 但是如果数据庞大时还是比较消耗性能。
2. 在备份周期在一定间隔时间做一次备份，所以如果 <font color="yellow">Redis 意外down 掉 </font>的话(如果正常关闭Redis, 仍然会进行RDB 备份, 不会丢失数据), 就会丢失最后一次快照后的所有修改

### 4. AOF（Append Only File）--- 数据一致性更好，优先级高

文档：[Redis persistence | Redis](https://redis.io/docs/management/persistence/)

1. 以日志的形式来记录每个写操作(<font color="yellow">**增量保存**</font>)，将Redis 执行过的所有写指令记录下来(比如set/del 操作会记录, 读操作get 不记录)
2. 只许追加文件但不可以改写文件
3. redis 启动之初会读取该文件重新构建数据
4. redis 重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作

AOF 持久化流程：

![image-20230715174311608](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151743681.png)

解读上图：

1. 客户端的请求写命令会被append 追加到AOF 缓冲区内
2. AOF 缓冲区根据AOF 持久化策略[always,everysec,no【将同步交给系统处理】将操作sync 同步到磁盘的AOF 文件中
3. AOF 文件大小超过重写策略或手动重写时，会对AOF 文件rewrite 重写，压缩AOF 文件容量
4. Redis 服务重启时，会重新load 加载AOF 文件中的写操作达到数据恢复的目的

#### 开启功能

1. 在redis.conf 中配置文件名称，默认为appendonly.aof

   ![image-20230715174659363](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307151746446.png)

2. AOF 文件的保存路径，同RDB 的路径一致

3. AOF 和RDB 同时开启，系统默认取AOF 的数据

#### 实例演示

#### AOF 启动/修复/恢复

基本说明：

AOF 的备份机制和性能虽然和RDB 不同, 但是备份和恢复的操作同RDB 一样, 都是拷贝备份文件, 需要恢复时再拷贝到Redis 工作目录下，启动系统即加载

正常修复

1. 修改默认的appendonly no，改为yes
2. 将有数据的aof 文件定时备份, 需要恢复时, 复制一份保存到对应目录(查看目录：config get dir)
3. 恢复：重启redis 然后重新加载

异常恢复

1. 如遇到AOF 文件损坏，通过/usr/local/bin/redis-check-aof --fix appendonly.aof 进行恢复
2. 建议先: 备份被写坏的AOF 文件
3. 恢复：重启redis，然后重新加载

#### 同步频率设置

![image-20230717105020383](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171050496.png)

解读：

1. appendfsync always

   始终同步，每次Redis 的写入都会立刻记入日志；性能较差但数据完整性比较好

2. appendfsync everysec【默认】

   每秒同步，每秒记入日志一次，如果宕机，本秒的数据可能丢失。

3. appendfsync no

   redis 不主动进行同步，把同步时机交给操作系统

[Redis 持久化机制 (baidu.com)](https://baijiahao.baidu.com/s?id=1740774723808931509&wfr=spider&for=pc)

#### Rewrite 压缩

rewrite 重写介绍

1. AOF 文件越来越大，需要定期对AOF 文件进行重写达到压缩
2. 旧的AOF 文件含有无效命令会被忽略，保留最新的数据命令, 比如set a a1 ; set a b1 ; set a c1; 保留最后一条指令就可以了
3. 多条写命令可以合并为一个, 比如set a c1 b b1 c c1
4. AOF 重写降低了文件占用空间
5. 更小的AOF 文件可以更快的被redis 加载

重写触发配置：

- 手动触发

  直接调用bgrewriteaof 命令

  ![image-20230717105802360](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171058426.png)

- 自动触发

  ![image-20230717105816811](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171058879.png)

  auto-aof-rewrite-min-size: AOF 文件最小重写大小, 只有当AOF 文件大小大于该值时候才能
  重写, 默认配置64MB

  auto-aof-rewrite-percentage: 当前AOF 文件大小和最后一次重写后的大小之间的比率等于
  或者大于指定的增长百分比，如100 代表当前AOF 文件是上次重写的两倍时候才重写

  系统载入时或者上次重写完毕时，Redis 会记录此时AOF 大小，设为base_size,

  如果Redis 的AOF 当前大小>= base_size +base_size*100% (默认)且当前
  大小>=64mb(默认)的情况下，Redis 会对AOF 进行重写

#### AOF 持久化小结

优势：

1. 备份机制更稳健，丢失数据概率更低。
2. 可读的日志文本，通过操作AOF 稳健，可以处理误操作

![image-20230717110024506](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171100556.png)

劣势：

1. 比起RDB 占用更多的磁盘空间
2. 恢复备份速度要慢
3. 每次读写都同步的话，有一定的性能压力

#### RDB 还是AOF?

[Redis persistence | Redis](https://redis.io/docs/management/persistence/)

官方推荐两个都启用

如果只做缓存：如果你只希望你的数据在服务器运行的时候存在, 你也可以不使用任何持久化方式



## 12. Redis_事务_锁机制_秒杀

### 1. Redis 事务

1. Redis 事务是一个单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行
2. 事务在执行的过程中，不会被其他客户端发送来的命令请求所打断
3. Redis 事务的主要作用就是串联多个命令防止别的命令插

### 2. Redis 事务三特性

#### 2.1 单独的隔离操作

1. 事务中的所有命令都会序列化、按顺序地执行
2. 事务在执行的过程中，不会被其他客户端发送来的命令请求所打断

#### 2.2 没有隔离级别的概念

队列中的命令(指令), 在没有提交前都不会实际被执行

#### 2.3 不保证原子性

事务执行过程中, 如果有指令执行失败，其它的指令仍然会被执行, 没有回滚,

### 3. 事务相关指令Multi、Exec、discard

![image-20230717112128273](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171121323.png)

1. 从输入Multi 命令开始，输入的命令都会依次进入命令队列中，但不会执行(类似Mysql的start transaction 开启事务)
2. 输入Exec 后，Redis 会将之前的命令队列中的命令依次执行(类似Mysql 的commit 提交事务)
3. 组队的过程中可以通过discard 来放弃组队(类似Mysql 的rollback 回顾事务)

<font color="red">Redis 事务和Mysql 事务本质是完全不同的</font>

快速入门

需求: 请依次向Redis 中, 添加三组数据, k1-v1 k2-v2 k3-v3, 要求使用Redis 的事务完成

![image-20230717112604416](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171126474.png)

注意事项和细节

1. 组队的过程中, 可以通过discard 来放弃组队

   ![image-20230717112656655](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171126710.png)

2. 如果在组队阶段报错, 会导致exec 失败, 那么事务的所有指令都不会被执行

   ![image-20230717112738066](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171127122.png)

   ![image-20230717112803800](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171128860.png)

3. 如果组队成功, 但是指令有不能正常执行的, 那么exec 提交, 会出现有成功有失败情况,也就是事务得到部分执行, 这种情况下, Redis 事务不具备原子性.

   ![image-20230717112845605](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171128675.png)

### 4. 事务冲突及解决方案

火车站抢票问题

![image-20230717162841892](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171628961.png)

解读：

1. 如果没有控制, 会造成超卖现象
2. 如果3 个指令, 都得到执行, 最后剩余的票数是 -2

#### 4.1 悲观锁

![image-20230717164400948](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171644054.png)

1. 悲观锁(Pessimistic Lock), 顾名思义，就是很悲观，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁
2. 这样别人 / 其它请求想拿这个数据就会block 直到它拿到锁。
3. 悲观锁是锁设计理念, 传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁.

#### 4.2 乐观锁

![image-20230717165313559](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307171653629.png)

1. 乐观锁(Optimistic Lock), 顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，所以不会上锁
2. 但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用<font color="yellow">版本号</font>等机制。
3. 乐观锁适用于多读的应用类型，这样可以提高吞吐量。Redis 就是利用这种check-and-set机制实现事务的
4. 乐观锁是锁设计理念

#### 4.3 watch【监控】 & unwatch

watch

1. 基本语法：watch key [key ...]
2. 在执行multi 之前，先执行watch key1 [key2],可以监视一个(或多个) key ，<font color="yellow">如果在事务执行【exec】之前这个(或这些) key 被其他命令所改动，那么事务将被打断</font>.
3. 这里可以结合乐观锁机制【在操作前对版本号进行校验】进行理解.

![image-20230718120718280](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307181207551.png)

unwatch

取消watch 命令对所有key 的监视。

如果在执行watch 命令后，exec 命令或discard 命令先被执行了的话，那么就不需要再执行unwatch 了

### 5. 火车票抢票

需求分析：

1. 一个user 只能购买一张票, 即不能复购
2. 不能出现超购,也是就多卖了
3. 不能出现火车票遗留问题/库存遗留, 即火车票不能留下

![image-20230718171828337](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307181718426.png)

### 版本1：基本购票流程【不考虑事务和并发】

```java
/**
 * 秒杀类：完成秒杀，抢购
 */
public class SecKillRedis {


    @Test
    public void testRedis(){
        Jedis jedis = new Jedis("192.168.8.230", 6379);
        jedis.auth("352420kobe24llq");
        System.out.println(jedis.ping());
        jedis.close();
    }

    //  秒杀方法
    public static boolean doSecKill(String uid, String ticketNo){
        if (uid == null || ticketNo == null){
            return false;
        }
        Jedis jedis = new Jedis("192.168.8.230", 6379);
        jedis.auth("352420kobe24llq");

        //  拼接票的库存 key
        String stockKey = "sk:" + ticketNo + ":ticket";

        //  拼接秒杀用户，放到 set 集合对应的 key 中，这个 set 集合存放多个 userId
        String userKey = "sk:" + ticketNo + ":user";

        //  获取到对应的票的库存
        String stock = jedis.get(stockKey);
        if (stock == null){
            System.out.println("秒杀还未开始，请等待");
            jedis.close();
            return false;
        }

        //  判断用户是否复购
        if (jedis.sismember(userKey, uid)){
            System.out.println(uid + " 不能重复秒杀...");
            jedis.close();
            return false;
        }

        //  判断如果票的数量是否还有剩余
        if (Integer.parseInt(stock) <= 0){
            System.out.println("票已售罄");
            jedis.close();
            return false;
        }

        //  可以购买
        //  1. stock - 1
        //  2. 将用户加入到 userKey中
        jedis.decr(stockKey);   //  库存 - 1
        jedis.sadd(userKey, uid);   //  用户买了 什么票(uid) 【放入到 set 集合中】
        System.out.println(uid  + "秒杀成功....");
        jedis.close();
        return true;
    }
}
```

```java
package com.llq.seckill.web;

public class SecKillServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  1. 请求时，模拟生成一个 userId
        String userId  = new Random().nextInt(10000) + "";
        //  2. 获取用户 够买票编号
        String ticketNo = request.getParameter("ticketNo");

        //  调用秒杀的方法
        boolean isOk = SecKillRedis.doSecKill(userId, ticketNo);
        response.getWriter().print(isOk);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
```

先放票

```bash
set sk:bj_cd:ticket 6
```

![image-20230718170823595](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307181708698.png)

### 抢票并发模拟，出现超卖问题

安装 ab工具模拟测试

```bash
sudo apt-get install apache2-utils
```

在 ~ 目录下，vim postfile

```bash
ticketNo=bj_cd&	# 带上&是防止还有其它参数
```

指令，测试前把Redis 的数据先重置一下

```bash
ab -n 1000 -c 100 -T application/x-www-form-urlencoded http://192.168.8.196:8080/second/secKillServlet
```

解读：

1. ab 是并发工具程序
2. -n 1000 表示一共发出1000 次http 请求
3. -c 100 表示并发时100 次, 你可以理解1000 次请求, 会在10 次发送完毕
4. -p ~/postfile 表示发送请求时, 携带的参数从当前目录的postfile 文件读取(这个你事先要准备好)
5. -T application/x-www-form-urlencoded 就是发送数据的编码是基于表单的url 编码
6. ~的含义: https://blog.csdn.net/m0_67401134/article/details/123973115

![image-20230718173612243](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307181736371.png)

![image-20230718173628836](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307181736923.png)

### 连接池技术

```java
package com.llq.seckill.util;

/**
 * 使用连接池方式来获取 Redis 连接
 */ 
public class JedisPoolUtil {

    //  volatile
    //  1. 线程的可见性：当一个线程去修改一个共享变量时，另外一个变量可以读取这个修改的值
    //  2. 顺序的一致性：禁止指令重排
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil() {

    }

    //  保证单例
    public static JedisPool getJedisPoolInstance() {
        if (jedisPool == null) {
            synchronized (JedisPoolUtil.class) {    //  加锁 ===> 保持同步
                if (jedisPool == null) {    //  单例的双重校验
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxTotal(200);
                    jedisPoolConfig.setMaxIdle(32);
                    jedisPoolConfig.setMaxWaitMillis(60 * 1000);
                    jedisPoolConfig.setBlockWhenExhausted(true);
                    jedisPoolConfig.setTestOnBorrow(true);  //  确保连接可用
                    jedisPool = new JedisPool(jedisPoolConfig, "192.168.8.230", 6379, 60000, "352420kobe24llq");
                }
            }
        }
        return jedisPool;
    }

    //  释放连接资源
    public static void release(Jedis jedis){
        if (jedis != null){
            jedis.close();  //  如果这个 jedis 是从 连接池获取的，这里 jedis.close() 就是将 jedis 连接，释放回连接池中
        }
    }
}
```

### 利用 Redis事务机制，解决超卖

乐观锁机制分析：【利用 <font color="yellow">**版本号** </font>来控制】

![image-20230719121439974](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191214120.png)

在事务开始前监视库存：

```java
package com.llq.seckill.redis;

/**
 * 秒杀类：完成秒杀，抢购
 */
public class SecKillRedis {


    @Test
    public void testRedis(){
        Jedis jedis = new Jedis("192.168.8.230", 6379);
        jedis.auth("352420kobe24llq");
        System.out.println(jedis.ping());
        jedis.close();
    }

    //  秒杀方法
    public static boolean doSecKill(String uid, String ticketNo){
        if (uid == null || ticketNo == null){
            return false;
        }

        JedisPool jedisPoolInstance = JedisPoolUtil.getJedisPoolInstance();

        Jedis jedis = jedisPoolInstance.getResource();

        //  拼接票的库存 key
        String stockKey = "sk:" + ticketNo + ":ticket";

        //  拼接秒杀用户，放到 set 集合对应的 key 中，这个 set 集合存放多个 userId
        String userKey = "sk:" + ticketNo + ":user";


        //  监控库存
        jedis.watch(stockKey);

        //  获取到对应的票的库存
        String stock = jedis.get(stockKey);
        if (stock == null){
            System.out.println("秒杀还未开始，请等待");
            jedis.close();  //  如果 jedis 是从连接池获取的，则这里的 close 就是 jedis 释放回连接池
            return false;
        }

        //  判断用户是否复购
        if (jedis.sismember(userKey, uid)){
            System.out.println(uid + " 不能重复秒杀...");
            jedis.close();
            return false;
        }

        //  判断如果票的数量是否还有剩余
        if (Integer.parseInt(stock) <= 0){
            System.out.println("票已售罄");
            jedis.close();
            return false;
        }


        //  使用事务，完成秒杀
        Transaction multi = jedis.multi();

        //  组队操作
        multi.decr(stockKey);
        multi.sadd(userKey, uid);

        //  执行
        List<Object> results = multi.exec();
        if (results == null || results.size() == 0){
            System.out.println("抢票失败...");
            jedis.close();
            return false;
        }

        // //  可以购买
        // //  1. stock - 1
        // //  2. 将用户加入到 userKey中
        // jedis.decr(stockKey);   //  库存 - 1
        // jedis.sadd(userKey, uid);   //  用户买了 什么票(uid) 【放入到 set 集合中】
        System.out.println(uid  + "秒杀成功....");
        jedis.close();
        return true;

    }
}
```

![image-20230719122622837](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191226922.png)

### 抢票并发模拟，出现库存遗留问题

这里我们把库存量设的较大, 为600

```bash
ab -n 1000 -c 300 -p ~/postfile -T application/x-www-form-urlencoded http://192.168.8.196:8080/second/secKillServlet
```

这里我们并发数变大-c 300

可以看到, 剩余票数为543, 并不是0

![image-20230719123211331](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191232424.png)

![image-20230719123016160](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191230240.png)

![image-20230719123504266](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191235342.png)

### LUA 脚本【在 Java 中写，然后发给 Redis，Redis内有 Lua脚本解释器】

1. Lua 是一个小巧的脚本语言，Lua 脚本可以很容易的被C/C++ 代码调用，也可以反过来调用C/C++的函数，Lua 并没有提供强大的库，一个完整的Lua 解释器不过200k，所以Lua 不适合作为开发独立应用程序的语言，而是作为嵌入式脚本语言。
2. 很多应用程序、游戏使用LUA 作为自己的嵌入式脚本语言，以此来实现可配置性、可扩展性。
3. 将复杂的或者多步的Redis 操作，写为一个脚本，一次提交给redis 执行，减少反复连接redis 的次数。提升性能。
4. LUA 脚本是类似Redis 事务，<font color="yellow">**有一定的原子性**，**不会被其他命令插队**</font>，可以完成一些redis 事务性的操作
5. Redis 的lua 脚本功能，只有在Redis 2.6 以上的版本才可以使用
6. 通过lua 脚本解决争抢问题，实际上是Redis 利用其 <font color="yellow">**单线程的特性**</font>，用 <font color="yellow">**任务队列**</font> 的方式解决多任务并发问题

![image-20230719124142483](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191241574.png)

```bash
# java 使用 + 拼接，而 lua 中使用 .. 来进行拼接
```

[Lua脚本语言_YYYMarshal的博客-CSDN博客](https://blog.csdn.net/qq_41286942/article/details/124161359)

```lua
local userid=KEYS[1]; -- 获取传入的第一个参数
local ticketno=KEYS[2]; -- 获取传入的第二个参数
local stockKey='sk:'..ticketno..:ticket; -- 拼接stockKey
local usersKey='sk:'..ticketno..:user; -- 拼接usersKey
local userExists=redis.call(sismember,usersKey,userid); -- 查看在redis 的
usersKey set 中是否有该用户
if tonumber(userExists)==1 then
return 2; -- 如果该用户已经购买, 返回2
end
local num= redis.call("get" ,stockKey); -- 获取剩余票数
if tonumber(num)<=0 then
return 0; -- 如果已经没有票, 返回0
else
redis.call("decr",stockKey); -- 将剩余票数-1
redis.call("sadd",usersKey,userid); -- 将抢到票的用户加入set
end
return 1 -- 返回1 表示抢票成功
```

```java
package com.llq.seckill.redis;

import com.llq.seckill.util.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/7/19 - 07 - 19 - 12:51
 * @Description: com.llq.seckill.redis
 * @version: 1.0
 */
public class SecKillRedisByLua {

    static String secKillScript = "local userid=KEYS[1];\r\n" +
            "local ticketno=KEYS[2];\r\n" +
            "local stockKey='sk:'..ticketno..\":ticket\";\r\n" +
            "local usersKey='sk:'..ticketno..\":user\";\r\n" +
            "local userExists=redis.call(\"sismember\",usersKey,userid);\r\n" +
            "if tonumber(userExists)==1 then \r\n" +
            "   return 2;\r\n" +
            "end\r\n" +
            "local num= redis.call(\"get\" ,stockKey);\r\n" +
            "if tonumber(num)<=0 then \r\n" +
            "   return 0;\r\n" +
            "else \r\n" +
            "   redis.call(\"decr\",stockKey);\r\n" +
            "   redis.call(\"sadd\",usersKey,userid);\r\n" +
            "end\r\n" +
            "return 1";

    //  使用 lua 脚本完成 秒杀的核心方法
    public static boolean doSecKill(String uid, String ticketNo){
        JedisPool jedisPoolInstance = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPoolInstance.getResource();
        //  这句哈就是将 lua 脚本加载到 内存
        String sha1 = jedis.scriptLoad(secKillScript);
        //  根据指定的 sha1 校验码，执行缓存在服务器的脚本
        Object result = jedis.evalsha(sha1, 2, uid, ticketNo);
        String resString = String.valueOf(result);
        if ("0".equals(resString)){
            System.out.println("票已售罄");
            jedis.close();
            return false;
        }

        if ("2".equals(resString)){
            System.out.println("不能重复购买");
            jedis.close();
            return false;
        }


        if ("1".equals(resString)){
            System.out.println("恭喜，抢到票了");
            jedis.close();
            return true;
        }else {
            System.out.println("购票失败");
            jedis.close();
            return false;
        }
    }
}
```



## 13. 主从复制

![image-20230719164821450](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191648568.png)

描述了主机数据更新后, 自动同步到备机的master/slaver 机制

Master 以写为主，Slaver 以读为主

好处：

- 读写分离, 提升效率
- 容灾快速恢复
- 主从复制, 要求是1 主多从, 不能有多个Master【如果有多个主服务器Master,那么slaver 不能确定和哪个Master 进行同步, 出现数据紊乱)】
- 要解决主服务器的高可用性, 可以使用Redis 集群

搭建：1组 2 从

- master 6379
- slave：6380，6381

```bash
include /home/ronnie/redis.conf
pidfile /var/run/redis_6379.pid
port 6379
dbfilename dump6379.rdb
```

![image-20230719170401480](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191704557.png)

启动三台服务

```bash
info replication # 查看是 master / slave
```

![image-20230719175429552](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191754638.png)

设置 slave

```bash
slaveof 127.0.0.1 6379 # 设置 slave
```

原理：

![image-20230719183027702](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191830809.png)

### 1. 一主二仆

1. 如果从服务器down 了, 重新启动, 仍然可以获取Master 的最新数据
2. 如果主服务器down 了, 从服务器并不会抢占为主服务器, 当主服务器恢复后, 从服务器仍然指向原来的主服务器.

### 2. 薪火相传【缓解主服务器的压力】

![image-20230719183708860](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307191837933.png)

1. 上一个Slave 可以是下一个slave 的Master，Slave 同样可以接收其他slaves 的连接和同步请求，那么该slave 作为了链条中下一个的master, 可以有效减轻master 的写压力,<font color="yellow">**去中心化** </font>降低风险
2. 用 slaveof <master_ip><master_port>
3. 风险是一旦某个slave 宕机，后面的slave 都没法同步
4. 主机挂了，从机还是从机，无法写数据了

### 3. 反客为主

```bash
slaveof no one # 将从机变成主机
```

### 4. 哨兵模式【sentinel】

反客为主的自动版，能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库

老韩实验：

1. 调整为一主二仆模式，6379 带着6380、6381 , 根据前面讲解的调整即可

2. 创建/hspredis/sentinel.conf , 名字不能乱写, 按照指定的来

   ```bash
   # redis_master：为监控对象起的服务器名称
   # 1：表示至少有多少个哨兵同意迁移的数量 ===> 这里配置1：表示只要有1个哨兵同意迁移就可以切换
   # 启动哨兵，默认端口为：26379
   sentinel monitor redis_master 127.0.0.1 6379 1
   sentinel auth-pass redis_master 352420kobe24llq # 主密码，不设置的话不能动态切换
   ```

   ![image-20230720101536800](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201015909.png)

```bash
/usr/local/bin/redis-sentinel /home/ronnie/redis6.26/sentinel.conf	# 启动  sentinel（26379）
```

![image-20230720102253802](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201022892.png)

将 6379 down 掉 ===> 将 master 替换成了 6381

![image-20230720110240219](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201102310.png)

后面当 6379 恢复后，会变成一个 slave

![image-20230720113108178](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201131254.png)

注意事项和细节

1. 在哨兵模式下，主机down 后的执行流程分析

   ![image-20230720113238990](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201132074.png)

解读上图- 哨兵如何在从机中, 推选新的Master 主机, 选择的条件依次为:

1. 优先级在redis.conf 中默认：replica-priority 100，<font color="red">**值越小优先级越高**</font>

2. 偏移量是指获得原主机数据的量, 数据量最全的优先级高

3. 每个redis 实例启动后都会随机生成一个40 位的runid, 值越小优先级越高

   ![image-20230720113751139](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201137223.png)

## 14. 集群【高可用性】

生产环境的实际需求和问题

- 容量不够，redis 如何进行扩容？
- 并发写操作， redis 如何分摊？
- 主从模式，薪火相传模式，主机宕机，会导致ip 地址发生变化，应用程序中配置需要修改对应的主机地址、端口等信息

![image-20230720114046490](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201140587.png)

解读：

1. 客户端请求先到代理服务器
2. 由代理服务器进行请求转发到对应的业务处理服务器
3. 为了高可用性, 代理服务、A 服务、B 服务、C 服务都需要搭建主从结构(至少是一主一从), 这样就需求搭建至少8 台服务器
4. 这种方案的缺点是: 成本高，维护困难, 如果是一主多从, 成本就会更高.

redis3.0 提供解决方案-无中心化集群配置

![image-20230720114227471](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201142555.png)

解读上图：

1. 各个Redis 服务仍然采用主从结构
2. 各个Redis 服务是连通的, <font color="yellow">任何一台服务器, 都可以作为请求入口</font>
3. 各个Redis 服务器因为是连通的, 可以进行请求转发
4. 这种方式, 就无中心化集群配置, 可以看到，只需要6 台服务器即可搞定
5. 无中心化集群配置, 还会 <font color="yellow">根据key 值, 计算slot , 把数据分散到不同的主机</font>, 从而缓解单个主机的存取压力
6. Redis 推荐使用无中心化集群配置
7. 在实际生成环境各个Redis 服务器, 应当部署到不同的机器(防止机器宕机, 主从复制失效)
8. 后面老师演示时, 为了教学方便, 是在一台机器(如果启6 个虚拟Linux, 老韩电脑撑不住),

集群介绍

1. Redis 集群实现了对Redis 的水平扩容，即启动N 个redis 节点，将整个数据库分布存储在这N 个节点中，每个节点存储总数据的1/N。
2. Redis 集群通过分区（partition）来提供一定程度的可用性（availability）： 即使集群中有一部分节点失效或者无法进行通讯， 集群也可以继续处理命令请求

Redis 集群搭建 【图中可以看作是：6个结点】

![image-20230720114511006](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201145083.png)

将rdb、aof 文件都删除掉

![image-20230720115909639](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201159710.png)

```bash
include /home/ronnie/redis6.26/redis.conf
pidfile "/var/run/redis_6379.pid"
port 6379
dbfilename "dump6379.rdb"
cluster-enabled yes	# 打开集群模式
cluster-config-file nodes-6379.conf	# 设定节点配置文件名
cluster-node-timeout 15000	# 设定节点失联时间，超过该时间（毫秒），集群自动进行主从切换
```

命令行替换指令：

```bash
:%s/6379/6380	# 替换命令
```

下面启动6个服务

![image-20230720122257375](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201222437.png)

看到结点文件也生成了

![image-20230720122815735](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201228794.png)

将 6 个 节点合成一个集群

![image-20230720123011133](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201230197.png)

![image-20230720123025881](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201230939.png)

将 6 个节点合成一个集群的指令：

```bash
redis-cli -a 352420kobe24llq --cluster create --cluster-replicas 1 192.168.8.228:6379 192.168.8.228:6380 192.168.8.228:6381 192.168.8.228:6389 192.168.8.228:6390 192.168.8.228:6391
```

![image-20230720132442718](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201324796.png)

注意事项和细节：

1. 组合之前，请确保所有redis 实例启动后，nodes-xxxx.conf 文件都生成正常
2. 此处不要用127.0.0.1, 请用真实IP 地址, 在真实生产环境, IP 都是独立的.
3. replicas 1 采用最简单的方式配置集群，一台主机，一台从机，正好三组
4. 搭建集群如果没有成功, 把sentinel 进程kill 掉, 再试一下
5. 分析主从对应关系

集群方式登录：

```bash
redis-cli -c -p 6379	# 注意 -c 一定要有
# 登录后，要验证密码
auth 密码
```

查看集群信息：

```bash
cluster nodes
```

![image-20230720134354252](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201343315.png)

![image-20230720164921488](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201649557.png)

一个Redis 集群包含16384 个插槽（hash slot），编号从0-16383, Reids 中的每个键都属于这16384 个插槽的其中一个

集群使用公式CRC16(key) % 16384 来计算键key 属于哪个槽， 其中CRC16(key) 语句用于计算键key 的CRC16 校验和

![image-20230720164954888](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201649952.png)

集群中的每个节点负责处理一部分插槽。举个例子， 如果一个集群可以有主节点， 其

- 节点A 负责处理0 号至5460 号插槽。
- 节点B 负责处理5461 号至10922 号插槽。
- 节点C 负责处理10923 号至16383 号插槽

在集群中录入值

1. 在redis 每次录入、查询键值，redis 都会计算出该key 应该送往的插槽，如果不是该客户端对应服务器的插槽，redis 会告知应前往的redis 实例地址和端口。

2. redis-cli 客户端提供了 <font color="yellow">–c</font> 参数实现 <font color="yellow">自动重定向</font>。

3. 如redis-cli -c –p 6379 登入后，再录入、查询键值对可以自动重定向

   ![image-20230720165720430](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201657493.png)

4. 不在一个 slot 下的键值，是不能使用 mget，mset 等多键操作

   ![image-20230720165810700](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201658760.png)

5. 可以通过{}来定义组的概念，从而使key 中{}内相同内容的键值对放到一个slot 中去

   ![image-20230720165841469](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201658527.png)

查询集群中的值

- 指令: CLUSTER KEYSLOT <key> 返回key 对应的slot 值
- 指令: CLUSTER COUNTKEYSINSLOT <slot> 返回slot 有多少个key
- 指令: CLUSTER GETKEYSINSLOT <slot><count> 返回count 个slot 槽中的键

![image-20230720170002527](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201700605.png)

集群故障恢复

1. 如果主节点下线, 从节点会自动升为主节点(注意15 秒超时, 再观察比较准确)

   ![image-20230720171522921](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201715004.png)

2. 主节点恢复后，主节点回来变成从机

   ![image-20230720171552904](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201715977.png)

   注意：这里在配置文件中要加上 masterauth 字段，后接密码

   ```bash
   masterauth 352420kobe24llq
   ```

   ![image-20230720174645425](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201746520.png)

   现在如果 80 又从重新恢复正常的话，那么它就会变成了 slave 了

   ![image-20230720175451391](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201754469.png)



如果所有某一段插槽的 <font color="yellow">**主从节点都宕掉**</font>，Redis 服务是否还能继续, 要根据不同的配置而言

- 如果某一段插槽的主从都挂掉，而cluster-require-full-coverage 为yes ，那么，整个集群都挂掉

- 如果某一段插槽的主从都挂掉，而cluster-require-full-coverage 为no , 那么, 只是该插槽数据不能使用，也无法存储

- redis.conf 中的参数cluster-require-full-coverage

  ![image-20230720175814841](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201758914.png)

### 集群的Jedis开发

1. 即使连接的不是主机，集群会自动切换主机存储。主机写，从机读
2. 无中心化主从集群。无论从哪台主机写的数据，其他主机上都能读到数据

```java
package com.llq.jedis;

public class JedisCluster_ {
    public static void main(String[] args) {
//老韩解读
//1. 这里set 也可以加入多个入口
//2. 因为没有配置日志, 会有提示, 但是不影响使用
//3. 如果使用集群，需要把集群相关的端口都打开,
//否则会报No more cluster attempts left
//4. JedisCluster 看源码有多个构造器, 也可以直接传入一个HostAndPort
/**
 * HostAndPort hostAndPort = new HostAndPort("192.168.198.130", 6379);
 * JedisCluster jedisCluster = new JedisCluster(hostAndPort);
 */
        Set<HostAndPort> set = new HashSet<HostAndPort>();
        set.add(new HostAndPort("192.168.8.228", 6379));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        
        JedisCluster jedisCluster = new JedisCluster(set, 5000, 5000, 3, "352420kobe24llq", jedisPoolConfig);

        jedisCluster.set("bj", "dkdld0901");
        String bj = jedisCluster.get("bj");
        System.out.println("bj= " + bj);
    }
}
```

### 优缺点

1、实现扩容
2、分摊压力
3、无中心配置相对简单

1、多键操作是不被支持的
2、多键的Redis 事务是不被支持的。lua 脚本不被支持
3、由于集群方案出现较晚，很多公司已经采用了其他的集群方案，而其它方案想要迁移至redis cluster，需要整体迁移而不是逐步过渡，复杂度较大

## 15. Redis 应用问题 & 解决方案

### 1. 缓存穿透【key 不存在】

![image-20230720182047549](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201820626.png)

缓存穿透的原因

1. key 对应的数据在数据源并不存在，每次针对此key 的请求从缓存获取不到，请求都会压到数据源, 可能压垮数据源
2. 比如: 用一个不存在的用户id 获取用户信息，不论缓存还是数据库都没有，若黑客利用此漏洞进行攻击可能压垮数据库
3. 也就是说：如果从存储层查不到数据则不会写入缓存，这将导致这个不存在的数据每次请求都要到存储层去查询, 失去了缓存的意义

缓存穿透的现象/表象  

1. 应用服务器压力变大
2. Redis 命中率降低
3. 一直查数据库

解决方法

1. 对<font color="yellow">空值缓存</font>

   如果一个查询返回的数据为空，我们仍然把这个空结果（null）进行缓存，设置空结果的过期时间应该短些，最长不超过五分钟

2. 设置可访问的名单(<font color="yellow">白名单</font>)

   定义一个可以访问的名单，每次访问和白名单的id 进行比较，如果访问id 不在白名单里面，进行拦截，不允许访问, 比如使用 bitmaps 实现.

3. 采用<font color="yellow">布隆过滤器</font>

   布隆过滤器可以用于检索一个元素是否在一个集合中。它的优点是空间效率和查询时间都远远超过一般的算法，缺点是有一定的误识别率和删除困难

4. 进行<font color="yellow">实时监控</font>

   当发现Redis 的命中率开始急速降低, 需要排查访问对象和访问的数据, 和运维人员配合,可以设置黑名单限制服务

### 2. 缓存击穿【key存在，但在 Redis中过期 ===> 某一个 key】

![image-20230720194344054](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201943141.png)

缓存击穿的原因

1. key 对应的数据存在，但在redis 中过期，此时若有大量并发请求过来，这些请求发现缓存过期, 会从后端DB 加载数据并回设到缓存，这时大并发的请求可能会瞬间把后端DB 压垮
2. 比如某个热点数据, 可能会在某些时间点, 被超高并发地访问, 容易出现缓存击穿

--缓存击穿的现象/表象

1. 数据库访问压力瞬时增加
2. Redis 里面没有出现大量key 过期
3. Redis 正常运行状态, 但是数据库可能瘫痪了

解决方案：

1. 预先 <font color="yellow">设置热门数据</font>

   在redis 高峰访问之前，把一些热门数据提前存入到redis 里面，加大这些热门数据key 的时长

2. <font color="yellow">实时调整</font>

   现场监控哪些数据热门，实时调整key 的过期时长

3. <font color="yellow">使用锁</font>

   ![image-20230720194745100](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201947187.png)

   就是在缓存失效的时候（判断拿出来的值为空），不是立即去load db

   先使用缓存工具的某些带成功操作返回值的操作（比如Redis 的SETNX）去set 一个mutex key

   - 当操作返回成功时，再进行load db 的操作，并回设缓存,最后删除mutex key；
   - 当操作返回失败，证明有线程在load db，当前线程睡眠一段时间再重试整个get 缓存的方法

### 3. 缓存雪崩【key存在，但在 Redis中过期 ===> 针对很多 key】

![image-20230720194924492](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201949572.png)

![image-20230720195108545](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307201951637.png)

缓存雪崩的原因

1. key 对应的数据存在，但在redis 中过期，此时若有大量并发请求过来，这些请求发现缓存过期一般都会从后端DB 加载数据并回设到缓存
2. 这个时候大并发的请求可能会瞬间把后端DB 压垮
3. 缓存雪崩与缓存击穿的区别在于这里针对很多key 缓存，前者则是某一个key

缓存雪崩的现象/表象

1. 数据库访问压力变大, 服务器崩溃
2. 在极短时间内, 访问大量Key, 而这些Key 集中过期

解决方案 / 思路

1. <font color="yellow">构建多级缓存架构</font>

   nginx 缓存+ redis 缓存+其他缓存（ehcache 等） , 这种方式开发/维护成本较高

2. 使用 <font color="Yellow">锁或队列</font>

   用加锁或者队列的方式来保证不会有大量的线程对数据库一次性进行读写，从而避免失效时大量的并发请求落到底层存储系统上。不适用高并发情况

3. 设置 <font color="yellow">过期标志更新缓存</font>

   记录缓存数据是否过期，如果过期会触发通知另外的线程在后台去更新实际key 的缓存。

4. 将缓存 <font color="yellow">失效时间分散 </font>开

   比如我们可以在原有的失效时间基础上增加一个随机值，比如1-5 分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件

### 4. 分布式锁

问题描述：

1. 单体单机部署的系统被演化成分布式集群系统后
2. 由于分布式系统多线程、多进程并且分布在不同机器上，这将使原单机部署情况下的并发控制锁策略失效
3. 单纯的Java API 并不能提供分布式锁的能力
4. 为了解决这个问题就需要一种 <font color="yellow">跨JVM **的互斥机制** </font>来控制共享资源的访问，这就是分布式锁要解决的问题
5. 示意图(说明: 我们探讨的分布式锁是针对分布式项目/架构而言[..])

![image-20230721103738674](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211037786.png)

![image-20230721103903611](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211039719.png)

#### 1. 实现方案

1. 基于数据库实现分布式锁
2. 基于缓存（Redis 等）
3. 基于Zookeeper

每一种分布式锁解决方案都有各自的优缺点：

1. 性能：redis 最高
2. 可靠性：zookeeper 最高
3. 我们讲解基于Redis 实现分布式锁

#### 2. Redis 实现分布式锁

1. 指令 setnx key value

   ```bash
   setnx(not exist) key value	# 上锁 / 加锁
   ```

2. 指令：del key

   ```bash
   del key # 释放锁
   ```

3. 指令：expire key seconds

   ```bash
   expire key seconds # 设置过期时间，防止死锁
   ```

4. 指令：ttl key

   ```bash
   ttl key # 查看某个锁 key 的过期时间
   ```

5. 指令: set key value nx ex seconds

   ```bash
   set key value nx ex seconds	# 设置锁的同时, 指定该锁的过期时间，防止死锁
   
   # 这个指令是原子性的，防止setnx key value / expire key seconds 两条指令, 中间执行被打断.
   ```

代码实现：

1. 需求说明/图解, 编写代码, 实现如下功能
2. 在SpringBoot+Redis 实现分布式锁的使用
3. 获取锁, key 为lock, 示意图

![image-20230721110337125](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211103223.png)

第一种情况：

- 如果获取到该分布式锁：就获取key 为num 的值, 并对num+1, 再更新num 的值, 并释放锁(key 为lock)
- 如果获取不到key 为num 的值, 就直接返回

第二种情况：

- 如果没有获取到该分布式锁
- 休眠100 毫秒, 再尝试获取

测试：

```java
@GetMapping("testLock")
public void testLock() {
    //1 获取锁，setnx
    Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "ok");
    //2 获取锁成功、查询num 的值
    if (lock) {
        Object value = redisTemplate.opsForValue().get("num");
        //2.1 判断num 为空return, 说明: 这里的isEmpty 到时替换成
        //if (value == null || !StringUtils.hasText(value.toString())) {
        // return;
        //}
        if (StringUtils.isEmpty(value)) {
            return;
        }
        //2.2 有值就转成成int
        int num = Integer.parseInt(value + "");
        //2.3 把redis 的num 加1
        redisTemplate.opsForValue().set("num", ++num);
        //2.4 释放锁，del
        redisTemplate.delete("lock");
    } else {
        //3 获取锁失败、每隔0.1 秒再获取
        try {
            Thread.sleep(100);
            testLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

ab 工具测试：

```bash
ab -n 1000 -c 100 http://192.168.8.161:8080/redisTest/testLock
```

#### 3. 优化-设置锁的过期时间, 防止死锁

```java
Boolean lock =
redisTemplate.opsForValue().setIfAbsent("lock", "ok", 3, TimeUnit.SECONDS);
```

#### 4. 优化-UUID 防误删锁【防止 A 用户去删 B用户的锁】

![image-20230721121859728](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211218873.png)

思路分析：

1. 在获取锁的时候, 给锁设置的值是唯一的uuid
2. 在释放锁时,判断释放的锁是不是同一把锁
3. 造成这个问题的本质原因, 是因为 <font color="yellow">**删除操作缺乏原子性**</font>

```java
@GetMapping("testLock")
public void testLock() {
    //1 获取锁，setnx
    //得到一个uuid 值，作为锁的值
    String uuid = UUID.randomUUID().toString();
    Boolean lock =
        redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
    //2 获取锁成功、查询num 的值
    if (lock) {
        Object value = redisTemplate.opsForValue().get("num");
        //2.1 判断num 为空return
        if (StringUtils.isEmpty(value)) {
            return;
        }
        //2.2 有值就转成成int
        int num = Integer.parseInt(value + "");
        //2.3 把redis 的num 加1
        redisTemplate.opsForValue().set("num", ++num);
        //2.4 释放锁，del
        //为了防止误删锁, 进行判断
        //判断当前这个锁是不是前面获取到的锁, 相同才进行删除/释放
        if (uuid.equals((String) redisTemplate.opsForValue().get("lock"))) {
            redisTemplate.delete("lock");
        }
        //redisTemplate.delete("lock");
    } else {
        //3 获取锁失败、每隔0.1 秒再获取
        try {
            Thread.sleep(100);
            testLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

#### 5. 优化-LUA 脚本保证删除原子性

```java
//编写方法,使用Redis分布式锁,完成对 key为num的+1操作
@GetMapping("/lock")
public void lock() {

    //得到一个uuid值,作为锁的值
    String uuid = UUID.randomUUID().toString();

    //1. 获取锁/设置锁 key->lock : setnx
    Boolean lock =
        redisTemplate.opsForValue().setIfAbsent("lock", uuid, 3, TimeUnit.SECONDS);
    if (lock) {//true, 说明获取锁/设置锁成功
        //这个key为num的数据，事先要在Redis初始化
        Object value = redisTemplate.opsForValue().get("num");
        //1.判断返回的value是否有值
        if (value == null || !StringUtils.hasText(value.toString())) {
            return;
        }
        //2.有值，就将其转成int
        int num = Integer.parseInt(value.toString());
        //3.将num+1,再重新设置回去
        redisTemplate.opsForValue().set("num", ++num);
        //释放锁-lock


        //为了防止误删除其它用户的锁,先判断当前的锁是不是前面获取到的锁,如果相同，再释放

        //=====使用lua脚本, 控制删除原子性========
        // 定义lua 脚本
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        // 使用redis执行lua执行
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        // 设置一下返回值类型 为Long
        // 因为删除判断的时候，返回的0,给其封装为数据类型。如果不封装那么默认返回String 类型，
        // 那么返回字符串与0 会有发生错误。
        redisScript.setResultType(Long.class);
        // 第一个是script 脚本 ，第二个需要判断的key，第三个就是key所对应的值
        // 老韩解读 Arrays.asList("lock") 会传递给 script 的 KEYS[1] , uuid 会传递给ARGV[1] , 其它的小伙伴应该很容易理解
        redisTemplate.execute(redisScript, Arrays.asList("lock"), uuid);


        //if (uuid.equals((String) redisTemplate.opsForValue().get("lock"))) {
        //    //...
        //    redisTemplate.delete("lock");
        //}

        //redisTemplate.delete("lock");

    } else { //获取锁失败,休眠100毫秒，再重新获取锁/设置锁

        try {
            Thread.sleep(100);
            lock();//重新执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

注意事项和细节

![image-20230721151935751](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211519889.png)

1. 定义锁的key, key 可以根据业务, 分别设置，比如操作某商品, key 应该是为每个sku 定义的，也就是每个sku 有一把锁
2. 为了确保分布式锁可用，要确保锁的实现同时满足以下四个条件
   - 互斥性。在任意时刻，只有一个客户端能持有锁。
   - 不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
   - 加锁和解锁必须是同一个客户端，A 客户端不能把B 客户端加的锁给解了【UUID】
   - 加锁和解锁必须具有原子性

## 16. Redis新功能（ACL：Redis Access Control List）

[ACL | Redis](https://redis.io/docs/management/security/acl/)

基本介绍

1. Redis ACL 是Access Control List（访问控制列表）的缩写，该功能根据可以执行的命令和可以访问的键来限制某些连接
2. 在Redis 5 版本之前，Redis 安全规则只有密码控制还有通过rename 来调整高危命令，比如flushdb ， KEYS* ， shutdown 等
3. Redis 6 则提供ACL 的功能对用户进行更细粒度的权限控制：
   - 接入权限:用户名和密码
   - 可以执行的命令
   - 可以操作的KEY

常用命令

1. acl list 命令展现用户权限列表

   ![image-20230720200413655](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307202004729.png)

2. acl cat 命令

   1. 查看添加权限指令类别

      ```bash
      acl cat
      ```

   2. 带上参数类型名, 可以查看该类型可以执行的指令

      ```bash
      acl cat string
      ```

   3. acl whoami 命令查看当前用户

      ```bash
      acl whoami
      # default
      ```

   4. acl setuser 命令创建和编辑用户ACL

3. ACL 规则说明

   | ACL规则               |               |                                                              |
   | --------------------- | ------------- | ------------------------------------------------------------ |
   | 类型                  | 参数          | 说明                                                         |
   | 启动和禁用用户        | on            | 激活某用户账号                                               |
   |                       | off           | 禁用某用户账号。注意，已验证的连接仍然可以工作。 如果默认用户被标记为off,则新连接将在未进行身份 验证的情况下启动，并要求用户使用AUTH选项发送 AUTH或HELLO，以便以某种方式进行身份验证。 |
   | 权限的添加删除        | +\<command>   | 将指令添加到用户可以调用的指令列表中                         |
   |                       | -\<command>   | 从用户可执行指令列表移除指令                                 |
   |                       | +@\<category> | 添加该类别中用户要调用的所有指令．有效类别为 admin.@set.@sortedset…等，通过调用ACL CAT 命令查看完整列表。特殊类别@all表示所有命令，包 括当前存在于服务器中的命令，以及将来将通过模块 加载的命令。 |
   |                       | -\<actegory>  | 从用户可调用指令中移除类别                                   |
   |                       | allcommands   | +@ll的别名                                                   |
   |                       | nocommand     | -@all的别名                                                  |
   | 可操作键的添加 或删除 | -\<pattern>   | 添加可作为用户可操作的键的模式。例如~*允许所有 的键          |

通过命令创建新用户默认权限

```bash
acl setuser tom
```

设置有用户名、密码、ACL 权限、并启用的用户

```bash
# ~cached:* : 表示操作的key 是以cached: 开头的
acl setuser jack on >123456 ~cached:* +get
```

![image-20230721153656378](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211536493.png)

给 jack 增加权限

````bash
# 用 default 登录，给 jack 分配 set 权限
acl setuser jack +set
````

![image-20230721154156982](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211541090.png)

删除用户：

```bash
acl deluser jack

# 再来查看下
 acl list
```

## 17 IO多线程

Redis 6 加入多线程, Redis 的 <font color="yellow">**多线程部分只是用来处理网络数据的读写和协议解析**</font>，执行命令仍然是单线程。之所以这么设计是不想因为多线程而变得复杂，需要去控制key、lua、事务，LPUSH/LPOP 等等的并发问题。整体的设计大体如下:

![image-20230721154825469](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307211548587.png)

另外，多线程IO 默认也是不开启的，需要再配置文件redis.conf 中配置

```bash
io-threads-do-reads yes
io-threads 4
```

## 18. 工具支持 cluster

另外官方redis-benchmark 工具开始支持cluster 模式了, 通过多线程的方式对多个分片进行压测





