package com.teamsparta.myblog.domain.feed.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.FeedCategory
import com.teamsparta.myblog.domain.feed.model.QFeed
import com.teamsparta.myblog.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime




class  FeedQueryDslRepositoryImpl(
) : FeedQueryDslRepository, QueryDslSupport() {



    private val feed: QFeed = QFeed.feed


    override fun findByDeletedFalse(pageable: Pageable,title : String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<Feed> {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.deleted.eq(false))
            .and(title?.let { titleLike(title) })
            .and(firstDay?.let { widthInDays(firstDay,secondDay!!) })
            .and(category?.let { searchByCategory(category) })

        val totalCount = queryFactory.select(feed.count()).from(feed).where(whereClause).fetchOne() ?: 0L

        val query = queryFactory.selectFrom(feed)
            .where(whereClause)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())



        if (pageable.sort.isSorted) {
            when (pageable.sort.first()?.property) {
                "id" -> query.orderBy(feed.id.asc())
                "title" -> query.orderBy(feed.title.asc())
                else -> query.orderBy(feed.createdAt.desc())
            }
        }

            val contents = query.fetch()

            return PageImpl(contents, pageable, totalCount)

    }

    override fun findAndDeleteByDeletedAtBefore(olderFeeds: LocalDateTime): List<Feed> {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.deletedAt.before(olderFeeds))


        val feedsToDelete = queryFactory
            .selectFrom(feed)
            .where(whereClause)
            .fetch()

        if (feedsToDelete.isNotEmpty()){
            queryFactory
                .delete(feed)
                .where(feed.`in`(feedsToDelete))
                .execute()
        }

        return feedsToDelete
    }

    override fun findByFeedIdWithComments(feedId: Long): Feed? {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.id.eq(feedId))

        val feedById = queryFactory.selectFrom(feed)
            .leftJoin(feed.comments).fetchJoin()
            .where(whereClause)
            .fetchOne()

        return feedById

    }

    private fun titleLike(title:String): BooleanExpression {
        return feed.title.contains(title)
    }

    private fun widthInDays(firstDay: Long, secondDay:Long): BooleanExpression {
      return feed.createdAt.between(LocalDateTime.now().minusDays(firstDay), LocalDateTime.now().minusDays(secondDay))
    }

    private fun searchByCategory(category:FeedCategory):BooleanExpression{
        return feed.feedCategory.eq(category)
    }
}
