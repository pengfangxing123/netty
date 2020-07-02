package com.netty.spring.xml.customxmltag.handler;

import com.netty.spring.xml.customxmltag.parser.UserDefinitionParser;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;

/**
 * @author 86136
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user", new UserDefinitionParser());
        //这个是验证装饰的BeanDefinitionParserDelegate#decorateBeanDefinitionIfRequired方法的
        registerBeanDefinitionDecorator("user", (node, definition, parserContext) -> definition);
    }

}
