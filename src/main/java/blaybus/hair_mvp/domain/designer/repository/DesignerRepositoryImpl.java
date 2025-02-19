package blaybus.hair_mvp.domain.designer.repository;

import static blaybus.hair_mvp.domain.designer.entity.QDesigner.designer;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DesignerRepositoryImpl implements DesignerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Designer> searchDesigners(Integer page, Integer size, MeetingType meetingType, String styling,
                                          String region, String minPrice, String maxPrice) {
        BooleanBuilder builder = new BooleanBuilder();

        if (meetingType != MeetingType.BOTH) {
            builder.and(designer.meetingType.eq(meetingType));
        }
        if (styling != null && !styling.isEmpty()) {
            String[] stylingList = styling.split(",");
            BooleanExpression stylingCondition = null;

            for (String s : stylingList) {
                if (stylingCondition == null) {
                    stylingCondition = designer.styling.contains(s);
                } else {
                    stylingCondition = stylingCondition.or(designer.styling.contains(s));
                }
            }
            builder.and(stylingCondition);
        }
        if (region != null && !region.isEmpty()) {
            builder.and(designer.region.contains(region));
        }
        if (minPrice != null && maxPrice != null) {
            if (meetingType == MeetingType.OFFLINE) {
                builder.and(designer.offlineConsultFee.goe(Integer.parseInt(minPrice)));
                builder.and(designer.offlineConsultFee.loe(Integer.parseInt(maxPrice)));
            } else if (meetingType == MeetingType.ONLINE) {
                builder.and(designer.onlineConsultFee.goe(Integer.parseInt(minPrice))
                        .and(designer.onlineConsultFee.loe(Integer.parseInt(maxPrice))));
            } else if (meetingType == MeetingType.BOTH) {
                builder.and(designer.offlineConsultFee.goe(Integer.parseInt(minPrice))
                        .and(designer.onlineConsultFee.loe(Integer.parseInt(maxPrice))));
            }
        }

        return queryFactory
                .selectFrom(designer)
                .where(builder)
                .orderBy(designer.rating.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<Designer> findDesignerByConditions(int page, int size, String styling) {
        BooleanBuilder stylingCondition = buildStylingCondition(styling);
        return queryFactory
                .selectFrom(designer)
                .orderBy(
                        new CaseBuilder()
                                .when(stylingCondition).then(1)
                                .otherwise(2)
                                .asc(),
                        designer.rating.desc()
                )
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @Override
    public List<Designer> findDesignerByConditions(int page, int size, MeetingType meetingType, String styling) {
        BooleanBuilder stylingCondition = buildStylingCondition(styling);
        return queryFactory
                .selectFrom(designer)
                .orderBy(
                        new CaseBuilder()
                                .when((designer.meetingType.eq(meetingType)
                                        .or(designer.meetingType.eq(MeetingType.BOTH)))
                                        .and(stylingCondition)).then(1)
                                .when(stylingCondition).then(2)
                                .otherwise(3)
                                .asc(),
                        designer.rating.desc()
                )
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    @NotNull
    private static BooleanBuilder buildStylingCondition(String styling) {
        String[] stylings = styling.split(",");
        BooleanBuilder stylingCondition = new BooleanBuilder();
        for (String s : stylings) {
            stylingCondition.or(designer.styling.contains(s));
        }
        return stylingCondition;
    }
}
