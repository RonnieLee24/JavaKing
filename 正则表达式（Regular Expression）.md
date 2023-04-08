- [正则表达式（Regular Expression）](#正则表达式regular-expression)
  - [1. 为什么要学习 RE](#1-为什么要学习-re)
  - [2. 正则表达式底层实现](#2-正则表达式底层实现)
    - [matcher.find()](#matcherfind)
    - [macher.group()](#machergroup)
      - [group(0)](#group0)
      - [考虑分组的情况](#考虑分组的情况)
  - [3. 正则表达式语法](#3-正则表达式语法)
      - [转义号 \\\\](#转义号-)
      - [字符匹配符](#字符匹配符)
        - [区分大小写（?i）insensitive【向后匹配】](#区分大小写iinsensitive向后匹配)
      - [选择匹配符（|）](#选择匹配符)
      - [限定符](#限定符)
      - [定位符（开头、结尾）](#定位符开头结尾)
      - [分组（非命名\[特别分组\]、重命名）](#分组非命名特别分组重命名)
      - [更多元字符](#更多元字符)
  - [4. 正则应用实例](#4-正则应用实例)
    - [4.1 汉字  （"^\[\\u0391-\\uffe5\]$"）](#41-汉字--u0391-uffe5)
    - [4.2 邮政编码](#42-邮政编码)
    - [4.3 QQ号码](#43-qq号码)
    - [4.4 手机号码](#44-手机号码)
    - [4.5 URL](#45-url)
  - [5. 正则表达式 3个常用的类](#5-正则表达式-3个常用的类)
    - [5.1 Pattern 类的方法 matches（整体匹配）](#51-pattern-类的方法-matches整体匹配)
    - [5.2 Matcher 类方法一览](#52-matcher-类方法一览)
  - [6. 分组、捕获、反向引用](#6-分组捕获反向引用)
    - [1. 分组](#1-分组)
    - [2. 捕获](#2-捕获)
    - [3.反向引用](#3反向引用)
      - [结巴去重案例](#结巴去重案例)
  - [7. String 类中使用正则表达式](#7-string-类中使用正则表达式)
    - [1. 替换功能【repalceAll(String regStr, String replacement)】](#1-替换功能repalceallstring-regstr-string-replacement)
    - [2. 判断功能【public boolean matches(String regex){}】](#2-判断功能public-boolean-matchesstring-regex)
    - [3. 分割功能 \[public String\[\] split(String regex)\]](#3-分割功能-public-string-splitstring-regex)
  - [8. 课后习题](#8-课后习题)
    - [8.1 验证邮件合法性](#81-验证邮件合法性)
    - [8.2 要求验证是不是整数或者小数【考虑正负】](#82-要求验证是不是整数或者小数考虑正负)
    - [8.3 对于给 URL进行解析](#83-对于给-url进行解析)
  - [9. Java正则表达式大全](#9-java正则表达式大全)

# 正则表达式（Regular Expression）

## 1. 为什么要学习 RE

处理文本利器！！！

提取文章中所有的 <font color="yellow">英文单词</font>

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212270106386.png)

```java
public class Regexp_ {
    public static void main(String[] args) {
 
        //  假定编写了爬虫，从百度页面得到如下文本
 
        String content = "1995年，互联网的蓬勃发展给了Oak机会。业界为了使死板、单调的静态网页能够“灵活”起来，" +
                "急需一种软件技术来开发一种程序，这种程序可以通过网络传播并且能够跨平台运行。于是，世界各大IT企业" +
                "为此纷纷投入了大量的人力、物力和财力。这个时候，Sun公司想起了那个被搁置起来很久的Oak，并且重新审" +
                "视了那个用软件编写的试验平台，由于它是按照嵌入式系统硬件平台体系结构进行编写的，所以非常小，特别" +
                "适用于网络上的传输系统，而Oak也是一种精简的语言，程序非常小，适合在网络上传输。Sun公司首先推出了" +
                "可以嵌入网页并且可以随同网页在网络上传输的Applet（Applet是一种将小程序嵌入到网页中进行执行的技术）" +
                "，并将Oak更名为Java（在申请注册商标时，发现Oak已经被人使用了，再想了一系列名字之后，最终，使用了提议者" +
                "在喝一杯Java咖啡时无意提到的Java词语）。5月23日，Sun公司在Sun world会议上正式发布Java和HotJava浏览器。" +
                "IBM、Apple、DEC、Adobe、HP、Oracle、Netscape和微软等各大公司都纷纷停止了自己的相关开发项目，竞相购买" +
                "了Java使用许可证，并为自己的产品开发了相应的Java平台。";
 
        //  提取文档中所有的英文单词
        //  1）传统方法，使用遍历方式，代码量大，效率低下
        //  2）正则表达式技术
 
        //  1.  先创建一个 Pattern 对象，模式对象，可以理解成就是一个正则表达式对象
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        //  2.  创建一个匹配器对象
        //  理解：就是 matcher 匹配器按照 pattern（模式 / 样式），到content 文本中去匹配
        //  找到就返回 true，否则就返回 false
        Matcher matcher = pattern.matcher(content);
        //  3.  可以开始循环匹配
        while (matcher.find()) {
            //  匹配内容，文本，放到 m.group(0)
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
```

提取文章中所有的<font color="yellow"> 数字</font>

```java
Pattern pattern = Pattern.compile("[0-9]+");
```

提取文章中所有的英文单词和数字

```java
Pattern pattern = Pattern.compile("([0-9]+)|([a-zA-Z]+)");
```

提取百度热榜 标题

```java
Pattern pattern = Pattern.compile("<a target=\"_blank\" title=\"(\\S*)\"");
```

提取 IP 地址

```java
Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
```

给你一个字符串（文章），找出 4个数字连在一起的子串

给你一个字符串（文章），找出 4个数字连在一起的子串（并且：1、4相同，2、3相同）

输入的邮件是否符合电子邮件格式

输入的手机号是否符合手机号格式



## 2. 正则表达式底层实现

```java
public class RegTheory {
    public static void main(String[] args) {
        String content = "1998年12月8日，第二代Java平台的企业版J2EE发布。1999年6月，" +
                "Sun公司发布了第二代Java平台（简称为Java2）的3个版本：J2ME（Java2 Micro Edition，" +
                "Java2平台的微型版），应用于移动、无线及有限资源的环境；" +
                "J2SE（Java 2 Standard Edition，Java 2平台的标准版），应用于桌面环境；" +
                "J2EE（Java 2Enterprise Edition，Java 2平台的企业版），应用于基于Java的应用服务器。" +
                "Java 2平台的发布，是Java发展过程中最重要的一个里程碑，标志着Java的应用开始普及。";
        //  目标：匹配所有4 个数字
        //  说明
        //  1. \\d 表示一个任意的数字
        String regStr = "\\d\\d\\d\\d";
        //  2. 创建模式对象 [正则表达式对象]
        Pattern pattern = Pattern.compile(regStr);
        //  3. 创建匹配器
        //  说明：创建匹配器 matcher，按照正则表达式的规则，去匹配我们的 content 字符串
        Matcher matcher = pattern.matcher(content);
        //  4. 开始匹配
        while (matcher.find()){
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
```

### matcher.find()

![image-20221227131321153](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271313206.png)

1. 根据指定的规则，定位满足规则的子字符串（比如：1998）

2. 找到后，将 子字符串的 **<font color="blue">开始索引</font>** 记录到 matcher 对象的属性 int[] groups：

   groups[0] = 0，把该子字符串的 **<font color="red">结束的索引 + 1</font>** 的值记录到 groups[1] = 4

   ![image-20221227103821693](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271038829.png)

3. 同时记录 oldLast 的值为：子字符串结束的索引 + 1 的值，即：4【下次执行 find方法时，就从 4 开始匹配】

   ![image-20221227104302573](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271043647.png)

### macher.group()

![image-20221227132301776](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271323875.png)

```java
public String group(int group) {
    if (first < 0)
        throw new IllegalState Exception("No match found");
    if (group < 0 || group > groupCount())
        throw new IndexOutOfBoundsException("No group " + group);
    if ((groups[group*2] == -1) || (groups[group*2+1] == -1))
        return null;
    return getSubSequence(groups[group * 2], groups[group * 2 + 1]).toString();
}
```

#### group(0)

当执行 matcher.group(0)时，执行 getSubSequence(groups[group * 2], groups[group * 2 + 1])

```java
CharSequence getSubSequence(int beginIndex, int endIndex) {
    return text.subSequence(beginIndex, endIndex);
}
```

根据 <font color="red">groups</font>[0] = 0 和 <font color="red">groups</font>[1] <= 4 的记录的位置，从 content 开始截取子字符串返回

就是：[0, 4) 的位置

#### 考虑分组的情况

```java
String regStr = "(\\d\\d)(\\d\\d)";
```

在 RE 中，有 （）表示分组

- 第一个 （）代表第一组
- 第二个（）代表第二组
- ……

开始分析： 

1. 根据指定的规则，定位满足规则的子字符串【比如：(19)(98)】

2. 找到后，将 子字符串的 **<font color="blue">开始索引</font>** 记录到 matcher 对象的属性 int[] groups：

   - groups[0] = 0，把该子字符串的 **<font color="red">结束的索引 + 1</font>** 的值记录到 groups[1] = 4
   - 记录第 1 组 () 匹配到的字符串 groups[2] = 0，groups[3] = 2
   - 记录第 2 组 () 匹配到的字符串 groups[4] = 2，groups[5] = 4
   - 如果有更多的分组，以此类推

   ![image-20221227114147919](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271141024.png)

   ![image-20221227114656929](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271146975.png)

小结：

```java
//	1. 如果 RE 有 () 即：分组
//	2. 取出匹配的字符串规则如下：
//	3. group(0) 表示匹配到的子字符串
//	4. group(1) 表示匹配到的子字符串的第一组字串
//	5. group(2) 表示匹配到的子字符串的第二组字串
//	6. 以此类推

//	注意：分组的数不能越界

while (matcher.find()){
    System.out.println("找到：" + matcher.group(0));
    System.out.println("匹配到第1组" + matcher.group(1));
    System.out.println("匹配到第2组" + matcher.group(2));
}
```

## 3. 正则表达式语法

如果想要灵活地运用正则表达式，必须了解其中各种 <font color="yellow">元字符</font> 功能

#### 转义号 \\\

注意：在 Java 正则表达式中，2个 \\\ 代表其它语言中的一个 \

例如：

1. 用 \$ 去匹配 "abc$(" 会怎样？
2. 用 ( 去匹配 "abc$(" 会怎样？

````java
//	调用 start() 方法得到出现的位置
public int start() {	//	返回的 是 first
    if (first < 0)
        throw new IllegalStateException("No match available");
    return first;
}
````

```java
public class RegExp02 {
    public static void main(String[] args) {

        String content = "abc$(abc(123(";
        //  匹配
        String regStr = "\\(";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()){
            System.out.println("找到 " + matcher.group(0) + " 出现的位置为：" +  matcher.start());
        }
    }
}
```

![image-20221227145514963](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212271455027.png)

需要用到转义符号有如下几个：<font color="yellow">. * + () $  / \ ? [] ^ {}</font>

```java
//	. 和 git add . 很像，类似 通配符 *
```

#### 字符匹配符

| 符号   | 符号                                        | 示例                                                         | 解释                                                  | 匹配输入                |
| ------ | ------------------------------------------- | ------------------------------------------------------------ | ----------------------------------------------------- | ----------------------- |
| []<br> | 可接收的字符列表                            | [efgh]                                                       | e、f、g、h中的任意1个字符                             |                         |
| [^]    | 不接收的字符列表                            | [^abc]                                                       | 除a、b、c之外的任意1个字符， 包括数字和特殊符号       |                         |
| -      | 连字符                                      | A-Z                                                          | 任意单个大写字母                                      |                         |
| .      | 匹配除\n 【换行】以外的任何字符             | a..b                                                         | 以a开头，b结尾，中间包括2个任意字 符的长度为4的字符串 | aaab、aefb、 a35b、a#*b |
| \\\d   | 匹配单个数字字符，相当于[0-9]               | \\\d{3}(\\\d)?<br><br>【<font color="yellow">?</font>:代表 0 或 1】 | 包含3个或4个数字的字符串                              | 123、9876               |
| \\\D   | 匹配单个非数字字符，相当于\[^0-9]           | \\\D(\\\d)*</br></br><br>【<font color="yellow">\*</font>：代表 0 或 多】 | 以单个非数字字符开头，后接任意个 数字字符串           | a、A342                 |
| \\\\w  | 数字、大小写字母、下划线，相当于[0-9a-zA-Z] | \\\d{3}\\\w{4}                                               | 以3个数字字符开头的长度为7的数字字母字符串            | 234abcd、 12345Pe       |
| \\\W   | [^0-9a-zA-Z]                                | \\\W+\\\d{2}</br><br>【<font color="yellow">+</font>:代表1到多】 | 以至少1个非数字字母字符开头，2个数 字字符结尾的字符串 | #29、#?@10              |
| \\\s   | 匹配任何空白字符（空格，制表符等）          |                                                              |                                                       |                         |
| \\\S   |                                             |                                                              |                                                       |                         |

字符匹配案例：

```java
public class Regexp03 {
    public static void main(String[] args) {

        String content = "a11c8";
        String regStr = "[a-z]";    //  匹配 a-z 之间任意一个字符
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
```

##### 区分大小写（?i）insensitive【向后匹配】

- (?i)abc：表示abc都不区分大小写
- a(?i)bc：表示bc不区分大小写
- a((?i)b)c：表示只有b不区分大小写

```java
//	说明：当创建 Pattern 对象时，指定 Pattern.CASE.INSENSITIVE，表示匹配不区分字母大小写
Pattern pat = Pattern.compile(regEx, Pattern.CASE.INSENSITIVE);
```

#### 选择匹配符（|）

在匹配某个字符串的时候是选择性的，即：可以匹配这个，又可以匹配那个，这时你需要用到 选择匹配符号 |

| 符号 | 符号                         | 示例   | 解释     |
| ---- | ---------------------------- | ------ | -------- |
| \|   | 匹配 “\|” 之前或之后的表达式 | ab\|cd | ab或者cd |

#### 限定符

用于指定其前面的字符和组合项连续出现多少次

细节：java 匹配模式默认为 <font color="yellow">贪婪匹配</font>，即：尽可能匹配多的

```java
//	例如：a{3, 4}
//	尽可能匹配多的
```

| 符号  | 含义                                   | 示例        | 说明                                               | 匹配输入                |
| ----- | -------------------------------------- | ----------- | -------------------------------------------------- | ----------------------- |
| *     | 指定字符重复0次或n次(无要求)零到多     | (abc)*      | 仅包含任意个abc的字符串，等效于\w*                 | abc、 abcabcabc         |
| +     | 指定字符重复1次或n次（至少 一次）1到多 | m+(abc)*    | 以至少1个m开头，后接任意个abc的字符串              | m. mabc. mabcabc        |
| ?     | 指定字符重复0次或1次(最多 一次）0到1   | m+abc?      | 以至少1个m开头，后接ab或abc的字符 串               | mab.mabc. mmmab. mmabc  |
| {n)   | 只能输入n个字符                        | [abcd]{3)   | 由abcd中字母组成的任意长度为3的字 符串             | abc、dbc. adc           |
| {n,}  | 指定至少 n 个匹配（$\geqslant n$）     | [abcd]{3,)  | 由abcd中字母组成的任意长度不小于3的字 符串         | aab、dbc. aaabdc        |
| {n,m} | 指定至少 n 个但不多于 m 个匹配 $[n,m]$ | [abcd]{3,5) | 由abcd中字母组成的任意长度不小于3，不大于5的字符串 | abc、abcd、aaaaa、bcdab |

#### 定位符（开头、结尾）

定位符，规定要匹配的字符串出现的位置，比如在字符串的开始还是结束的位置

| 符号 | 含义                   | 示例            | 说明                                                         | 匹配输入                                                     |
| ---- | ---------------------- | --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ^    | 指定起始字符           | ^[0-9]+[a-z]*   | 以至少1个数字开头，后接任意个小写字 母的字符串               | 123、6aa. 555edf                                             |
| $    |以1个数字开头后接连字符 "-"，并以至少1个小写字母结尾的字符串|^[0-9]\\\\-[a-z]+$|以1个数字开头后接连字符 "-"，并以至少1个小写字母结尾的字符串|1-a|
| \\\b | 匹配目标字符串的边界   | han\\\b         | 这里说的字符串的边界指的是子串间有 <font color="yellow">空格</font>，或者是自标字符串的 <font color="yellow">结束位置</font> | hanshunping sp<font color="yellow">han</font> nn<font color="yellow">han</font> |
| \\\B | 匹配目标字符串的非边界 | han\\\B         | 和\b的含义刚刚相反                                           | <font color="yellow">han</font>shunping sphan nnhan          |

#### 分组（非命名[特别分组]、重命名）

| 常用分组构造形式  | 说明                                                         |
| ----------------- | ------------------------------------------------------------ |
| (pattern)         | 非命名捕获。<br>捕获匹配的子字符串。<br>编号为零的第一个捕获是由整个正则 表达式模式匹配的文本，<br>其它捕获结果则根据左括号的顺序从1开始自动编号. |
| (?\<name>pattern) | 命名捕获。<br>将匹配的子字符串捕获到一个 <font color="yellow">组</font>名称或 <font color="yellow">编号</font>名称中。<br>用于 name 的字符串不能包含任何标点符号，并且不能以数字开头。<br>可以使用单引号 替代尖括号，例如(?'name') |

```java
public class RegExp07 {
    public static void main(String[] args) {

        String content = "lilongqi s7788 nn1189llq";

        //  命名分组：即可以给分组取名
        //  说明
        //  1）matcher.group(0)得到匹配到的字符串
        //  2）matcher.group(1)得到匹配到的字符串的第1个分组的内容
        //  3）matcher.group(2)得到匹配到的字符串的第2个分组的内容
        //
        String regStr = "(?<g1>\\d\\d)(?<g2>\\d\\d)"; //  匹配4个数字的 字符串

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            System.out.println("找到 " + matcher.group(0));
            System.out.println("找到第一组 " + matcher.group(1));
            System.out.println("找到第一组【通过组名】 " + matcher.group("g1"));	//	方法重载（通过组名获取）
            System.out.println("找到第二组 " + matcher.group(2));
            System.out.println("找到第二组【通过组名】 " + matcher.group("g2"));
        }
    }
}
```

特别分组：

<font color="yellow">非捕获匹配</font> 不能使用 matcher.<font color="yellow">group(1)</font> 会 <font color="yellow">报错</font>

| 常用分组构造形式 | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| (?:pattern)      | 匹配pattern但不捕获该匹配的子表达式，即它是一个非捕获匹配，不存 储供以后使用的匹配。这对于用"or"字符(\|)组合模式部件的情况很有用。 <br>例如，'industr(?:y\|ies)是比'industr<font color="red">y</font>\|industr<font color="red">ies</font>' 更经济的表达式。 |
| (?=pattern)      | 它是一个非捕获匹配。<br>例如，"Windows (?=95\|98\|NT\|2000)" 匹配 "Windows 2000"中的 "Windows"，但不匹配"Windows 3.1"中的 Windows"。 |
| (?!pattern)      | 该表达式匹配不处于匹配pattern的字符串的起始点的搜索字符串。它是 一个非捕获匹配。<br>例如，"Windows (?l95[98\|NT[2000)’匹配 "Windows 3.中的“Windows"，但不匹配"Windows 2000"中的 "Windows"。 |

```java
/**
 * 演示非捕获分组，语法比较奇怪
 */
public class RegExp08 {
    public static void main(String[] args) {

        String content = "hello韩顺平教育 jack韩顺平老师 韩顺平同学hello";
        String regStr = "韩顺平(?:教育|老师|同学)";

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()){
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
```

找到韩顺平这个关键字，但是只是查找 韩顺平教育，韩顺平老师中包含有的韩顺平

```java
public class RegExp08 {
    public static void main(String[] args) {

        String content = "hello韩顺平教育 jack韩顺平老师 韩顺平同学hello";

        //  找到 韩顺平教育，韩顺平老师，韩顺平同学 子字符串
        String regStr = "韩顺平(?=教育|老师)";

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()){
            System.out.println("找到：" + matcher.group(0) + "位置为：" + matcher.start());
        }
    }
}
```

```java
String regStr = "韩顺平(?!教育|老师)";
```

#### 更多元字符

| **字符**        | **说明**                                                     |
| --------------- | ------------------------------------------------------------ |
| **\\**          | **将下一字符标记为特殊字符、文本、反向引用或八进制转义符。例如，"n"匹配字符"n"。"\n"匹配换行符。序列"\\\\"匹配"\\"，"\\("匹配"("。** |
| **^**           | **匹配输入字符串开始的位置。如果设置了 RegExp 对象的 Multiline 属性，^ 还会与"\n"或"\r"之后的位置匹配。** |
| **$**           | **匹配输入字符串结尾的位置。如果设置了 RegExp 对象的 Multiline 属性，$ 还会与"\n"或"\r"之前的位置匹配。** |
| *****           | **零次或多次匹配前面的字符或子表达式。例如，zo\* 匹配"z"和"zoo"。\* 等效于 {0,}。** |
| **+**           | **一次或多次匹配前面的字符或子表达式。例如，"zo+"与"zo"和"zoo"匹配，但与"z"不匹配。+ 等效于 {1,}。** |
| **?**           | **零次或一次匹配前面的字符或子表达式。例如，"do(es)?"匹配"do"或"does"中的"do"。? 等效于 {0,1}。** |
| **{n}**         | ***n\*** **是非负整数。正好匹配** ***n\*** **次。例如，"o{2}"与"Bob"中的"o"不匹配，但与"food"中的两个"o"匹配。** |
| **{n,}**        | ***n\*** **是非负整数。至少匹配** ***n\*** **次。例如，"o{2,}"不匹配"Bob"中的"o"，而匹配"foooood"中的所有 o。"o{1,}"等效于"o+"。"o{0,}"等效于"o\*"。** |
| **{n,m}**       | ***m\*** **和** ***n\*** **是非负整数，其中** ***n\*** **<=** ***m\*****。匹配至少** ***n\*** **次，至多** ***m\*** **次。例如，"o{1,3}"匹配"fooooood"中的头三个 o。'o{0,1}' 等效于 'o?'。注意：您不能将空格插入逗号和数字之间。** |
| **?**           | **当此字符紧随任何其他<font color="yellow">限定(*、+、？、{n}、{n,}、{n,m}之后</font>时，匹配模式是"<font color="yellow">非贪心的</font>"。<br>"非贪心的"模式匹配搜索到的、尽可能短的字符串，而默认的"贪心的"模式匹配搜索到的、尽可能长的字符串。<br>例如，在字符串"oooo"中，"o+?"只匹配单个"o"，而"o+"匹配所有"o"。** |
| **.**           | **匹配除"\r\n"之外的任何单个字符。若要匹配包括"\r\n"在内的任意字符，请使用诸如"[\s\S]"之类的模式。** |
| **{pattern}**   | **匹配** ***pattern\*** **并捕获该匹配的子表达式。可以使用 $0…$9 属性从结果"匹配"集合中检索捕获的匹配。若要匹配括号字符 ( )，请使用"\("或者"\)"。** |
| **{?:pattern}** | **匹配** ***pattern\*** **但不捕获该匹配的子表达式，即它是一个非捕获匹配，不存储供以后使用的匹配。这对于用"or"字符 (\|) 组合模式部件的情况很有用。例如，'industr(?:y\|ies) 是比 'industry\|industries' 更经济的表达式。** |
| **{?=pattern}** | **执行正向预测先行搜索的子表达式，该表达式匹配处于匹配** ***pattern\*** **的字符串的起始点的字符串。它是一个非捕获匹配，即不能捕获供以后使用的匹配。例如，'Windows (?=95\|98\|NT\|2000)' 匹配"Windows 2000"中的"Windows"，但不匹配"Windows 3.1"中的"Windows"。预测先行不占用字符，即发生匹配后，下一匹配的搜索紧随上一匹配之后，而不是在组成预测先行的字符后。** |
| **{?!pattern}** | **执行反向预测先行搜索的子表达式，该表达式匹配不处于匹配** ***pattern\*** **的字符串的起始点的搜索字符串。它是一个非捕获匹配，即不能捕获供以后使用的匹配。例如，'Windows (?!95\|98\|NT\|2000)' 匹配"Windows 3.1"中的 "Windows"，但不匹配"Windows 2000"中的"Windows"。预测先行不占用字符，即发生匹配后，下一匹配的搜索紧随上一匹配之后，而不是在组成预测先行的字符后。** |
| **x\|y**        | **匹配** ***x\*** **或** ***y\*****。例如，'z\|food' 匹配"z"或"food"。'(z\|f)ood' 匹配"zood"或"food"。** |
| **[xyz]**       | **字符集。匹配包含的任一字符。例如，"[abc]"匹配"plain"中的"a"。** |
| **[^xyz]**      | **反向字符集。匹配未包含的任何字符。例如，"[^abc]"匹配"plain"中"p"，"l"，"i"，"n"。** |
| **[a-z]**       | **字符范围。匹配指定范围内的任何字符。例如，"[a-z]"匹配"a"到"z"范围内的任何小写字母。** |
| **[^a-z]**      | **反向范围字符。匹配不在指定的范围内的任何字符。例如，"[^a-z]"匹配任何不在"a"到"z"范围内的任何字符。** |
| **\b**          | **匹配一个字边界，即字与空格间的位置。例如，"er\b"匹配"never"中的"er"，但不匹配"verb"中的"er"。** |
| **\B**          | **非字边界匹配。"er\B"匹配"verb"中的"er"，但不匹配"never"中的"er"。** |
| **\cx**         | **匹配** ***x\*** **指示的控制字符。例如，\cM 匹配 Control-M 或回车符。*****x\*** **的值必须在 A-Z 或 a-z 之间。如果不是这样，则假定 c 就是"c"字符本身。** |
| **\d**          | **数字字符匹配。等效于 [0-9]。**                             |
| **\D**          | **非数字字符匹配。等效于 [^0-9]。**                          |
| **\f**          | **换页符匹配。等效于 \x0c 和 \cL。**                         |
| **\n**          | **换行符匹配。等效于 \x0a 和 \cJ。**                         |
| **\r**          | **匹配一个回车符。等效于 \x0d 和 \cM。**                     |
| **\s**          | **匹配任何空白字符，包括空格、制表符、换页符等。与 [ \f\n\r\t\v] 等效。** |
| **\S**          | **匹配任何非空白字符。与 [^ \f\n\r\t\v] 等效。**             |
| **\t**          | **制表符匹配。与 \x09 和 \cI 等效。**                        |
| **\v**          | **垂直制表符匹配。与 \x0b 和 \cK 等效。**                    |
| **\w**          | **匹配任何字类字符，包括下划线。与"[A-Za-z0-9_]"等效。**     |
| **\W**          | **与任何非单词字符匹配。与"[^A-Za-z0-9_]"等效。**            |
| **\xn**         | **匹配** ***n\*****，此处的** ***n\*** **是一个十六进制转义码。十六进制转义码必须正好是两位数长。例如，"\x41"匹配"A"。"\x041"与"\x04"&"1"等效。允许在正则表达式中使用 ASCII 代码。** |
| **\num**        | **匹配** ***num\*****，此处的** ***num\*** **是一个正整数。到捕获匹配的反向引用。例如，"(.)\1"匹配两个连续的相同字符。** |
| **\n**          | **标识一个八进制转义码或反向引用。如果 \*****n\*** **前面至少有** ***n\*** **个捕获子表达式，那么** ***n\*** **是反向引用。否则，如果** ***n\*** **是八进制数 (0-7)，那么** ***n\*** **是八进制转义码。** |
| **\nm**         | **标识一个八进制转义码或反向引用。如果 \*****nm\*** **前面至少有** ***nm\*** **个捕获子表达式，那么** ***nm\*** **是反向引用。如果 \*****nm\*** **前面至少有** ***n\*** **个捕获，则** ***n\*** **是反向引用，后面跟有字符** ***m\*****。如果两种前面的情况都不存在，则 \*****nm\*** **匹配八进制值** ***nm\*****，其中** ***n\*** **和** ***m\*** **是八进制数字 (0-7)。** |
| **\nml**        | **当** ***n\*** **是八进制数 (0-3)，*****m\*** **和** ***l\*** **是八进制数 (0-7) 时，匹配八进制转义码** ***nml\*****。** |
| **\un**         | **匹配** ***n\*****，其中** ***n\*** **是以四位十六进制数表示的 Unicode 字符。例如，\u00A9 匹配版权符号 (©)。** |

```java
/**
 * 非贪婪匹配
 */
public class RegExp09 {
    public static void main(String[] args) {

        String content = "hello1111111 ok";
        String regStr = "\\d+?"; //  非贪婪匹配

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()){
            System.out.println("找到：" + matcher.group(0));
        }
    }
}
```

## 4. 正则应用实例

### 4.1 汉字  （"^[\u0391-\uffe5]$"）

```java
String regStr = "^[\u0391-\uffe5]$";
```

由于开始和结束都已经定下来了，不可能找到2个。

```java
public class fe {
    public static void main(String[] args) {
        String content = "爱新觉罗";
        String regStr = "^[\u0391-\uffe5]+$"; // 这里如果不写 + 的话，就代表开头结尾都是由一个汉字，所以只能匹配一个汉字的情况
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        //	开始和结束都指定了，就只能找到1个，所以用 if 即可
        if (matcher.find()){
            System.out.println("满足格式");
            System.out.println(matcher.group(0));
        }
    }
}
```

### 4.2 邮政编码

要求：是1-9开头的一个六位数，比如：123890

```java
//	开头是 1-9
//	结尾是 5个数
String regStr = "^[1-9]\\d{5}$";
```

### 4.3 QQ号码

要求：是 1-9 开头的一个（5位数-10位数），比如：12389，1345687，187698765

```java
//	开头是 1-9
//	结尾是 4 到 9 个数
String regStr = "^[1-9]\\d{4,9}$";
```



### 4.4 手机号码

要求：必须以13，14，15，18开头的11位数，比如：13588889999

```java
//	开头是 1-9
//	结尾是 4 到 9 个数
String regStr = "^1[3|4|5|8]\\d{9}$";
```

### 4.5 URL

![image-20221229154552182](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212291545315.png)

https://www.bilibili.com/video/BV1fh411y7R8/?vd_source=c4ea01289437e2fa09f1eee848010521

分析思路：

```java
String regStr = "^((http|https)://)([\\w-]+\\.)+[\\w-]+(\\/[\\w-?=&/%.#]*)?$";
```

1. 先确定 url 的开始部分 https:// | http://

   ```java
   String regStr = "^((http|https)://)";
   ```

2. www.bilibli

   ```java
   ([\\w-]+\\.)+
   ```

3. com

   ```java
   [\\w-]+
   ```

4. /video/BV1fh411y7R8/?vd_source=c4ea01289437e2f【最后这部分，可能有，也可能没有】

   ```java
   //	注意：[.] 表示匹配就是 . 本身，
   (\\/[\\w-?=&/%.#]*)?$
   ```



## 5. 正则表达式 3个常用的类

java.util.regex 包主要包括一下三个类 Pattern类、Matcher类和 PatternSyntaxException

1. Pattern 类

   - Pattern 对象是一个正则表达式对象。

   - Pattern 类没有公共构造方法（构造器）。

   - 要创建一个 Pattern 对象，<font color="red">调用 </font>其 <font color="yellow">公共静态方法 compile</font>，它 <font color="yellow">返回一个 Pattern 对象</font>

     ```java
     public static Pattern compile(String regex) {
         return new Pattern(regex, 0);
     }
     ```

   - 该方法接收一个 正则表达式作为它的第一个参数：

     ```java
     Pattern pattern = Pattern.compile(regStr);
     ```

2. Matcher 类

   - Matcher 对象是对输入字符串进行解释和匹配的引擎

   - 与Pattern类一样，Matcher也没有公共构造方法

   - 你需要 <font color="red">调用</font> <font color="yellow">Pattern 对象的 matcher 方法</font> 来获得一个Matcher对象

     ```java
     public Matcher matcher(CharSequence input) {
         if (!compiled) {
             synchronized(this) {
                 if (!compiled)
                     compile();
             }
         }
         //	动态绑定，this ---> Pattern 对象
         Matcher m = new Matcher(this, input);
         return m;
     }
     ```

3. PatternSyntaxException
   - PatternSyntaxException 是一个非强制异常类
   - 它表示一个正则表达式模式中的语法错误

### 5.1 Pattern 类的方法 matches（整体匹配）

- 验证一个输入的字符串是否满足我们的要求
- 自带定位符 <font color="yellow">^$</font>

```java
/**
 * 演示 matches 方法，用于整体匹配，在验证输入字符串是否i满足条件时使用
 */
public class PatternMethod {
    public static void main(String[] args) {
        String content = "hello abc hello, 韩顺平教育";
        String regStr = "hello";
        boolean matches = Pattern.matches(regStr, content);
        System.out.println("整体匹配= " + matches);
    }
}

//	要想全部匹配：修改正则表达式为如下：
String regStr = "hello.*;
```

追下源码：

- 本质还是先创建 Pattern 对象，在调用 Pattern 对象的 matcher方法 获取 Matcher 对象

- 最后调用 Matcher 对象的 matches() 方法

  ![image-20221229163534751](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202212291635854.png)

```java
public static boolean matches(String regex, CharSequence input) {
    Pattern p = Pattern.compile(regex);	//	创建 Pattern 对象
    Matcher m = p.matcher(input);	//	创建 匹配器 对象 Matcher 对象
    return m.matches();	//	真正去匹配的还是 Matcher类 的 matches 方法
}
```

```java
public boolean matches() {
    return match(from, ENDANCHOR);
}
```

```java
boolean match(int from, int anchor) {
    this.hitEnd = false;
    this.requireEnd = false;
    from = from < 0 ? 0 : from;
    this.first  = from;
    this.oldLast = oldLast < 0 ? from : oldLast;
    for (int i = 0; i < groups.length; i++)
        groups[i] = -1;
    for (int i = 0; i < localsPos.length; i++) {
        if (localsPos[i] != null)
            localsPos[i].clear();
    }
    acceptMode = anchor;
    boolean result = parentPattern.matchRoot.match(this, from, text);
    if (!result)
        this.first = -1;
    this.oldLast = this.last;
    this.modCount++;
    return result;
}
```

### 5.2 Matcher 类方法一览

| 方法                                                         | 说明                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public int start()                                           | 返回以前匹配的初始索引                                       |
| public int start(int group)                                  | 返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引   |
| public int end()                                             | 返回最后匹配字符之后的偏移量                                 |
| public int end(int group)                                    | 返回在以前的匹配操作期间，由给定组所捕获子序列的最后字符之后的偏移量。 |
| public boolean lookingAt()                                   | 尝试将从区域开头开始的输入序列与该模式匹配。                 |
| public boolean find()                                        | 尝试查找与该模式匹配的输入序列的下一个子序列。               |
| public boolean find(int start)                               | 重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下 一个子序列。 |
| public boolean matches()                                     | 尝试将 <font color="yellow">**整个区域** </font>与模式匹配。 |
| public Matcher appendReplacement(StringBuffer sb, String replacement) | 实现非终端添加和替换步骤                                     |
| public StringBuffer appendTail(StringBuffer sb)              | 实现终端添加和替换步骤                                       |
| public String <font color="yellow">replaceAll</font> (String replacement) | 替换模式与给定字符串相匹配的输入序列的每个子序列             |
| public String replaceFirst(String replacement)               | 替换模式与给定字符串相匹配的输入序列的第一个子序列           |
| public static String quoteReplacement(String s)              | 返回指定字符串的字面替换字符串。<br />这个方法返回一个字符串，就像传递给 Mathcer 类的 append类的 appendReplacement 方法一个字面字符串一样工作 |

```java
public class PatternMethod {
    public static void main(String[] args) {
        String content = "hello abc hello,fdasfoijsdoifehello,fsdaofwehello";
        String regStr = "hello";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println(matcher.replaceAll("爱新觉罗LQ"));
        }
        System.out.println();
        //  注意：返回的字符串才是替换后的字符串，原来的 content 不会变化
        System.out.println(content);
    }
}
```

![image-20230114170505990](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141705076.png)

## 6. 分组、捕获、反向引用

提出需求：

- 给你一段文本
- 找出所有4个数字连在一起的子串，并且这4个数字要满足：
  - 第一位和第四位相同
  - 第二位和第三位相同
- 比如：1221、5775

### 1. 分组

我们可以用圆括号组成一个比较复杂的匹配模式，那么一个圆括号的部分我们可以看作是一个子表达式 / 一个分组

### 2. 捕获

把正则表达式中 表达式 / 分组匹配的内容，保存到内存中以数字编号或显式命名的组里，方便后面引用

- 从左向右，以分组的左括号为标志
- 第一个出现的分组的组号为1
- 第二个为2
- 以此类推
- 组0代表的是整个正则式

### 3.反向引用

圆括号的内容被捕获后，可以在这个括号后被引用，从而写出一个比较实用的匹配模式，这个我们称为 <font color="Yellow">**反向引用**</font>

- 这种引用既可以是在正则表达式内部，也可以是在正则表达式外部
- 内部反向引用 <font color="yellow">\\\ 分组号</font>
- 外部反向引用 <font color="yellow">$ 分组号</font>



看几个小案例：

1. 匹配2个连续的相同数字

   ```py
   # (\\d)\\1
   ```

2. 匹配5个连续的相同数字

   ```py
   # (\\d)\\1{4}
   ```

3. 匹配个位与千位相同，十位与百位相同的数

   ```py
   # (\\d)(\\d)\\2\\1
   ```

```java
public class reg11 {
    public static void main(String[] args) {
        String content = "fefasdf 1221 sdfase 77 7887";
        String regStr = "(\\d)(\\d)\\2\\1";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println(matcher.group(0));
        }
    }
}
```

![image-20230114173532570](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141735647.png)

思考题：

- 字符串中检索商品编号
  - 形式如：12321-333999111，要求满足
  - 前面一个是 5位数
  - 然后一个 - 号
  - 然后是一个九位数，连续的每3个要相同

```java
public class reg11 {
    public static void main(String[] args) {
        String content = "fefasdf 1221 sdfase 77 7887 12321-333999111";
        //  注意：只有被括号包裹的，才能通过 引用来访问！！！
        String regStr = "\\d{5}\\-(\\d)\\1{2}(\\d)\\2{2}(\\d)\\3{2}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println(matcher.group(0));
        }
    }
}
```

#### 结巴去重案例

![image-20230114174624459](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301141746644.png)

```java
public class jieba {
    public static void main(String[] args) {
        String content = "我....我要....学学学学....编程java!";
        //  1. 去掉所有的 .
        String regStr = "\\.";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        String s = matcher.replaceAll("");
        System.out.println(s);

        //  2. 去掉重复的字
        //  找到所有连续的重复字段 ---> 缩减成一个！！！
        String regStr1 = "(.)\\1+"; //  分组的捕获内容记录到 $1
        Pattern pattern1 = Pattern.compile(regStr1);
        Matcher matcher1 = pattern1.matcher(s);
        while (matcher1.find()){
            System.out.println(matcher1.group(0));
        }
        //  使用反向引用 $1 来替换匹配到的内容
        System.out.println(matcher1.replaceAll("$1"));
        //  "我" ---> "我我"
        //  "学" ---> "学学学学"
    }
}
```

```java
//	replaceAll() 方法 ---> 返回一个 String
public String replaceAll(String replacement) {
    reset();
    boolean result = find();
    if (result) {
        StringBuilder sb = new StringBuilder();
        do {
            appendReplacement(sb, replacement);
            result = find();
        } while (result);
        appendTail(sb);
        return sb.toString();
    }
    return text.toString();
}
```

可以使用一条语句来去重：

```java
String s1 = Pattern.compile(regStr1).matcher(s).replaceAll("$1");
```



## 7. String 类中使用正则表达式

### 1. 替换功能【repalceAll(String regStr, String replacement)】

```java
public class tihuan {
    public static void main(String[] args) {
        String s = "JDK1.3 fsdfaefwa JDK1.3 fdsafefdsafe48978 JDK1.4";
        String regStr = "JDK1\\.3|JDK1\\.4";
        String replace = s.replaceAll(regStr, "JDK");
        System.out.println(replace);
    }
}
```

![image-20230115170821395](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301151708476.png)

### 2. 判断功能【public boolean matches(String regex){}】

```java
// 使用Pattern 和Matcher 类
```

```java
public class tihuan {
    public static void main(String[] args) {
        String s = "13688889999";
        if (s.matches("1(36|39)\\d{8}")){
            System.out.println("验证成功");
        }else {
            System.out.println("验证失败");
        }
    }
}
```

### 3. 分割功能 [public String[] split(String regex)]

要求按照 <font color="Yellow">#</font> 或者 <font color="yellow">-</font> 或者 <font color="yellow">~</font> 或者 <font color="yellow">数字</font> 来分割

```java
public class tihuan {
    public static void main(String[] args) {
        String s = "hello#abc-jack12smith~北京";
        String[] split = s.split("#|-|~|\\d+");
        for (String s1 : split) {
            System.out.print(s1 + " ");
        }
    }
}
```

![image-20230115172443191](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301151724269.png)

## 8. 课后习题

### 8.1 验证邮件合法性

1. 只能一个 @
2. @ 前面是用户名，可以是a-z A-Z 0-9 _- 字符
3. @ 后面是域名，并且域名只能是英文字母，比如：suhu.com 或者tsinghua.org.cn
4. 写出对应的正则表达式，验证输入的字符串是否满足规则

```java
//	表示 [] 内的任意一个 字符
String str = "[\\w-]+"
```

```java
//	. 写在 [] 内代表 . 本身
//	. 写在 () 内代表 匹配任意字符
```

String 的 mathes（） 方法是整体匹配

```java
//	虽然 String 的 matches() 方法默认是整体匹配
//	但是保险起见
//	还是写上定位符 ^$
public class tihuan {
    public static void main(String[] args) {
        String s = "125-58176@qq.org.cn";
        if (s.matches("^[\\w-]+@([a-zA-Z]+\\.)+(com|cn)$")){
            System.out.println("匹配成功！！");
        }else {
            System.out.println("匹配失败");
        }
    }
}
```

### 8.2 要求验证是不是整数或者小数【考虑正负】

比如：123 -345 34.89 -87.9 -0.01 0.45等

思路：

- 先写出简单的正则表达式
- 再逐步完善

注意：负号 - 最多只能有一个，放在最前面，使用 ? 来进行限定！！！

| 实际值  | 正则表达式                     |
| ------- | ------------------------------ |
| 123     | [\\\d]+                        |
| -345    | [-+]?\[\\\d]+                  |
| 34.89   | [-+]?[\\\d]+(\\\\.\\\d+)?      |
| 0034.89 | [-+]?([1-9]\\\d*)(\\\\.\\\d+)? |
| 0.1     | 最终版本如下：                 |

 ```java
 public class tihuan {
     public static void main(String[] args) {
         //  123 -345 34.89 -87.9 -0.01 0.45等
         String s = "3.1";
         if (s.matches("^[-]?([1-9]\\d*|0)(\\.\\d+)?$")){
             System.out.println("匹配成功！！");
         }else {
             System.out.println("匹配失败");
         }
     }
 }
 ```

### 8.3 对于给 URL进行解析

要求：对一个 URL 进行解析

```html
http://www.sohu.com:8080/abc/index.html
```

| 正则处理 | 得到          |
| -------- | ------------- |
| 协议     | http          |
| 域名     | www.souhu.com |
| 端口     | 8080          |
| 文件名   | index.html    |

实际上就是一个<font color="yellow">**分组**</font>

````java
package Regexp;

import java.net.Socket;

/**
 * @Author: Ronnie LEE
 * @Date: 2023/1/15 - 01 - 15 - 16:59
 * @Description: Regexp
 * @version: 1.0
 */
public class tihuan {
    public static void main(String[] args) {
        //  123 -345 34.89 -87.9 -0.01 0.45等
        String s = "http://www.sohu.com:8080/abc/index.html";
        String[] split = s.split("\\.|(://|/)|:");
        System.out.println("协议为：" + split[0]);
        System.out.println("域名为：" + split[1] + split[2] + split[3]);
        System.out.println("端口为：" + split[4]);
        System.out.println("文件名为：" + split[6] + "." +split[7]);
    }
}
````

分组：分别获取到对应的值

自己的思路：

```java
public class tihuan {
    public static void main(String[] args) {
        //  123 -345 34.89 -87.9 -0.01 0.45等
        String s = "http://www.sohu.com:8080/abc/index.html";
        String regStr = "([a-zA-Z]+)://(([a-zA-Z]+\\.)+com|cn):(\\d+)/([a-zA-Z]+/)+([a-zA-Z]+\\.[a-zA-Z]+)";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            System.out.println("整体匹配成功，内容是：" + matcher.group(0));
            System.out.println("协议是：" + matcher.group(1));
            System.out.println("域名是：" + matcher.group(2));
            System.out.println("端口号：" + matcher.group(4));
            System.out.println("文件名: " + matcher.group(6));
        }
    }
}
```

![image-20230115192722809](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301151927890.png)

弊端：

每次下标都会变化，要改成和老韩的一样才行，这样才具有泛用性！！！

因为正则表达式是根据要求来编写的，所以，需要的话，可以改进 

几个核心间隔如下：

![image-20230115193013177](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202301151930294.png)

分组：

- 4组
- 分别获取到对应的值

```java
public class tihuan {
    public static void main(String[] args) {
        //  123 -345 34.89 -87.9 -0.01 0.45等
        String s = "http://www.sohu.com:8080/abc/index.html";
        String regStr = "^([a-zA-Z]+)://([a-zA-Z.]+):(\\d+)[\\w-/)]*/([\\w.]+)$";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()){
            System.out.println("整体匹配成功，内容是：" + matcher.group(0));
            System.out.println("协议是：" + matcher.group(1));
            System.out.println("域名是：" + matcher.group(2));
            System.out.println("端口号：" + matcher.group(3));
            System.out.println("文件名: " + matcher.group(4));
        }
    }
}
```

## 9. Java正则表达式大全

```bash
一、校验数字的表达式
1 数字：^[0-9]*$
2 n位的数字：^\d{n}$
3 至少n位的数字：^\d{n,}$
4 m-n位的数字：^\d{m,n}$
5 零和非零开头的数字：^(0|[1-9][0-9]*)$
6 非零开头的最多带两位小数的数字：^([1-9][0-9]*)+(.[0-9]{1,2})?$
7 带1-2位小数的正数或负数：^(\-)?\d+(\.\d{1,2})?$
8 正数、负数、和小数：^(\-|\+)?\d+(\.\d+)?$
9 有两位小数的正实数：^[0-9]+(.[0-9]{2})?$
10 有1~3位小数的正实数：^[0-9]+(.[0-9]{1,3})?$
11 非零的正整数：^[1-9]\d*$ 或 ^([1-9][0-9]*){1,3}$ 或 ^\+?[1-9][0-9]*$
12 非零的负整数：^\-[1-9][]0-9"*$ 或 ^-[1-9]\d*$
13 非负整数：^\d+$ 或 ^[1-9]\d*|0$
14 非正整数：^-[1-9]\d*|0$ 或 ^((-\d+)|(0+))$
15 非负浮点数：^\d+(\.\d+)?$ 或 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$
16 非正浮点数：^((-\d+(\.\d+)?)|(0+(\.0+)?))$ 或 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
17 正浮点数：^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$ 或 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
18 负浮点数：^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$ 或 ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
19 浮点数：^(-?\d+)(\.\d+)?$ 或 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$

二、校验字符的表达式
1 汉字：^[\u4e00-\u9fa5]{0,}$
2 英文和数字：^[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}$
3 长度为3-20的所有字符：^.{3,20}$
4 由26个英文字母组成的字符串：^[A-Za-z]+$
5 由26个大写英文字母组成的字符串：^[A-Z]+$
6 由26个小写英文字母组成的字符串：^[a-z]+$
7 由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$
8 由数字、26个英文字母或者下划线组成的字符串：^\w+$ 或 ^\w{3,20}$
9 中文、英文、数字包括下划线：^[\u4E00-\u9FA5A-Za-z0-9_]+$
10 中文、英文、数字但不包括下划线等符号：^[\u4E00-\u9FA5A-Za-z0-9]+$ 或 ^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$
11 可以输入含有^%&',;=?$\"等字符：[^%&',;=?$\x22]+
12 禁止输入含有~的字符：[^~\x22]+


三、特殊需求表达式
1 Email地址：^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
2 域名：[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?
3 InternetURL：[a-zA-z]+://[^\s]* 或 ^https://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$
4 手机号码：^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$
5 电话号码("XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX)：^(\(\d{3,4}-)|\d{3.4}-)?\d{7,8}$ 
6 国内电话号码(0511-4405222、021-87888822)：\d{3}-\d{8}|\d{4}-\d{7}
7 身份证号：
		15或18位身份证：^\d{15}|\d{18}$
		15位身份证：^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$
		18位身份证：^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$
8 短身份证号码(数字、字母x结尾)：^([0-9]){7,18}(x|X)?$ 或 ^\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$
9 帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)：^[a-zA-Z][a-zA-Z0-9_]{4,15}$
10 密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)：^[a-zA-Z]\w{5,17}$
11 强密码(必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间)：^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$ 
12 日期格式：^\d{4}-\d{1,2}-\d{1,2}
13 一年的12个月(01～09和1～12)：^(0?[1-9]|1[0-2])$
14 一个月的31天(01～09和1～31)：^((0?[1-9])|((1|2)[0-9])|30|31)$ 
15 钱的输入格式：
16 1.有四种钱的表示形式我们可以接受:"10000.00" 和 "10,000.00", 和没有 "分" 的 "10000" 和 "10,000"：^[1-9][0-9]*$ 
17 2.这表示任意一个不以0开头的数字,但是,这也意味着一个字符"0"不通过,所以我们采用下面的形式：^(0|[1-9][0-9]*)$ 
18 3.一个0或者一个不以0开头的数字.我们还可以允许开头有一个负号：^(0|-?[1-9][0-9]*)$ 
19 4.这表示一个0或者一个可能为负的开头不为0的数字.让用户以0开头好了.把负号的也去掉,因为钱总不能是负的吧.下面我们要加的是说明可能的小数部分：^[0-9]+(.[0-9]+)?$ 
20 5.必须说明的是,小数点后面至少应该有1位数,所以"10."是不通过的,但是 "10" 和 "10.2" 是通过的：^[0-9]+(.[0-9]{2})?$ 
21 6.这样我们规定小数点后面必须有两位,如果你认为太苛刻了,可以这样：^[0-9]+(.[0-9]{1,2})?$ 
22 7.这样就允许用户只写一位小数.下面我们该考虑数字中的逗号了,我们可以这样：^[0-9]{1,3}(,[0-9]{3})*(.[0-9]{1,2})?$ 
23 8.1到3个数字,后面跟着任意个 逗号+3个数字,逗号成为可选,而不是必须：^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$ 
24 备注：这就是最终结果了,别忘了"+"可以用"*"替代如果你觉得空字符串也可以接受的话(奇怪,为什么?)最后,别忘了在用函数时去掉去掉那个反斜杠,一般的错误都在这里
25 xml文件：^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\.[x|X][m|M][l|L]$
26 中文字符的正则表达式：[\u4e00-\u9fa5]
27 双字节字符：[^\x00-\xff] (包括汉字在内，可以用来计算字符串的长度(一个双字节字符长度计2，ASCII字符计1))
28 空白行的正则表达式：\n\s*\r (可以用来删除空白行)
29 HTML标记的正则表达式：<(\S*?)[^>]*>.*?|<.*? /> (网上流传的版本太糟糕，上面这个也仅仅能部分，对于复杂的嵌套标记依旧无能为力)
30 首尾空白字符的正则表达式：^\s*|\s*$或(^\s*)|(\s*$) (可以用来删除行首行尾的空白字符(包括空格、制表符、换页符等等)，非常有用的表达式)
31 腾讯QQ号：[1-9][0-9]{4,} (腾讯QQ号从10000开始)
32 中国邮政编码：[1-9]\d{5}(?!\d) (中国邮政编码为6位数字)
33 IP地址：\d+\.\d+\.\d+\.\d+ (提取IP地址时有用)
```













 
