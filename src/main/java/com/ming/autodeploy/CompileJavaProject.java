package com.ming.autodeploy;

import java.io.*;

public class CompileJavaProject {
    public static void main(String[] args) {
        String command = "mvn clean install";  // 使用 Maven 构建 Java 工程
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Project compiled successfully.");
            } else {
                System.out.println("Compilation failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
