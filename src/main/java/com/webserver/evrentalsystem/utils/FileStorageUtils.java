package com.webserver.evrentalsystem.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileStorageUtils {

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được rỗng");
        }

        // Tạo thư mục uploads nếu chưa tồn tại
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }

        // Lấy tên file gốc
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Sinh tên file random
        String filename = UUID.randomUUID() + extension;

        // Đường dẫn lưu file
        Path filePath = UPLOAD_DIR.resolve(filename);

        // Ghi file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về URL (giả sử bạn serve file từ /uploads/)
        return "/uploads/" + filename;
    }
}

