package blaybus.hair_mvp.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1053485015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final blaybus.hair_mvp.domain.common.QBaseTimeEntity _super = new blaybus.hair_mvp.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final blaybus.hair_mvp.aws.s3.entity.QS3File file;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath profileImage = createString("profileImage");

    public final ListPath<blaybus.hair_mvp.domain.reservation.entity.Reservation, blaybus.hair_mvp.domain.reservation.entity.QReservation> reservations = this.<blaybus.hair_mvp.domain.reservation.entity.Reservation, blaybus.hair_mvp.domain.reservation.entity.QReservation>createList("reservations", blaybus.hair_mvp.domain.reservation.entity.Reservation.class, blaybus.hair_mvp.domain.reservation.entity.QReservation.class, PathInits.DIRECT2);

    public final ListPath<blaybus.hair_mvp.domain.review.entity.Review, blaybus.hair_mvp.domain.review.entity.QReview> reviews = this.<blaybus.hair_mvp.domain.review.entity.Review, blaybus.hair_mvp.domain.review.entity.QReview>createList("reviews", blaybus.hair_mvp.domain.review.entity.Review.class, blaybus.hair_mvp.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new blaybus.hair_mvp.aws.s3.entity.QS3File(forProperty("file")) : null;
    }

}

