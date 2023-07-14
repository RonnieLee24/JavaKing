# CleanCode【CC】

```java
还记得那个关于小提琴家在去表演的路上迷路的老笑话吗?他在街角拦住--位长者，问
他怎么才能去卡耐基音乐厅(Carnegie Hall)。长者看了看小提琴家，又看了看他手中的琴，
说道:“ 你还得练，孩子，还得练!”
```

## 1. 有意义的命名

1. 名副其实

2. 避免误导

   ```java
   //	即使容器就是一个 List，最好也别在名称中写出容器类型名
   accountGroup、bounchOfAccounts
   ```

3. 做有意义的区分

4. 使用读得出来的名称

   ```java
   class Customer{
       private Date generationTImestamp;
       private Date modificationTimestamp;
       private final String redordId = "102";
   }
   ```

5. 使用可搜索的名称

   ```java
   //	使用 WORK_DAYS_PER_WEEK 代替单字符
   ```

6. 使用解决方案领域名称

   ```java
   //	对于熟悉访问者（VISITOR）模式的程序员来说
   AccountVisitor //	富有意义
   ```

   

## 2. 函数

1. 短小
2. 只做一件事
3. Switch语句【多态】



















