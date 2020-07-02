package com.netty.spring.xml.dom;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 86136
 */
public class Simple {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //建立DocumentBuilderFactor，用于获得DocumentBuilder对象：
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.建立DocumentBuidler：
        DocumentBuilder builder = factory.newDocumentBuilder();
        //3.建立Document对象，获取树的入口：
        InputStream inputStream = new ClassPathResource("spring/parent.xml").getInputStream();
        Document doc = builder.parse(new InputSource(inputStream));
        //4.建立NodeList：
        NodeList node = doc.getElementsByTagName("bean");
        //5.进行xml信息获取
        for(int i=0;i<node.getLength();i++){
            Element e = (Element)node.item(i);
            //System.out.println("姓名："+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            if(e!=null){
                NodeList property = e.getElementsByTagName("property");
                for(int j=0;j<property.getLength();j++){
                    Element inner= (Element)property.item(j);
                    //name
                    System.out.println( inner.getAttribute("name"));
                    //David
                    System.out.println( inner.getAttribute("value"));
                    //null
                    System.out.println( inner.getNodeValue());
                }
            }
        }

    }

}
