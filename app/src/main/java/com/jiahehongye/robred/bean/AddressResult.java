package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2016/12/23.15:52
 */
public class AddressResult {


    /**
     * result : success
     * data : [{"recentTime":"刚刚","id":"A01145450322617741456","distance":"在火星","personalDescription":"","nickName":"hb79641","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:37:29","mobile":"15640509852"},{"recentTime":"刚刚","id":"A01148412582567448763","distance":"0.01km","personalDescription":"","nickName":"嗷嗷嗷～","GENDER":"1","userLevel":"1","longitude":"116.394182","latitude":"39.967546","USER_PHOTO":"http://img1.jinhoubaobao.com/group1/M00/00/08/ezjIvViAgTKAO0YAAAEAAE2fHK461.jpeg","RECE_LOGIN_DATE":"2017-01-20 16:37:28","mobile":"18810512346"},{"recentTime":"刚刚","id":"A01145843631670486834","distance":"在火星","personalDescription":"","nickName":"hb81197","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:37:11","mobile":"13843774680"},{"recentTime":"刚刚","id":"A01147064618696329065","distance":"在火星","personalDescription":"","nickName":"hb54394","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"http://img1.jinhoubaobao.com/group1/M00/00/06/ezjIvVfi-hOAHDs3AAC5GCXyJjw740.jpg","RECE_LOGIN_DATE":"2017-01-20 16:37:01","mobile":"17707855030"},{"recentTime":"刚刚","id":"A01147966153222971459","distance":"在火星","personalDescription":"","nickName":"hb24813","GENDER":"1","userLevel":"1","longitude":"0.0","latitude":"0.0","USER_PHOTO":"http://img1.jinhoubaobao.com/group1/M00/00/06/ezjIvVhSnQ6ACwKQAADWi_ytwjY431.jpg","RECE_LOGIN_DATE":"2017-01-20 16:34:45","mobile":"13226169970"},{"recentTime":"刚刚","id":"A01145753800594559583","distance":"在火星","personalDescription":"","nickName":"hb99410","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:34:14","mobile":"13304372199"},{"recentTime":"刚刚","id":"A01145942293885988700","distance":"在火星","personalDescription":"","nickName":"hb07973","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:31:12","mobile":"13664599050"},{"recentTime":"刚刚","id":"A01147998463496632337","distance":"0.03km","personalDescription":"Qwe","nickName":"双子座","GENDER":"1","userLevel":"1","longitude":"116.39436","latitude":"39.96736","USER_PHOTO":"http://img1.jinhoubaobao.com/group1/M00/00/06/ezjIvVhfgQSAYBsTAAAlgHSRpss87.jpeg","RECE_LOGIN_DATE":"2017-01-20 16:30:38","mobile":"18618348641"},{"recentTime":"刚刚","id":"A01147959124927096545","distance":"522.50km","personalDescription":"","nickName":"如果我爱你","GENDER":"2","userLevel":"1","longitude":"116.714443","latitude":"35.280734","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:27:47","mobile":"15864148571"},{"recentTime":"刚刚","id":"A01146181811465627424","distance":"在火星","personalDescription":"","nickName":"hb68309","GENDER":"0","userLevel":"1","longitude":"","latitude":"","USER_PHOTO":"","RECE_LOGIN_DATE":"2017-01-20 16:26:39","mobile":"13351562391"}]
     */

    private String result;
    /**
     * recentTime : 刚刚
     * id : A01145450322617741456
     * distance : 在火星
     * personalDescription :
     * nickName : hb79641
     * GENDER : 0
     * userLevel : 1
     * longitude :
     * latitude :
     * USER_PHOTO :
     * RECE_LOGIN_DATE : 2017-01-20 16:37:29
     * mobile : 15640509852
     */

    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String recentTime;
        private String id;
        private String distance;
        private String personalDescription;
        private String nickName;
        private String GENDER;
        private String userLevel;
        private String longitude;
        private String latitude;
        private String USER_PHOTO;
        private String RECE_LOGIN_DATE;
        private String mobile;

        public String getRecentTime() {
            return recentTime;
        }

        public void setRecentTime(String recentTime) {
            this.recentTime = recentTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getPersonalDescription() {
            return personalDescription;
        }

        public void setPersonalDescription(String personalDescription) {
            this.personalDescription = personalDescription;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getGENDER() {
            return GENDER;
        }

        public void setGENDER(String GENDER) {
            this.GENDER = GENDER;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getUSER_PHOTO() {
            return USER_PHOTO;
        }

        public void setUSER_PHOTO(String USER_PHOTO) {
            this.USER_PHOTO = USER_PHOTO;
        }

        public String getRECE_LOGIN_DATE() {
            return RECE_LOGIN_DATE;
        }

        public void setRECE_LOGIN_DATE(String RECE_LOGIN_DATE) {
            this.RECE_LOGIN_DATE = RECE_LOGIN_DATE;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
