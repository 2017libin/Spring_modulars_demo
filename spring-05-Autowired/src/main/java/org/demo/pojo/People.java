package org.demo.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;

public class People {

//
//    @Autowired(required = false)  // 如果不存在Cat类型的bean时，则为null

//    @Qualifier(value = "cat1")  // 如果存在多个Cat类型的bean时，可以通过Qualifier执行具体的bean进行注入
    @Resource(name = "cat1")  // 默认通过byName自动装配，不存在同名bean时则通过byType自动装配，如果存在多个type则抛出异常，或者使用name属性手动指定具体的bean
    private Cat cat;
    @Autowired
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
