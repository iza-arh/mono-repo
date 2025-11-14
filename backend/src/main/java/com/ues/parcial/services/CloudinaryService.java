package com.ues.parcial.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.ues.parcial.dtos.reportPhoto.ReportPhotoUploadResult;
import com.ues.parcial.exceptions.fileExceptions.CloudStorageException;
import com.ues.parcial.exceptions.fileExceptions.InvalidFileException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@SuppressWarnings("unchecked")
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("rawtypes")
    public ReportPhotoUploadResult uploadPhoto(MultipartFile multipartFile, UUID reportId) throws IOException {
        validateFile(multipartFile);

        Transformation<Transformation> thumbnail300 = createThumbnailConfig(300, 300);
        Transformation<Transformation> thumbnail150 = createThumbnailConfig(150, 150);

        List<Transformation<Transformation>> eagerTransformations = Arrays.asList(thumbnail300, thumbnail150);
        Map<String, Object> uploadOptions = createUploadOptions(eagerTransformations, reportId);

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), uploadOptions);
            return buildUploadResult(uploadResult);
        } catch (IOException e) {
            throw new CloudStorageException("Failed to upload image to cloud storage", e);
        }
    }

    @SuppressWarnings("rawtypes")
    public ReportPhotoUploadResult uploadPhoto(byte[] fileBytes, String originalFilename, UUID reportId) throws IOException {
        validateFileBytes(fileBytes);

        Transformation<Transformation> thumbnail300 = createThumbnailConfig(300, 300);
        Transformation<Transformation> thumbnail150 = createThumbnailConfig(150, 150);

        List<Transformation<Transformation>> eagerTransformations = Arrays.asList(thumbnail300, thumbnail150);
        Map<String, Object> uploadOptions = createUploadOptions(eagerTransformations, reportId);

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes, uploadOptions);
            return buildUploadResult(uploadResult);
        } catch (IOException e) {
            throw new CloudStorageException("Failed to upload image to cloud storage", e);
        }
    }

    public Map<String, Object> deletePhoto(String publicId) throws IOException {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            if (!"ok".equals(result.get("result"))) {
                throw new CloudStorageException("Failed to delete image from cloud storage, result was not 'ok'", null);
            }
            return result;
            
        } catch (IOException e) {
            throw new CloudStorageException("Failed to delete image from cloud storage", e);
        }
    }

    private ReportPhotoUploadResult buildUploadResult(Map<String, Object> cloudinaryResult) {
        String url = (String) cloudinaryResult.get("secure_url");
        String publicId = (String) cloudinaryResult.get("public_id");
        String format = (String) cloudinaryResult.get("format");
        Long sizeBytes = Long.parseLong(cloudinaryResult.get("bytes").toString());
        
        List<Map<String, Object>> eagerResults = (List<Map<String, Object>>) cloudinaryResult.get("eager");
        String thumbnailUrl = extractThumbnailUrl(eagerResults);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("width", cloudinaryResult.get("width"));
        metadata.put("height", cloudinaryResult.get("height"));
        metadata.put("public_id", publicId);
        metadata.put("format", format);
        metadata.put("resource_type", cloudinaryResult.get("resource_type"));
        metadata.put("created_at", cloudinaryResult.get("created_at"));
        
        if (eagerResults != null) {
            metadata.put("thumbnails", eagerResults);
        }

        return new ReportPhotoUploadResult(url, thumbnailUrl, publicId, "image/" + format, sizeBytes, metadata);
    }

    private String extractThumbnailUrl(List<Map<String, Object>> eagerResults) {
        if (eagerResults != null && !eagerResults.isEmpty()) {
            Map<String, Object> firstTransformation = eagerResults.get(0);
            return (String) firstTransformation.get("secure_url");
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private Transformation<Transformation> createThumbnailConfig(int width, int height) {
        return new Transformation<Transformation>()
                .width(width)
                .height(height)
                .crop("fill")
                .gravity("auto")
                .quality("auto");
    }

    private Map<String, Object> createUploadOptions(@SuppressWarnings("rawtypes") List<Transformation<Transformation>> eagerTransformations, UUID reportId) {
        Map<String, Object> options = new HashMap<>();
        options.put("folder", "UrbanoFix/reports/" + reportId.toString());
        options.put("resource_type", "image");
        options.put("eager", eagerTransformations);
        options.put("eager_async", false);
        options.put("quality", "auto");
        options.put("format", "jpg"); // convert all uploads to jpg
        return options;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File cannot be null or empty");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
            throw new InvalidFileException("File size exceeds maximum limit of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException("Only image files are allowed");
        }

        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");
        if (!allowedTypes.contains(contentType)) {
            throw new InvalidFileException("Unsupported image format. Allowed: JPEG, PNG, GIF, WEBP");
        }
    }

    private void validateFileBytes(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new InvalidFileException("File bytes cannot be null or empty");
        }

        if (fileBytes.length > 10 * 1024 * 1024) { // 10MB limit
            throw new InvalidFileException("File size exceeds maximum limit of 10MB");
        }
    }
}