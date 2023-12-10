package io.dc.shardingjdbc.model;

import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author dc on 2023/12/6
 */
public class Message {
    /**
     * 消息id
     */
    @TableId
    private Long mid;
    /**
     * 消息文本
     */
    private String body;
    /**
     * 消息类型
     */
    private Long type;
    /**
     * 消息创建时间
     */
    private Long createdTime;
    private String createdTimeStr;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getCreatedTimeStr() {
        return createdTimeStr;
    }

    public void setCreatedTimeStr(String createdTimeStr) {
        this.createdTimeStr = createdTimeStr;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", body='" + body + '\'' +
                ", type=" + type +
                ", createdTime=" + createdTime +
                ", createdTimeStr='" + createdTimeStr + '\'' +
                '}';
    }
}

