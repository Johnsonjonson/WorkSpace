package fish.servlet;//package com.fish.servlet;
//
//import com.fish.data.FishTankBean;
//import com.fish.data.FishTankData;
//import com.google.gson.Gson;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//@WebListener
//public class Config implements ServletContextListener {
//    private static Gson gson  = new Gson();
//
//    public void contextInitialized(ServletContextEvent event) {
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                        try{
////                            ServerSocket serverSocket=new ServerSocket(8686);
////                            while(true) {
////                                System.out.println("服务端已启动，等待客户端连接..");
////                                Socket socket = serverSocket.accept();//侦听并接受到此套接字的连接,返回一个Socket对象
////                                //根据输入输出流和客户端连接
////                                InputStream inputStream = socket.getInputStream();//得到一个输入流，接收客户端传递的信息
////                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);//提高效率，将自己字节流转为字符流
////                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//加入缓冲区
////                                String temp = null;
////                                String info = "";
////                                while ((temp = bufferedReader.readLine()) != null) {
////                                    info += temp;
////                                }
////                                System.out.println("已接收到客户端连接" + ",当前客户端ip为：" + socket.getInetAddress().getHostAddress());
////                                System.out.println("收到客户端消息" + info);
////                                FishTankBean fishTank = gson.fromJson(info, FishTankBean.class);
////                                boolean needChange = FishTankData.getNeedChange();
////                                double tempValue = fishTank.getTemp();
////                                FishTankData.setCurTemp(tempValue);
////                                FishWebSocket.sendMessage(tempValue,needChange);
////                                System.out.println("服务端接收到客户端信息：" + fishTank.getTemp());
////
////                                OutputStream outputStream = socket.getOutputStream();//获取一个输出流，向服务端发送信息
////                                PrintWriter printWriter = new PrintWriter(outputStream);//将输出流包装成打印流
////                                printWriter.print(needChange);
////                                printWriter.flush();
////                                socket.shutdownOutput();//关闭输出流
////
////                                //关闭相对应的资源
////                                printWriter.close();
////                                outputStream.close();
////                                bufferedReader.close();
////                                inputStream.close();
////                                socket.close();
////                            }
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                    }
////            }).start();
//
//    }
//    public void contextDestroyed(ServletContextEvent event) {
//        // Do stuff during webapp's shutdown.
//    }
//}