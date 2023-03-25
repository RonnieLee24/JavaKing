# JAVA IO 原理与分类

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051512364.png)

## 1. 文件

文件是保存数据的地方

### 1.1 文件流

文件在程序中是以 <font color="yellow">**流**</font> 的形式来操作的

- 流：数据在数据源（文件）和程序（内存）之间经历的 路径
  - 输入流：文件 ---> 内存
  -  输出流：内存 ---> 文件

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051515411.png)

## 1.2 常用的文件操作

### 1.2.1 创建文件对象相关构造器和方法

相关方法：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051516448.png)

在 F盘下，拆创建3个文件： news1.txt，news2.txt，news3.txt

【注意】IDEA会强制让你考虑文件创建失败的情况

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051516152.png)

 使用 ctrl + alt + t 来快速 try - catch

#### a. new File（String pathname) 根据路径构建一个 File 对象

```java
//  方式1：new File（String pathname）  根据路径构建一个 File 对象
@Test
public void create01(){
    String filePath = "f:/news1.txt";
    //  这里还可以写为 "f:\\news1.txt"; 【左1右2】
    File file = new File(filePath); //  现在就只是有了这个对象，但是你还没有创建它，需要调用一个方法
    try {
        file.createNewFile();
        System.out.println("文件创建成功");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### b. new File（File, String child）根据父目录文件 + 子路径构建

```java
//  方式2：new File（File, String child） 根据父目录文件 + 子路径构建
@Test
public void create02(){
    File parent0File = new File("f:/");
    String fileName = "news2.txt";
    //  这里的File对象，在java程序中，只是一个对象【还在内存中】
    //  只有执行了 createNewFile 方法，才会真正的，在磁盘创建该文件
    File file = new File(parent0File, fileName);
    try {
        file.createNewFile();
        System.out.println("创建成功");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### c. new File（String parent，String child） 根据父目录 + 子路径构建【⭐】

```java
//  方式3：new File（String parent，String child）  //根据父目录 + 子路径构建
@Test
public void create03(){
    String parentPath = "f:/";
    String fileName = "news3.txt";
    File file = new File(parentPath, fileName);
    try {
        file.createNewFile();
        System.out.println("创建oK");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### 1.2.2 获取文件的相关信息

getName，getAbsolutePath，getParent，length，exists，isFile，isDirectory

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051523768.png)

```java
//  获取文件信息
@Test
public void info() {
    //  先创建文件对象
    File file = new File("f:/news1.txt");

    //  调用相应的方法，得到对应信息
    System.out.println("文件名字=" + file.getName());

    //  绝对路径
    System.out.println("文件绝对路径" + file.getAbsolutePath());

    //  父级目录
    System.out.println("文件父级目录" + file.getParent());
    System.out.println("文件大小（字节）" + file.length());
    System.out.println("文件是否存在" + file.exists());
    System.out.println("是不是一个文件" + file.isFile());
    System.out.println("是不是一个目录" + file.isDirectory());
}
```

### 1.2.3 目录的删除和文件删除【⭐】

- mkdir创建一级目录
- mkdirs创建多级目录
- delete删除 空目录 或 文件

1. 文件存在就删除

   ```java
   //  判断 f:/news1.txt 是否存在，如果存在就删除
   @Test
   public void m1() {
       String filePath = "f:/news1.txt";
       File file = new File(filePath);
       if (file.exists()){
           if (file.delete()){
               System.out.println(filePath + "删除成功");
           }else {
               System.out.println("删除失败");
           }
       }else {
           System.out.println("该文件不存在...");
       }
   }
   ```

2. 路径存在就删除

   ```java
   //  判断 e:/demo 是否存在，存在就删除，否则提示不存在
   //  这里我们需要体会到，在 java 编程中，目录也会被当作文件
   @Test
   public void m2() {
       String filePath = "f:/demo";
       File file = new File(filePath);
       if (file.exists()){
           if (file.delete()){
               System.out.println(filePath + "删除成功");
           }else {
               System.out.println("删除失败");
           }
       }else {
           System.out.println("该目录不存在...");
       }
   }
   ```

    注意：这里不区分大小写

3. 判断目录 f:/demo/a/b/c 是否存在，如果存在就提示已经存在，否则就创建

   ```java
   //  判断 f:/demo/a/b/c 是否存在，如果存在就提示已经存在，否则就创建
   @Test
   public void m3() {
       String directoryPath = "f:/demo/a/b/c";
       File file = new File(directoryPath);
       if (file.exists()){
           System.out.println("目录已经存在");
       }else {
           if (file.mkdirs()){
               System.out.println(directoryPath + "创建成功..");
           }else {
               System.out.println(directoryPath + "创建失败..");
           }
       }
   }
   ```

   注意：创建多级目录时，要使用 mkdirs（），否则会提示创建失败！！！

## 2. IO流动原理及流的分类

### 2.1 Java IO流原理

这里输入和输出都是要求我们 <font color="yellow">站在内存的角度</font> 上来说的

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051530694.png)

1. I / O 是 input / Output 的缩写

   IO技术是非常实用的技术，用于处理数据传输。如：读 / 写文件，网络通讯等

2. Java程序中，对于数据的 输入 / 输出操作以 "流（stream）" 的方式进行

3. java.io包下提供了各种 “流” 类和接口，用以获取不同种类的数据，并通过方法输入和输出数据

4. 输入input：读取外部数据（磁盘、光盘等存储设备的数据）到程序（内存）中

5. 输出output：将程序（内存）数据输出到磁盘、光盘等存储设备中

### 2.2 流的分类

1. Java的IO流共涉及40多个类，实际上非常规则，都是从如上4个抽象基类派生的
2. 由这4个类派生出来的子类名称都是以其父类名作为子类名后缀

| 抽象基类 | 字节流       | 字符流 |
| -------- | ------------ | ------ |
| 输入流   | InputStream  | Reader |
| 输出流   | OutputStream | Writer |

 InputStream 和 OutputStream 都是***\*抽象类\****，本身是不能直接创建的，只能***\*由它的实现子类\****来创建相应的流对象。

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051535393.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051535953.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051535408.png)

 Reader 和 Writer 与上面类似

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051536946.png)

#### 2.2.1 按照数据单位不同分

 字节流（8 bit）处理 二进制文件【无损操作】

 字符流（按字符）处理 文本文件 ---> 对应几个字节 ---> 和你的编码 有关！！！

#### 2.2.2 按数据流的流向不同分

输入流

输出流

#### 2.2.3 按流的角色的不同分

节点流

处理流 / 包装流

### 2.3 IO流体系图 --- 常用的类

1. IO 流体系图

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051542966.png)

2. 文件 VS 流

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051543330.png)

3. InputStream 常用的子类

   - FileInputStream：文件输入流
   - BufferedInputStream：缓冲字节输入流
   - ObjectInputStream：对象字节输入流

   注意：BufferedInputStream的直接父类并不是 FilterInputStream

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051546759.png)

   这里我们使用 Ctrl + alt + P 来寻找直接父类

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051547776.png)

#### 2.3.1 FileInputStream 介绍

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051547408.png)

 FileInputStream应用实例

读取hello.txt文件，并将文件内容显示到控制台

```java
//演示 FileInputStream 的使用（字节输入流  文件 ---> 程序）
@Test
public void readFile01() {
    String filePath = "f:/hello.txt";
    int readData = 0;
    FileInputStream fileInputStream = null;
    try {
        //  创建 FileInputStream 对象，用于读取 文件
        fileInputStream = new FileInputStream(filePath);
        //  从该输入流读取一个字节的数据，如果没有输入可用，此方法将中止
        //  如果返回 -1，表示读取完毕
        while ((readData = fileInputStream.read())!= -1) {
            System.out.print((char)readData);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        // 关闭文件流，释放资源
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

使用 read(byte[] b)读取文件，提高效率

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051550912.png)

```java
//  使用 read(byte[] b) 读取文件，提高效率
@Test
public void readFile02() {
    String filePath = "f:/hello.txt";
    int readLen = 0;
    //  定义字节数组
    byte[] buf = new byte[8];   //  一次读取8个字节
    FileInputStream fileInputStream = null;
    try {
        //  创建 FileInputStream 对象，用于读取 文件
        fileInputStream = new FileInputStream(filePath);
        //  从该输入流读取最多 buf.length 个字节的数据 读入一个 byte数组中
        //  如果返回 -1，表示读取完毕
        //  如果读取正常，返回实际读取的字节数
        while ((readLen = fileInputStream.read(buf))!= -1) {
            System.out.print(new String(buf, 0, readLen)); //   显示
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        // 关闭文件流，释放资源
        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

第一次的情况：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051553752.png)

 第二次的情况：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051553762.png)

 第三次的情况

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051554328.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051605675.jpeg)

通过观察：从第二次开始，buf 中如果没有更新的话，就维持红色，如果更新了的话，旧刷新为蓝色	

#### 2.3.2 FileOutputStream介绍

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051557987.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051558964.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051558284.png)

 要求：请使用FileOutputStream在a.txt文件中写入 "hello, world"，如果文件不存在，会创建文件（注意：前提是目录已经存在）

``` java
/*
    * 演示使用 FileOutputStream 将数据写入到 文件中
    * 如果该文件不存在，则创建该文件
    * */
@Test
public void writeFile(){
    //  创建  FileOutputStream 对象
    String filePath = "f:/a.txt";
    FileOutputStream fileOutputStream = null;
    try {
        //  得到 FileOutputStream 对象
        fileOutputStream = new FileOutputStream(filePath);
        //  写入一个字节
        fileOutputStream.write('H');
    } catch (IOException e) { //  这里写成IOException 就可以处理更多的异常
        e.printStackTrace();
    } finally {
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051600097.png)

那么如果要写入多个字节要怎么做呢??? ---> 代码如下：

- 写入字符串
- getBytes() 方法可以将 String 转化为 bytes[] 数组

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051600966.png)

```java
try {
    //  得到 FileOutputStream 对象
    fileOutputStream = new FileOutputStream(filePath);
    //  写入字符串
    String str = "hello,world!!!";
    //  str.getBytes() 可以把 字符串 ---> 字节数组
    fileOutputStream.write(str.getBytes());
} 
```

输出前3个字符

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051601921.png)

```java
try {
    //  得到 FileOutputStream 对象
    fileOutputStream = new FileOutputStream(filePath);
    //  写入字符串
    String str = "hello,world!!!";
    //  str.getBytes() 可以把 字符串 ---> 字节数组
    fileOutputStream.write(str.getBytes(), 0, 3);
} 
```

有关覆盖的问题

说明：

1. new FileOUtputStream(filePath)创建方式，当写入内容时，会覆盖原来的内容

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051603273.png)

2. new FileOutputStream(filePath, true) 创建方式，当写入内容时，是 追加到 文件后面

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051604526.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051607498.png)

### 2.4 文件拷贝【⭐边读边写】

编程完成 图片 / 音乐 的拷贝

将 f 盘的 T1.jpg 拷贝到 d 盘中去

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051607915.png)

