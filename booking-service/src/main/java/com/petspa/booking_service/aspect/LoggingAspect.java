    package com.petspa.booking_service.aspect;

    import lombok.extern.slf4j.Slf4j;
    import org.aspectj.lang.ProceedingJoinPoint;
    import org.aspectj.lang.annotation.Around;
    import org.aspectj.lang.annotation.Aspect;
    import org.slf4j.MDC;
    import org.springframework.stereotype.Component;

    import java.util.UUID;

    @Slf4j
    @Aspect
    @Component
    public class LoggingAspect {

        @Around("execution(* com.petspa.booking_service.controller..*(..))")
        public Object logController(ProceedingJoinPoint pjp) throws Throwable {
            String traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
            try {
                log.info("Incoming request: {} args={}", pjp.getSignature(), pjp.getArgs());
                Object result = pjp.proceed();
                log.info("Response for {}: {}", pjp.getSignature(), result);
                return result;
            } finally {
                MDC.clear();
            }
        }
    }
