package com.teamsparta.myblog.domain.feed.scheduler


import com.teamsparta.myblog.domain.feed.repository.FeedRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class FeedCleanupScheduler(private val feedRepository: FeedRepository) {

    private val logger = LoggerFactory.getLogger(FeedCleanupScheduler::class.java)

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun cleanupDeletedFeeds() {
        logger.info("Starting cleanupDeletedFeeds task")

        try {
            val olderFeeds = LocalDateTime.now().minusHours(3)

            val deletedFeed = feedRepository.findAndDeleteByDeletedAtBefore(olderFeeds)

            logger.info("Found ${deletedFeed.size} feeds to delete")


            logger.info("Finished cleanupDeletedFeeds task")

        } catch (ex: Exception) {
            logger.error("Error in cleanupDeletedFeeds task: ${ex.message}", ex)
        }
    }
}