```java
package outputstream;
 
public class FileCopy {
    public static void main(String[] args) {
        //  将 f 盘的 T1.jpg 拷贝到 d 盘中去
        //  思路分析
        //  1）创建文件的输入流，将文件读入到程序
        //  2）创建文件的输出流，将读取到的文件数据，写入到指定的文件
        String filePath = "f:/T1.jpg";
        String filePath1 = "d:/T1.jpg";
 
 
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
 
        try {
            fileInputStream = new FileInputStream(filePath);    //  输入流
            fileOutputStream = new FileOutputStream(filePath1);
            //  定义一个字节数组，提高读取效率
            byte[] buf = new byte[1024];
            int readLen = 0;
            while ((readLen = fileInputStream.read(buf))!=-1){
                //  读取到后，就写入到文件，通过 fileOutputStream
                //  即：一遍读，一遍写
                fileOutputStream.write(buf, 0, readLen);    //  一定要使用这个方法
            }
            System.out.println("拷贝成功~");
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //  关闭输入流 和输出流，释放资源
                if (fileInputStream != null){
                    fileInputStream.close();
                }
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### 2.5 FileReader 和 FileWriter 介绍

FileReader 和 FileWriter 是字符流，即：按照字符来操作io

#### 2.5.1 FileReader相关方法

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051611090.png)

1. new FileReader（File / String）
2. read()：每次读取单个字符，返回该字符，如果文件末尾则返回 -1
3. fread(char[])：批量读取多个字符到数组，返回读取到字符数，如果到文件末尾返回 -1

相关 API：

1. new String(char[])：将char[] 转换成 String
2. new String(char[], off, len) ：将 char[] 的指定部分转换成 String

使用read，单个字符读取

```java
package reader;
 
