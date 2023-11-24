# 1. 基础知识

1. POJO：简单Java对象，不需要遵循任何规范
2. JavaBean：满足一定规范的类
   1. 类型为public
   2. 可序列化
   3. 有默认的无参构造
   4. 通过getter、setter函数获取和设置属性
3. EJB：企业JavaBean，EJB设计的目标是分布式应用
4. 项目结构
   1. myweb
      1. src
         1. main
            1. java：源代码
            2. resources：配置文件
               1. **applicationContext.xml**
            3. webapp：
               1. WEB-INF：无法通过url访问的资源
               2. index.jsp
         2. test：存放测试代码
         3. pom.xml
      2. test
5. 依赖添加
   1. servlet依赖
6. 常见的依赖
   1. @Override：覆盖父类的方法
   2. @Deprecated：弃用或不建议使用的代码
   3. @SuppressWarnings：忽略编译器的警告

# 2. Spring

## 2.1 IOC 控制反转

### IOC 基础

1. IOC 控制反转：由配置文件创建并注入到IOC容器中，程序中不需要new对象而是直接从IOC容器中取出来用
   1. 实际上，除了通过配置文件创建，还可以通过注解进行创建和注入
   2. 实现IOC的两种方法
      1. 依赖查找：首先将依赖或服务进行注册，再通过容器提供的API来查找依赖对象
      2. 依赖注入（DI）：通过配置文件注入，属性注入或者构造器注入是spring中两种常见的注入方式
   3. bean：IOC容器创建和管理的对象称为**bean**
      1. **bean工厂**：用于创建和管理bean的对象，是实现了BeanFactory接口的类的对象
      2. spring启动时会创建一个bean工厂，其也被称为**容器**
   4. ApplicationContext：和BeanFactory一样，是为了创建和管理bean的接口。ApplicationContext继承了BeanFactory，并提供了新的高级功能
      1. 一般来说，通过实现实现ApplicationContext接口的类的对象作为容器！
      2. ApplicationContext通过**xml配置文件**直接创建并注册对象
   
2. ==xml配置文件==

   1. **bean标签**：用于创建bean对象

      1. ==创建对象的方式有以下两种==

         1. 无参构造+**set方法注入**
            1. property标签对属性进行注入
               1. name属性：对应的是属性成员名称
               2. value属性：简单类型的属性注入，(int、string、...)
               3. ref属性：引用类型的属性注入
               4. array、list、set、map==标签==：集合类型的属性注入
               5. null==标签==：null值的属性注入
               6. props==标签==：Properties类型的属性注入
            2. p/c命名空间注入：
               1. p对应的是property
               2. c对应的是constructor（有参构造注入）

         ```xml
             <bean id="user" class="com.github.subei.pojo.User">
                 <property name="name" value="subei"/>
             </bean>
         
         	<!-- p命名空间注入，可以直接注入属性的值: property -->
             <bean id="user" class="com.github.subei.pojo.User"
                   p:name="subei"
                   p:age="21" />
         ```

         2. **有参构造注入**：参数有三种类型进行注入，通过位置、类型、参数名进行注入
            1. type：通过类型匹配注入属性
            2. index：通过位置配置注入属性
            3. value：简单类型的属性注入
            4. c命名空间注入
               1. 如果属性注入依赖bean，则在参数名后面加上 -ref：`<bean id="user" class="xxx" c:parent-ref="parent" />` 

         ```xml
             <!-- 第一种：根据index参数下标设置 -->
         <!--    <bean id="userT" class="com.github.subei.pojo.UserT">-->
         <!--        <constructor-arg index="0" value="subeily——"/>-->
         <!--    </bean>-->
         
         
             <!-- 第二种：根据参数类型设置，不建议使用 -->
         <!--    <bean id="userT" class="com.github.subei.pojo.UserT">-->
         <!--        <constructor-arg type="java.lang.String" value="subeily2——"/>-->
         <!--    </bean>-->
         
             <!-- 第三种：根据参数名字设置 -->
             <bean id="userT" class="com.github.subei.pojo.UserT">
                 <!-- name指参数名 -->
                 <constructor-arg name="name" value="subeily3——"/>
             </bean>
         ```

      2. **bean的配置**

         1. 类的要求：一般的POJO即可

         2. class是类的完整路径

         3. id是类的实例对象的唯一标识，缺省则为类名的首字母小写作为标识

            1. **别名**

               1. **name属性**：除了使用id，name属性也可以指定名称/别名，**多个别名用逗号隔开**
               
               2. **alias标签：**alias标签可以用来设置对象的别名
               
                  ```xml
                  <alias name="userT" alias="userNew"/>
                  ```
               
               ![image-20230604134847360](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20230604134847360.png)

   2. **import标签**：一般用于团队开发使用，他可以将多个配置文件，导入合并为一个

