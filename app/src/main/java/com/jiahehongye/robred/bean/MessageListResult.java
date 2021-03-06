package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/29.16:34
 */
public class MessageListResult {


    /**
     * id : A01148290754525783260
     * messTitle : 系统消息
     * messType : 1
     * messCont : 大家好
     * memId : A01148186009079368501
     * manaId :
     * yygImg :
     * isRead : true
     * createDate : 2016-12-28 14:41:32
     * modifyDate : 2016-12-29 16:34:08
     * flag : false
     */

    private List<FindMessageBean> findMessage;

    public List<FindMessageBean> getFindMessage() {
        return findMessage;
    }

    public void setFindMessage(List<FindMessageBean> findMessage) {
        this.findMessage = findMessage;
    }

    public static class FindMessageBean {
        private String id;
        private String messTitle;
        private String messType;
        private String messCont;
        private String memId;
        private String manaId;
        private String yygImg;
        private boolean isRead;
        private String createDate;
        private String modifyDate;
        private boolean flag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessTitle() {
            return messTitle;
        }

        public void setMessTitle(String messTitle) {
            this.messTitle = messTitle;
        }

        public String getMessType() {
            return messType;
        }

        public void setMessType(String messType) {
            this.messType = messType;
        }

        public String getMessCont() {
            return messCont;
        }

        public void setMessCont(String messCont) {
            this.messCont = messCont;
        }

        public String getMemId() {
            return memId;
        }

        public void setMemId(String memId) {
            this.memId = memId;
        }

        public String getManaId() {
            return manaId;
        }

        public void setManaId(String manaId) {
            this.manaId = manaId;
        }

        public String getYygImg() {
            return yygImg;
        }

        public void setYygImg(String yygImg) {
            this.yygImg = yygImg;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }
}