public class FileReader_ {
    public static void main(String[] args) {
 
        String filePath = "f:/story.txt";
        FileReader fileReader = null;
        int data = 0;
 
        //  1.  创建FileReader 对象
        try {
            fileReader = new FileReader(filePath);
            //  循环读取 使用 read，单个字符读取
            while ((data = fileReader.read())!= -1) {
                System.out.print((char)data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

循环读取，使用 read(buf)，返回的是实际读取到字符数

如果返回 -1，说明到文件结束处了

```java
@Test
public void readFile02() {
    String filePath = "f:/story.txt";
    FileReader fileReader = null;
    int readLen = 0;
    char[] buf = new char[8];

    //  1.  创建FileReader 对象
    try {
        fileReader = new FileReader(filePath);
        //  循环读取 使用 read(buf)，返回的是实际读取到的字符数
        //  如果返回 -1，说明到文件结束，
        while ((readLen = fileReader.read(buf)) != -1) {
            System.out.print(new String(buf, 0, readLen));
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### 2.5.2 FileWriter常用方法

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051615298.png)

1. new FileWriter(File / String)：覆盖模式，相当于流的指针在首端
2. new FIleWirter(File / String, true)：追加模式，相当于流的指针在尾端口
3. write(int)：写入单个字符
4. write(char[])：写入指定数组
5. write(char[], off, len)：写入数组的指定部分
6. write(string)：写入整个字符串
7. write(string, off, len)：写入字符串的指定部分

相关 API：

String类：toCharArray：将String 转换成 char[]

<font color="yellow">**注意**</font>：FileWriter使用后，必须要关闭（close）或刷新（flush）否则写入不到指定的文件！

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051618127.png)

```java
package writer;
 
public class FileWriter_ {
    public static void main(String[] args) {
 
    }
 
    @Test
    //  单个字符
    public void Writer1(){
        String filePath = "f:/note.txt";
        //  创建 FileWriter对象
        FileWriter fileWriter = null;
 
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write('H');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  对于 FileWriter，一定要关闭流。或者 flush 才能真正地把数据写入到文件
            //  后面看源码就知道为什么了
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("程序结束");
    }
 
    @Test
    //  数组
    public void Writer2(){
        String filePath = "f:/note.txt";
        //  创建 FileWriter对象
        FileWriter fileWriter = null;
        char[] chars = {'a', 'b', 'c'};
 
 
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(chars);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  对于 FileWriter，一定要关闭流。或者 flush 才能真正地把数据写入到文件
            //  后面看源码就知道为什么了
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("程序结束");
    }
 
    @Test
    //  数组（指定输出具体的部分）
    public void Writer3(){
        String filePath = "f:/note.txt";
        //  创建 FileWriter对象
        FileWriter fileWriter = null;
        String string1 = "爱新觉罗LQ";
 
 
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(string1.toCharArray(), 0, 4);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  对于 FileWriter，一定要关闭流。或者 flush 才能真正地把数据写入到文件
            //  后面看源码就知道为什么了
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("程序结束");
    }
 
    @Test
    //  写入整个字符串
    public void Writer4(){
        String filePath = "f:/note.txt";
        //  创建 FileWriter对象
        FileWriter fileWriter = null;
        String string7 = "我就是小丑皇本皇了";
 
 
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(string7);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  对于 FileWriter，一定要关闭流。或者 flush 才能真正地把数据写入到文件
            //  后面看源码就知道为什么了
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("程序结束");
    }
 
    @Test
    //  指定字符串的某个部分
    public void Writer5(){
        String filePath = "f:/note.txt";
        //  创建 FileWriter对象
        FileWriter fileWriter = null;
        String string8 = "我就是沸羊羊";
 
 
        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(string8, 0, 4);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  对于 FileWriter，一定要关闭流。或者 flush 才能真正地把数据写入到文件
            //  后面看源码就知道为什么了
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("程序结束");
    }
}
```

#### 为什么 close后，才会真正写入到文件中！！！【⭐】

现在，我们就来研究下，为什么说，只有在 close()【flush()】后，才会真正地写入到文件中？？？

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051620842.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051620670.png)

 注意：这个方法也要 force step into才可以进入

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051621937.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051622426.png)

 下面才是真正干活的地方

```java
private void writeBytes() throws IOException {
    bb.flip();
    int lim = bb.limit();
    int pos = bb.position();
    assert (pos <= lim);
    int rem = (pos <= lim ? lim - pos : 0);

    if (rem > 0) {
        if (ch != null) {
            if (ch.write(bb) != rem)
                assert false : rem;
        } else {
            out.write(bb.array(), bb.arrayOffset() + pos, rem);	//	out 也是字节流
        }
    }
    bb.clear();
}
```

看到 FileWriter的底层 用的是 FileOutputStream 【字符流也是基于字节流的】

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051626154.png)

```java
public final byte[] array() {
    if (hb == null)
        throw new UnsupportedOperationException();
    if (isReadOnly)
        throw new ReadOnlyBufferException();
    return hb;
}
```

flush同理，最后也是进入到writeBytes() 方法 ---> 这个方法属于 StreamEncoder

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051626263.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051627586.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051627998.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051627444.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051627645.png)

 最后就还是走到 out 对象（FileOutputStream） （即最底层用的还是文件输出流）

总结：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303051628224.png)

### 2.6 节点流和处理流【包装流】【Buffer】

节点流和处理流一览图

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061432110.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061433642.png)

 数据源：就是存放数据的地方

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061434457.png)

这种灵活性和效率会受到一些影响，功能可能不是很强大

这个时候Java的设计者提供了另外一种流叫 **处理流**，这个处理流也叫做 **包装流**，简单地说它就是对节点流进行一个包装。让我们这个流的功能更加地强大。

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061435771.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061436011.png)

 只要是 Reader 的子类 都可以封装到 BufferedReader 中

Reader

- InputStreamReader
- FileReader
- StringReader
- CharArrayReader

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061436481.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061438227.png)

同理Writer也是一样的

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061438743.png)

 这个BufferedWriter也含有Writer这个属性

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061439753.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061439669.png)

 我们来看下这个 BufferedWriter的构造器也是非常丰富的：

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061440078.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061441944.png)

同理：再来看下 ObjectOutputStream

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061441933.png)

#### 2.6.1 节点流和处理流的区别和联系

处理流得到重点放在了功能，节点流的重点是操作的对象

包装流的好处就是把各类的节点流包装起来了，屏蔽底层差异，类似 Spring Cloud Stream

1）节点流是底层流 / 低级流，直接和数据源头相接

2）处理流（包装流）包装节点流，既可以 <font color="yellow">**消除不同节点流的实现差异**</font>，也可以提供更方便的方法来完成输入输出。

3）处理流（也叫包装流）对节点进行包装，使用了修饰器设计模式，不会直接与数据源相连【模拟 <font color="yellow">**修饰器设计模式**</font>】 ---> 只是去调用不同的节点流

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061448228.png)

 现在我们要进行扩展：BufferedReader_

这里涉及到抽象类的知识

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061511032.png)

 Reader58这里为抽象类！！!

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061511321.png)

这里在根据封装的运行类型来决定：调用哪个方法

【本质上：还是多态罢了！！！】

在 BufferedReader_ 中对方法进行了封装

```java
package IO_;
/**
 * 做成处理流 / 包装流
 */
public class BufferedReader_ extends Reader58 {
 
    private Reader58 reader58;  //  属性是 Reader58 类型
 
    //  接收 Reader58 子类对象
    public BufferedReader_(Reader58 reader58) {
        this.reader58 = reader58;
    }
    //  我们这个继承 Reader58的，那么我们就可以调用 Reader58的方法
 
    //  1）可以原封不动地调用方法
 
    public void readFile() {    //  封装一层
        reader58.readFile();
    }
 
    public void readString(){
        reader58.readString();
    }
 
    //  让方法更加灵活，多次读取文件，或者加缓冲 char[] ...
 
    public void readFiles(int num) {
        for (int i = 0; i < num; i++) {
            reader58.readFile();
        }
    }
 
    //  扩展 readString，批量处理字符串数据
    public void readString(int num) {
        for (int i = 0; i < num; i++) {
            reader58.readString();
        }
    }
}
```

```java
package IO_;
 
public class Test_ {
    public static void main(String[] args) {
        BufferedReader_ bufferedReader_ = new BufferedReader_(new Filereader_());
        bufferedReader_.readFiles(3);
        BufferedReader_ bufferedReader_1 = new BufferedReader_(new StringReader_());
        bufferedReader_1.readString(4);
    }
}
```

处理流的功能主要体现在以下 2个方面

1. 性能的提高：主要以增加 <font color="yellow">**缓冲的方式**</font> 来提高输入输出的效率
2. 操作的便捷：处理流可能提供了
   - 一系列便捷的方法
   - 来一次输入输出最大批量的数据
   - 使用更加灵活方便

#### 2.6.2  处理流 BufferedReader 和 BufferedWriter ---> 处理文本文件

尽量操作文本文件，不要是二进制文件（按照字节来组织的）

二进制文件：图片，声音，视频

BufferedReader 和 BufferedWriter 属于 **<font color="yellow">字符流</font>**，是按照字符来读取数据的

关闭时处理流时，只需要 <font color="red">**关闭外层流** </font>即可 【后面看源码】 ---> <font color="red">**真正来工作的是我们的节点流**</font>，而不是处理流

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061528964.png)

 在我们关闭 ***\*处理流\****（包装流）bufferedReader_ 的时候，它在底层会自动地去关闭我们***\*包装的节点流\**** new FileReader_()

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061529704.png)

1. 在使用 BufferedReader 读取不同的文件，并显示在控制台

   ```java
   package reader;
   
   /**
    * 演示 bufferedReader 使用
    */
   public class BufferedReader_ {
       public static void main(String[] args) throws Exception {
    
           String filePath = "f:/story.txt";
    
           //  创建 bufferedReader 对象
           BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
           //  读取
           String line;    //  按行读取，效率高
           //  说明
           //  1）bufferedReader.readLine() 是按行读取文件
           //  2）当返回 null 时，表示文件读取完毕
           while ((line = bufferedReader.readLine())!= null) {
               System.out.println(line);
           }
    
           //  关闭流，这里注意，只需要关闭 BufferedReader，因为底层会自动地关闭  节点流 FileReader
           bufferedReader.close();
       }
   }
   ```

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061603308.png)

   - 这个 in 就是我们的 FileReader

   - 其实它在底层调用的是我们传进去的 节点流的 对象的 close()

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061605052.png)

2. 使用 BufferedWriter 将 "hello, 韩顺平教育"，写入到文件中

   ```java
   package writer;
    
   public class BufferedWriter_ {
       public static void main(String[] args) throws IOException {
           String filePath = "f:/爱新觉罗LQ.txt";
           BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
           bufferedWriter.write("hello, 欢迎回来！！!");
           //  插入一个和系统相关的换行符
           bufferedWriter.newLine();
           bufferedWriter.write("hello, 欢迎回来！！!");
           //  说明：关闭外层流即可，传入的 new FIleWriter(filePath)，会在底层关闭
           bufferedWriter.close();
       }
   }
   ```

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061608680.png)

   我们看到 BufferedWriter没有提供，但是 FileWriter(String, boolean)提供了

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061608145.png)

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061608462.png)

3. 综合使用 BufferedReader 和 BufferedWriter 完成文本文件拷贝 【注意文件编码】

   ```java
   package writer;
    
   public class BufferedCopy_ {
       public static void main(String[] args) throws Exception {
    
           //  老韩说明
           //  1）BufferedReader 和 BufferedWriter 是按照字符操作的
           //  2）不要去操作 二进制文件【声音，视频，doc，pdf等等】，可能造成文件损坏
           String srcFilePath = "f:/爱新觉罗LQ.txt";
           String desFilePath = "f:/爱新觉罗LQ24.txt";
           BufferedReader br = null;
           BufferedWriter bw = null;
    
           try {
               br = new BufferedReader(new FileReader(srcFilePath));
               bw = new BufferedWriter(new FileWriter(desFilePath));
               String line;
    
               //  说明：readline 读取一行内容，但是没有带换行符
               while ((line = br.readLine()) != null) {
                   //  每读取一行就写入
                   bw.write(line);
                   bw.newLine();	// ---> 读完一行要换行！！！
               }
               System.out.println("拷贝完毕~~~");
           } finally {
               //  关闭流
               if (br != null) {
                   br.close();
               }
               if (bw != null) {
                   bw.close();
               }
           }
       }
   }
   ```

#### 2.6.3 处理流 BufferedInputStream 和 BufferedOutputStream ---> 图片，音乐拷贝

BufferedInputStream 是 <font color="yellow">**字节流**</font>，在创建 BufferedInputStream 时，会创建一个内部缓冲数组

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061613213.png)

 BufferedOutputStream是***\*字节流\****，实现缓冲的输出流，可以将多个字节写入底层输出流中，而不必对每次字节写入调用底层系统。

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061614217.png)

处理流的整体流程就是我们前面讲的修饰器模式，只是发生了一点点儿变化：

它 包装起来的对象【in，out】，是从父类继承下来的，

应用实例：

编程完成图片 / 音乐的拷贝（要求使用 Buffered..流）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061624532.png)

```java
package outputstream;

/**
 * 演示使用 BufferedOutputStream 和 BufferedInputStream
 */
public class BufferedCopy02 {
    public static void main(String[] args) {
 
        String srcFilePath = "f:/T1.jpg";
        String destFilePath = "f:/T2.jpg";
 
        //  创建 BufferedOutputStream 和 BufferedInputStream 对象
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
 
        try {
            //  因为 FileInputStream 是 InputStream 子类
 
            bis = new BufferedInputStream(new FileInputStream(srcFilePath));
            bos = new BufferedOutputStream(new FileOutputStream(destFilePath));
 
            //  循环地读取文件，并写入到 destFilePath
            byte[] buff = new byte[1024];
            int readLen = 0;
            // 当返回 -1 时，就表示文件读取完毕
            while ((readLen = bis.read(buff)) != -1){
                bos.write(buff, 0, readLen);
            }
            System.out.println("文件拷贝完毕！！！");
 
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //  关闭流，关闭外层的处理流即可，底层回去自动关闭节点流
            try {
                if (bis != null){
                    bis.close();
                }
 
                if (bos != null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

思考：字节流可以操作二进制文件，可以操作文本文件么？？？

可以因为文本文件底层最终还是按照字节来处理，

字节是基本的单位，它既可以操作二进制文件，也可以操作文本文件

### 2.7 对象处理流

ObjectInputStream 和 ObjectOutputStream

需求：

1. 将int num = 100 这个 int 数据保存到文件中，注意不是 数字 100，而是 Int 100，并且，能够从文件中直接恢复 int 100

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061630365.png)

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061631785.png)

2. 将Dog dog = new Dog("小黄", 3) 这个dog 对象 保存到文件中，并且能够从文件中恢复

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061632851.png)

3. 上面的需求，就是 能够将 基本数据类型 或者 对象 进行序列化 和 反序列化操作

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061632946.png)

   需要让某个对象支持序列化机制，则必须让其类是可序列化的，为了让某个类是可序列化的，该类必须实现如下2个接口之一：

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061633971.png)

   - Serializable：这是一个标记接口（声明性质的，里面没有任何方法）

   - Externalizable：【它也是实现了 Serializable接口】，里面有2 个方法，那就需要去实现这 2个 方法

     ```java
     public interface Externalizable extends java.io.Serializable {
     
         void writeExternal(ObjectOutput out) throws IOException;
      
         void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
     }
     ```

#### 2.7.1 序列化【保存到本地】

在保存数据（输出流）时，保存数据的 **值** 和 **数据类型**

#### 2.7.2 反序列化【读入到内存中】

在恢复数据（输入流）时，恢复数据的 **值** 和 **数据类型**

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061638187.png)

类似之前的修饰者模式

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061639907.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303061639500.png)

#### 2.7.3  应用案例

1. 使用ObjectOutputStream 序列化 基本数据类型和 一个Dog 对象（name, age），并保存到 data.dat文件中

   - 注意：写入字符串使用的是 writeUTF（String str）方法

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141924654.png)

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141925348.png)

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141926535.png)

     ```java
     package outputstream;
     
     /**
      * 演示 ObjectOutputStream的使用，完成数据的序列化
      */
     public class ObjectOutStream_ {
         public static void main(String[] args) throws Exception {
             //  序列化后，保存的文件格式，不是纯文本，而是按照它的格式来保存
             String filePath = "f:/data.dat";
      
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
      
             //  序列化数据到：f:/data.dat
             oos.write(100); //  int --> integer(实现了 Serializable)
             oos.writeBoolean(true); //  boolean --> Boolean(实现了 Serializable)
             oos.writeChar('a'); //  char --> Character（实现了 Serializable）
             oos.writeDouble(9.5);   //double --> Double(实现了 Serializable)
             oos.writeUTF("韩顺平教育");  //  String
      
             //   保存一个dog对象
      
             oos.writeObject(new Dog("阿柴", 3));
      
             oos.close();
      
             System.out.println("数据保存完毕（序列化形式）");
         }
     }
      
     //  如果需要序列化某个类的对象，实现 Serializable 接口
     class Dog implements Serializable {
         private String name;
         private int age;
      
         public Dog(String name, int age) {
             this.name = name;
             this.age = age;
         }
     }
     ```

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141927899.png)