3. 总结：

   1. spring容器是实现ApplicationContext接口的对象
   2. 通过配置文件实现依赖注入
   3. 管理的对象称为bean，bean可以由最简单的POJO实例化
   4. 除了xml配置bean，还可以使用**注解**、或者**java配置类**来配置bean

### IOC 进阶

1. bean实例化的更多方式

   1. 静态内部类
   2. 静态工厂方法：返回该类的唯一静态对象
   3. 实例工厂方法：通过工厂类的工厂方法获得目标实例
      1. 相比静态工厂方法相比，实例工厂方法返回的是其他类型的对象，而静态工厂方法返回的是同类型的对象

2. bean的属性配置

   1. scope属性

      1. singleton：单例作用域，全局共享一个bean。
         1. 创建起容器时就同时自动创建了一个bean的对象，不管你是否使用，他都存在了，每次获取到的对象都是同一个对象。
      2. prototype：原型作用域，每次返回新的bean
      3. request：每次HTTP请求，都会创建一个bean    

   2. init-method、destroy-method：初始化或销毁的生命周期回调，另外两种实现方式

      1. 继承接口
      2. 注解：@PorstConstruct、@PreDestroy
      3. 执行顺序：注解、继承接口、xml配置

   3. lazy-init：懒加载，只有getBean获取时才会创建bean

   4. parent：继承，将父类bean的配置用于配置子类，==目的是为了简化配置==（如果父类bean具有子类bean没有的属性，创建子类bean时会抛出异常）

      ```\
      Exception in thread "main" org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'childBean' defined in class path resource [applicationContextChpt04.xml]: Error setting property values; nested exception is org.springframework.beans.NotWritablePropertyException: Invalid property 'parentId' of bean class [com.chase.chpt04.ChildBean]: Bean property 'parentId' is not writable or has an invalid setter method. Does the parameter type of the setter match the return type of the getter?
      ```

   5. abstract：仅将该bean作为父类，而不会实例化（可以缺省bean）

3. 内部bean：嵌套定义匿名bean，即没有id属性的bean

4. 多XML配置文件：`<import resource="resources/services.xml" />`

### 自动装配

> 自动装配指的是**自动给bean装配属性！**，可以分为以下三种类型 
>
> 1. 在xml中显示的配置；
> 2. 在java中显示配置：使用注解
> 3. 隐式的自动装配bean。【重要！】

1. 在xml中显示的配置：存在三种方式

   1. ByName自动装配：会自动在容器上下文中查找，和属性名相同的beanid对应的bean进行注入！
   2. ByType自动装配：需要保证自动注入属性的类型存在且只存在一个对应的bean！

2. 使用注解实现自动装配:

   1. 在xml中声明支持注解配置：` <context:annotation-config/`>

   2. @Autowired注解自动装配：**对应到byType自动装配**

      1. Autowired中允许添加参数，或者配合其他注解进行使用
         1. **requried=false**：表示对象可以为null
            1. 即如果**不存在对应类型的bean时**，该属性的值为null！
            2. 如果不添加这个参数，则自动装配失败，抛出异常！
         2. **@Qualifier**：如果存在多个同类型的bean时，必须通过@Qualifier注解指定具体的bean来进行属性注入

   3. @Resource注解自动装配：**默认通过byname的方式实现**，如果找不到名字，**则通过byType实现**！如果两个都找不到的倩况下，就报错！

      1. Resource中允许添加参数，或者配合其他注解进行使用
         1. name=xxx：如果不能满足自动装配时，可以手动指定使用具体的bean进行注入，相当于@Qualifier(value=xxx)

   4. 直接在**属性**上使用！也可以在**setter方法**上使用！

      ```java
          @Autowired
          private Cat cat;
          @Autowired
          private Dog dog;
      ```

   5. 使用Autowired我们**可以不用编写Set方法了**，前提是你这个自动装配的属性在容器中有对应名称的bean存在！

