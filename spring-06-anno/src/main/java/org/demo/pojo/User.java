package org.demo.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 等价于  <bean id="user" class="com.github.subei.pojo.User"/>
// @Component 组件
@Component
public class User {

    // 相当于  <property name="name" value="subeiLY"/>
    @Value("subeiLY")
    public String name;

    public void setName(String name) {
        this.name = name;
    }
}