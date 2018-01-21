package com.jiahehongye.robred.bean;

/**
 * Created by huangjunhui on 2016/12/16.11:30
 */
public class LoginResult {


    /**
     * result : success
     * data : {"id":"A01145439046434062291","redIntegral":"87","memberType":"0","memberRank":"4","mobile":"18310606130"}
     */

    private String result;
    /**
     * id : A01145439046434062291
     * redIntegral : 87
     * memberType : 0
     * memberRank : 4
     * mobile : 18310606130
     */

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
        private String id;
        private String redIntegral;
        private String memberType;
        private String memberRank;
        private String mobile;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRedIntegral() {
            return redIntegral;
        }

        public void setRedIntegral(String redIntegral) {
            this.redIntegral = redIntegral;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getMemberRank() {
            return memberRank;
        }

        public void setMemberRank(String memberRank) {
            this.memberRank = memberRank;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
