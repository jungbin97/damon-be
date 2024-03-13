package damon.backend.service;

import damon.backend.util.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final S3Client s3Client; // S3Client 주입

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.review-prefix}")
    private String reviewPrefix;

    public class UploadResult {
        private final String fileKey;
        private final String fileUrl;

        public UploadResult(String fileKey, String fileUrl) {
            this.fileKey = fileKey;
            this.fileUrl = fileUrl;
        }

        public String getFileKey() {
            return fileKey;
        }

        public String getFileUrl() {
            return fileUrl;
        }
    }

//    public UploadResult uploadImage(MultipartFile multipartFile) throws IOException {
//        String fileName = multipartFile.getOriginalFilename();
//        String extension = fileName.substring(fileName.lastIndexOf("."));
//        String fileKey = reviewPrefix + UUID.randomUUID().toString() + extension;
//
//        s3Client.putObject(PutObjectRequest.builder()
//                        .bucket(bucket)
//                        .key(fileKey)
//                        .build(),
//                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
//
//        String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileKey)).toString();
//
//        return new UploadResult(fileKey, fileUrl);
//    }
//
//    // 3S 내 이미지 삭제
//    public void deleteImage(String fileKey) {
//        s3Client.deleteObject(DeleteObjectRequest.builder()
//                .bucket(bucket)
//                .key(fileKey)
//                .build());
//    }


    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String uuidFileName = UUID.randomUUID().toString() + ext;

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(reviewPrefix + uuidFileName)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        String result = s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(reviewPrefix + uuidFileName)).toString();
        Log.info("AwsS3Service uploadImage return:" + result);
        return result;
    }

    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String imageUrl = uploadImage(file);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    // AwsS3Service 내 이미지 삭제 메서드
    public void deleteImage(String fileKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build());
    }


}