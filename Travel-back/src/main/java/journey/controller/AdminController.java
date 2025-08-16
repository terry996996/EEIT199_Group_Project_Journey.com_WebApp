package journey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import journey.dto.admin.CreateVendorRequestDTO;
import journey.dto.admin.CreateVendorResponseDTO;
import journey.dto.admin.VendorListDTO;
import journey.dto.admin.UserListDTO;
import journey.service.user.VendorService;
import journey.service.user.AllUserService;

/**
 * 管理員控制器
 * 
 * @author Max
 * @since 2025-07-08
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AllUserService allUserService;

    /**
     * 創建商家帳號
     */
    @PostMapping("/create-vendor")
    public ResponseEntity<CreateVendorResponseDTO> createVendor(@RequestBody CreateVendorRequestDTO request) {
        try {
            CreateVendorResponseDTO response = vendorService.createVendor(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CreateVendorResponseDTO errorResponse = new CreateVendorResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 獲取所有商家列表
     */
    @GetMapping("/vendors")
    public ResponseEntity<List<VendorListDTO>> getAllVendors() {
        try {
            List<VendorListDTO> vendors = vendorService.getAllVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 獲取所有用戶列表
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        try {
            List<UserListDTO> users = allUserService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 刪除商家
     */
    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
        try {
            vendorService.deleteVendor(id);
            return ResponseEntity.ok("商家刪除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("刪除失敗: " + e.getMessage());
        }
    }

    /**
     * 刪除用戶
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            allUserService.deleteUser(id);
            return ResponseEntity.ok("用戶刪除成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("刪除失敗: " + e.getMessage());
        }
    }
} 