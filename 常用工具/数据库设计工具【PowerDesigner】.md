# 数据库设计工具【PowerDesigner】

## 1. 数据库建模

主要功能

- 数据库建模
  - 利用 ER关系图创建 "概念数据模型" CDM【Conceptual Data Model】
  - 根据 CDM 产生基于特定数据库的 "物理数据模型" PDM【Physical Data Model】
  - 根据 PDM 产生为 SQL语句并可以文件形式存储
  - 由已存在的数据库或者SQL语句反向生成 PDM、CDM
- 面向对象设计（UML建模）
  - 用例图、类图、对象图
  - 时序图、状态图、协作图
  - 状态图、组件图、部署图
- 业务流程图（BPM）Business Process Modeling

![nnjj](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241054235.png)

### 1.1 PD介绍

### 1.2 数据库表一对多建模

部门 dept 和员工 emp

完成步骤：

- 创建 CDM【抽象的、和具体 DB 没有关系】
- 产生 PDM【具体的，和具体 DB 有关系】
- 产生 DB 脚本【由 PDM 生成可执行的 DB 脚本】
- 实际开发中，也可能直接从 PDM 开始，PDM可以向上转换成 CDM，也可以向下转换成 DB 脚本

![image-20230524111851358](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241118419.png)

![image-20230524112955489](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241129545.png)

![image-20230524113325723](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241133771.png)

![image-20230524113710461](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241137500.png)

![image-20230524113851934](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241138988.png)

![image-20230524114505005](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241145054.png)

然后生成数据库脚本

点击 PDM 图，然后点击 Datebase ---> Generate Database

![image-20230524121529215](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241215268.png)

```sql
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/5/24 12:08:56                           */
/*==============================================================*/


drop table if exists dept;

drop table if exists emp;

/*==============================================================*/
/* Table: dept                                                  */
/*==============================================================*/
create table dept
(
   deptno               int not null,
   dname                varchar(13),
   loc                  varchar(13),
   primary key (deptno)
);

/*==============================================================*/
/* Table: emp                                                   */
/*==============================================================*/
create table emp
(
   empno                int not null,
   deptno               int,
   ename                varchar(13) not null,
   job                  varchar(10),
   mgr                  int,
   hiredate             date,
   sal                  float(6,2) not null,
   comm                 float(6,2),
   primary key (empno)
);

alter table emp add constraint FK_Relationship_1 foreign key (deptno)
      references dept (deptno) on delete restrict on update restrict;
```

### 1.3 数据库表多对多建模

学生选课

这次我们先建立 PDM 模型

![image-20230524132423365](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241324414.png)

![image-20230524133333483](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241333555.png)

注意：如果是多对多关系的话

- 拉一个中间表
- 变成 2 个1对多
- 中间这个是多【有外键 fk 就是多的】

![image-20230524134103079](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241341135.png)

最后生成数据库脚本

```sql
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/5/24 13:41:56                           */
/*==============================================================*/


drop table if exists course;

drop table if exists sc;

drop table if exists stu;

/*==============================================================*/
/* Table: course                                                */
/*==============================================================*/
create table course
(
   cno                  int(4) not null,
   cname                varchar(20),
   cscore               int(1),
   primary key (cno)
);

/*==============================================================*/
/* Table: sc                                                    */
/*==============================================================*/
create table sc
(
   scno                 int(7) not null,
   no                   int(6),
   cno                  int(4),
   score                int(1),
   primary key (scno)
);

/*==============================================================*/
/* Table: stu                                                   */
/*==============================================================*/
create table stu
(
   no                   int(6) not null,
   sname                varbinary(10),
   age                  int(3),
   sex                  char(1),
   primary key (no)
);

alter table sc add constraint FK_Reference_1 foreign key (no)	# 外键
      references stu (no) on delete restrict on update restrict;

alter table sc add constraint FK_Reference_2 foreign key (cno)	# 外键
      references course (cno) on delete restrict on update restrict;
```

![image-20230524134855353](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241348423.png)

### 1.4 逆向工程

![image-20230524135249279](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241352344.png)

以之前的家居购为例

![image-20230524135442546](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241354608.png)

逆向：将 Oracle中的数据导出到 Mysql中

![image-20230524135600798](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241356864.png)

![image-20230524135726203](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241357248.png)

这里可以导出 sql 语句

然后进入 PD 中：File ---> Reverse Engineer --> Database【这里生成的 PDM】

![image-20230524135942511](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241359572.png)

下面将 PDM ---> CDM

![image-20230524140508453](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241405511.png)

![image-20230524140840032](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241408089.png)

## 2. 数据库建模

### 2.1 数据库表之间的三种关联关系

#### 1. 一对多

在多的一段增加一个外键列，外键表示的就是一种 一对多的关联

![image-20230524153841075](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241538143.png)

#### 2. 多对多

- 增加一个中间表，将一个多对多转换为 2个一对多
- 中间表中有外键

![image-20230524154137817](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241541874.png)

#### 3. 一对一

有外键关联和主键关联 2种方式，本质是都是外键关联

![image-20230524154306386](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241543437.png)

![image-20230524155958360](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241559416.png)

### 2.2 数据库设计三大范式【NF：NormalForm】

![image-20230524161648107](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241616168.png)

#### 1. 什么是范式

