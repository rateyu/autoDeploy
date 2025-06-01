package com.ming.autodeploy;

public class GetEnvVariables {
    public static void main(String[] args) {
        String path = System.getenv("PATH");  // 获取 PATH 环境变量
        System.out.println("PATH: " + path);
    }
}