2. 使用 ObjectInputStream 读取 data.dat 并反序列化恢复数据

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141928453.png)

   在转换时，这个ObjectInputStream_中也应该有 Dog 类的定义！！！

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141929599.png)

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141929934.png)

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141930258.png)

    总之：就是反序列化之前，需要把对应的类导入进来

   ```java
   package inputstream;
    
   public class ObjectInputStream_ {
       public static void main(String[] args) throws IOException, ClassNotFoundException {
    
           //  指定反序列化的文件
           String filePath = "f:/data.dat";
           ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
    
           //  读取
           //  老韩解读
           //  1）读取（反序列化）的顺序需要和你保存数据（序列化）的顺序一致
           //  否则会出现异常
    
           System.out.println(ois.readInt());
           System.out.println(ois.readBoolean());
           System.out.println(ois.readChar());
           System.out.println(ois.readDouble());
           System.out.println(ois.readUTF());
    
           //  将狗读回来！！！
    
           //  dog 的编译类型是 Object，dog的运行类型是 Dog
           Object dog = ois.readObject();
           System.out.println("运行类型" + dog.getClass());
           System.out.println("dog信息=" + dog);     //  底层 Object --> Dog
    
           //  1）如果我们希望调用 Dog 方法，需要向下转型
           //  2）需要我们将Dog类的定义，放在可以引用的位置
           Dog1 dog2 = (Dog1)dog;
           System.out.println(dog2.getAge());
    
           //  关闭流，关闭外层流即可，底层会关闭 FileInputStream 流
               ois.close();
       }
   }
   ```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141932718.png)

