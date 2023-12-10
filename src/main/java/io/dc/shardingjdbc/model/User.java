package io.dc.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author dc on 2023/12/6
 */
public class User {
    private Long uid;
    private Integer sex;
    private Integer age;
    private String name;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", sex=" + sex +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

