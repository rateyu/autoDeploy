package com.ming.autodeploy;

import com.jcraft.jsch.*;

public class SFTPFileUpload {
    public static void main(String[] args) {
        String host = "your-remote-server-ip";
        String username = "your-ssh-username";
        String privateKeyPath = "/path/to/your/private/key";  // SSH 私钥路径
        String localFile = "path/to/local/file";
        String remoteDir = "/path/to/remote/directory/";

        try {
            // 创建 JSch 对象
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath); // 使用私钥认证

            // 创建 SSH 会话
            Session session = jsch.getSession(username, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // 打开 SFTP 通道
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            // 上传文件
            sftpChannel.put(localFile, remoteDir);  // 上传本地文件到远程目录

            System.out.println("File uploaded successfully.");
            sftpChannel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