#### 2.7.4 注意事项和细节说明

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141932601.png)

1）读写顺序要一致

2）要求序列化或反序列化对象，需要实现 Serializable

3）序列化的类中建议添加 SerialVersionUID，为了提高版本的兼容性

4）序列化对象时，默认将里面所有属性都进行序列化，但除了 static 或 transient 修饰的成员

5）序列化对象时，要求里面属性的类型也需要实现序列化接口

6）序列化具备可继承性，也就是如果某类已经实现了序列化，则它的所有子类也已经默认实现了序列化

### 2.8 标准输入输出流

|                     | 类型        | 默认设备 |
| ------------------- | ----------- | -------- |
| System.in 标准输入  | InputStream | 键盘     |
| System.out 标准输出 | PrintStream | 显示器   |

```java
package standard_;
 
public class InputAndOutput_ {
    public static void main(String[] args) {
        // System 类 的 public static final InputStream in = null;
        // System.in 编译类型 InputStream
        // System.in 运行类型 BufferedInputStream (属于字节流，同时还是处理流)
        // 表示的是 标准输入 键盘
        System.out.println(System.in);
 
        // System 类 的 public static final PrintStream out = null;
        // 编译类型 PrintStream
        // 运行类型 PrintStream
        // 表示标准输出 显示器
        System.out.println(System.out);
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141940768.png)

```java
//  我们实际上给扫描器传入的就是 BufferedInputStream
Scanner scanner = new Scanner(System.in);
System.out.println("请输入内容：");
//  它就会从我们标准输入（键盘）得到输入
String next = scanner.next();
System.out.println("next=" + next);
```

### 2.9 转换流【解决乱码问题 字节流 ---> 字符流】InputStreamReader / OutputStreamWriter

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141943667.png)

先看一个文件乱码问题，引出学习转换的必要性

```java
package transformation;
 
