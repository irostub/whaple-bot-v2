package com.irostub.domain.repository;

import com.irostub.domain.entity.market.QResaleBoard;
import com.irostub.domain.entity.market.ResaleBoard;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.irostub.domain.entity.market.QResaleBoard.*;

@RequiredArgsConstructor
public class ResaleBoardQueryRepositoryImpl implements ResaleBoardQueryRepository{
    private final JPAQueryFactory query;

    @Override
    public Page<ResaleBoard> findBySearch(Pageable pageable, String name, String keyword) {
        List<ResaleBoard> contents = query.selectFrom(resaleBoard)
                .distinct()
                .join(resaleBoard.webAppUser)
                .fetchJoin()
                .leftJoin(resaleBoard.images)
                .fetchJoin()
                .where(nameContain(name),
                        keywordContain(keyword)
                )
                .orderBy(resaleBoard.createdDate.desc(), resaleBoard.soldout.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(contents, pageable, ()-> getSearchCount(name, keyword));
    }

    private Long getSearchCount(String name, String keyword){
        return (long) query.selectFrom(resaleBoard)
                .distinct()
                .join(resaleBoard.webAppUser)
                .fetchJoin()
                .leftJoin(resaleBoard.images)
                .fetchJoin()
                .where(nameContain(name),
                        keywordContain(keyword)
                ).fetch().size();
    }

    private static Predicate keywordContain(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return resaleBoard.title.upper().contains(keyword)
                .or(resaleBoard.content.contains(keyword));
    }

    private static Predicate nameContain(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return resaleBoard.webAppUser.fullname1.contains(name)
                .or(resaleBoard.webAppUser.fullname2.contains(name))
                .or(resaleBoard.webAppUser.username.contains(name));
    }
}
