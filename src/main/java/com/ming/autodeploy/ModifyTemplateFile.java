package com.ming.autodeploy;

import java.io.*;
import java.nio.file.*;

public class ModifyTemplateFile {
    public static void main(String[] args) {
        String templateFilePath = "path/to/template.txt";
        String outputFilePath = "path/to/output.txt";
        
        try {
            // 读取模板文件
            String content = new String(Files.readAllBytes(Paths.get(templateFilePath)));

            // 替换占位符
            content = content.replace("{{name}}", "John Doe");
            content = content.replace("{{date}}", "2023-06-01");

            // 写入新文件
            Files.write(Paths.get(outputFilePath), content.getBytes());

            System.out.println("File generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
