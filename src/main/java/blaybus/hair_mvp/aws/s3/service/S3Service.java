package blaybus.hair_mvp.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadImage(String filePath, UUID uuid, MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return uploadFile(filePath, uuid, file, metadata);
    }

    private String uploadFile(String filePath, UUID uuid, MultipartFile file, ObjectMetadata metadata) {
        String fileName = filePath + uuid + "_" + file.getOriginalFilename();

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패", e);
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
