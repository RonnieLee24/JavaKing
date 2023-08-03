# JUC高并发编程

![image-20230725002759109](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307250028262.png)

## 1. JUC 概述

java.util.concurrent（JUC）处理并发编程的工具包

进程：资源分配的最小单位

线程：程序执行的最小单位

- new
- runnable
- blocked
- wait
- terminated

sleep 和 wait区别：

- wait 是 Object 的方法，任何对象实例都能调用
- sleep不会释放锁，也不需要占用锁
- 它们都可以被 interrupted 方法打断

并发和并行

- 并发：同一时刻多个线程访问统一资源
- 并行：多项任务一起执行，之后再汇总

<font color="yellow">管程</font>：Monitor【锁🔒】

用户线程和守护线程（daemon）

- 用户：自定义
- 守护：GC回收

```java
public class sfe {
    public static void main(String[] args) {
        Thread aa = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "::" + Thread.currentThread().isDaemon());
                while (true) {

                }
            }
        }, "aa");
        aa.start();
        System.out.println(Thread.currentThread().getName() + "over");
    }
}
```

Lambda形式：

```bash
new Thread(() -> {
	System.out.println(Thread.currentThread().getName() + "::" + Thread.currentThread().isDaemon());
	while (true) {

	}
}, "aa");
```

![image-20230725005815520](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307250058577.png)

主线程结束，用户线程还在运行，jvm 存活

