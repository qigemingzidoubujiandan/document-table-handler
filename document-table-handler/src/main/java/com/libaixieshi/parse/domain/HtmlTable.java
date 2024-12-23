package com.libaixieshi.parse.domain;

import com.libaixieshi.parse.domain.Enum.FileTypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
@Data
public class HtmlTable extends Table {

    private List<Cell> thList = new ArrayList<>();
    private List<List<Cell>> tdList = new ArrayList<>();

    @Override
    public List<? extends Cell> getTh() {
        return thList;
    }

    @Override
    public FileTypeEnum source() {
        return FileTypeEnum.HTML;
    }

    @Override
    public List<List<Cell>> getData() {
        return tdList;
    }

    @Override
    public void setData(List<List<Cell>> data) {
       this.tdList = data;
    }
}