1. 必须保证 DB设计的合理性
   - DB设计关系整个系统的架构，关系到后续开发效率和运行效率
   - DB设计主要包含了设计 <font color="yellow">**表结构**</font> 和 <font color="Yellow">**表之间的联系**</font>
2. 如何是合理数据库
   - 结构合理
   - 冗余较小
   - 尽量避免插入、删除、修改异常
3. 如何才能保证DB 设计水平
   - 遵循一定的规则
   - 在关系型DB中这种规则就称为范式
4. 什么是范式（NF：NormalForm）
   - 范式是符合某一种设计要求的总结
   - 要想设计一个结构合理的关系型数据库，必须满足一定的范式

#### 2. 范式分类

各个范式是依次嵌套包含的

范式越高，设计质量越高，在现实设计中也越难实现

一般DB设计，只要达到第三范式，即可避免异常的出现

#### 1. 第一范式【字段不能再分】

- 最基本的范式
- DB表中每一列都是不可分割的基本数据项，同一列中不能有多个值
- 简单说就是要确保 <font color="yellow">**每列保持原子性**</font>
- 第一范式的合理遵循需要根据系统的实际需求来定

#### 2. 第二范式【不存在局部依赖】

- 确保DB表中的每一列都和主键相关，而不只与主键的一部分相关【主要针对于联合主键而言】
- 即在一个DB表中只能保存一种数据，不可以把多种数据保存在同一张DB表中

![image-20230524162647679](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241626747.png)

#### 3. 第三范式【⭐不含传递依赖（间接依赖）】

- 确保DB表中每一列数据都和主键直接相关，而不能间接相关
- 属性不依赖于其它非主属性

![image-20230524163321436](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305241633510.png)

Boyce Codd 范式 = BCNF

由 Boyce 和 Codd 提出

比 3 NF又进一步

通常认为是修正的第三范式

#### 4. 第四范式

#### 5. 第五范式

优缺点：

- 优
  - 结构合理
  - 冗余较小
  - 尽量避免插入、删除、修改异常
- 缺
  - 性能降低
  - 多表查询比单表查询速度慢
- DB的设计应该根据当前情况和需求做出灵活的处理
  - 在实际设计中，要整体遵循范式理论
  - 如果在某些特定的情况下还死死遵循范式也是不可取的，因为可能降低DB效率，此时可以 <font color="red">**适当增加冗余而提升性能**</font>

## 3. 面向对象建模

UML（Unified Modeling Languag）：统一建模语言或标准建模语言

- 它是一个支持模型化和软件系统开发的图形化语言，为软件开发的所有阶段提供 <font color="yellow">**模型化** </font>和 <font color="yellow">**可视化**</font> 支持
- 统一了 Booch、Rumbaugh、Jacobason，形成标准建模语言
- UML 定义了10种模型图，对应软件设计开发的不同阶段
  - 用例图
  - 静态图：类图，包图，对象图
  - 行为图：状态图和活动图
  - 交互图：顺序图和协作图
  - 实现图：组件图，部署图

### 3.1 类的六种关系及其类图

#### 1. 继承关系（泛化关系 Generlization

Attributes：属性

Operations：方法

- 双机方法前面的箭头 ---> Parameters
  - 下拉箭头：挑选系统预设类型
  - 三个点：选择我们自定义的类型

![image-20230525200132981](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252001062.png)

#### 2. 实现关系（Realization）

![image-20230525200551313](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252005373.png)

#### 3. 依赖关系（Dependency）---> 局部变量

一个类 A使用到了另一个类B，但是这种使用关系是具有偶然性的、临时性的、非常弱的、但是类 B 的变化会影响到 类 A

语法：类 B 作为 A 的方法参数（或者局部变量存在）

![image-20230525201323663](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252013736.png)

#### 4. 关联关系（Association）---> 成员变量

1. 比依赖关系强，必然的、长期的、强烈的
2. 分别单项关联【班级中加入学生】，双向关联【学生也添加班级属性】
3. 分为一对一（学生和学生证）、一对多（班级和学生）、多对多关联（学生和课程）
4. 有2个类的关联（客户和订单，订单和商品），还有一个类和自身关联（领导也是员工）

![image-20230525202109543](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252021594.png)

#### 5. 聚合关系（Aggregation）

整体和部分的关系

- 整体部分可分离
- 整体的生命周期和部分的声明周期不同
- has-a 关系

![image-20230525202312578](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252023647.png)

注意：菱形【空心】永远靠近整体

#### 6. 组合关系（Composition）

整体和部分的关系

- 整体部分不可分离、比聚合更强
- 整体和部分生命周期相同
- contains-a 关系

![image-20230525202608167](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252026230.png)

注意：菱形【实心】永远靠近整体

### 3.2 用例图

https://www.cnblogs.com/lcword/p/10472040.html

用例图是指由 参与者【Actor】、用例【Use Case】、边界以及它们之间的关系构成的用于描述系统功能的视图

用例图（User Case）是外部用户（被称为参与者）所能观察到的系统功能的模型图

用例是系统的蓝图

- 呈现了一些参与者，一些用例，以及它们之间的关系
- 主要用户对系统、子系统或类的功能进行建模

![image-20230525203224400](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252032466.png)

![image-20230525203244092](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252032141.png)

我们来自己建立一个用例图

![image-20230525211814517](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252118573.png)

修改用例图关系线设置成直线

![image-20230525210628111](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202305252106174.png)



## 4. 业务流程图

### 4.1 时序图

### 4.2 业务流程图