import java.io.*;
/**
 * 看一个中文乱码问题
 */
public class CodeQuestion {
    public static void main(String[] args) throws IOException {
 
        //  读取：f:/llq.txt 文件到程序
        //  思路
        //  1）创建字符输入流 BufferedReader[处理流]
        //  2）使用 BufferedReader 对象读取 llq.txt
        //  3）默认情况下，读取文件是按照 UTF-8 编码
        String filePath = "f:/llq.txt";
        BufferedReader br = new BufferedReader(new FileReader(filePath));
 
        String s = br.readLine();
        System.out.println("读取到的内容：" + s);
        br.close();
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141945846.png)

默认情况下，读取文件是按照 UTF-8 编码

现在我们把编码改为：ANSI

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141947723.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141947825.png)

 乱码原因：你没有去指定读取文件采用的编码方式！！！

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141948854.png)

<font color="yellow">字节流 + 编码方式 ---> 字符流</font>

#### 1. InputStreamReader

Reader的子类，可以将InputStream（字节流）**包装** 成 Reader（字符流）

底层用的是字节流，但是处理的时候是按照字符方式来处理的 ---> 所以称为 包装【从类的继承关系来说】

有些地方会说成 **转换**：

从作用和最后达到的效果来说，可以说是转换

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141950999.png)

