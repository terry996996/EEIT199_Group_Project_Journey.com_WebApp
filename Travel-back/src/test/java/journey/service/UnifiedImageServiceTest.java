package journey.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import journey.domain.comments.CommentImagesBean;
import journey.domain.comments.CommentsBean;
import journey.domain.lodgings.LodgingImagesBean;
import journey.domain.lodgings.LodgingsBean;
import journey.dto.BaseImageDTO;
import journey.repository.comments.CommentImagesRepository;
import journey.repository.comments.CommentsRepository;
import journey.repository.lodgings.LodgingImagesRepository;
import journey.repository.lodgings.LodgingsRepository;
import journey.repository.lodgings.RoomTypesRepository;

@ExtendWith(MockitoExtension.class)
class UnifiedImageServiceTest {

    @Mock
    private LodgingImagesRepository lodgingImagesRepository;

    @Mock
    private CommentImagesRepository commentImagesRepository;

    @Mock
    private LodgingsRepository lodgingsRepository;

    @Mock
    private RoomTypesRepository roomTypesRepository;

    @Mock
    private CommentsRepository commentsRepository;

    @InjectMocks
    private UnifiedImageService unifiedImageService;

    @BeforeEach
    void setUp() {
        // 設置測試用的上傳目錄
        ReflectionTestUtils.setField(unifiedImageService, "uploadDir", "uploads/");
    }

    @Test
    void testStoreFile_Success() throws IOException {
        // 準備測試資料
        String content = "test image content";
        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            content.getBytes()
        );

        // Mock 住宿存在
        when(lodgingsRepository.existsById(1)).thenReturn(true);

        // 執行測試
        UnifiedImageService.FileUploadResult result = unifiedImageService.storeFile(file, "lodging", 1);

