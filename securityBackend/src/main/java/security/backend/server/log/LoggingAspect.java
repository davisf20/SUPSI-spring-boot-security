package security.backend.server.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* security.backend.server.controller.AuthenticationController.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        LOGGER.info("Entering method: {} with arguments: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @After("execution(* security.backend.server.controller.AuthenticationController.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        LOGGER.info("Exiting method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* security.backend.server.controller.AuthenticationController.*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        LOGGER.info("Exiting method: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @Around("execution(* security.backend.server.controller.AuthenticationController.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.info("Execution time of method {} is {} ms", proceedingJoinPoint.getSignature().getName(), elapsedTime);
        return result;
    }

    @AfterThrowing(pointcut = "execution(* security.backend.server.controller.AuthenticationController.*(..))", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) {
        LOGGER.error("Exception thrown in method: {} with message: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
