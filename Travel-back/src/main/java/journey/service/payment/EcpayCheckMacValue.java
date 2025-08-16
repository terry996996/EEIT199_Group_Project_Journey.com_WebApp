package journey.service.payment;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

// 檢查碼計算與轉換邏輯
@Service
public class EcpayCheckMacValue {

    // 也可以改成從 application.properties 或 application.yml 或屬性檔 引入 HashKey 和 HashIV
    // @Value(${})
    // 這邊先直接寫在這裡

    private static final String HASH_KEY = "5294y06JbISpM5x9"; // 綠界測試用
    private static final String HASH_IV = "v77hoKGq4kWxNNIS"; // 綠界測試用

    public static String generateCheckMacValue(Map<String, String> params) {
        try {
            // 1. 使用 TreeMap 進行 A-Z 排序（排除 CheckMacValue 本身）
            Map<String, String> sortedParams = new TreeMap<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!"CheckMacValue".equalsIgnoreCase(entry.getKey()) && entry.getValue() != null) { // 忽略大小寫以防萬一
                    sortedParams.put(entry.getKey(), entry.getValue());
                }
            }

            // 2. 組合參數字串，值不需要在此階段進行 URL 編碼
            StringBuilder paramString = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
                if (!first) {
                    paramString.append("&");
                }
                paramString.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }

            // 3. 前後加上 HashKey 和 HashIV
            // 這是要進行整體 URL 編碼前的原始字串
            String rawStringForEncoding = "HashKey=" + HASH_KEY + "&" + paramString.toString() + "&HashIV=" + HASH_IV;
            System.out.println("準備整體 URL 編碼前字串: " + rawStringForEncoding);

            // 4. 對整個字串進行 URL 編碼
            // *注意：這裡的 URLEncoder.encode 會將空格轉為 '+'，以及編碼 '&' 和 '=' 等
            String urlEncodedString = URLEncoder.encode(rawStringForEncoding, "UTF-8");
            System.out.println("整體 URL 編碼後: " + urlEncodedString);

            // 5. 綠界特殊字元處理（關鍵步驟！）
            // 由於 URLEncoder.encode 會將 -, _, ., !, *, (, ) 這些字元編碼，
            // 綠界要求再將它們轉回原來的符號。
            // 同時，URLEncoder.encode 會將空格編碼為 %20，綠界要求轉為 '+'。
            String processedString = urlEncodedString
                    .replace("%2D", "-") // 替換 '-'
                    .replace("%5F", "_") // 替換 '_'
                    .replace("%2E", ".") // 替換 '.'
                    .replace("%21", "!") // 替換 '!'
                    .replace("%2A", "*") // 替換 '*'
                    .replace("%28", "(") // 替換 '('
                    .replace("%29", ")") // 替換 ')'
                    .replace("%20", "+"); // 替換空格為 '+'

            System.out.println("特殊字元處理後: " + processedString);

            // 6. 轉為小寫
            String lowerCaseString = processedString.toLowerCase();
            System.out.println("轉小寫: " + lowerCaseString);

            // 7. SHA256 加密
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(lowerCaseString.getBytes("UTF-8"));

            // 8. 轉為16進位大寫
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02X", b));
            }

            String checkMacValue = hexString.toString();
            System.out.println("最終 CheckMacValue: " + checkMacValue);

            return checkMacValue;

        } catch (Exception e) {
            System.err.println("CheckMacValue 產生錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("CheckMacValue 產生錯誤: " + e.getMessage(), e);
        }
    }
}