#### 2. OutputStreamWriter

Writer的子类，实现将 OutputStream（字节流）包装成 Writer（字符流）

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141951870.png)

当处理纯文本数据时，如果使用字符流效率更高，并且可以有效地解决中文问题，所以建议将字节流转换成字符流

可以在使用时指定编码格式（比如：ufr-8，gbk，gb2312，ISO8859-1 等）

```java
package transformation;
 
import java.io.*;
 
/**
 * @Description: transformation
 * 演示使用 InputStreamReader 转换流解决中文乱码问题
 * 将字节流 FileInputStream 转成字符流 InputStreamReader，指定编码 gbk / utf-8
 */
public class InputStreamReader_ {
    public static void main(String[] args) throws IOException {
        String filePath = "f:/llq.txt";
        //  老韩解读
        //  1）FileInputStream(filePath) 转成 InputStreamReader
        //  2）同时指定了编码 utf-8
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "gbk");
        //  3）把 InputStreamReader 传入BufferedReader
        BufferedReader br = new BufferedReader(isr);
 
        //  4）读取
        String s = br.readLine();
        System.out.println("读取内容=" + s);
 
        //  5）关闭外层流
        br.close();
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141953284.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141953209.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141953455.png)

### 2.10 打印流 - PrintStream 和 PrintWriter

注意：打印流只有输出流，没有输入流

#### 1. PrintStream

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141958609.png)

 不止可以打印到显示器上，也可以把信息打印到一个文件里面去

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141959940.png)

演示 字节打印流

由于 out 的类型为 PrintStream

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141959381.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303141959247.png)

```java
public static void setOut(PrintStream out) {
    checkIO();
    setOut0(out);
}
```

注意：这个setOut0(PrintStream out) 方法是一个 native 方法 ---> 底层是 C++ 了

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142000981.png)

```java
package printstream_;

/**
 * 演示PrintStream（字节打印流 / 输出流）
 */
public class printStream_ {
    public static void main(String[] args) throws IOException {
 
        PrintStream out = System.out;
        //  在默认情况下，PrintStream 输出数据的位置是 标准输出，即：显示器
 
       /* public void print(String s) {
            write(String.valueOf(s));
        }*/
        out.print("hello!!");
        //  因为print 底层使用的是 write，所以我们可以直接调用 write 进行打印 / 输出
        out.write("爱新觉罗LQ".getBytes()); //  这里的 write 是方法，不是 writer类
        out.close();
 
        //  我们可以去修改打印流输出的位置 / 设备
 
      /*  public static void setOut(PrintStream out) {
            checkIO();
            setOut0(out);
        }*/
 
        //  1）  现在修改到 f 盘的文件中
        //  2）  "hello!!!7788" 就会输出到该文件中
       System.setOut(new PrintStream("f:/F1.txt"));
       System.out.println("hello!!!7788");
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142001962.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142005514.png)

#### 2. PrintWriter

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142005999.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142005246.png)

打印到控制台

```java
public class PrintWriter_ {
    public static void main(String[] args) {
 
        //  打印到控制台
        PrintWriter printWriter = new PrintWriter(System.out);
        printWriter.print("hello!!！Ronnie");
        printWriter.close();
 
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142006722.png)

打印到指定文件中去

```java
public class PrintWriter_ {
    public static void main(String[] args) throws IOException {
 
        //  打印到指定文件中去
        PrintWriter printWriter = new PrintWriter(new FileWriter("f:/F2.txt"));
        printWriter.print("hi,369");
        printWriter.close();  // flush + 关闭流，才会将数据写入到文件
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142006727.png)

### 2.11 Properties 类

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142007637.png)

1）专门用于读写配置文件的集合类

配置文件的格式：

键 = 值

键 = 值

2）注意：键值对不需要要有空格，值不需要要用引号括起来，默认类型是 String

3）Properties的常见方法

- load：加载配置文件的键值对到 Properties对象

- list：将数据显示到指定设备 / 流对象

- getProperty(key)：根据键获得值 【根据 key 获取相应的 value】

- setProperty(key, value)：设置键值对到 Properties 对象

- store：将Properties 中的键值对存储到配置文件，在idea中，保存信息到配置文件中，如果含有中文，会存储为 unicode码

unicode码查询工具：https://tool.chinaz.com/tools/unicode.aspx

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142008250.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303142008341.png)

传统实现方法

​	先创建个数据库配置文件

​	![image-20230316114830705](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161148807.png)

```java
package properties_;
 
public class Properties01 {
    public static void main(String[] args) throws IOException {
 
        //  读取 mysql.properties 文件，并得到 ip，user 和 pwd
        BufferedReader br = new BufferedReader(new FileReader("src\\mysql.properties"));
 
        String line = null;
        while ((line = br.readLine()) != null) {  //  循环读取
            String[] split = line.split("=");
            //  如果要求指定得到 ip值
            if ("ip".equals(split[0])) {
                System.out.println(split[0] + "值是：" + split[1]);
            }
            br.close();
        }
    }
}
```

#### 1. Properties 读文件

