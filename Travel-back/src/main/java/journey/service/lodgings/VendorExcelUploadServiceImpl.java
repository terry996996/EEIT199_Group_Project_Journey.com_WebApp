package journey.service.lodgings;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import journey.dto.lodgings.LodgingRequestDTO;
import journey.dto.lodgings.RoomTypeCreateRequestDTO;

@Service
public class VendorExcelUploadServiceImpl implements VendorExcelUploadService {
    @Autowired
    private VendorLodgingService vendorLodgingService;
    @Autowired
    private VendorRoomTypeService vendorRoomTypeService;

    @Override
    @Transactional
    public Object importLodgingsExcel(MultipartFile file, String type, Integer vendorId) throws Exception {
        List<String> results = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            // 1. 解析 lodgings sheet
            Sheet lodgingSheet = workbook.getSheet("lodgings");
            if (lodgingSheet != null) {
                List<LodgingRequestDTO> lodgingDTOs = parseLodgingSheet(lodgingSheet);
                for (LodgingRequestDTO dto : lodgingDTOs) {
                    try {
                        vendorLodgingService.createLodging(dto, vendorId);
                        results.add("旅宿: " + dto.getLodgingName() + " 匯入成功");
                    } catch (Exception e) {
                        results.add("旅宿: " + dto.getLodgingName() + " 匯入失敗: " + e.getMessage());
                    }
                }
            } else {
                results.add("未找到 lodgings 工作表");
            }
            // 2. 解析 room_types sheet
            Sheet roomTypeSheet = workbook.getSheet("room_types");
            if (roomTypeSheet != null) {
                List<RoomTypeCreateRequestDTO> roomTypeDTOs = parseRoomTypeSheet(roomTypeSheet);
                for (RoomTypeCreateRequestDTO dto : roomTypeDTOs) {
                    try {
                        vendorRoomTypeService.createRoomType(dto);
                        results.add("房型: " + dto.getRoomTypeName() + " 匯入成功");
                    } catch (Exception e) {
                        results.add("房型: " + dto.getRoomTypeName() + " 匯入失敗: " + e.getMessage());
                    }
                }
            } else {
                results.add("未找到 room_types 工作表");
            }
            // TODO: 解析 images, beds, facilities, availability sheet 並寫入
        }
        return results;
    }

    private List<LodgingRequestDTO> parseLodgingSheet(Sheet sheet) {
        List<LodgingRequestDTO> list = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) return list;
        Row header = rowIterator.next();
        Map<String, Integer> colIdx = new HashMap<>();
        for (Cell cell : header) {
            colIdx.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(colIdx.get("lodging_name")) == null) continue;
            LodgingRequestDTO dto = new LodgingRequestDTO();
            dto.setLodgingName(getString(row, colIdx.get("lodging_name")));
            dto.setLodgingTypeId(getInt(row, colIdx.get("lodging_type_id")));
            dto.setDescription(getString(row, colIdx.get("description")));
            dto.setAddress(getString(row, colIdx.get("address")));
            dto.setCityId(getInt(row, colIdx.get("city_id")));
            dto.setCountryId(getInt(row, colIdx.get("country_id")));
            dto.setLatitude(getBigDecimal(row, colIdx.get("latitude")));
            dto.setLongitude(getBigDecimal(row, colIdx.get("longitude")));
            dto.setLodgingTel(getString(row, colIdx.get("lodging_tel")));
            dto.setEmail(getString(row, colIdx.get("email")));
            dto.setIsActive(getBoolean(row, colIdx.get("is_active")));
            // 圖片、設施等可後續擴充
            list.add(dto);
        }
        return list;
    }

    private List<RoomTypeCreateRequestDTO> parseRoomTypeSheet(Sheet sheet) {
        List<RoomTypeCreateRequestDTO> list = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) return list;
        Row header = rowIterator.next();
        Map<String, Integer> colIdx = new HashMap<>();
        for (Cell cell : header) {
            colIdx.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(colIdx.get("room_type_name")) == null) continue;
            RoomTypeCreateRequestDTO dto = new RoomTypeCreateRequestDTO();
            dto.setRoomTypeName(getString(row, colIdx.get("room_type_name")));
            dto.setDescription(getString(row, colIdx.get("description")));
            dto.setPricePerNight(getInt(row, colIdx.get("price_per_night")));
            dto.setMaxGuests(getInt(row, colIdx.get("max_guests")));
            dto.setIsActive(getBoolean(row, colIdx.get("is_active")));
            dto.setLodgingId(getInt(row, colIdx.get("lodging_id")));
            // 床型、設施、圖片等可後續擴充
            list.add(dto);
        }
        return list;
    }

    // 工具方法
    private String getString(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        return cell == null ? null : cell.toString().trim();
    }
    private Integer getInt(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        try { return Integer.parseInt(cell.toString().trim()); } catch (Exception e) { return null; }
    }
    private BigDecimal getBigDecimal(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return BigDecimal.valueOf(cell.getNumericCellValue());
        try { return new BigDecimal(cell.toString().trim()); } catch (Exception e) { return null; }
    }
    private Boolean getBoolean(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.BOOLEAN) return cell.getBooleanCellValue();
        String val = cell.toString().trim();
        return "1".equals(val) || "true".equalsIgnoreCase(val);
    }
} 