package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author VNElinpe
 * @since 2023-07-09
 */
@TableName("board_info")
public class BoardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 画板编号
     */
    private Long boardId;

    /**
     * 描述
     */
    private String description;

    /**
     * 类别
     */
    private String categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 喜欢数
     */
    private Integer likeCount;

    /**
     * 采集数
     */
    private Integer pinCount;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 最后更新时间
     */
    private Long updatedAt;
    /**
     * 原始json
     */
    private String rawJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getPinCount() {
        return pinCount;
    }

    public void setPinCount(Integer pinCount) {
        this.pinCount = pinCount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    @Override
    public String toString() {
        return "BoardInfo{" +
            "userId = " + userId +
            ", boardId = " + boardId +
            ", description = " + description +
            ", categoryId = " + categoryId +
            ", title = " + title +
            ", followCount = " + followCount +
            ", likeCount = " + likeCount +
            ", pinCount = " + pinCount +
            ", createdAt = " + createdAt +
            ", updatedAt = " + updatedAt +
        "}";
    }
}
