package com.teamsparta.myblog.domain.feed.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.teamsparta.myblog.domain.comment.model.QComment
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

    private val comment: QComment = QComment.comment

    //전체 조회시 페이지 정렬 기능 및 필터 기능
    override fun findByDeletedFalse(pageable: Pageable,title : String?,firstDay: Long?,secondDay: Long?,category: FeedCategory?): Page<Feed> {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.deleted.eq(false))

        // 람다 함수 익숙해지기
        title?.let {
            whereClause.and(titleLike(title))
        }
        firstDay?.let {
            whereClause.and(widthInDays(firstDay,secondDay!!))
        }
        category?.let {
            whereClause.and(searchByCategory(category))
        }

        val totalCount = queryFactory.select(feed.count()).from(feed).where(whereClause).fetchOne() ?: 0L

        // offset 좀 더 이해 하기
        val query = queryFactory.selectFrom(feed)
            .where(whereClause)
            .leftJoin(feed.comments, comment).fetchJoin()
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

    //스케줄링 기능 삭제된 시점으로  특정 시간이 지난 feed들을 리스트로 만들어 한번에 삭제
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

    //feed와 comment join을 통해 한번에 조회
    override fun findByFeedIdWithComments(feedId: Long): Feed? {
        val whereClause = BooleanBuilder()
        whereClause.and(feed.id.eq(feedId))

        val feedById = queryFactory.selectFrom(feed)
            .leftJoin(feed.comments).fetchJoin()
            .where(whereClause)
            .fetchOne()

        return feedById

    }

    private fun titleLike(title:String):BooleanExpression{
        return feed.title.contains(title)
    }

    private fun widthInDays(firstDay: Long, secondDay:Long): BooleanExpression {
      return feed.createdAt.between(LocalDateTime.now().minusDays(firstDay), LocalDateTime.now().minusDays(secondDay))
    }

    private fun searchByCategory(category:FeedCategory):BooleanExpression{
        return feed.feedCategory.eq(category)
    }
}
