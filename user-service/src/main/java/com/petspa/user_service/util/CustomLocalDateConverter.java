package com.petspa.user_service.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public Class<LocalDate> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    // ✨ Đọc từ Excel -> Java (LocalDate)
    @Override
    public LocalDate convertToJavaData(ReadConverterContext<?> context) throws Exception {
        return LocalDate.parse(context.getReadCellData().getStringValue(), FORMATTER);
    }

    // ✨ Ghi từ Java -> Excel (String)
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<LocalDate> context) throws Exception {
        return new WriteCellData<>(FORMATTER.format(context.getValue()));
    }
}
