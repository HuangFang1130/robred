package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2017/1/5.16:42
 */
public class MessageDetailResult {


    /**
     * id : A01148412180661153071
     * messTitle : 您参与的一元夺宝开奖啦！
     * messType : 4
     * messCont : [第1期]飞科(FLYCO)FS372
     * memId : A01148410094915528699
     * manaId : b84e63f611bc11e69cad842b2b8fd827
     * yygImg : http://img1.jinhoubaobao.com/group1/M00/00/05/ezjIvVfO2ACAcHwsAACvXeIfR6w661.jpg
     * isRead : true
     * createDate : 2017-01-11 16:03:27
     * modifyDate : 2017-01-12 14:03:58
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
