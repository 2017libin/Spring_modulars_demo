package org.demo.pojo;

import org.springframework.stereotype.Component;

// 等价于  <bean id="user" class="com.github.subei.pojo.User"/>
// @Component 组件
@Component
public class User {
    public String name = "哇哈哈";
}