package com.teamsparta.myblog.infra.aop


import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory



@Aspect
@Component
class TimeAop {

   private val logger = LoggerFactory.getLogger(TimeAop::class.java)


    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    fun checkTime(joinPoint: ProceedingJoinPoint,
    ) :Any? {
        val controllerName = joinPoint.signature.name
        val startTime = System.currentTimeMillis()


        val result =joinPoint.proceed()
        val endTime = System.currentTimeMillis()
        val duration = (endTime - startTime).toDouble() / 1000
        logger.info("$controllerName 실행 시간 $duration 초")
        return result


    }


}