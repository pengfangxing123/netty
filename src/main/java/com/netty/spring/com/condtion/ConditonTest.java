package com.netty.spring.com.condtion;

import com.netty.spring.com.entity.Person;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Administrator
 */
@Configuration
@Conditional(value=myConditon.class)
public class ConditonTest {
    @Bean
    public Person person(){
        return  new Person();
    }

}

class myConditon implements Condition{
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return false;
    }
}
