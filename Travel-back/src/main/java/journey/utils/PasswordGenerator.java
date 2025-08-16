package journey.utils;

/**
 * 密碼生成工具類
 * 用於生成admin用戶的密碼hash
 * 
 * @author 系統管理員
 * @version 1.0
 * @since 2025-01-07
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        // 生成admin123密碼的hash
        String password = "admin123";
        String hash = PasswordHashUtils.getPasswordHash(password);
        
        System.out.println("=== 密碼Hash生成工具 ===");
        System.out.println("原始密碼: " + password);
        System.out.println("生成的Hash: " + hash);
        System.out.println();
        System.out.println("=== SQL插入語句 ===");
        System.out.println("請將以下SQL語句執行到資料庫中：");
        System.out.println();
        System.out.println("USE EEIT199travel;");
        System.out.println();
        System.out.println("-- 確保user_types表有admin類型");
        System.out.println("IF NOT EXISTS (SELECT * FROM user_types WHERE [type] = 'admin')");
        System.out.println("BEGIN");
        System.out.println("    INSERT INTO user_types([type]) VALUES ('admin');");
        System.out.println("END");
        System.out.println();
        System.out.println("-- 插入admin用戶");
        System.out.println("IF NOT EXISTS (SELECT * FROM all_users WHERE username = 'admin')");
        System.out.println("BEGIN");
        System.out.println("    INSERT INTO all_users (username, password_hash, email, icon, icon_type, user_type, created_at, last_modified)");
        System.out.println("    VALUES (");
        System.out.println("        'admin',");
        System.out.println("        '" + hash + "',");
        System.out.println("        'admin@journey.com',");
        System.out.println("        'uploads/avatars/default/user-default.svg',");
        System.out.println("        'image/svg+xml',");
        System.out.println("        2, -- admin user_type id");
        System.out.println("        GETDATE(),");
        System.out.println("        GETDATE()");
        System.out.println("    );");
        System.out.println("END");
        System.out.println();
        System.out.println("-- 插入admin詳細資料");
        System.out.println("IF NOT EXISTS (SELECT * FROM admins WHERE admin_id = (SELECT id FROM all_users WHERE username = 'admin'))");
        System.out.println("BEGIN");
        System.out.println("    INSERT INTO admins (admin_id, [role])");
        System.out.println("    VALUES (");
        System.out.println("        (SELECT id FROM all_users WHERE username = 'admin'),");
        System.out.println("        '超級管理者'");
        System.out.println("    );");
        System.out.println("END");
        System.out.println();
        System.out.println("-- 驗證插入結果");
        System.out.println("SELECT 'Admin User Created:' as info;");
        System.out.println("SELECT ");
        System.out.println("    au.id,");
        System.out.println("    au.username,");
        System.out.println("    au.email,");
        System.out.println("    au.user_type,");
        System.out.println("    ut.[type] as user_type_name,");
        System.out.println("    a.[role] as admin_role");
        System.out.println("FROM all_users au");
        System.out.println("JOIN user_types ut ON au.user_type = ut.id");
        System.out.println("LEFT JOIN admins a ON au.id = a.admin_id");
        System.out.println("WHERE au.username = 'admin';");
    }
} 