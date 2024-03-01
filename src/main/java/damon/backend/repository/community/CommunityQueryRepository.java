package damon.backend.repository.community;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import damon.backend.entity.community.Community;
import damon.backend.enums.CommunityType;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import static damon.backend.entity.community.QCommunity.community;

@Repository
public class CommunityQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CommunityQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Community> searchCommunity(String keyword, CommunityType type, Pageable pageable) {
        List<Community> communities = queryFactory
                .selectFrom(community)
                .leftJoin(community.user).fetchJoin()
                .where(containsKeyword(keyword), typeEquals(type))
                .orderBy(community.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = queryFactory
                .selectFrom(community)
                .where(containsKeyword(keyword), typeEquals(type))
                .fetchCount();

        return new PageImpl<>(communities, pageable, totalCount);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return community.title.contains(keyword).or(community.content.contains(keyword));
    }

    private BooleanExpression typeEquals(CommunityType type) {
        if (type == null) {
            return null;
        }
        return community.type.eq(type);
    }
}