设置守护线程 【没有用户线程了，都是守护线程，{JVM 结束】

```java
aa.setDaemon(true);	//	设置守护线程
```

![image-20230725010043452](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307250100502.png)

## 2. Lock 接口

多线程编程步骤（上部）

1. 创建资源类，在资源类创建属性和操作方法
2. 创建多个线程，调用资源类的操作方法

Lock需要手动上锁和手动释放锁，否则会发生死锁 ===> 应该在 finally 中释放该锁

Lock可以让等待锁的线程响应中断，而 synchronized 却不行，使用 synchronized时，等待的线程会一直等待下去，不能响应中断

通过 Lock 可以知道有没有成功获取到锁，而 synchronized 却无法办到

Lock可以提高多个线程进行读操作的效率

当竞争资源非常激烈时（有大量线程同时竞争），此时 Lock 的性能要远远优于 synchronized



可重入锁：re<font color="yellow">**entrant**</font>lock

```java
package com.llq.lock;

class Ticket{
    private int number = 30;

    //  创建可重入锁
    private final ReentrantLock lock = new ReentrantLock();

    public void sale(){
        //  上锁
        lock.lock();

        try {
            if (number > 0){
                System.out.println(Thread.currentThread().getName() + "卖出：" + (number--) + "剩下:"  + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //  解锁
            lock.unlock(); 
        }
    }
}

public class LSaleTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "aa").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "bb").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "cc").start();
    }
}
```

## 3. 线程间通信

![image-20230725105435120](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251054213.png)

2个线程：对一个初始值为0的变量

- 一个线程 +1
- 一个线程 -1

```java
package com.llq.sync;

class Share{
    private int num = 0;

    public synchronized void incr() throws InterruptedException {
        if (num != 0){  //  判断
           this.wait();
        }
        //  如果 number 为0，就 +1 操作
        num++;
        System.out.println(Thread.currentThread().getName()+ "::" + num);
        //  通知其它线程
        this.notifyAll();

    }

    public synchronized void decr() throws InterruptedException {
        if (num != 1){  //  判断
            this.wait();
        }
        //  如果 number 为1，就 -1 操作
        num--;
        System.out.println(Thread.currentThread().getName()+ "::" + num);
        //  通知其它线程
        this.notifyAll();
    }
}


public class ThreadDemo1 {
    public static void main(String[] args) {
        Share share = new Share();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "aa").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.decr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "bb").start();
    }
}
```

线程间通信可能出现的问题：扩展为 4 个线程

![image-20230725111224988](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251112068.png)

![image-20230725111330753](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251113817.png)

wait() 方法特点：在哪里睡，就在哪里醒

![image-20230725112017498](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251120554.png)

改进：将 if 判断改为 while 判断

![image-20230725112401712](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251124795.png)

下面使用 lock 接口做相同的实现

![image-20230725112531711](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251125777.png)

```java
package com.llq.sync;

class Share1{
    private int num = 0;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public void incr() throws InterruptedException {
        lock.lock();

        try {
            while (num != 0){
                condition.await();
            }
            num++;  //  干活
            System.out.println(Thread.currentThread().getName()+ "::" + num);
            condition.notifyAll();  //  通知
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public void decr() throws InterruptedException {
        lock.lock();

        try {
            while (num != 1){
                condition.await();
            }
            num--;  //  干活
            System.out.println(Thread.currentThread().getName()+ "::" + num);
            condition.notifyAll();  //  通知
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


public class ThreadDemo2 {
    public static void main(String[] args) {
        Share1 share = new Share1();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "aa").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.decr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "bb").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.incr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "cc").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        share.decr();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "dd").start();
    }
}
```

## 4. 线程间定制化通信【通过 condition 唤醒指定线程】

Condition 接口描述了可能会与锁有关联的条件变量。这些变量在用法上与使用 Object.wait 访问的隐式监视器类似，但提供了更强大的功能。需要特别指出的是，单个 Lock 可能与多个 Condition 对象关联。为了避免兼容性问题，Condition 方法的名称与对应的 Object 版本中的不同。

在使用notify/notifyAll()方法进行通知时，被通知的线程是有JVM选择的，使用ReentrantLock类结合Condition实例可以实现“**选择性通知**”，这个功能非常重要，而且是Condition接口默认提供的。

启动三个线程：【难点：按照顺序调用】

- AA：打印5次，BB：10次，CC：15次
- .............................................................

进行 10 轮

解决：<font color="yellow">给每个线程定义一个标志位 flag</font>

![image-20230725115546202](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251155290.png)

```java
package com.llq.lock;

class ShareResource{
    //  定义标志位
    private int flag = 1;

    private Lock lock = new ReentrantLock();

    //  创建 3 个 condition
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void print5(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 1){
                condition1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + " ：轮数：" + loop);
            }
            setFlag(2); //  先修改标志位，再去通知
            condition2.signal();    //  通知 c2
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void print10(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 2){
                condition2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + " ：轮数：" + loop);
            }
            setFlag(3);
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void print15(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 3){
                condition3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + " ：轮数：" + loop);
            }
            setFlag(1);
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }


}

public class TheadDemo3 {

    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        shareResource.print5(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "AA").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        shareResource.print10(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "BB").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        shareResource.print15(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "CC").start();
    }

}
```

## 5. 集合的线程安全

```java
package com.llq.jihe;

public class ThreadDemo4 {
    public static void main(String[] args) {
       List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString().substring(0, 8));
                    System.out.println(list);
                }
            }, String.valueOf(i)).start();
        }
    }
}
```

会产生并发修改异常

![image-20230725153329895](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251533105.png)

解决方案：

### 5.1 Vector【JDK 1.0 】

```java
List<String> list = new Vector<>();
```

![image-20230725153744618](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251537707.png)

### 5.2 Collections

```java
List<String> list = Collections.synchronizedList(new ArrayList<>());
```

### 5.3 CopyOnWriteArrayList 【写时复制技术】

并发读、独立写

```java
List<String> list = new CopyOnWriteArrayList<>();
```

![image-20230725154651087](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251546160.png)

```java
public boolean add(E e) {
    synchronized (lock) {
        Object[] es = getArray();
        int len = es.length;
        es = Arrays.copyOf(es, len + 1);
        es[len] = e;
        setArray(es);	//	将新的数组合并之前的数组
        return true;
    }
}
```

### 5.4. CopyOnWriteArraySet

### 5.5 ConcurrentHashMap

## 6. 多线程锁

### 6.1 Synchronized 锁的八种情况

1. 标准访问，先打印短信还是邮件

   短信

2. 停 4s 在方法内，先打印短信还是 邮件

   短信

3. 新增普通的 hello方法，是先打短信还是 hello

   hello()

4. 现在有 2 部手机，先打印短信还是邮件

   邮件

5. 2个静态同步方法，1部手机，先打印短信还是邮件

   短信

6. 2个静态同步方法，2部手机，先打印短信还是邮件

   短信【静态是共用的】

7. 1个静态同步方法，1个普通方法，1部手机，先打印短信还是邮件

   hello

8. 1个静态同步方法，1个普通同步方法，2部手机，先打印短信还是邮件

   email

分析：

- 是否是同一把锁
- 锁🔐的范围

1、2：锁的都是当前的对象

4：2个对象，所以用的就不是同一把锁

5、6：锁的是当前类的 class 对象

7、8：用的都不是一把锁

- static：当前类的 class 对象
- 非静态：当前对象

总结：synchronized实现同步的基础：Java中的每一个对象都可以作为锁

具体表现为以下3 种形式：

1. 对于普通同步方法，锁是当前实例对象
2. 对鱼静态同步方法，锁是当前类的 Class 对象
3. 对于同步方法块，锁是 Synchronized 括号里配置的对象

### 6.2 公平锁和非公平锁

```java
private final ReentrantLock lock = new ReentrantLock(true);	//	公平锁
```

![image-20230725170044583](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251700650.png)

### 6.3 可重入锁

![image-20230725170351492](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251703560.png)

![image-20230725170534872](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251705960.png)

![image-20230725170909420](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251709515.png)

### 6.4 死锁

![image-20230725191357208](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251913315.png)

如何验证死锁：

jps、jstack

- jps -l

```bash
ps -ef # linux：查看当前进程(jps)
```

![image-20230725192059463](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251920642.png)

![image-20230725192121992](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251921134.png)

## 7. Callable 接口（第三种创建线程的方式）

使用 Runnable 缺少一项功能：

- 当线程终止时（即：run() 完成时），我们无法使线程返回结果。

 

|              | Runnable接口 | Callable接口 |
| ------------ | ------------ | ------------ |
| 是否有返回值 | 无           | 有           |
| 是否抛出异常 | 无           | 有           |
| 实现方法不同 | run（）方法  | call（）方法 |

![image-20230725193652030](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251936108.png)



![image-20230725194134300](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307251941467.png)

### 7.1 FutureTask 概述及原理（创建线程）

```java
//  Callable 方式创建线程
FutureTask<Integer> integerFutureTask = new FutureTask<>(new Callable<Integer>() {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        return 1024;
    }
});

new Thread(integerFutureTask, "BB").start();

while (!integerFutureTask.isDone()){
    System.out.println("wait.....");
}


System.out.println(integerFutureTask.get());
```

## 8. JUC 强大的辅助类

### 1. 减少计数 CountDownLatch

CountDownLatch 类可以设置一个计数器

- 通过 countDown 方法进行 - 1 操作
- 使用 await 方法等待计数器不大于 0，然后继续执行 await 方法之后的语句

CountDownLatch 主要有2个方法

- 当一个或多个线程调用 await 方法时，这些线程会阻塞
- 其它线程调用 countDown 方法会将计数器减一（调用countDown 方法的线程不会阻塞）
- 当计数器的值变为 0 时，因 await 方法阻塞的线程会被唤醒，继续执行

```java
package com.llq.juc;

/**
问题引入
 */
public class countDownLatchDemof {
    //  6 个同学陆续离开教室之后，班长锁门
    public static void main(String[] args) {
        for (int i = 1; i <= 6 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 号同学离开了教室");
                }
            }, String.valueOf(i)).start();
        }

        System.out.println(Thread.currentThread().getName() + " 班长锁门走人了...");
    }
}
```

![image-20230725203230433](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307252032532.png)

创建 CountDownLatch 对象 ===> 设置初始值

```java
package com.llq.juc;

public class countDownLatchDemof {
    //  6 个同学陆续离开教室之后，班长锁门
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 号同学离开了教室");
                    //  计数 - 1
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }
        //  阻塞【当没有变成 0 的时候，就一直等待】
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " 班长锁门走人了...");
    }
}
```

![image-20230725203651161](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307252036247.png)



### 2. 循环栅栏 CyclicBarrier【7龙珠】

一个同步辅助类，它允许一组线程互相等待。知道到达某个公共屏障点（common barrier point），在涉及一组固定大小的线程的程序中，这些线程必须不时地等待，此时 CyclicBarrier 很有用，因为该 barrier 在释放等待线程后可以重用。因为该 barrier 在释放等待线程后可以重用，所以称它为循环的 barrier。

![image-20230725211220134](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307252112231.png)

```java
package com.llq.juc;

//  集齐7颗龙珠可以召唤神龙
public class CyclicBarrierDemo {

    //  创建固定值
    private static final int NUMBER = 7;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        //  创建 CyclicBarrier 对象
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, new Runnable() {
            @Override
            public void run() {
                System.out.println("集齐7颗龙珠就可以召唤神龙");
            }
        });

        //  集齐 7 颗龙珠的过程
        for (int i = 1; i <= 7; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 星龙珠被找到了");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i)).start();
        }
    }
}
```



### 3. 信号灯 Semaphore

![image-20230726100851257](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261008402.png)

模拟 6 辆汽车，停在 3个 停车位的过程

```java
package com.llq.juc;

public class SemaPhoreDemo {
    public static void main(String[] args) {
        //  创建 SemaPhore：许可证许可数量
        Semaphore semaphore = new Semaphore(3);
        //  模拟 6 辆汽车
        for (int i = 1; i <= 6 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();    //  抢占
                        System.out.println(Thread.currentThread().getName() + " 抢到了车位");
                        Thread.sleep(new Random().nextInt(5000));
                        System.out.println(Thread.currentThread().getName() + " 离开了车位");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();    //  释放
                    }
                }
            }, String.valueOf(i)).start();

        }
    }
}
```

![image-20230726103634978](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261036127.png)

## 9. ReentrantReadWriteLock 读写锁

![image-20230726121318793](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261213901.png)

![image-20230726122833877](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261228961.png)

Synchronized存在明显问题：读与读之间互斥

而我们想要的效果是：读与读互不影响，读和写互斥，写和写互斥，提高读写效率

Java并发包中ReadWriteLock是一个接口，主要有两个方法，如下：

```java
public interface ReadWriteLock {
    /**
     * Returns the lock used for reading.
     *
     * @return the lock used for reading
     */
    Lock readLock();

    /**
     * Returns the lock used for writing.
     *
     * @return the lock used for writing
     */
    Lock writeLock();
}
```

eadWriteLock管理一组锁，一个是只读的锁，一个是写锁。

Java并发库中ReetrantReadWriteLock实现了ReadWriteLock接口并添加了 <font color="yellow">**可重入** </font>的特性。

### 9.1 获取锁顺序

非公平锁

- 当以非公平初始化时，读锁和写锁的获取的顺序是不确定的。非公平锁主张竞争获取，可能会延缓一个或多个读或写线程，但是会比公平锁有更高的吞吐量。

公平锁

- 当以公平模式初始化时，线程将会以 <font color="yellow">**队列的顺序** </font>获取锁。当前线程释放锁后，等待时间最长的写锁线程就会被分配写锁；或者有一组读线程组等待时间比写线程长，那么这组读线程组将会被分配读锁。

### 9.2 可重入

一个线程在获取某个锁后，还可以继续获取该锁，即允许一个线程多次获取同一个锁。比如synchronized内置锁就是可重入的，如果A类有2个synchornized方法method1和method2，那么method1调用method2是允许的。显然重入锁给编程带来了极大的方便。假如内置锁不是可重入的，那么导致的问题是：1个类的synchornized方法不能调用本类其他synchornized方法，也不能调用父类中的synchornized方法。与内置锁对应，JDK提供的显示锁ReentrantLock也是可以重入的

### 9.3 锁降级

锁升级：读锁 ---> 写锁

锁降级：写锁 ---> 读锁

```java
/**
 *Test Code 1
 **/
package test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test1 {

    public static void main(String[] args) {
        ReentrantReadWriteLock rtLock = new ReentrantReadWriteLock();
        rtLock.readLock().lock();
        System.out.println("get readLock.");
        rtLock.writeLock().lock();
        System.out.println("blocking");
    }
}
```

结论：上面的测试代码会产生死锁，因为同一个线程中，在没有释放读锁的情况下，就去申请写锁，这属于**锁升级，ReentrantReadWriteLock是不支持的**。

```java
/**
 *Test Code 2
 **/
package test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test2 {

    public static void main(String[] args) {
        ReentrantReadWriteLock rtLock = new ReentrantReadWriteLock();  
        rtLock.writeLock().lock();  
        System.out.println("writeLock");  
          
        rtLock.readLock().lock();  
        System.out.println("get read lock");  
    }
}
```

结论：**ReentrantReadWriteLock支持锁降级**，上面代码不会产生死锁。这段代码虽然不会导致死锁，但没有正确的释放锁。从写锁降级成读锁，并不会自动释放当前线程获取的写锁，仍然需要显示的释放，否则别的线程永远也获取不到写锁。

### 9.4. ReetrantReadWriteLock对比使用

1. Java并发库中ReetrantReadWriteLock实现了ReadWriteLock接口并添加了可重入的特性
2. ReetrantReadWriteLock读写锁的效率明显高于synchronized关键字
3. eetrantReadWriteLock读写锁的实现中，读锁使用共享模式；写锁使用独占模式，换句话说，读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的
4. ReetrantReadWriteLock读写锁的实现中，需要注意的，当有读锁时，写锁就不能获得；而当有写锁时，除了获得写锁的这个线程可以获得读锁外，其他线程不能获得读锁

### 案例实现

```java
package com.llq.lock;

class MyCache{
    //  创建 map 集合
    private volatile Map<String, Object> map = new HashMap<>();

    //  放数据
    public void put(String key, Object value){
        System.out.println(Thread.currentThread().getName() + "正在进行写操作" + key);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + " 写完了" + key);
    }

    //  取数据
    public Object get(String key){
        Object result = null;
        System.out.println(Thread.currentThread().getName() + "正在进行读取操作" + key);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = map.get(key);
        System.out.println(Thread.currentThread().getName() + "获取完了" + key);
        return result;
    }
}

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 1; i <= 5 ; i++) {
            final int num = i;
            new Thread(new Runnable() { //  加数据
                @Override
                public void run() {
                    myCache.put(num+"", num+"");
                }
            }, String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5 ; i++) {
            final int num = i;
            new Thread(new Runnable() { //  取数据
                @Override
                public void run() {
                    myCache.get(num+"");
                }
            }, String.valueOf(i)).start();
        }

    }
}
```

问题引出：还没写完，就去读取了

![image-20230726115641839](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261156948.png)

![image-20230726115731967](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261157053.png)

1. 先创建读写锁对象

   ```java
   private ReadWriteLock reentrantLock =  new ReentrantReadWriteLock(); 
   ```

2. 写操作，加上写锁

   ```java
   //  放数据
   public void put(String key, Object value){
       reentrantLock.writeLock().lock();
       try {
           System.out.println(Thread.currentThread().getName() + "正在进行写操作" + key);
           Thread.sleep(300);
           map.put(key, value);
           System.out.println(Thread.currentThread().getName() + " 写完了" + key);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }finally {
           reentrantLock.writeLock().unlock();
       }
   }
   ```

3. 读操作，加上读锁

   ```java
   //  取数据
   public Object get(String key){
       reentrantLock.readLock().lock();
       Object result = null;
       try {
           System.out.println(Thread.currentThread().getName() + "正在进行读取操作" + key);
           Thread.sleep(300);
           result = map.get(key);
           System.out.println(Thread.currentThread().getName() + "获取完了" + key);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }finally {
           reentrantLock.readLock().unlock();
       }
       return result;
   }
   ```

   ![image-20230726120956277](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261209416.png)

![image-20230726123833992](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261238113.png)

读写锁读的时候，不能写，只有读完成后，才可以写【锁降级的过程】，写操作可以读

![image-20230726124458289](C:\Users\李隆齐\AppData\Roaming\Typora\typora-user-images\image-20230726124458289.png)

```java
package com.llq.juc;

public class Demo1 {
    public static void main(String[] args) {
        ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        Lock readLock = reentrantReadWriteLock.readLock();
        Lock writeLock = reentrantReadWriteLock.writeLock();

        //  锁降级
        //  1. 获取写锁
        writeLock.lock();
        System.out.println("ACLQ");

        //  2. 获取读锁
        readLock.lock();
        System.out.println("---read");

        //  3. 释放写锁
        writeLock.unlock();

        //  4. 释放读锁
        readLock.unlock();
    }
}
```

## 10. BlockingQueue阻塞队列

通过一个 <font color="yellow">**共享的队列**</font>，可以使得数据由队列的一端输入，从另外一端输出

![image-20230726133521080](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261335199.png)

![image-20230726133748480](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261337586.png)

好处：我们不需要关心什么时候需要阻塞线程，什么时候需要唤醒线程，因为这一切 BockingQueue 都给你一手包办了

![image-20230726134232493](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261342599.png)

![image-20230726134542172](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261345272.png)

![image-20230726134648136](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261346251.png)

## 11. ThreadPool 线程池（第四种创建线程的方式）

### 1. 概述

控制运行的线程数量，处理过程中 <font color="yellow">**将任务放入队列**</font>，然后在线程创建后启动这些任务，如果线程数量超过了最大数量，超出数量的线程排队等候，等其它线程执行完毕，再从队列中取出任务来执行

优点：

- 降低资源消耗
- 提高响应速度
- 提高线程的可管理性
- Java 中的线程池是通过 Executor 框架实现的，该框架中用到了下面几个类
  - Executor
  - Executors【工具类：可以实现线程池中的相关操作！！！】
  - ExecutorService
  - ThreadPoolExecutor

![image-20230726164935847](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261649041.png)



### 2. 使用方式

### 2.1 一池 N 线程 【Executors.newFixedThreadPool(int)】：银行

```java
//  1. 一池 N 线程
ExecutorService threadPool1 = Executors.newFixedThreadPool(5);  // 5 个窗口

//  10 个顾客
try {
    for (int i = 1; i <= 10; i++) {
        //  执行
        threadPool1.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 办理业务");
            }
        });
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    threadPool1.shutdown(); //  处理完之后，将线程都放回线程池中
}
```

### 2.2 一池一线程（一个任务一个任务执行)【Executors.newSingleThreadExecutor()】：只有一个窗口的银行

```java
//  2. 一池一线程
ExecutorService threadPool2 = Executors.newSingleThreadExecutor();  //  一个窗口
//  10 个顾客
try {
    for (int i = 1; i <= 10; i++) {
        //  执行
        threadPool2.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 办理业务");
            }
        });
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    threadPool2.shutdown(); //  处理完之后，将线程都放回线程池中
}
```

### 2.3 根据需求创建线程，可扩容【Executors.newCachedThreadPool()】：可扩容

```java
//  一池可扩容线程
ExecutorService threadPool3 = Executors.newCachedThreadPool();
try {
    for (int i = 1; i <= 10; i++) {
        //  执行
        threadPool3.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 办理业务");
            }
        });
    }
} catch (Exception e) {
    e.printStackTrace();
} finally {
    threadPool3.shutdown(); //  处理完之后，将线程都放回线程池中
}
```

### 3. 底层原理

都是

```java
new ThreadPoolExecutor() // 只是构造器中的参数不同
```

### 4. 7个参数

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
```

![image-20230726171727435](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261717539.png)

### 5. 底层工作原理

细节：

- 执行 execute() 方法后，线程才会创建
- 3、4、5满了后，6进来了，会给你优先办理，3、4、5 继续等待
- 当来了第9个后，会采用拒绝策略

![image-20230726172301972](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261723136.png)

![image-20230726172612866](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261726982.png)



### 6. 自定义线程池（new ThreadPoolExecutor）

![image-20230726172807761](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261728871.png)

```java
package com.llq.pool;

public class ThreadPoolDemo2 {
    public static void main(String[] args) {
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                5,
                2L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        try {
            for (int i = 1; i <= 10; i++) {
                //  执行
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + " 办理业务");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown(); //  处理完之后，将线程都放回线程池中
        }

    }
}
```

超过 5(max) + 3(阻塞队列) = 8 就会报异常

![image-20230726174154601](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307261741769.png)

## 12. Fork / join 分支合并框架

Fork / Join 可以将一个大的任务拆分成多个子任务进行并行处理，最后将子任务结果合并成最后的计算结果，并进行输出

- Fork：把一个复杂的任务进行分拆，大事化小
- Join：把分拆任务的结果进行合并

![image-20230727002729731](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307270027867.png)

![image-20230727003003153](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307270030309.png)

分支合并池   类比 ===> 线程池

![image-20230727003403169](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307270034291.png)

```java
package com.llq.forkjoin;

class MyTask extends RecursiveTask<Integer>{
    //  拆分差值不能超过 10
    private static final  Integer VALUE = 10;

    private int begin;

    private int end;

    private int result;

    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {   //  拆分和合并的过程
        //  判断 2 数差值是否 > 10
        if (end - begin <= VALUE){
            //  想加
            for (int i = begin; i <= end ; i++) {
                result += i;
            }
        }else { //  进一步拆分
            int mid = begin + (end - begin) / 2;
            //  拆分左侧
            MyTask myTask01 = new MyTask(begin, mid);
            //  拆分右侧
            MyTask myTask02 = new MyTask(mid + 1, end);
            //  调用方法拆分
            myTask01.fork();
            myTask02.fork();

            //  合并结果
            result = myTask01.join() + myTask02.join();
        }
        return result;
    }
}

public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyTask myTask = new MyTask(1, 100);
        //  创建分支合并池对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        //  获取最终合并之后的结果
        Integer result = forkJoinTask.get();
        System.out.println(result);
        forkJoinPool.shutdown();    //  关闭池对象
    }
}
```



## 13. CompletableFuture 异步回调

![image-20230727010216258](https://cdn.jsdelivr.net/gh/RonnieLee24/PicGo_Pictures@master/imgs/DB/202307270102373.png)

```java
package com.llq.completable;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //  异步调用：没有返回值的
        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "completableFuture1");
            }
        });
        completableFuture1.get();


        //  异步调用：有返回值的
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                System.out.println(Thread.currentThread().getName() + "completableFuture2");
                return 1024;
            }
        });
        completableFuture2.whenComplete(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {
                System.out.println(integer);    //  方法的返回值
                System.out.println(throwable);  //  异常信息

            }
        }).get();

    }
}
```

## 14. 多线程面试题

2个线程：

- 分别打印a 和  打印 b
- AB、AB这样打印，交替打印
- 然后一共打印100个字符【即：每个线程打印50个字符】

注意：condition.await() 方法会释放锁



```java
package com.llq.homework;

class ShareSource{
    private static int flag = 1;    //  标志位

    private Lock lock = new ReentrantLock();

    //  创建 2 个 condition
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();

    public static void setFlag(int flag) {
        ShareSource.flag = flag;
    }

    public void printA(int loop){
        lock.lock();
        try {
            while (flag != 1){
                condition1.await();	//	调用 await() 方法会释放锁！！！
            }
            System.out.println(Thread.currentThread().getName() + "::" + ":轮数: " + loop);
            setFlag(2);
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB(int loop){
        lock.lock();
        try {
            while (flag != 2){
                condition2.await();
            }
            System.out.println(Thread.currentThread().getName() + "::" + ":轮数:" + loop);
            setFlag(1);
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}

public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {

        ShareSource shareSource = new ShareSource();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50 ; i++) {
                    shareSource.printA(i);
                }
            }
        }, "A").start();

        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50 ; i++) {
                    shareSource.printB(i);
                }
            }
        }, "B").start();
    }
}
```

可见，用什么condition执行等待，那么唤醒的时候，就是唤醒使用对应condition类的线程。
