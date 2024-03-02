package damon.backend.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import damon.backend.entity.QReview;
import damon.backend.entity.Review;
import damon.backend.entity.QTag;
import damon.backend.repository.ReviewRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Review> searchByCriteria(String searchMode, String keyword, Pageable pageable) {
        QReview review = QReview.review;
        QTag tag = QTag.tag;

        BooleanExpression predicate = null;
        switch (searchMode) {
            case "nickname":
                predicate = review.user.nickname.containsIgnoreCase(keyword);
                break;
            case "title":
                predicate = review.title.containsIgnoreCase(keyword);
                break;
            case "tag":
                // 태그 검색 시 QTag의 value 필드 사용
                predicate = tag.value.containsIgnoreCase(keyword).and(tag.review.id.eq(review.id));
                break;
        }

        List<Review> results = this.queryFactory.selectFrom(review)
                .leftJoin(review.tags, tag)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}