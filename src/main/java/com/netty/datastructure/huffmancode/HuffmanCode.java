package com.netty.datastructure.huffmancode;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 赫夫曼编码
 * @author 86136
 */
public class HuffmanCode {

    public static void main(String[] args) {
//        String content="i like like like java do you like a java ttt yy fsd gdf 213 fds";
//        List<HumNode> list = getNodes(content.getBytes());
//        HumNode huc = createHuc(list);
//        //huc.preOrder();
//        Map<Byte, String> humMap = getHumMap(huc);
//        System.out.println(content.getBytes().length);
//        byte[] zip = zipByte(humMap, content.getBytes());
//        System.out.println(zip.length);
//        byte[] unzip = unzip(humMap, zip);
//        System.out.println(new String(unzip));

        //测试压缩文件
		String srcFile = "d://Uninstall.xml";
		String dstFile = "d://Uninstall.zip";

		zipFile(srcFile, dstFile);
		System.out.println("压缩文件ok~~");


        //测试解压文件
		String zipFile = "d://Uninstall.zip";
		String zdstFile = "d://Uninstall2.xml";
		unZipFile(zipFile, dstFile);
		System.out.println("解压成功!");
    }

    /**
     * 将字符串转为节点集合
     * @param bytes
     * @return
     */
    public static List<HumNode> getNodes(byte[] bytes){
        //1创建一个ArrayList
        ArrayList<HumNode> nodes = new ArrayList<HumNode>();

        //遍历 bytes , 统计 每一个byte出现的次数->map[key,value]
        Map<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes) {
            Integer count = counts.get(b);
            // Map还没有这个字符数据,第一次
            if (count == null) {
                counts.put(b, 1);
            } else {
                counts.put(b, count + 1);
            }

            //上面代码可以用 这个代替 counts.merge(b, 1, Integer::sum);
        }

