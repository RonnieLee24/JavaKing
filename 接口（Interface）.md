- [接口（Interface）【接口中的属性，是 public static final】](#接口interface接口中的属性是-public-static-final)
  - [1. 快速入门](#1-快速入门)
  - [2. 基本介绍](#2-基本介绍)
  - [3. 应用场景](#3-应用场景)
  - [4. 注意事项](#4-注意事项)
  - [5. 接口 VS 继承](#5-接口-vs-继承)
  - [6. 接口多态特性【instance of 判断的是运行类型】](#6-接口多态特性instance-of-判断的是运行类型)
  - [7. 接口多态传递](#7-接口多态传递)
  - [8. 课后练习](#8-课后练习)

# 接口（Interface）【接口中的属性，是 public static final】

## 1. 快速入门

![image-20230328103439893](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281034062.png)

```java
package com.interface_;

public interface UsbInterface { //  接口 （制定规范）
    //  规定接口的相关方法，老师规定的，即规范
    public void start();
    public void stop();
}
```

```java
package com.interface_;

//  Phone 类 实现了 UsbInterface
//  解读1. 即：Phone 类需要实现 UsbInterface接口 规定/声明的方法
public class Phone implements UsbInterface{
    @Override
    public void start() {
        System.out.println("手机开始工作...");
    }

    @Override
    public void stop() {
        System.out.println("手机停止工作...");
    }
}
```

```java
package com.interface_;

public class Camera implements UsbInterface {

    @Override
    public void start() {
        System.out.println("相机开始工作...");

    }

    @Override
    public void stop() {
        System.out.println("相机停止工作...");

    }
}
```

电脑中也有对应接口

```java
package com.interface_;
	
public class Computer {
    //  编写一个方法，计算机工作
    public void work(UsbInterface usbInterface) {
        usbInterface.start();
        usbInterface.stop();
    }
}
```

通过接口，来调用方法	

![com](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281041422.png)

测试：

```java
package com.interface_;

public class Interface01 {
    public static void main(String[] args) {

        //  创建手机，相机对象
        Camera camera = new Camera();
        Phone phone = new Phone();
        //  创建计算机
        Computer computer = new Computer();
        computer.work(phone);   //  把手机接入到计算机
        System.out.println("================");
        computer.work(camera);  //  把相机接入到计算机
    }
}
```

![image-20230328104458034](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281044088.png)

## 2. 基本介绍

接口就是将一些没有实现的方法，封装到一起，到某个类使用的时候，再根据具体的情况把这些方法写出来

语法：

```java
interface 接口名{
    //	属性
    //	方法【1. 抽象方法（abstract 可以省略） 2。默认实现方法 3. 静态方法】
}

class 类名 implements 接口{
    自己属性
    自己方法
    必须实现的接口的抽象方法
}
```

小结：

- 接口是更加抽象的抽象类
- 抽象类里的方法可以有方法体，接口里的所有方法都没有方法体【JDKf 7.0】				
- 接口体现了程序设计的多态和高内聚低耦合的设计思想
- 注意：JDK 8.0 后接口类可以有 <font color="yellow">**静态（static）** </font>方法，<font color="yellow">**默认（default）** </font>方法，也就是说接口中可以有方法的具体实现

```java
package com.interface_;

public interface AInterface {
    //  写属性
    public int n1 = 10;
    //  写方法
    //  在接口中，抽象方法，可以省略abstract关键字
    public void hi();

    //  在jdk8 后，可以有默认实现方法，需要使用 default关键字修饰
    default public void ok(){
        System.out.println("ok....");
    }

    //  jdk8后，可以有静态方法
    public static void cry(){
        System.out.println("cry....");
    }
}
```

```java
package com.interface_;

//  如果一个类 implements 实现接口
//  需要将该接口的所有抽象方法都实现
class A implements AInterface{

    @Override
    public void hi() {
        System.out.println("hi()....");
    }
}
```

## 3. 应用场景

![jjq](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281104317.jpg)

![mmq](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281105170.jpg)

![com](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281119927.png)

## 4. 注意事项

```java
package com.interface_;

//  1）接口不能被实例化
//  2）接口中所有的方法是 public 方法，接口中抽象方法可以不用 abstract 修饰
//  3）一个普通类实现接口，就必须将该接口的所有方法都实现
//	4) 抽象类实现接口，可以不用实现接口的方法

interface IA{
    void say();	//	abstract 可以省略
    void hi();
}

class Cat implements IA{     //  alt + enter
    @Override
    public void say() {

    }

    @Override
    public void hi() {

    }
}

abstract class Tiger implements IA{
    
}
```

```java
package com.interface_;

public class InterfaceDetail02 {
    public static void main(String[] args) {

        //  证明：接口中的属性，是 public static final
        System.out.println(IB.n1);  //  说明 n1 就是 static

        // 再来证明 final
        // IB.n1 = 9; [Cannot assign a value to final variable 'n1']
        
        System.out.println(ID.n1); //	7. 接口中属性访问形式：接口.类名
    }
}

interface IB{

    // 6. 接口中的属性，只能是final的，而且是 public static final 修饰符
    int n1 = 10;    //  等价 public static final int n1 = 10【必须初始化】;

    void hi();
}

interface ID extends IB{	//	8. 接口不能继承其它的类，但是可以继承多个别的接口
}

interface IC{	//	9. 接口的修饰符，只能是 public 和 默认，这点和类的修饰符是一样的
    void say();
}

//  5. 一个类可以同时实现多个接口
class Pig implements IB, IC {

    @Override
    public void hi() {

    }

    @Override
    public void say() {

    }
}
```

课堂练习

```java
package com.interface_;

public class InterfaceExercise01 {
    public static void main(String[] args) {
        B1 b1 = new B1();
        System.out.println(b1.a);   //  对象实例
        System.out.println(A1.a);
        System.out.println(B1.a);   //  B1 实现了 接口 A1 ，当然可以使用里面的属性【同时该属性是 static的】

    }
}

interface A1{
    int a = 23; //  等价于 public static final int a = 23;
}

class B1 implements A1 {    //  正确

}
```

## 5. 接口 VS 继承

对单继承机制的一种补充

```java
package com.interface_;

public class ExtendsVsInterface {
    public static void main(String[] args) {
        LittleMonkey wukong = new LittleMonkey("悟空");
        wukong.climbing();
        wukong.swimming();
        wukong.flying();
    }
}

//  接口
interface FIshable {
    void swimming();
}

interface Birdable {
    void flying();
}


//  猴子
class Monkey {
    private String name;

    public Monkey(String name) {
        this.name = name;
    }

    public void climbing() {
        System.out.println( name + "会爬树...");
    }

    public String getName() {
        return name;
    }
}

//  继承
//	当子类继承父类，就自动拥有父类的功能
//	如果子类需要扩展功能，可以通过实现接口的方式扩展	
class LittleMonkey extends Monkey implements FIshable, Birdable {
    public LittleMonkey(String name) {
        super(name);
    }

    @Override
    public void swimming() {
        System.out.println(getName() + " 通过学习， 可以像鱼儿一样游泳...");
    }

    @Override
    public void flying() {
        System.out.println(getName() + " 通过学习， 可以像鸟儿一样飞翔...");

    }
}
```

## 6. 接口多态特性【instance of 判断的是运行类型】

1. 多态参数

   - 形参是接口类型 UsbInterface
   - 看到可以接收 实现了 UsbInterface接口 的对象实例
   - 既可以接收手机对象，又可以接收相机对象

   ```java
   package com.interface_;
   	
   public class Computer {
       //  编写一个方法，计算机工作
       public void work(UsbInterface usbInterface) {
           usbInterface.start();
           usbInterface.stop();
       }
   }
   ```

2. 多态数组

   - 给 Usb 数组中，存放 Phone 和 相机对象
   - Phone类还有一个特有的方法 call()
   - 请遍历 Usb 数组，如果是 Phone对象，除了调用 Usb 接口定义的方法外，还需要调用 Phone 特有方法 call

   ```java
   package com.interface_;
   
   public class InterfacePolyArr {
       public static void main(String[] args) {
   
           //  多态数组 --> 接口类型数组
           Usb[] usb = new Usb[2];
           usb[0] = new Phone_();
           usb[1] = new Camera_();
   
           for (Usb usb1 : usb) {
               usb1.work(); //  动态绑定
               //  和前面一样， 我们仍然需要进行类型的向下转型
               if (usb1 instanceof Phone_){ //  判断它的运行类型是否是 Phone_
                   ((Phone_) usb1).call();
               }
           }
       }
   }
   
   interface Usb{
       void work();
   }
   
   class Phone_ implements Usb {
       public void call(){
           System.out.println("手机可以打电话...");
       }
   
       @Override
       public void work() {
           System.out.println("手机工作中....");
       }
   }
   
   class Camera_ implements Usb{
       @Override
       public void work() {
           System.out.println("相机工作中...");
       }
   }
   ```

   ![image-20230328121732543](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281217633.png)

## 7. 接口多态传递

![image-20230328122711510](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202303281227613.png)

```java
package com.interface_;

//  演示多态传递现象
public class InterfacePolyPass {
    public static void main(String[] args) {

        //  接口类型变量可以指向，实现了该接口的类的对象实例
        IG ig = new Teacher();
        //  如果IG 它继承了 IH 接口， 而Teacher类实现了 IG接口
        //  那么，实际上就相当于 Teacher 类也实现了 IH接口
        IH ih = new Teacher();
    }
}

interface IH {
    void hi();
}
interface IG extends IH {}

class Teacher implements IG {

    @Override
    public void hi() {

    }
}
```

## 8. 课后练习

```java
package com.interface_;

public class InterfaceExercise02 {
    public static void main(String[] args) {
        new C9().pX();
    }
}

interface A9{
    int x = 0;  //  等价于 Public static final int x = 0;
}


class B9{
    int x = 1;  //  普通属性
}

class C9 extends B9 implements A9 {
    public void pX() {
        //  可以明确指定 x
        //  访问接口的 x 就使用A9.x
        //  访问父类的 x 就使用super.x
        System.out.println(A9.x + " " + super.x);
    }
}
```

|      | 区别       | this                                                         | super                                  |
| ---- | ---------- | ------------------------------------------------------------ | -------------------------------------- |
| 1    | 访问属性   | 访问本类中的属性<br />如果本类中没有此属性，则从父类中继续查找 | 从父类开始查找属性                     |
| 2    | 调用方法   | 访问本类中的方法<br />如果本类中没有此方法，则从父类中继续查找 | 从父类开始查找                         |
| 3    | 调用构造器 | 调用本类构造器<br />必须放在构造器首行                       | 调用父类构造器<br />必须放在构造器首行 |
| 4    | 特殊       | 表示当前对象                                                 | 子类中访问父类对象                     |

