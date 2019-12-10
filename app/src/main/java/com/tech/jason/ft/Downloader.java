package com.tech.jason.ft;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Downloader {

    private Socket connection;
    private ServerSocket serverSocket;
    private int port1=55255;
    private int port2=55256;
    private int port3=55257;
    private File file_to_save;
    private String file_parent;


    public Downloader(String file_parent){
        this.file_parent = file_parent;
    }



    public void Download_String_Task() {
        try {
            serverSocket = new ServerSocket(port1);
            System.out.println("已进入监听模式，正在等待对方连接...");
            connection = serverSocket.accept();
            System.out.println("对方已连接...");
            InputStreamReader inReader = new InputStreamReader(connection.getInputStream());
            char[] chars = new char[1024];                                            //读取客户端发来的文件名
            int len = inReader.read(chars);
            System.out.println("文件名读取完成...");
            String file_name_local = new String(chars, 0, len);
            System.out.println("读取到的文件名是： " + file_name_local + "\t文件名长度为" + file_name_local.length()+" ...");
            System.out.println("file_parent: " + file_parent + "\tfile_name_local: " + file_name_local+" ...");
            String file_to_save_path = file_parent + File.separator + file_name_local;
            file_to_save = new File(file_to_save_path);
            file_to_save.createNewFile();  //本地先创建一个空文件，文件名为file_name_local
            System.out.println("已在本地的" + file_parent + "/ 的目录下创建了一个名为" + file_name_local + "空文件...");
            serverSocket.close();
            connection.close();
            inReader.close();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send_CS_Task();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void Download_File_Task(){
        try {
            System.out.println("正在下载文件中... ...");
            serverSocket = new ServerSocket(port3);
            connection = serverSocket.accept();
            BufferedInputStream bufin = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bufout = new BufferedOutputStream(new FileOutputStream(file_to_save));            //下载（客户端发来的）文件
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bufin.read(bytes)) != -1) {
                bufout.write(bytes, 0, len);
                bufout.flush();
            }
            System.out.println("文件下载完成! ^^\n你可以选择进行第二次文件传输或者退出\n\n.");
            if(serverSocket!=null){serverSocket.close();}
            if (connection!=null){connection.close();}
            if (bufin!=null){bufin.close();}
            if (bufout!=null){bufout.close();}
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void send_CS_Task() {

        //  for(int i=0;i<=2;i++) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
            System.out.println("开始传输CS...");
            Socket socket = new Socket(connection.getInetAddress(), port2);
            System.out.println("receiver执行send_CS_Task()方法时，实例的socket中传入的client_IP是: " + connection.getInetAddress());
            OutputStreamWriter outWriter = new OutputStreamWriter(socket.getOutputStream());
            String CONFIRM_SIGNAL = "Y";
            outWriter.write(CONFIRM_SIGNAL);
            outWriter.flush();
            System.out.println("CS传输完成...");
            if (socket != null) {
                socket.close();
            }
            if (outWriter != null) {
                outWriter.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // }


    }


}
