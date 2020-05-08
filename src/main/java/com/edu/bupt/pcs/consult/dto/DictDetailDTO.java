package com.edu.bupt.pcs.consult.dto;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author: wzz
 * @date: 19-7-7 下午7:01
 * @description
 */
@Entity
@Table(name = "dict_detail", schema = "pcs")
public class DictDetailDTO {
    private int id;
    private String label;
    private String value;
    private String sort;
    private Integer dictId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Basic
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name = "sort")
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Basic
    @Column(name = "dict_id")
    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictDetailDTO that = (DictDetailDTO) o;
        return id == that.id &&
                Objects.equals(label, that.label) &&
                Objects.equals(value, that.value) &&
                Objects.equals(sort, that.sort) &&
                Objects.equals(dictId, that.dictId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, value, sort, dictId);
    }
}
