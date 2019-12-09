package com.netty.excel.factory;

import com.netty.excel.content.ExcelExportContent;
import com.netty.excel.service.ExcelExportStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 86136
 */
@Component
public class ExcelExportContentFactory implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    public ExcelExportContent getExcelExportContent(String beanName){
        if(StringUtils.isBlank(beanName)){

        }
        ExcelExportStrategy bean = getStrategyBean(beanName);
        return new ExcelExportContent(bean);
    }

    private ExcelExportStrategy getStrategyBean(String beanName) {
        Object bean = applicationContext.getBean(beanName);

        if(bean==null || !ExcelExportStrategy.class.isAssignableFrom(bean.getClass())){

        }

        return (ExcelExportStrategy)bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
