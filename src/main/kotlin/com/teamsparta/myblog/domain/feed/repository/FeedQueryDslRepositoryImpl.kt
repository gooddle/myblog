package com.teamsparta.myblog.domain.feed.repository

import com.querydsl.core.BooleanBuilder
import com.teamsparta.myblog.domain.comment.model.QComment
import com.teamsparta.myblog.domain.feed.model.Feed
import com.teamsparta.myblog.domain.feed.model.QFeed
import com.teamsparta.myblog.infra.querydsl.QueryDslSupport
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class  FeedQueryDslRepositoryImpl(
) : FeedQueryDslRepository, QueryDslSupport() {


    private val feed: QFeed = QFeed.feed

    private val comment: QComment = QComment.comment


    override fun findByDeletedFalse(pageable: Pageable): Page<Feed> {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.deleted.eq(false))

        val totalCount = queryFactory.select(feed.count()).from(feed).where(whereClause).fetchOne() ?: 0L

        val query = queryFactory.selectFrom(feed)
            .where(feed.deleted.eq(false))
            .leftJoin(feed.comments,comment).fetchJoin()
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

        if (feedsToDelete.isNotEmpty()) {
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
}