package com.teamsparta.myblog.domain.common


import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration


//redis bean 등록
@Component
class RedisUtils(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    //인증 코드 유효 시간 5분
    private val CODENUMBER_TIME = 1000 * 60 * 5L



    //Key 값을 통해 redis 에서 조회
    fun getData(key: String): String? {
        val valueOperations = redisTemplate.opsForValue()
        return valueOperations[key]
    }


    // key-value 값을 저장 하고 만료 시간을 저장한다.
    fun setDataExpire(key: String, value: String) {
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.set(key, value, Duration.ofMillis(CODENUMBER_TIME))
    }


    // 캐쉬에서 데이터 삭제
    fun deleteData(key: String) {
        redisTemplate.delete(key)
    }

}