### 基于注解开发

1. 开启注解
   1. `<context:annotation-config />`
   2. `<context:component-scan base-package="com.chase.xxx">`
2. 常用的注解
   1. 1. - 
   2. @Autowired：用于自动装配的注解
   3. @Scope：设置bean的作用域 singleton/prototype
   4. @PostConstruct、@PreDestroy
   5. 创建bean的注解
      1. @Named：创建bean，并指定bean的name
      2. @Component：Spring本身的注解，更为常用
         1. @Component：相当于`<bean id="user" class="com.github.subei.pojo.User"/>`
         2. @Value("subeiLY")：`相当于  <property name="name" value="subeiLY"/>`
         3. @Component 有几个**衍生注解**，我们在web开发中，会按照mvc三层架构分层！
            - dao：【**@Repository**】
            - service：【**@Service**】
            - controller：【**@Controller**】
            - 这四个注解功能都是一样的，都是代表将某个类注册到Spring中，装配Bean。
      3. @Bean：方法层级的注解，对应的类需要注册bean才能生效
         1. 默认方法名作为id，可以使用name和value设置bean的名称和别名
         2. 如果方法有参数，则根据参数名寻找相应的bean进行注入
   6. 注入属性的注解
      1. @Resource(name="xxx")：相当于value属性和ref属性
         1. Resource是自动装配的注解，也可以使用name参数手动指定bean进行注入属性
      2. @Inject：作用和Resource一样
      3. @Autowired：bean的name不一定和属性名一致，默认通过类型匹配。如果有多个bean都满足类型匹配则会抛出异常。默认注解的对象在容器中已经注册。
         1. 方法的参数默认是required=true，即属性不为空，必须使用bean注入属性
         2. 通过使用Nullable注解或者required设置为false进行取消
         3. 多个满足条件的bean可以进一步精确
            1. @Primary标识主候选bean
            2. @Qualifier精确查找依赖
   7. @Required：检查该属性是否被注入（初始化），没有则抛出异常
   8. @Order：集合注入时的bean的顺序

### Java配置类

> 配置类，就是通过Confuguration注解的类，用于替换xml文件，彻底通过java代码进行管理

1. 常见的注解
   1. @Configuration：表示该类是一个配置类
   2. @ComponentScan("com.github.subei.pojo")：指定需要进行扫描的包，用于开启注解扫描
   3. @Import(SunConfig2.class)：导入其他的配置类
2. 容器初始化：`ApplicationContext context = new AnnotationConfigAplicationContext(xxx.class);`
   1. 参数也可以是通过Component注解的类的class
   2. 后续通过register方法来进行初始化

```java
import org.demo.config.SunConfig;
import org.demo.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    // 如果完全使用了配置类方式去做，我们就只能通过Annotationconfig 上下文来获收容器，通过配置类的class对象加载！
    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(SunConfig.class);
        User user1 = (User) applicationContext.getBean("getUser");
//        User user2 = (User) applicationContext.getBean("user");
        System.out.println(user1.getName());
//        System.out.println(user2.getName());
    }
}
```

## 2.2 AOP 切面

### 代理模式

> 代理模式分为**静态代理**和**动态代理**

