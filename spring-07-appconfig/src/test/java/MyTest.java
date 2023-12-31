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