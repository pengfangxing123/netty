package com.netty.rpc.spring;

import com.netty.rpc.provder.ApplicationConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 服务端配置信息
 * @author 86136
 */
public class ApplicationConfigDefinitionParser extends AbstractSingleBeanDefinitionParser {

    //logger
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigDefinitionParser.class);


    @Override
    protected Class getBeanClass(Element element) {
        return ApplicationConfig.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder bean) {

        try {
            String timeOut = element.getAttribute("timeout");
            String serverPort = element.getAttribute("serverPort");
            String appKey = element.getAttribute("appKey");

            bean.addPropertyValue("serverPort", Integer.parseInt(serverPort));
            bean.addPropertyValue("timeout", Integer.parseInt(timeOut));
            bean.addPropertyValue("appKey", appKey);

        } catch (Exception e) {
            logger.error("ApplicationConfigDefinitionParser error.", e);
            throw new RuntimeException(e);
        }

    }


}