1. **静态代理**

   1. 角色分析
      1. 抽象角色：一般会使用接口或者抽象类来解决；
      2. 真实角色：被代理的角色；
         1. 实现抽象角色
      3. 代理角色：用来代理真实角色，代理了真实角色后，我们一般会在前后做一些附属操作；
         1. 实现抽象角色
      4. 客户：访问代理角色的人！
   2. 实例
      1. UserService接口：增删查改
      2. UserServiceImpl实现类：实现增删查改的具体逻辑
      3. UserServiceProxy实现类：来处理日志，处理日志之后再调用UserServiceImpl来实现增删查改
      4. Client类：通过调用UserServiceProxy来完成增删查改，同时来能记录日志
   3. 静态代理的优点
      1. 可以使得我们的真实角色更加纯粹，不再去关注一些公共的事情。
         1. 公共的事情指的是记录日志
      2. 公共的业务由代理来完成，实现了业务的分工。
      3. 公共业务发生扩展时，方便集中管理。
   4. 静态代理的缺点：
      1. 一个真实角色就会产生一个代理角色；代码量会翻倍，开发效率会变低。

2. **动态代理**

   > 最直观的区别是动态代理是根据InvocationHandler接口、Proxy类实现的代理功能

   1. 动态代理的代理类是动态生成的，不是我们直接写好的；
   2. 动态代理分为两大类：基于接口的动态代理，基于类的动态代理。
      - 基于接口——JDK动态代理【我们在这里使用】
      - 基于类——cglib
      - Java字节码实现：javasist
   3. **需要了解两个类**: InvocationHandler、Proxy，打开[JDK帮助文档](https://www.matools.com/api/java8)。
   4. 动态代理的优点
      1. 可以使得我们的真实角色更加纯粹，不再去关注一些公共的事情。
      2. 公共的业务由代理来完成，实现了业务的分工。
      3. 公共业务发生扩展时，方便集中管理。
      4. 一个动态代理，一般代理某一类业务。
      5. 一个动态代理可以代理多个类，代理的是接口！
         1. **思考：静态代理能不能实现相同的功能？**

### 方法1：Spring接口实现AOP

1. 编写前置增强类和后置增强类

   ```java
   package com.github.subei.log;
   
   import org.springframework.aop.MethodBeforeAdvice;
   
   import java.lang.reflect.Method;
   
   public class Log implements MethodBeforeAdvice {
       // method:要执行的目标对象的方法
       // args:参数
       // target:目标对象
       public void before(Method method, Object[] args, Object target) throws Throwable {
           System.out.println(target.getClass().getName() + "的" + method.getName() + "方法被执行了！");
       }
   }
   ```

   ```java
   package com.github.subei.log;
   
   import org.springframework.aop.AfterReturningAdvice;
   
   import java.lang.reflect.Method;
   
   public class AfterLog implements AfterReturningAdvice {
       // returnValue:返回值
       // method:被调用的方法
       // args:被调用的方法的对象的参数
       // target:被调用的目标对象
       public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
           System.out.println("执行了" + method.getName() + target.getClass().getName()
                   +"的"+ "返回结果为:" + returnValue);
       }
   }
   ```

2. 在xml的文件中注册 , 并实现AOP切入实现 , 注意导入约束

3. 编写测试类对接口方法进行调用，观察是否进行额外的公共操作

### 方法2：自定义类来实现AOP

1. 编写一个普通类，后面被用来当切入类
2. 配置xml文件
3. 测试类调用方法，观察是否成功切入

### 方法3：使用注解实现AOP

1. 常用注解
   1. @Aspect：标注该类是一个切面
   2. @Before：调用前执行，参数表示要切入的点
   3. @After：调用后执行
   4. @Around：方法中可以控制调用，在调用前后可以进行额外操作



# 错误

## java: Compilation failed: internal java compiler error

- 核对以下的几处设置是否一致

![image-20230604085622099](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20230604085622099.png)

![image-20230604085649422](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20230604085649422.png)

![image-20230604085702130](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20230604085702130.png)

![image-20230604085725641](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20230604085725641.png)

## 未配置文件？

![image-20231124224948968](https://pic-go1.oss-cn-guangzhou.aliyuncs.com/image-20231124224948968.png)

## <context:annotation-config /> 作用？

1. 不开启这个也能够使用注解开发？

## @Bean和@Value注入的关系？

1. 通过方法初始化和@Value进行注入属性优先级问题？
2. 为什么不使用@ComponentScan("org.demo.pojo")也能够使得注解生效？

