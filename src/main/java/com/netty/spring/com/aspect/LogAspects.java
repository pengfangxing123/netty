package com.netty.spring.com.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Aspect
@Component
public class LogAspects {

    @Pointcut("execution(public void com.netty.spring.com.aspect.Car.*(..))")
    public void webLog() {
        //空方法
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getDeclaringType()+"===="+joinPoint.getSignature().getName());
    }

    @After("webLog()")
    public void doEnd(){
        System.out.println("run is end");
    }

    @AfterReturning("webLog()")
    //@AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning() {
        System.out.println("doAfterReturning");
    }

    @Around("webLog()")
    public void test(){
        System.out.println("MethodCallback");
    }
}
