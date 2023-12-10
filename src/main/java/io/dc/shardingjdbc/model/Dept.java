package io.dc.shardingjdbc.model;

/**
 * @author dc on 2023/12/6
 */
public class Dept {
    private Long did;
    private Integer userNum;
    private String name;

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "did=" + did +
                ", userNum=" + userNum +
                ", name='" + name + '\'' +
                '}';
    }
}