package com.example.employeeapi.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.employeeapi.controller..*(..)) || execution(* com.example.employeeapi.service..*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.debug("Entering {} with args={}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @Around("execution(* com.example.employeeapi.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Executed {} in {} ms", pjp.getSignature(), duration);
        }
    }
}


