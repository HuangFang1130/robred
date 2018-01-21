package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/28.17:07
 */
public class GrabDetailPeopleListResult {


    /**
     * result : success
     * data : {"grabRedEnveList":[{"shareIncomeTotalMoney":"0.00","mobileTerm":"null","phoneAreaName":"河北 保定","userPhoto":"http://img1.jinhoubaobao.com/group1/M00/00/00/dwozhlawM5WAYdrdAABCatO3X_A45.jpeg","divideMoney2":"0.00","modifyDate":"","divideMoney1":"0.00","channelSource":"2","redEnveDesc":"","mobileTermModel":"Simulator","grabMoney":"6.54","grabUser":"A01145438652011443124","id":"5df6e2bfcbe211e69d48842b2b8fd827","redEnveCode":"HBA01148280811207014536","nickName":"宝宝心里苦","finalMoney":"6.54","sendUser":"A01145438652011443124","redEnveType":false,"userName":"15933434904","createDate":"2016-12-27 11:12:50","isEnterUserWap":"","isEnterAdPage":"","flag":false,"grabUserProp":false,"shareLevel":"0","upperShareUser":"","sendUserProp":false,"phoneArea":"199","grabDate":"2016-12-27 11:12:50","totalMoney":"6.54"},{"shareIncomeTotalMoney":"0.00","mobileTerm":"null","phoneAreaName":"北京 北京","userPhoto":"http://192.168.0.106:8080/upload/image/20161226/6ac0fa94047a42909dc705b412a087d5.jpeg","divideMoney2":"0.00","modifyDate":"","divideMoney1":"0.00","channelSource":"2","redEnveDesc":"","mobileTermModel":"Simulator","grabMoney":"3.55","grabUser":"A01148213130517368397","id":"f7d5d197cbf511e69d48842b2b8fd827","redEnveCode":"HBA01148280811207014536","nickName":"Hhh","finalMoney":"3.55","sendUser":"A01145438652011443124","redEnveType":false,"userName":"18618348641","createDate":"2016-12-27 13:33:09","isEnterUserWap":"","isEnterAdPage":"","flag":false,"grabUserProp":false,"shareLevel":"0","upperShareUser":"","sendUserProp":false,"phoneArea":"1","grabDate":"2016-12-27 13:33:09","totalMoney":"3.55"}]}
     */

    private String result;
    private DataBean data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * shareIncomeTotalMoney : 0.00
         * mobileTerm : null
         * phoneAreaName : 河北 保定
         * userPhoto : http://img1.jinhoubaobao.com/group1/M00/00/00/dwozhlawM5WAYdrdAABCatO3X_A45.jpeg
         * divideMoney2 : 0.00
         * modifyDate :
         * divideMoney1 : 0.00
         * channelSource : 2
         * redEnveDesc :
         * mobileTermModel : Simulator
         * grabMoney : 6.54
         * grabUser : A01145438652011443124
         * id : 5df6e2bfcbe211e69d48842b2b8fd827
         * redEnveCode : HBA01148280811207014536
         * nickName : 宝宝心里苦
         * finalMoney : 6.54
         * sendUser : A01145438652011443124
         * redEnveType : false
         * userName : 15933434904
         * createDate : 2016-12-27 11:12:50
         * isEnterUserWap :
         * isEnterAdPage :
         * flag : false
         * grabUserProp : false
         * shareLevel : 0
         * upperShareUser :
         * sendUserProp : false
         * phoneArea : 199
         * grabDate : 2016-12-27 11:12:50
         * totalMoney : 6.54
         */

        private List<GrabRedEnveListBean> grabRedEnveList;

        public List<GrabRedEnveListBean> getGrabRedEnveList() {
            return grabRedEnveList;
        }

        public void setGrabRedEnveList(List<GrabRedEnveListBean> grabRedEnveList) {
            this.grabRedEnveList = grabRedEnveList;
        }

        public static class GrabRedEnveListBean {
            private String shareIncomeTotalMoney;
            private String mobileTerm;
            private String phoneAreaName;
            private String userPhoto;
            private String divideMoney2;
            private String modifyDate;
            private String divideMoney1;
            private String channelSource;
            private String redEnveDesc;
            private String mobileTermModel;
            private String grabMoney;
            private String grabUser;
            private String id;
            private String redEnveCode;
            private String nickName;
            private String finalMoney;
            private String sendUser;
            private boolean redEnveType;
            private String userName;
            private String createDate;
            private String isEnterUserWap;
            private String isEnterAdPage;
            private boolean flag;
            private boolean grabUserProp;
            private String shareLevel;
            private String upperShareUser;
            private boolean sendUserProp;
            private String phoneArea;
            private String grabDate;
            private String totalMoney;

