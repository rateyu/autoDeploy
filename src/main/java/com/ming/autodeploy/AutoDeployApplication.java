package com.ming.autodeploy;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class AutoDeployApplication implements CommandLineRunner {

    public static void main(String[] args) {
        String host = "192.168.1.153";
        String username = "myu";
        String password = "ym2wgf"; // 如果使用密码的话
//        String privateKeyPath = "path/to/your/private/key"; // 如果使用 SSH 密钥认证
        String privateKeyPath = null; // 如果使用 SSH 密钥认证
//        String remoteCommand = "cd /opt/myapp && git pull && mvn clean package && systemctl restart myapp";
        String remoteCommand = "cd /home/myu && ls -al";

        try {
            // 创建 JSch 对象
            JSch jsch = new JSch();

            // 创建 SSH 会话
            Session session = jsch.getSession(username, host, 22); // 使用 IP 和端口号连接
            session.setPassword(password); // 设置密码
            session.setConfig("StrictHostKeyChecking", "no"); // 不检查 host key，适用于测试环境
            session.connect(); // 连接到远程服务器

            // 打开一个执行命令的通道
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(remoteCommand);  // 设置远程执行的命令
            channel.setInputStream(null);  // 不发送输入流
            channel.setErrStream(System.err); // 错误流

            // 获取远程执行命令的标准输出流
            InputStream inputStream = channel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // 获取远程执行命令的错误输出流
            InputStream errorStream = channel.getErrStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));

            // 执行命令
            channel.connect();

            // 读取标准输出并打印
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 读取错误输出并打印
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // 等待命令执行完成
            while (!channel.isClosed()) {
                Thread.sleep(1000);
            }

            // 打印远程命令的输出和错误信息
            System.out.println("Command Output:\n" + output.toString());
            System.out.println("Error Output:\n" + errorOutput.toString());
            System.out.println("Exit Status: " + channel.getExitStatus());

            // 关闭通道和会话
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) {
        SpringApplication.run(AutoDeployApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动时执行部署程序
        String host = "192.168.1.153";
        String username = "myu";
        String password = "ym2wgf"; // 如果使用密码的话
//        String privateKeyPath = "path/to/your/private/key"; // 如果使用 SSH 密钥认证
        String privateKeyPath = null; // 如果使用 SSH 密钥认证
//        String remoteCommand = "cd /opt/myapp && git pull && mvn clean package && systemctl restart myapp";
        String remoteCommand = "cd /home && ls -a";

        // 调用部署方法
        deployApp(host, username, password, privateKeyPath, remoteCommand);
    }

    public void deployApp(String host, String username, String password, String privateKeyPath, String remoteCommand) {
        try {
            JSch jsch = new JSch();
//            jsch.addIdentity(password);
//            jsch.addIdentity(privateKeyPath); // 使用 SSH 密钥认证

            Session session = jsch.getSession(username, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // 不检查 host key，适用于测试环境
            session.connect();

            // 打开一个执行命令的通道
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(remoteCommand);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            // 执行命令
            channel.connect();

            // 等待命令执行完成
            while (!channel.isClosed()) {
                Thread.sleep(1000);
            }

            System.out.println("Deployment completed successfully.");
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
