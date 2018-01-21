package com.jiahehongye.robred.bean;

/**
 * Created by qianduan on 2017/6/1.
 */
public class BankBean {

    /**
     * result : success
     * data : {"id":"A01146857002768684921","memId":"A01145448027309536478","bankCard":"15901584543","cardType":"1","bankId":"","bankName":"","realName":"杨宁","idCard":"","bindingMobile":"","createDate":"2016-07-15 16:07:08","modifyDate":"","flga":""}
     */

    private String result;
    private String data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : A01146857002768684921
         * memId : A01145448027309536478
         * bankCard : 15901584543
         * cardType : 1
         * bankId :
         * bankName :
         * realName : 杨宁
         * idCard :
         * bindingMobile :
         * createDate : 2016-07-15 16:07:08
         * modifyDate :
         * flga :
         */

        private String id;
        private String memId;
        private String bankCard;
        private String cardType;
        private String bankId;
        private String bankName;
        private String realName;
        private String idCard;
        private String bindingMobile;
        private String createDate;
        private String modifyDate;
        private String flga;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemId() {
            return memId;
        }

        public void setMemId(String memId) {
            this.memId = memId;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getBindingMobile() {
            return bindingMobile;
        }

        public void setBindingMobile(String bindingMobile) {
            this.bindingMobile = bindingMobile;
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

        public String getFlga() {
            return flga;
        }

        public void setFlga(String flga) {
            this.flga = flga;
        }
    }
}
