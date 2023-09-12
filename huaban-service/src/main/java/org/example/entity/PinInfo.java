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
@TableName("pin_info")
public class PinInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 画板id
     */
    private Long boardId;

    /**
     * pin_id
     */
    private Long pinId;

    /**
     * 来源
     */
    private String source;

    /**
     * 来源url
     */
    private String sourceUrl;

    /**
     * 原始标题
     */
    private String rawText;

    /**
     * 图片链接
     */
    private String fileUrl;

    /**
     * 转采数
     */
    private Integer repinCount;

    /**
     * 喜欢数
     */
    private Integer likeCount;

    /**
     * 转采自-用户编号
     */
    private Integer viaUserId;

    /**
     * 转采自-用户名
     */
    private String viaUsername;

    /**
     * 转采自-huaban系统内部的用户名
     */
    private String viaUrlname;

    /**
     * 转采自-用户头像
     */
    private String viaUserAvatar;

    /**
     * 创建时间戳(秒)
     */
    private Long createdAt;

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

    public Long getPinId() {
        return pinId;
    }

    public void setPinId(Long pinId) {
        this.pinId = pinId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRawText() {
        return rawText;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public Integer getRepinCount() {
        return repinCount;
    }

    public void setRepinCount(Integer repinCount) {
        this.repinCount = repinCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViaUserId() {
        return viaUserId;
    }

    public void setViaUserId(Integer viaUserId) {
        this.viaUserId = viaUserId;
    }

    public String getViaUsername() {
        return viaUsername;
    }

    public void setViaUsername(String viaUsername) {
        this.viaUsername = viaUsername;
    }

    public String getViaUrlname() {
        return viaUrlname;
    }

    public void setViaUrlname(String viaUrlname) {
        this.viaUrlname = viaUrlname;
    }

    public String getViaUserAvatar() {
        return viaUserAvatar;
    }

    public void setViaUserAvatar(String viaUserAvatar) {
        this.viaUserAvatar = viaUserAvatar;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    @Override
    public String toString() {
        return "PinInfo{" +
            "userId = " + userId +
            ", boardId = " + boardId +
            ", pinId = " + pinId +
            ", source = " + source +
            ", sourceUrl = " + sourceUrl +
            ", rawText = " + rawText +
            ", repinCount = " + repinCount +
            ", likeCount = " + likeCount +
            ", viaUserId = " + viaUserId +
            ", viaUsername = " + viaUsername +
            ", viaUrlname = " + viaUrlname +
            ", viaUserAvatar = " + viaUserAvatar +
        "}";
    }
}