            public String getShareIncomeTotalMoney() {
                return shareIncomeTotalMoney;
            }

            public void setShareIncomeTotalMoney(String shareIncomeTotalMoney) {
                this.shareIncomeTotalMoney = shareIncomeTotalMoney;
            }

            public String getMobileTerm() {
                return mobileTerm;
            }

            public void setMobileTerm(String mobileTerm) {
                this.mobileTerm = mobileTerm;
            }

            public String getPhoneAreaName() {
                return phoneAreaName;
            }

            public void setPhoneAreaName(String phoneAreaName) {
                this.phoneAreaName = phoneAreaName;
            }

            public String getUserPhoto() {
                return userPhoto;
            }

            public void setUserPhoto(String userPhoto) {
                this.userPhoto = userPhoto;
            }

            public String getDivideMoney2() {
                return divideMoney2;
            }

            public void setDivideMoney2(String divideMoney2) {
                this.divideMoney2 = divideMoney2;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public String getDivideMoney1() {
                return divideMoney1;
            }

            public void setDivideMoney1(String divideMoney1) {
                this.divideMoney1 = divideMoney1;
            }

            public String getChannelSource() {
                return channelSource;
            }

            public void setChannelSource(String channelSource) {
                this.channelSource = channelSource;
            }

            public String getRedEnveDesc() {
                return redEnveDesc;
            }

            public void setRedEnveDesc(String redEnveDesc) {
                this.redEnveDesc = redEnveDesc;
            }

            public String getMobileTermModel() {
                return mobileTermModel;
            }

            public void setMobileTermModel(String mobileTermModel) {
                this.mobileTermModel = mobileTermModel;
            }

            public String getGrabMoney() {
                return grabMoney;
            }

            public void setGrabMoney(String grabMoney) {
                this.grabMoney = grabMoney;
            }

            public String getGrabUser() {
                return grabUser;
            }

            public void setGrabUser(String grabUser) {
                this.grabUser = grabUser;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRedEnveCode() {
                return redEnveCode;
            }

            public void setRedEnveCode(String redEnveCode) {
                this.redEnveCode = redEnveCode;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getFinalMoney() {
                return finalMoney;
            }

            public void setFinalMoney(String finalMoney) {
                this.finalMoney = finalMoney;
            }

            public String getSendUser() {
                return sendUser;
            }

            public void setSendUser(String sendUser) {
                this.sendUser = sendUser;
            }

            public boolean isRedEnveType() {
                return redEnveType;
            }

            public void setRedEnveType(boolean redEnveType) {
                this.redEnveType = redEnveType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getIsEnterUserWap() {
                return isEnterUserWap;
            }

            public void setIsEnterUserWap(String isEnterUserWap) {
                this.isEnterUserWap = isEnterUserWap;
            }

            public String getIsEnterAdPage() {
                return isEnterAdPage;
            }

            public void setIsEnterAdPage(String isEnterAdPage) {
                this.isEnterAdPage = isEnterAdPage;
            }

            public boolean isFlag() {
                return flag;
            }

            public void setFlag(boolean flag) {
                this.flag = flag;
            }

            public boolean isGrabUserProp() {
                return grabUserProp;
            }

            public void setGrabUserProp(boolean grabUserProp) {
                this.grabUserProp = grabUserProp;
            }

            public String getShareLevel() {
                return shareLevel;
            }

            public void setShareLevel(String shareLevel) {
                this.shareLevel = shareLevel;
            }

            public String getUpperShareUser() {
                return upperShareUser;
            }

            public void setUpperShareUser(String upperShareUser) {
                this.upperShareUser = upperShareUser;
            }

            public boolean isSendUserProp() {
                return sendUserProp;
            }

            public void setSendUserProp(boolean sendUserProp) {
                this.sendUserProp = sendUserProp;
            }

            public String getPhoneArea() {
                return phoneArea;
            }

            public void setPhoneArea(String phoneArea) {
                this.phoneArea = phoneArea;
            }

            public String getGrabDate() {
                return grabDate;
            }

            public void setGrabDate(String grabDate) {
                this.grabDate = grabDate;
            }

            public String getTotalMoney() {
                return totalMoney;
            }

            public void setTotalMoney(String totalMoney) {
                this.totalMoney = totalMoney;
            }
        }
    }
}
