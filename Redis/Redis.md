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

type key ：查看你的key 是什么类型

del【阻塞式】 key ： 删除指定的key 数据

unlink【非阻塞式】 key：根据value 选择非阻塞删除【仅将keys 从keyspace 元数据中删除，真正的删除会在后续异步操作】

expire key 10 ： 10 秒钟：为给定的key 设置过期时间

ttl key 查看还有多少秒过期，-1 表示永不过期，-2 表示已过期

### 6.2.  对 db 操作

select： 命令切换数据库

1. redis 安装后，默认有16 个库, 0-15
2. 默认操作的是redis 的 0 号库
   - dbsize：查看当前数据库的key 的数量
   - flushdb：清空当前库
   - flushall：清空全部库

### 6.3 Redis 五大数据类型/结构

#### 操作文档

官方文档: https://redis.io/commands

中文文档: http://redisdoc.com/

#### Redis 数据存储格式

一句话: redis 自身是一个Map，其中所有的数据都是采用key : value 的形式存储

- key 是字符串
- value 是数据，数据支持多种类型/结构

#### Redis 数据类型-5 种常用

##### 1. string

1. String 是Redis 最基本的类型，一个key 对应一个value。
2. String 类型是二进制安全的, Redis 的string 可以包含任何数据。比如jpg 图片或者序列化
    的对象。
3. String 类型是Redis 基本的数据类型，一个Redis 中字符串value 最多可以是512M

String 常用指令

- set <key><value>添加键值对
- get <key>查询对应键值
- append <key><value>将给定的<value> 追加到原值的末尾
- strlen <key>获得值的长度
- setnx <key><value>只有在key 不存在时设置key 的值
- incr <key> 将key 中储存的数字值(字符串)增1， 只能对数字值操作，如果为空，新增值为 1
- decr <key> 将key 中储存的数字值(字符串)减1 ， 只能对数字值操作，如果为空，新增值为-1
- incrby / decrby <key><步长>将key 中储存的数字值增减。自定义步长
- mset <key1><value1><key2><value2> ....., 同时设置一个或多个key-value 对
- mget <key1><key2><key3> ..... 同时获取一个或多个value
- msetnx <key1><value1><key2><value2> ..... 同时设置一个或多个key-value 对，当且仅当所有给定key 都不存在, 原子性，有一个失败则都失败
- getrange <key><起始位置><结束位置> , 获得值的范围，类似 java 中的 substring
- setrange <key><起始位置><value> 用<value> 覆写<key>所储存的字符串值，从<起始位置>开始(索引从0 开始)。
- setex <key><过期时间><value> 设置键值的同时，设置过期时间，单位秒。
- getset <key><value> , 以新换旧，设置了新值同时获得旧值

##### 2. list

一句话: list 类型, 保存多个数据，底层使用双向链表存储结构实现

![jiji-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302202342376.jpg)

1. Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头
    部(左边)或者尾部(右边)。
2. 底层是个双向链表，对两端的操作性能高，通过索引下标的操作中间的节点性能较差

list 指令一览：http://redisdoc.com/list/index.html

- lpush/rpush <key><value1><value2><value3> .... 从左边/右边插入一个或多个值
- lpop/rpop <key>从左边/右边吐出一个值
- rpoplpush <key1><key2>从<key1>列表右边吐出一个值，插到<key2>列表左边
- lrange <key><start><stop> 按照索引下标获得元素(从左到右)
- lrange mylist 0 -1 0 左边第一个，-1 右边第一个，（0-1 表示获取所有）
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

##### 3. set

一句话: set 提供的功能与list 类似是一个列表的功能，特殊之处在于set 是可以自动排重的, 即值是不允许重复的

set 常用指令&使用

set 指令一览：http://redisdoc.com/set/index.html

set 指令操作示意图

![888-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302202355192.jpg)

sadd <key><value1><value2> ..... 将一个或多个member 元素加入到集合key 中，已经存在的member 元素将被忽略

![koko-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302210004712.jpg)

- smembers <key>取出该集合的所有值。
- sismember <key><value>判断集合<key>是否为含有该<value>值，有1，没有0
- scard<key>返回该集合的元素个数。
- srem <key><value1><value2> .... 删除集合中的某个元素。
- spop <key>随机从该集合中吐出一个值。
- srandmember <key><n>随机从该集合中取出n 个值。不会从集合中删除。
- smove <source><destination>value 把集合中一个值从一个集合移动到另一个集合
- sinter <key1><key2>返回两个集合的交集元素。
- sunion <key1><key2>返回两个集合的并集元素。
- sdiff <key1><key2>返回两个集合的差集元素(key1 中的，不包含key2 中的)

##### 4. hash

Redis hash 是一个键值对集合，hash 适合用于存储对象， 类似Java 里面的Map<String,Object>

Redis hash 存储结构简单示意图

![oopp-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302210007759.jpg)

hash 常用指令&使用

hash 指令一览：http://redisdoc.com/hash/index.html

hash 指令操作示意图

![lll-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302210020506.jpg)

hset <key><field><value>给<key>集合中的<field>键赋值<value>

hget <key1><field>从<key1>集合<field>取出value

hmset <key1><field1><value1><field2><value2>... 批量设置hash 的值

h<font color="yellow">m</font>get <key1><field1> <field2>... 批量取出hash 的filed 值

h<font color="Yellow">exists</font><key1><field>查看哈希表key 中，给定域field 是否存在

hkeys <key>列出该hash 集合的所有field

hvals <key>列出该hash 集合的所有value

hincrby <key><field><increment>为哈希表key 中的域field 的值加上增量1 -1

hsetnx <key><field><value>将哈希表key 中的域field 的值设置为value ，当且仅当域field 不存在

##### 5. 有序集合Zset(sorted set)

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

![ppl](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302211106492.jpg)

zadd <key><score1><value1><score2><value2>… 将一个或多个member 元素及其score 值加入到有序集key 当中。

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

## 8. 发布和订阅

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

#### 1. 一个发布者，多个订阅者

主要应用：通知、公告

可以作为 消息 <font color="yellow">**队列**</font> 或者 消息 <font color="yellow">**管道**</font>

![1ko-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261901432.jpg)

#### 2. 多个发布者，一个订阅者

各应用程序作为Publisher 向Channel 中发送消息，Subscriber 端收到消息后执行相应的业务逻辑，比如写数据库，显示..

主要应用：

- 排行榜
- 投票
- 计数

![qqq-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261903350.jpg)

#### 3. 多个发布者，多个订阅者

可以向不同的Channel 中发送消息，由不同的Subscriber 接收

主要应用：群聊、聊天

![mimi-Redis](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202302261904666.jpg)

### 4. 命令行实现发布和订阅

#### 发布订阅操作

1. PUBLISH channel msg
   - 将信息message 发送到指定的频道channel
2. SUBSCRIBE channel [channel ...]
   - 订阅频道，可以同时订阅多个频道
3. UNSUBSCRIBE [channel ...]
   - 取消订阅指定的频道, 如果不指定频道，则会取消订阅所有频道
4. PSUBSCRIBE pattern [pattern ...]
   - 订阅一个或多个符合给定模式的频道，每个模式以* 作为匹配符，比如
     - it* 匹配所有以it 开头的频道( it.news 、it.blog 、it.tweets 等等)
     - news.* 匹配所有以news. 开头的频道( news.it 、news.global.today 等等)，诸如此类
5. PUNSUBSCRIBE [pattern [pattern ...]]
   - 退订指定的规则, 如果没有参数则会退订所有规则

#### 快速入门



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







