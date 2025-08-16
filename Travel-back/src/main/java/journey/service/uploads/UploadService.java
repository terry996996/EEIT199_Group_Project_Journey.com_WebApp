package journey.service.uploads;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import journey.dto.user.UploadIconDTO;
import journey.utils.FileNameGenerator;

/**
 * handling user uploads and save to project local directory, not to database
 * 
 * @author 乃文
 * @version 1.0
 * @since 07/02/2025
 */
@Service
public class UploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * user upload icons
     * 
     * @param uploadIcon the upload files received from controller
     * @param id         all_user entity id
     * @return DTO with the path to the uploaded file and it's mime type
     * @throws IOException
     */
    public UploadIconDTO storeIcon(MultipartFile uploadIcon, Integer id) throws IOException {
        UploadIconDTO dto = new UploadIconDTO();
        if (uploadIcon != null && id != null) {
            String avatarDirPath = uploadDir + "avatars/" + id;
            Path iconPath = Paths.get(avatarDirPath);

            // if uploads/avatars/username not exist then create it
            if (!Files.exists(iconPath)) {
                try {
                    Files.createDirectory(iconPath);
                } catch (IOException e) {
                    System.out.println("Error creating directory: " + avatarDirPath);
                    e.printStackTrace();
                }
            }

            // rename the filename to username_icon.${fileExtension}
            String originalName = uploadIcon.getOriginalFilename();
            String fileExtend = StringUtils.getFilenameExtension(originalName);
            if (fileExtend == null) {
                throw new IOException("Error saving file: Missing FileExtension!");
            } else {
                // uploads/avatars/id/uuid_yyyyMMdd_HHmmss.png
                String fileName = avatarDirPath + "/" + FileNameGenerator.getNewFileName(fileExtend);

                try (InputStream inStream = uploadIcon.getInputStream();) {
                    Files.copy(inStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
                    dto.setIconPath(fileName);
                } catch (IOException e) {
                    System.out.println("Error With Files.copy() in storeIcon()");
                    e.printStackTrace();
                }
            }
            String mimeType = uploadIcon.getContentType();
            dto.setMimeType(mimeType);

            return dto;
        }
        return null;
    }

}
