package org.tensorflow.lite.examples.detection;

import android.util.Log;

import com.jcraft.jsch.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;
import java.io.ByteArrayOutputStream;

public class ServerActivity {
    private static final String username = "gusanr7489";
    private static final String host = "203.252.117.166";
    private static final int port = 22;
    private static final String password = "gusanr96";

    private static Session session;
    private static ChannelExec channelExec;
    private static ChannelShell channelShell;
    private static ChannelSftp channelSftp;
    //Start SFTP FROM HERE

    private static void connectSSH() throws JSchException, InterruptedException {
        //Thread.sleep(2);
        session = new JSch().getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");       // 호스트 정보를 검사하지 않도록 설정
        session.connect();
    }

    private static void disConnectSSH() {
        if (session != null) session.disconnect();
        if (channelExec != null) channelExec.disconnect();
        if (channelShell != null) channelShell.disconnect();
        if (channelSftp != null) channelSftp.disconnect();
    }

    public static void waitForPrompt(ByteArrayOutputStream outputStream) throws InterruptedException {
        int retries = 5;
        for (int x = 1; x < retries; x++) {
            Thread.sleep(1000);
            if (outputStream.toString().indexOf("$") > 0) {
                System.out.print(outputStream.toString());
                outputStream.reset();
                return;
            }
        }
    }
    public static void command() {
        try {



            connectSSH();
            channelSftp = (ChannelSftp) session.openChannel("sftp");	// 실행할 channel 생성

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            channelShell = (ChannelShell) session.openChannel("shell");
            channelShell.setOutputStream(outputStream);

            PrintStream stream = new PrintStream(channelShell.getOutputStream());
            //channelExec = (ChannelExec) session.openChannel("exec");	// 실행할 channel 생성
            channelShell.connect();

            stream.println("cd /home/gloryko3901");
            stream.flush();
            waitForPrompt(outputStream);

            stream.println("touch HelloAndroid.txt");
            stream.flush();
            waitForPrompt(outputStream);

//            channelExec.setCommand("cd /home/gloryko3901");
//            channelExec.connect();
//            channelExec.setCommand("touch HelloFromAndroid.txt");// 실행할 command 설정
//            channelExec.connect();		// command 실행
            Log.e("COME!!!!!!","5@@@@@@@@@@@@@@") ;

        } catch (JSchException e) {
            Log.e("JschError", "EROR:" + e.getMessage()) ;
            Logger.getLogger("JSchException");
        } catch (InterruptedException e)
        {
            Log.e("ThreadError", "ERROR ON THREAD") ;
        } catch (IOException e) {
            Log.e("IOException", "ERROR: " + e.getMessage()) ;
        } finally {
            disConnectSSH();
        }
    }
}

