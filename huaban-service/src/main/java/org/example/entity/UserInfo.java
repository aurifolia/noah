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
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId
    private Long userId;

    /**
     * 类别
     */
    private String type;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户关注他人的数量
     */
    private Integer userFollowingCount;

    /**
     * 该用户在huaban系统里的名称
     */
    private String urlname;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 采集数
     */
    private Integer pinCount;

    /**
     * 画板数
     */
    private Integer boardCount;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 粉丝数
     */
    private Integer followerCount;

    /**
     * 关注数
     */
    private Integer followingCount;

    /**
     * 喜欢数
     */
    private Integer likeCount;

    /**
     * 工作职位类别
     */
    private String job;

    /**
     * 工作职位名
     */
    private String jobName;

    /**
     * 工作地点
     */
    private String jobLocation;

    /**
     * 上次登录地点
     */
    private String lastLoginLocation;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 绑定的qq空间的用户名
     */
    private String qzoneUsername;

    /**
     * 绑定的qq空间的用户头像
     */
    private String qzoneAvatar;

    /**
     * 绑定的weibo的用户名
     */
    private String weiboUsername;

    /**
     * 绑定的weibo的用户头像
     */
    private String weiboAvatar;

    /**
     * 绑定的weibo的用户简介
     */
    private String weiboAbout;

    /**
     * 绑定的weibo的用户email
     */
    private String weiboEmail;

    /**
     * 绑定的weibo的用户性别
     */
    private String weiboGender;

    /**
     * 绑定的weibo的用户位置
     */
    private String weiboLocation;

    /**
     * 绑定的weibo的用户主页url
     */
    private String weiboUrl;
    /**
     * 原始json
     */
    private String rawJson;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserFollowingCount() {
        return userFollowingCount;
    }

    public void setUserFollowingCount(Integer userFollowingCount) {
        this.userFollowingCount = userFollowingCount;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }

    public Integer getPinCount() {
        return pinCount;
    }

    public void setPinCount(Integer pinCount) {
        this.pinCount = pinCount;
    }

    public Integer getBoardCount() {
        return boardCount;
    }

    public void setBoardCount(Integer boardCount) {
        this.boardCount = boardCount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getLastLoginLocation() {
        return lastLoginLocation;
    }

    public void setLastLoginLocation(String lastLoginLocation) {
        this.lastLoginLocation = lastLoginLocation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQzoneUsername() {
        return qzoneUsername;
    }

    public void setQzoneUsername(String qzoneUsername) {
        this.qzoneUsername = qzoneUsername;
    }

    public String getQzoneAvatar() {
        return qzoneAvatar;
    }

    public void setQzoneAvatar(String qzoneAvatar) {
        this.qzoneAvatar = qzoneAvatar;
    }

    public String getWeiboUsername() {
        return weiboUsername;
    }

    public void setWeiboUsername(String weiboUsername) {
        this.weiboUsername = weiboUsername;
    }

    public String getWeiboAvatar() {
        return weiboAvatar;
    }

    public void setWeiboAvatar(String weiboAvatar) {
        this.weiboAvatar = weiboAvatar;
    }

    public String getWeiboAbout() {
        return weiboAbout;
    }

    public void setWeiboAbout(String weiboAbout) {
        this.weiboAbout = weiboAbout;
    }

    public String getWeiboEmail() {
        return weiboEmail;
    }

    public void setWeiboEmail(String weiboEmail) {
        this.weiboEmail = weiboEmail;
    }

    public String getWeiboGender() {
        return weiboGender;
    }

    public void setWeiboGender(String weiboGender) {
        this.weiboGender = weiboGender;
    }

    public String getWeiboLocation() {
        return weiboLocation;
    }

    public void setWeiboLocation(String weiboLocation) {
        this.weiboLocation = weiboLocation;
    }

    public String getWeiboUrl() {
        return weiboUrl;
    }

    public void setWeiboUrl(String weiboUrl) {
        this.weiboUrl = weiboUrl;
    }

    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "userId = " + userId +
            ", userName = " + userName +
            ", userFollowingCount = " + userFollowingCount +
            ", urlname = " + urlname +
            ", tagCount = " + tagCount +
            ", pinCount = " + pinCount +
            ", boardCount = " + boardCount +
            ", createdAt = " + createdAt +
            ", followerCount = " + followerCount +
            ", followingCount = " + followingCount +
            ", likeCount = " + likeCount +
            ", job = " + job +
            ", jobName = " + jobName +
            ", jobLocation = " + jobLocation +
            ", lastLoginLocation = " + lastLoginLocation +
            ", sex = " + sex +
            ", avatar = " + avatar +
            ", qzoneUsername = " + qzoneUsername +
            ", qzoneAvatar = " + qzoneAvatar +
            ", weiboUsername = " + weiboUsername +
            ", weiboAvatar = " + weiboAvatar +
            ", weiboAbout = " + weiboAbout +
            ", weiboEmail = " + weiboEmail +
            ", weiboGender = " + weiboGender +
            ", weiboLocation = " + weiboLocation +
            ", weiboUrl = " + weiboUrl +
        "}";
    }
}
