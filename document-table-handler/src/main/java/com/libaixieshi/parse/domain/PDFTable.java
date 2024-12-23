package com.libaixieshi.parse.domain;

import com.libaixieshi.parse.domain.Enum.FileTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * pdf 表格的属性
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("rawtypes")
@Data
public class PDFTable extends Table {

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.PDF;
    }

}
