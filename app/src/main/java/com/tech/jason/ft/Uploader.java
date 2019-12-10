package com.tech.jason.ft;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;

public class Uploader {

    private String KEY_SENT;
    private int port1=55255;
    private int port2=55256;
    private int port3=55257;
    private String target_IP;
    private String message;
    private File file;

    public Uploader(String target_IP,String message,File file){
        this.target_IP = target_IP;
        this.message = message;
        this.file = file;
    }



    public void Upload_Task_String(){
        try {
            Socket socket = new Socket(target_IP, port1);
            OutputStreamWriter outWriter = new OutputStreamWriter(socket.getOutputStream());
            outWriter.write(message);
            outWriter.flush();
            outWriter.close();
            socket.close();
            System.out.println("^^ ! ^^");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("无法连接目标IP！请检查目标IP是否和你在同一个局域网内，并确认你所输入的是否正确。或确认对方是否已经正确进入本客户端的receive模式.");
            System.out.println("TargetIP: "+target_IP);
        }
    }

    public void Upload_Task_File() {

        if (Recieve_CS_Task()) {
            try {
                System.out.println("开始传输文件...");
                Socket socket = new Socket(target_IP, port3);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                int len;
                double count = 0;
                Number progress0;
                Number fileSum = file.length();

                byte[] bytes = new byte[2048];
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    count = count + len;
                    progress0 = count * 100 / fileSum.intValue();
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    nf.setMaximumFractionDigits(2);
                    System.out.println(nf.format(progress0) + "%" + "..");
                    bufferedOutputStream.write(bytes, 0, len);
                    bufferedOutputStream.flush();
                }
                System.out.println("文件传输完成! ^^");
                System.out.println("文件下载完成! ^^\n你可以选择进行第二次文件传输或者退出.\n\n");
                if (socket != null) {
                    socket.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean Recieve_CS_Task() {
        try {
            ServerSocket serverSocket = new ServerSocket(port2);
            System.out.println("uploader正在等待对方(receiver)的密钥验证...  ");
            Socket connection = serverSocket.accept();
            System.out.println("sender已连接到uploader(本机)的55256端口...");

            InputStreamReader inReader = new InputStreamReader(connection.getInputStream());
            char[] chars = new char[1024];
            int len = inReader.read(chars);
            System.out.println("已读取到receiver的许可协议...");
            KEY_SENT = new String(chars, 0, len);
            System.out.println("读取到的结果为： "  + " ~ " + KEY_SENT.equals("YES")+"...");
            serverSocket.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }System.out.println(KEY_SENT);
        return (KEY_SENT.equals("YES"));
    }

    public class Uploader_Thread extends Thread{
        @Override
        public void run(){
            synchronized(this) {
                Upload_Task_String();
                Upload_Task_File();
            }
        }
    }

    public void Uploader_Thread_Start(){
        Uploader_Thread uploader_thread = new Uploader_Thread();
        uploader_thread.start();
    }

}