```java
package printstream_;
 
public class Properties01 {
    public static void main(String[] args) throws IOException {
 
        //  使用 Properties 类来读取 mysql.properties 文件
 
        //  1）创建 Properties 对象
        Properties properties = new Properties();
        //  2）加载指定配置文件
        properties.load(new FileReader("src\\mysql.properties"));
        //  3）把 k - v 显示在控制台
        properties.list(System.out);
        //  4）根据key 获取对应的值
        String user = properties.getProperty("user");
        String pwd = properties.getProperty("pwd");
        System.out.println("用户名=" + user);
        System.out.println("密码=" + pwd);
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161152670.png)

#### 2. Properties 修改文件

```java
package properties_;
 
public class Properties03 {
    public static void main(String[] args) throws IOException {
        //  使用 Properties 类来创建 配置文件，修改配置文件内容
        Properties properties = new Properties();
        //  创建 【现在键值对是存在内存里面】
        properties.setProperty("charset", "urf8");
        properties.setProperty("user", "汤姆");  
        properties.setProperty("pwd", "abc789");
 
        //  将 k - v 存储到文件中
        properties.store(new FileOutputStream("src\\mysql2.properties"), null);
        System.out.println("配置文件保存成功~")
    }
}
```

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161202441.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161202218.png)

 我们看到这里已经指定了字节流的 编码为：ISO_8859_1.INSTANCE

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161203498.png)

![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303161203723.png)

 看吧，代码最后有 bw.flush()，所以最后才会写入成功的！！！

第二个参数 comment 代表着 注释的意思

setProperty(key, value)

- 如果改文件没有key，就是创建
- 如果该文件有key，就是修改

## 3. 本章作业

1. 判断f盘下是否有文件夹 mytemp，如果没有就创建 mytemp

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171449036.png)

2. 在 f:/mytemp 目录下，创建文件 hello.txt

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171450455.png)

3. 如果 hello.txt 已经存在，提示该文件已经存在，就不要再重复创建了

   ```java
   package properties_;
    
   public class homework1 {
       public static void main(String[] args) throws IOException {
    
           //  创建目录
           String dirPath = "f:/mytemp";
           if (!new File(dirPath).exists()) {  //  如果对象不存在，就创建
               if (new File(dirPath).mkdirs()){    //  对象调用方法
                   System.out.println("创建 " + dirPath + " 成功");
               }else {
                   System.out.println("创建 " + dirPath + " 失败");
               }
           }
    
           //  在目录中创建文件
           String filePath = dirPath + "/hello.txt";
           if(!new File(filePath).exists()){
               if (new File(filePath).createNewFile()){
                   System.out.println(filePath + " 创建成功~");
               }else {
                   System.out.println("文件创建失败~");
               }
           }else {
               //  如果文件已经存在了，给出提示信息
               System.out.println(filePath + " 已经存在了，不再重复创建...");
    
           }
       }
   }
   ```

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171451275.png)

4. 并且在 hello.txt 文件中，写入 hello,world~

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171454474.png)

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171454621.png)

5. 编程题：

   - 使用BufferedReader（字节流可以指定字符 ---> 字符流提升效率 ---> 修饰器模式）

   - 读取一个文本文件，为每行加上行号，再连同内容一并输出到屏幕上

     ```java
     package properties_;
      
     public class homework02 {
         public static void main(String[] args) throws IOException {
             String filepath = "f:/77.txt";
             int row = 0;
             String Line = null;
             BufferedReader br = new BufferedReader(new FileReader(filepath));
             while ((Line =  br.readLine()) != null){
                 System.out.println(++row + "  " + Line);
             }
         }
     }
     ```

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171457356.png)

     现在将文件的编码改为：gbk

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171500604.png)

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171500335.png)

     使用转换流

     ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171501600.png)

     ```java
     package properties_;
      
     public class homework02 {
         public static void main(String[] args) throws IOException {
             String filepath = "f:/77.txt";
             int row = 0;
             String Line = null;
             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "gbk"));
             while ((Line =  br.readLine()) != null){
                 System.out.println(++row + "  " + Line);
             }
         }
     }
     ```

   

   编程题：

   1. 要编写一个 dog.properties
      - name = tom
      - age = 5
      - color = red
   2. 编写Dog类（name，age，color）创建一个 dog 对象，读取dog.properties 用相应的内容完成属性的初始化，并输出
   3. 将创建的Dog 对象，序列化到 dog.dat 文件

   ![img](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303171503430.png)

```java
package properties_;
 
public class homework03 {
    public static void main(String[] args) throws IOException {
 
        String filePath = "src\\dog.properties";
        Properties properties = new Properties();
        properties.load(new FileReader(filePath));
        properties.list(System.out);
 
        Object name = properties.get("name"); //    Object -> String
        String name1 = name + "";
 
        Object age = properties.get("age"); //  Object -> int
        int age1 = Integer.parseInt(age + "");
 
        Object color = properties.get("color");
        String color1 = color + "";
 
        Dog dog = new Dog(name1, age1, color1);
        System.out.println("=====dog对象信息=====");
        System.out.println(dog);
 
 
        //  将创建的 Dog 对象，序列化到 文件 狗.dat
        String serpath = "f:/狗.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serpath));
        oos.writeObject(dog);
 
        //  关闭流
        oos.close();
        System.out.println("Dog对象序列化完成...");
 
    }
    //  再编写一个方法，反序列化dog
    @Test
    public void m1() throws IOException, ClassNotFoundException {
        String serpath = "f:/狗.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serpath));
        Dog dog1 = (Dog)ois.readObject();   //  向下转型
        System.out.println("=== 反序列化 dog ====");
        System.out.println(dog1);
        ois.close();
    }
}
 
class Dog implements Serializable {
 
    private String name;
    private int age;
    private String color;
 
    public Dog(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }
 
    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }
}
```

## 4. IO 流内容梳理

处理流【Buffered】的作用是封装节点流，节点流【字节流，字符流】和一个具体的数据源相关联