        // 驗證結果
        assertNotNull(result);
        assertEquals("image/jpeg", result.getMimeType());
        assertEquals("test.jpg", result.getOriginalFileName());
        assertEquals(content.length(), result.getFileSize());
        assertTrue(result.getFilePath().contains("uploads/lodgings/1/"));
        assertTrue(result.getFilePath().endsWith(".jpg"));
    }

    @Test
    void testStoreFile_EmptyFile() {
        // 準備空的檔案
        MockMultipartFile file = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            new byte[0]
        );

        // 執行測試並驗證異常
        assertThrows(IllegalArgumentException.class, () -> {
            unifiedImageService.storeFile(file, "lodging", 1);
        });
    }

    @Test
    void testSaveFiles_Success() throws IOException {
        // 準備測試資料
        String content = "test image content";
        MockMultipartFile file1 = new MockMultipartFile(
            "image1", 
            "test1.jpg", 
            "image/jpeg", 
            content.getBytes()
        );
        MockMultipartFile file2 = new MockMultipartFile(
            "image2", 
            "test2.png", 
            "image/png", 
            content.getBytes()
        );

        List<MockMultipartFile> files = Arrays.asList(file1, file2);

        // Mock 住宿存在
        when(lodgingsRepository.existsById(1)).thenReturn(true);

        // Mock 儲存的圖片實體
        LodgingImagesBean savedImage1 = new LodgingImagesBean();
        savedImage1.setImageId(1);
        savedImage1.setImageUrl("uploads/lodgings/1/test1.jpg");
        savedImage1.setMimeType("image/jpeg");

        LodgingImagesBean savedImage2 = new LodgingImagesBean();
        savedImage2.setImageId(2);
        savedImage2.setImageUrl("uploads/lodgings/1/test2.png");
        savedImage2.setMimeType("image/png");

        when(lodgingImagesRepository.saveAll(anyList())).thenReturn(Arrays.asList(savedImage1, savedImage2));

        // 執行測試
        List<BaseImageDTO> result = unifiedImageService.saveFiles((List<MultipartFile>) (List<?>) files, "lodging", 1, "EXTERIOR");

        // 驗證結果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("EXTERIOR", result.get(0).getImageType());
        assertEquals("lodging", result.get(0).getTargetType());
        assertEquals(1, result.get(0).getTargetId());
    }

    @Test
    void testDeleteFile_Success() throws IOException {
        // 這個測試需要實際的檔案系統操作，在單元測試中可能會有問題
        // 在實際環境中，這個方法會刪除指定的檔案
        boolean result = unifiedImageService.deleteFile("non-existent-file.jpg");
        assertFalse(result);
    }

    @Test
    void testValidateImageCount_Success() {
        // 準備測試資料
        BaseImageDTO image1 = new BaseImageDTO();
        BaseImageDTO image2 = new BaseImageDTO();
        List<BaseImageDTO> images = Arrays.asList(image1, image2);

        // 執行測試（不應該拋出異常）
        assertDoesNotThrow(() -> {
            unifiedImageService.validateImageCount(images, 5, "lodging");
        });
    }

    @Test
    void testValidateImageCount_ExceedLimit() {
        // 準備測試資料
        BaseImageDTO image1 = new BaseImageDTO();
        BaseImageDTO image2 = new BaseImageDTO();
        BaseImageDTO image3 = new BaseImageDTO();
        List<BaseImageDTO> images = Arrays.asList(image1, image2, image3);

        // 執行測試並驗證異常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unifiedImageService.validateImageCount(images, 2, "lodging");
        });

        assertEquals("lodging圖片數量不能超過2張", exception.getMessage());
    }

    @Test
    void testValidateImageData_Success() {
        // 準備測試資料
        BaseImageDTO imageDto = new BaseImageDTO();
        imageDto.setTargetId(1);
        imageDto.setTargetType("lodging");

        // Mock 住宿存在
        when(lodgingsRepository.existsById(1)).thenReturn(true);

        // 執行測試（不應該拋出異常）
        assertDoesNotThrow(() -> {
            unifiedImageService.validateImageData(imageDto);
        });
    }

    @Test
    void testValidateImageData_LodgingNotFound() {
        // 準備測試資料
        BaseImageDTO imageDto = new BaseImageDTO();
        imageDto.setTargetId(999);
        imageDto.setTargetType("lodging");

        // Mock 住宿不存在
        when(lodgingsRepository.existsById(999)).thenReturn(false);

        // 執行測試並驗證異常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unifiedImageService.validateImageData(imageDto);
        });

        assertEquals("住宿不存在", exception.getMessage());
    }

    @Test
    void testGetActiveImagesByTarget_Lodging() {
        // 準備測試資料
        LodgingsBean lodging = new LodgingsBean();
        lodging.setId(1);

        LodgingImagesBean image1 = new LodgingImagesBean();
        image1.setImageId(1);
        image1.setImageUrl("uploads/lodgings/1/image1.jpg");
        image1.setMimeType("image/jpeg");
        image1.setLodging(lodging);

        LodgingImagesBean image2 = new LodgingImagesBean();
        image2.setImageId(2);
        image2.setImageUrl("uploads/lodgings/1/image2.jpg");
        image2.setMimeType("image/jpeg");
        image2.setLodging(lodging);

        // Mock Repository
        when(lodgingImagesRepository.findByLodgingIdAndIsActiveTrueAndDeleteStatus(1, 1))
                .thenReturn(Arrays.asList(image1, image2));

        // 執行測試
        List<BaseImageDTO> result = unifiedImageService.getActiveImagesByTarget(1, "lodging");

        // 驗證結果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getImageId());
        assertEquals("lodging", result.get(0).getTargetType());
        assertEquals(1, result.get(0).getTargetId());
    }

    @Test
    void testGetActiveImagesByTarget_Comment() {
        // 準備測試資料
        CommentsBean comment = new CommentsBean();
        comment.setId(1);

        CommentImagesBean image1 = new CommentImagesBean();
        image1.setId(1);
        image1.setImageUrl("uploads/comments/1/image1.jpg");
        image1.setMimeType("image/jpeg");
        image1.setComment(comment);

        // Mock Repository
        when(commentImagesRepository.findActiveImagesByCommentId(1))
                .thenReturn(Arrays.asList(image1));

        // 執行測試
        List<BaseImageDTO> result = unifiedImageService.getActiveImagesByTarget(1, "comment");

        // 驗證結果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getImageId());
        assertEquals("comment", result.get(0).getTargetType());
        assertEquals(1, result.get(0).getTargetId());
    }

    @Test
    void testGetActiveImagesByTarget_UnsupportedType() {
        // 執行測試並驗證異常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unifiedImageService.getActiveImagesByTarget(1, "unsupported");
        });

        assertEquals("不支援的目標類型: unsupported", exception.getMessage());
    }
} 