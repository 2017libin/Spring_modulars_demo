package org.demo.proxyDemo1;

public class Client {
    public static void main(String[] args) {
        // 真实业务
        UserServiceImpl userService = new UserServiceImpl();
        // 代理类
        UserServiceProxy proxy = new UserServiceProxy();
        // 代理类实现
        proxy.setUserService(userService);

        proxy.add();
        proxy.query();
    }
}