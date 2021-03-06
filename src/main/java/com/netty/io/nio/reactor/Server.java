package com.netty.io.nio.reactor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author 80004148
 */
public class Server {

    public static void main(String[] args) throws IOException {
        //开启一个socket连接
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //默认是true->阻塞，而注册到selectors上的channel必须是非阻塞的，所以设置为false
        socketChannel.configureBlocking(false);
        //Inet4Address.getLocalHost()只能通过本机的ipv4访问
        socketChannel.socket().bind(new InetSocketAddress(Inet4Address.getLocalHost(),8090));
        //jdk大于1.7也可以这样绑定
        //socketChannel.bind(new InetSocketAddress(Inet4Address.getLocalHost(),8090));
        //开启一个selector
        Selector selector = Selector.open();

        //将socket连接注册到selector
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            int n = selector.select();
            if (n==0){
                //目前个人觉得 这个判断多余的 selector.select()会阻塞
                //补充：其实是有必要的，因为selector会存在bug，会返回0
                // (原因好像是，操作系统给了错误的信息，给获取事件的代码，去获取事件，实际上是没有没实际的，所以会返回0)
                continue;
            }
            //获取准备就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key=iterator.next();

                //忽略无效的 SelectionKey
                if(!key.isValid())
                    continue;
                //连接事件
                if(key.isAcceptable()){
                    //key.channel()获取的ServerSocketChannel，因为注册accept事件的只有ServerSocketChannel
                    //ServerSocketChannel#accept获取客户端channel
                    SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
                    channel.configureBlocking(false);
                    channel.setOption(StandardSocketOptions.SO_KEEPALIVE,true);
                    //现在把客户端的channel也注册到了服务端的selector了,并渐渐读事件
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()){
//                    SocketChannel channel = (SocketChannel) key.channel();
//                    ByteBuffer buffer = ByteBuffer.allocate(1024);
//                    int read = channel.read(buffer);
//                    if(read==-1){
//                        System.out.println("没有读取到数据，连接已经被断开");
//                        channel.register(selector,0);//参数为0取消该channel注册
//                        return;
//                    }
//                    System.out.println("读取到"+read);
                    int buffersize= 1024;//设置缓冲区大小

                    String localCharSet="UTF-8"; //设置编码格式
                    SocketChannel sc=(SocketChannel)key.channel();// SocketChannel 是一个连接到 TCP 网络套接字的通道

                    ByteBuffer buffer=(ByteBuffer)key.attachment();//从 SocketChannel读取到的数据将会放到这个 buffer中

                    buffer.clear();

                    try {
                        if((sc.read(buffer))!=-1) {

                            buffer.flip();//flip方法将Buffer从写模式切换到读模式

                            String receive = Charset.forName(localCharSet).newDecoder().decode(buffer).toString();
                            //将此 charset 中的字节解码成 Unicode 字符

                            String[] requestMessage = receive.split("\r\n");//接受请求的信息

                            for (String message : requestMessage) {

                                if (message.isEmpty()) {//如果是空行说明信息已经结束了

                                    break;
                                }
                            }
                            //控制台打印
                            String[] firsetLine = requestMessage[0].split(" ");

                            System.out.println("----控制台输出：-------");

                            System.out.println("Method:t"+firsetLine[0]);

                            System.out.println("url是:\t"+firsetLine[1]);

                            System.out.println("Httpversion是:\t"+firsetLine[2]);

                            System.out.println("-----输出结束-------------");

                            //返回客户端
                            StringBuilder sendStr = new StringBuilder();

                            sendStr.append("Http/1.1 200 Ok\r\n");

                            sendStr.append("Content-Type:text/html;charset="+localCharSet+"\r\n");

                            sendStr.append("\r\n");

                            sendStr.append("<html><head><title>显示报文</title></head><body>");

                            sendStr.append("接受到请求的报文是：+<br>");

                            for (String s : requestMessage) {

                                sendStr.append(s+"<br/>");

                            }
                            sendStr.append("</body></html>");

                            buffer=ByteBuffer.wrap(sendStr.toString().getBytes(localCharSet));
                            sc.register(selector, SelectionKey.OP_WRITE, buffer);
                            sc.write(buffer);
                            //sc.close();
                        }else {
                            //sc.close();
                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                if (key.isWritable() && key.isValid()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer=(ByteBuffer)key.attachment();
                    channel.write(buffer);
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("写入");
                    channel.close();
                }
                if (key.isConnectable()){
                    System.out.println("isConnectable = true");
                }
                iterator.remove();

            }
        }
    }
}


