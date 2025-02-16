package blaybus.hair_mvp.aws.s3.service;

import blaybus.hair_mvp.aws.s3.S3FileRepository;
import blaybus.hair_mvp.aws.s3.entity.S3File;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3FileService {
    private final S3FileRepository s3FileRepository;

    public void save(S3File s3File) {
        s3FileRepository.save(s3File);
    }

    public void delete(S3File s3File) {
        s3FileRepository.delete(s3File);
    }

    public S3File findById(UUID id) {
        return s3FileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
    }
}
