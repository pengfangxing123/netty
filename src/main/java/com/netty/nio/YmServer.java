//package com.netty.nio;
//
///**
// * @author Administrator
// */
//public class YmServer {
//  2:
//          3:     private ServerSocketChannel serverSocketChannel;
//  4:     private Selector selector;
//  5:
//          6:     public NioServer() throws IOException {
//        7:         // 打开 Server Socket Channel
//        8:         serverSocketChannel = ServerSocketChannel.open();
//        9:         // 配置为非阻塞
//        10:         serverSocketChannel.configureBlocking(false);
//        11:         // 绑定 Server port
//        12:         serverSocketChannel.socket().bind(new InetSocketAddress(8080));
//        13:         // 创建 Selector
//        14:         selector = Selector.open();
//        15:         // 注册 Server Socket Channel 到 Selector
//        16:         serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        17:         System.out.println("Server 启动完成");
//        18:
//        19:         handleKeys();
//        20:     }
// 21:
//         22:     private void handleKeys() throws IOException {
//        23:         while (true) {
//            24:             // 通过 Selector 选择 Channel
//            25:             int selectNums = selector.select(30 * 1000L);
//            26:             if (selectNums == 0) {
//                27:                 continue;
//                28:             }
//            29:             System.out.println("选择 Channel 数量：" + selectNums);
//            30:
//            31:             // 遍历可选择的 Channel 的 SelectionKey 集合
//            32:             Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//            33:             while (iterator.hasNext()) {
//                34:                 SelectionKey key = iterator.next();
//                35:                 iterator.remove(); // 移除下面要处理的 SelectionKey
//                36:                 if (!key.isValid()) { // 忽略无效的 SelectionKey
//                    37:                     continue;
//                    38:                 }
//                39:
//                40:                 handleKey(key);
//                41:             }
//            42:         }
//        43:     }
// 44:
//         45:     private void handleKey(SelectionKey key) throws IOException {
//        46:         // 接受连接就绪
//        47:         if (key.isAcceptable()) {
//            48:             handleAcceptableKey(key);
//            49:         }
//        50:         // 读就绪
//        51:         if (key.isReadable()) {
//            52:             handleReadableKey(key);
//            53:         }
//        54:         // 写就绪
//        55:         if (key.isWritable()) {
//            56:             handleWritableKey(key);
//            57:         }
//        58:     }
// 59:
//         60:     private void handleAcceptableKey(SelectionKey key) throws IOException {
//        61:         // 接受 Client Socket Channel
//        62:         SocketChannel clientSocketChannel = ((ServerSocketChannel) key.channel()).accept();
//        63:         // 配置为非阻塞
//        64:         clientSocketChannel.configureBlocking(false);
//        65:         // log
//        66:         System.out.println("接受新的 Channel");
//        67:         // 注册 Client Socket Channel 到 Selector
//        68:         clientSocketChannel.register(selector, SelectionKey.OP_READ, new ArrayList<String>());
//        69:     }
// 70:
//         71:     private void handleReadableKey(SelectionKey key) throws IOException {
//        72:         // Client Socket Channel
//        73:         SocketChannel clientSocketChannel = (SocketChannel) key.channel();
//        74:         // 读取数据
//        75:         ByteBuffer readBuffer = CodecUtil.read(clientSocketChannel);
//        76:         // 处理连接已经断开的情况
//        77:         if (readBuffer == null) {
//            78:             System.out.println("断开 Channel");
//            79:             clientSocketChannel.register(selector, 0);
//            80:             return;
//            81:         }
//        82:         // 打印数据
//        83:         if (readBuffer.position() > 0) {
//            84:             String content = CodecUtil.newString(readBuffer);
//            85:             System.out.println("读取数据：" + content);
//            86:
//            87:             // 添加到响应队列
//            88:             List<String> responseQueue = (ArrayList<String>) key.attachment();
//            89:             responseQueue.add("响应：" + content);
//            90:             // 注册 Client Socket Channel 到 Selector
//            91:             clientSocketChannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
//            92:         }
//        93:     }
// 94:
//         95:     @SuppressWarnings("Duplicates")
// 96:     private void handleWritableKey(SelectionKey key) throws ClosedChannelException {
//        97:         // Client Socket Channel
//        98:         SocketChannel clientSocketChannel = (SocketChannel) key.channel();
//        99:
//        100:         // 遍历响应队列
//        101:         List<String> responseQueue = (ArrayList<String>) key.attachment();
//        102:         for (String content : responseQueue) {
//            103:             // 打印数据
//            104:             System.out.println("写入数据：" + content);
//            105:             // 返回
//            106:             CodecUtil.write(clientSocketChannel, content);
//            107:         }
//        108:         responseQueue.clear();
//        109:
//        110:         // 注册 Client Socket Channel 到 Selector
//        111:         clientSocketChannel.register(selector, SelectionKey.OP_READ, responseQueue);
//        112:     }
//113:
//        114:     public static void main(String[] args) throws IOException {
//        115:         NioServer server = new NioServer();
//        116:     }
//117:
//        118: }
//