        //把每一个键值对转成一个Node 对象，并加入到nodes集合
        //遍历map
        for(Map.Entry<Byte, Integer> entry: counts.entrySet()) {
            nodes.add(new HumNode(entry.getKey(), entry.getValue()));
        }
        return nodes;
    };

    /**
     * 将节点集合转换赫夫曼树
     * @param list
     * @return
     */
    public static HumNode createHuc(List<HumNode> list){
        //操作集合，最好接受一下避免破坏传入的list
        List<HumNode> curList=list;

        while (curList.size()>1){
            Collections.sort(curList, new Comparator<HumNode>() {
                @Override
                public int compare(HumNode o1, HumNode o2) {
                    return o1.weight-o2.weight;
                }
            });

            HumNode newNode = new HumNode(null, curList.get(0).weight + curList.get(1).weight);
            newNode.left=curList.get(0);
            newNode.right=curList.get(1);

            curList.remove(1);
            curList.remove(0);

            curList.add(newNode);
        }
        return curList.get(0);
    }


    /**
     * 从赫夫曼树 中得到byte和 赫夫曼编码的对应关系
     * @param node
     * @return
     */
    public static Map<Byte, String> getHumMap(HumNode node){
        Map<Byte, String> ret = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        getHumMap(ret,node.left,"0",builder);
        getHumMap(ret,node.right,"1",builder);
        return ret;
    }


    /**
     * 从赫夫曼树 中得到byte和 赫夫曼编码的对应关系
     * @param ret map
     * @param node 节点
     * @param code 左节点：0，右节点：1
     * @param builder 
     */
    public static void getHumMap(Map<Byte, String> ret ,HumNode node ,String code,StringBuilder builder){
        StringBuilder curBuilder = new StringBuilder(builder);
        //将当前的code加入
        curBuilder.append(code);
        if(node!=null){
            if(node.data!=null){
                //说明是叶子节点(因为我们创建树的时候非叶子节点data值为Null),不需要再继续走下去
                ret.put(node.data,curBuilder.toString());
            }else{
                //非叶子节点继续往下寻找
                getHumMap(ret,node.left,"0",curBuilder);
                getHumMap(ret,node.right,"1",curBuilder);
            }
        }
    }

    /**
     * 将byte[] 按赫夫曼编码进行压缩
     * @param humMap
     * @param unzip
     * @return
     */
    public static byte[] zipByte(Map<Byte, String> humMap,byte[] unzip){
        //先将byte数组转为对应的位 字符串
        StringBuilder builder = new StringBuilder();
        for(byte b:unzip){
            builder.append(humMap.get(b));
        }
        //String humStr = builder.toString();

        //将字符串转为byte数组
        //计算byte长度
        int zipLength = (builder.length() + 7) / 8;
        byte[] zip = new byte[zipLength];
        for(int i=0 ,j=0;i<zip.length;i++,j+=8){
            if((j+8)<=builder.length()){
                zip[i]=(byte) Integer.parseInt(builder.substring(j,j+8),2);
            }else{
                //这里是错误的，不足8位要补0，还要记录补了多少
                zip[i]=(byte) Integer.parseInt(builder.substring(j),2);
            }
        }
        return zip;
    }

    public static byte[] unzip(Map<Byte, String> humMap,byte[] zip){
        StringBuilder builder = new StringBuilder();

        //将byte数组转成二进制的字符串
        for(int i = 0; i < zip.length; i++) {
            byte b = zip[i];
            //判断是不是最后一个字节
            boolean flag = (i == zip.length - 1);
            builder.append(byteToBitString(!flag, b));
        }
        //把字符串安装指定的赫夫曼编码进行解码
        //把赫夫曼编码表进行调换，因为反向查询 a->100 100->a
        Map<String, Byte>  map = new HashMap<>();
        for(Map.Entry<Byte, String> entry: humMap.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }

        //创建要给集合，存放byte
        List<Byte> list = new ArrayList<>();
        //i 可以理解成就是索引,扫描 stringBuilder
        for(int  i = 0; i < builder.length(); ) {
            int count = 1; // 小的计数器
            boolean flag = true;
            Byte b = null;

            while(flag) {
                //1010100010111...
                //递增的取出 key 1
                //i 不动，让count移动，指定匹配到一个字符
                String key = builder.substring(i, i+count);
                b = map.get(key);
                if(b == null) {
                    //说明没有匹配到
                    count++;
                }else {
                    //匹配到
                    flag = false;
                }
            }
            list.add(b);
            //i 直接移动到 count
            i += count;
        }
        //当for循环结束后，我们list中就存放了所有的字符  "i like like like java do you like a java"
        //把list 中的数据放入到byte[] 并返回
        byte unzip[] = new byte[list.size()];
        for(int i = 0;i < unzip.length; i++) {
            unzip[i] = list.get(i);
        }
        return unzip;
    }

    /**
     * 将一个byte 转成一个二进制的字符串
     * @param b 传入的 byte
     * @param flag 标志是否需要补高位如果是true ，表示需要补高位，如果是false表示不补, 如果是最后一个字节，无需补高位
     * @return 是该b 对应的二进制的字符串，（注意是按补码返回）
     */
    private static String byteToBitString(boolean flag, byte b) {
        //将 b 转成 int
        int temp = b;
        //如果是正数我们还存在补高位
        if(flag) {
            //按位与 256  1 0000 0000  | 0000 0001 => 1 0000 0001
            temp |= 256;
        }
        //返回的是temp对应的二进制的补码
        String str = Integer.toBinaryString(temp);
        if(flag) {
            return str.substring(str.length() - 8);
        } else {
            return str;
        }
    }

    /**
     * 解压
     * @param zipFile 准备解压的文件
     * @param dstFile 将文件解压到哪个路径
     */
    public static void unZipFile(String zipFile, String dstFile) {

        //定义文件输入流
        InputStream is = null;
        //定义一个对象输入流
        ObjectInputStream ois = null;
        //定义文件的输出流
        OutputStream os = null;
        try {
            //创建文件输入流
            is = new FileInputStream(zipFile);
            //创建一个和  is关联的对象输入流
            ois = new ObjectInputStream(is);
            //读取byte数组  huffmanBytes
            byte[] huffmanBytes = (byte[])ois.readObject();
            //读取赫夫曼编码表
            Map<Byte,String> huffmanCodes = (Map<Byte,String>)ois.readObject();

            //解码
            byte[] bytes = unzip(huffmanCodes, huffmanBytes);
            //将bytes 数组写入到目标文件
            os = new FileOutputStream(dstFile);
            //写数据到 dstFile 文件
            os.write(bytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            try {
                os.close();
                ois.close();
                is.close();
            } catch (Exception e2) {
                System.out.println(e2.getMessage());
            }

        }
    }

    //编写方法，将一个文件进行压缩
    /**
     *
     * @param srcFile 你传入的希望压缩的文件的全路径
     * @param dstFile 我们压缩后将压缩文件放到哪个目录
     */
    public static void zipFile(String srcFile, String dstFile) {

        //创建输出流
        OutputStream os = null;
        ObjectOutputStream oos = null;
        //创建文件的输入流
        FileInputStream is = null;
        try {
            //创建文件的输入流
            is = new FileInputStream(srcFile);
            //创建一个和源文件大小一样的byte[]
            byte[] b = new byte[is.available()];
            //读取文件
            is.read(b);
            //直接对源文件压缩
            List<HumNode> list = getNodes(b);
            Map<Byte, String> humMap = getHumMap(createHuc(list));
            byte[] huffmanBytes = zipByte(humMap,b);
            //创建文件的输出流, 存放压缩文件
            os = new FileOutputStream(dstFile);
            //创建一个和文件输出流关联的ObjectOutputStream
            oos = new ObjectOutputStream(os);
            //把 赫夫曼编码后的字节数组写入压缩文件
            oos.writeObject(huffmanBytes); //我们是把
            //这里我们以对象流的方式写入 赫夫曼编码，是为了以后我们恢复源文件时使用
            //注意一定要把赫夫曼编码 写入压缩文件
            oos.writeObject(humMap);


        }catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }finally {
            try {
                is.close();
                oos.close();
                os.close();
            }catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }

    }
}

class HumNode{
    Byte data;
    int weight;
    HumNode left;
    HumNode right;

    public HumNode(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    public void preOrder(){
        System.out.println(toString());
        if(left!=null){
            left.preOrder();
        }
        if(right!=null){
            right.preOrder();
        }
    }

    @Override
    public String toString() {
        return "HumNode{" +
                "data=" + data +
                ", weight=" + weight +
                '}';
    }
}
