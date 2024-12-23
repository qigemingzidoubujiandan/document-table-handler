package com.libaixieshi.parse.domain;

import com.libaixieshi.parse.domain.Enum.FileTypeEnum;
import lombok.Data;

@SuppressWarnings("rawtypes")
@Data
public class WordTable extends Table {

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.WORD;
    }
}
