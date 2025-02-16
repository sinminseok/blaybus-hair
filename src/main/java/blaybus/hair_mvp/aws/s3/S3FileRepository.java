package blaybus.hair_mvp.aws.s3;

import blaybus.hair_mvp.aws.s3.entity.S3File;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRepository extends JpaRepository<S3File, UUID> {
}
