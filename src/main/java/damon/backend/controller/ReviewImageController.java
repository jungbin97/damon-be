package damon.backend.controller;

import damon.backend.service.AwsS3Service;
import damon.backend.util.Log;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "리뷰 이미지 API", description = "리뷰 이미지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewImageController {

    private final AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public List<String> imageUpload(@RequestParam("images") List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = awsS3Service.uploadImage(image);
                    imageUrls.add(imageUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return imageUrls;
    }

        //        try {
//            String result = awsS3Service.uploadImage(image);
//            Log.info(result);
//            return result;
//        } catch (IOException e) {
//            Log.info("ReviewImageController imageUpload에서 예외 발생");
//            throw new RuntimeException(e);
//        }

//        Map<String, Object> responseData = new HashMap<>();
//
//        if (file.isEmpty()) {
//            responseData.put("uploaded", false);
//            responseData.put("error", Map.of("message", "Cannot upload empty file"));
//            return ResponseEntity.badRequest().body(responseData);
//        }
//
//        try {
//            String s3Url = awsS3Service.uploadImage(file);
//            responseData.put("uploaded", true);
//            responseData.put("url", s3Url);
//        } catch (IOException e) {
//            responseData.put("uploaded", false);
//            responseData.put("error", Map.of("message", "Could not upload image: " + e.getMessage()));
//        }
//        return ResponseEntity.internalServerError().body(responseData);
//    }
}