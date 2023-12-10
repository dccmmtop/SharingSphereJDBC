package io.dc.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author dc on 2023/12/6
 */
public class Dict {
    @TableId
    private Long did;
    private Integer type;
    private Integer keyword;
    private String value;

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getKeyword() {
        return keyword;
    }

    public void setKeyword(Integer keyword) {
        this.keyword = keyword;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Dict{" +
                "did=" + did +
                ", type=" + type +
                ", keyword=" + keyword +
                ", value='" + value + '\'' +
                '}';
    }
}

