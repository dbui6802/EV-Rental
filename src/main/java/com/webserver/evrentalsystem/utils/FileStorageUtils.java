package com.webserver.evrentalsystem.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileStorageUtils {

    private static final Path UPLOAD_DIR = Paths.get("uploads");
    private static final Path PUBLIC_UPLOAD_DIR = Paths.get("public", "uploads");

    public static String saveFile(MultipartFile file) throws IOException {
        return saveToDirectory(file, UPLOAD_DIR, "/uploads/");
    }

    public static String savePublicFile(MultipartFile file) throws IOException {
        return saveToDirectory(file, PUBLIC_UPLOAD_DIR, "/public/uploads/");
    }

    private static String saveToDirectory(MultipartFile file, Path directory, String urlPrefix) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng");
        }

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Lấy phần mở rộng
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Sinh tên file ngẫu nhiên
        String filename = UUID.randomUUID() + extension;

        // Ghi file
        Path filePath = directory.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về URL tương ứng
        return urlPrefix + filename;
    }
}

