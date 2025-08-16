package journey.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * create a uuid + date file name
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/02/2025
 */
public class FileNameGenerator {
    public static String getNewFileName(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            throw new IllegalArgumentException("檔案副檔名不能為空");
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return uuid + "_" + date + "." + extension.toLowerCase();
    }
}
