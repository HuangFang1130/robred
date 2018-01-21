package com.jiahehongye.robred.bean;

/**
 * Created by huangjunhui on 2017/5/25.15:16
 */
public class AttentionResult {


    /**
     * result : success
     * data : {"msg":"关注成功","fansCount":1,"errorCode":"5001","errorMsg":"账号不存在"}
     */

    private String result;
    /**
     * msg : 关注成功
     * fansCount : 1
     * errorCode : 5001
     * errorMsg : 账号不存在
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
        private String msg;
        private int fansCount;
        private String errorCode;
        private String errorMsg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
