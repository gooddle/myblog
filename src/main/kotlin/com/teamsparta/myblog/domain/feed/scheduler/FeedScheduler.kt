package com.teamsparta.myblog.domain.feed.scheduler


import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@EnableScheduling
class FeedCleanupScheduler(private val feedRepository: FeedRepository) {

    private val logger = LoggerFactory.getLogger(FeedCleanupScheduler::class.java)

    @Transactional
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    fun cleanupDeletedFeeds() {
        logger.info("feed 삭제 시작")

        // 실행 시간 측정 시작
        val startTime = System.currentTimeMillis()

        try {
            val olderFeeds = LocalDateTime.now().minusHours(12)

            val deletedFeed = feedRepository.findAndDeleteByDeletedAtBefore(olderFeeds)

            logger.info("Found ${deletedFeed.size} 개 삭제 ")

            logger.info("feed 삭제 완료")
        } catch (ex: Exception) {
            logger.error("Error in cleanupDeletedFeeds task: ${ex.message}", ex)
        } finally {
            // 실행 시간 측정 종료
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime // 실행 시간 (밀리초)
            logger.info("cleanupDeletedFeeds 메소드 실행 시간: ${duration}ms")
        }
    }
}