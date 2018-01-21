package com.jiahehongye.robred.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangjunhui on 2016/12/26.15:28
 */
public class AddressDetailResult {
    /**
     * result : fail
     * errorCode : 4002
     * errorMsg : 缺少必须的参数
     */


    private String errorCode;
    private String errorMsg;

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


    /**
     * result : success
     * data : {"id":"A01145438652011443124","createDate":"2016-02-02 12:15:20","modifyDate":"2016-10-10 11:23:45","flag":false,"userName":"15933434904","realName":"","nickName":"宝宝心里苦","eMail":"","isVeriEmail":false,"password":"e7a1878fb11c0373dc646ae5ae18e43b","gender":"2","accountIntegral":"0","amount":"0.00","accountBala":"0.00","lockedBala":"-192.00","accountStatus":"0","loginFailureCount":"1","lockDate":"2016-05-19 10:24:23","regIp":"111.202.110.164","receLoginDate":"2016-12-26 15:10:03","receLoginIp":"192.168.0.157","birthday":"","mobile":"15933434904","isVeriMobile":false,"phone":"","phoneAreaName":"河北 保定","phoneArea":"199","memberRank":"4","safeKeyExpire":"","safeKeyValue":"","identityCard":"","area":"199","areaName":"河北 保定","zipCode":"","address":"","userPhoto":"http://img1.jinhoubaobao.com/group1/M00/00/00/dwozhlawM5WAYdrdAABCatO3X_A45.jpeg","regSource":"移动端","accoRedEnve":"12.93","redIntegral":"11","memberType":"0","compName":"","compIndustry":"","compProfile":"","compAddr":"","isAgreeRegProtocol":true,"memberProp":"","txYezfPass":"96e79218965eb72c92a549dd5a330112","hobbies":"","personalDescription":"","datingPurpose":"","schoolRecord":"","personalPhoto":"","maritalStatus":"","profession":"","constellation":"","convertibleCurrency":"612","flower":"0","diamond":"667","userLevel":"1","latitude":"","longitude":"","resList":[]}
     */

    private String result;
    /**
     * id : A01145438652011443124
     * createDate : 2016-02-02 12:15:20
     * modifyDate : 2016-10-10 11:23:45
     * flag : false
     * userName : 15933434904
     * realName :
     * nickName : 宝宝心里苦
     * eMail :
     * isVeriEmail : false
     * password : e7a1878fb11c0373dc646ae5ae18e43b
     * gender : 2
     * accountIntegral : 0
     * amount : 0.00
     * accountBala : 0.00
     * lockedBala : -192.00
     * accountStatus : 0
     * loginFailureCount : 1
     * lockDate : 2016-05-19 10:24:23
     * regIp : 111.202.110.164
     * receLoginDate : 2016-12-26 15:10:03
     * receLoginIp : 192.168.0.157
     * birthday :
     * mobile : 15933434904
     * isVeriMobile : false
     * phone :
     * phoneAreaName : 河北 保定
     * phoneArea : 199
     * memberRank : 4
     * safeKeyExpire :
     * safeKeyValue :
     * identityCard :
     * area : 199
     * areaName : 河北 保定
     * zipCode :
     * address :
     * userPhoto : http://img1.jinhoubaobao.com/group1/M00/00/00/dwozhlawM5WAYdrdAABCatO3X_A45.jpeg
     * regSource : 移动端
     * accoRedEnve : 12.93
     * redIntegral : 11
     * memberType : 0
     * compName :
     * compIndustry :
     * compProfile :
     * compAddr :
     * isAgreeRegProtocol : true
     * memberProp :
     * txYezfPass : 96e79218965eb72c92a549dd5a330112
     * hobbies :
     * personalDescription :
     * datingPurpose :
     * schoolRecord :
     * personalPhoto :
     * maritalStatus :
     * profession :
     * constellation :
     * convertibleCurrency : 612
     * flower : 0
     * diamond : 667
     * userLevel : 1
     * latitude :
     * longitude :
     * resList : []
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
        private int fansCount;

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        private String id;
        private String createDate;
        private String modifyDate;
        private boolean flag;
        private String userName;
        private String realName;
        private String nickName;
        private String eMail;
        private boolean isVeriEmail;
        private String password;
        private String gender;
        private String accountIntegral;
        private String amount;
        private String accountBala;
        private String lockedBala;
        private String accountStatus;
        private String loginFailureCount;
        private String lockDate;
        private String regIp;
        private String receLoginDate;
        private String receLoginIp;
        private String birthday;
        private String mobile;
        private boolean isVeriMobile;
        private String phone;
        private String phoneAreaName;
        private String phoneArea;
        private String memberRank;
        private String safeKeyExpire;
        private String safeKeyValue;
        private String identityCard;
        private String area;
        private String areaName;
        private String zipCode;
        private String address;
        private String userPhoto;
        private String regSource;
        private String accoRedEnve;
        private String redIntegral;
        private String memberType;
        private String compName;
        private String compIndustry;
        private String compProfile;
        private String compAddr;
        private boolean isAgreeRegProtocol;
        private String memberProp;
        private String txYezfPass;
        private String hobbies;
        private String personalDescription;
        private String datingPurpose;
        private String schoolRecord;
        private String personalPhoto;
        private String maritalStatus;
        private String profession;
        private String constellation;
        private String convertibleCurrency;
        private String flower;
        private String diamond;
        private String userLevel;
        private String latitude;
        private String longitude;
        private List<PhotosBean> resList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEMail() {
            return eMail;
        }

        public void setEMail(String eMail) {
            this.eMail = eMail;
        }

        public boolean isIsVeriEmail() {
            return isVeriEmail;
        }

        public void setIsVeriEmail(boolean isVeriEmail) {
            this.isVeriEmail = isVeriEmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAccountIntegral() {
            return accountIntegral;
        }

        public void setAccountIntegral(String accountIntegral) {
            this.accountIntegral = accountIntegral;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAccountBala() {
            return accountBala;
        }

        public void setAccountBala(String accountBala) {
            this.accountBala = accountBala;
        }

        public String getLockedBala() {
            return lockedBala;
        }

        public void setLockedBala(String lockedBala) {
            this.lockedBala = lockedBala;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getLoginFailureCount() {
            return loginFailureCount;
        }

        public void setLoginFailureCount(String loginFailureCount) {
            this.loginFailureCount = loginFailureCount;
        }

        public String getLockDate() {
            return lockDate;
        }

        public void setLockDate(String lockDate) {
            this.lockDate = lockDate;
        }

        public String getRegIp() {
            return regIp;
        }

        public void setRegIp(String regIp) {
            this.regIp = regIp;
        }

        public String getReceLoginDate() {
            return receLoginDate;
        }

        public void setReceLoginDate(String receLoginDate) {
            this.receLoginDate = receLoginDate;
        }

        public String getReceLoginIp() {
            return receLoginIp;
        }

        public void setReceLoginIp(String receLoginIp) {
            this.receLoginIp = receLoginIp;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isIsVeriMobile() {
            return isVeriMobile;
        }

        public void setIsVeriMobile(boolean isVeriMobile) {
            this.isVeriMobile = isVeriMobile;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhoneAreaName() {
            return phoneAreaName;
        }

        public void setPhoneAreaName(String phoneAreaName) {
            this.phoneAreaName = phoneAreaName;
        }

        public String getPhoneArea() {
            return phoneArea;
        }

        public void setPhoneArea(String phoneArea) {
            this.phoneArea = phoneArea;
        }

        public String getMemberRank() {
            return memberRank;
        }

        public void setMemberRank(String memberRank) {
            this.memberRank = memberRank;
        }

        public String getSafeKeyExpire() {
            return safeKeyExpire;
        }

        public void setSafeKeyExpire(String safeKeyExpire) {
            this.safeKeyExpire = safeKeyExpire;
        }

        public String getSafeKeyValue() {
            return safeKeyValue;
        }

        public void setSafeKeyValue(String safeKeyValue) {
            this.safeKeyValue = safeKeyValue;
        }

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getRegSource() {
            return regSource;
        }

        public void setRegSource(String regSource) {
            this.regSource = regSource;
        }

        public String getAccoRedEnve() {
            return accoRedEnve;
        }

        public void setAccoRedEnve(String accoRedEnve) {
            this.accoRedEnve = accoRedEnve;
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

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getCompIndustry() {
            return compIndustry;
        }

        public void setCompIndustry(String compIndustry) {
            this.compIndustry = compIndustry;
        }

        public String getCompProfile() {
            return compProfile;
        }

        public void setCompProfile(String compProfile) {
            this.compProfile = compProfile;
        }

        public String getCompAddr() {
            return compAddr;
        }

        public void setCompAddr(String compAddr) {
            this.compAddr = compAddr;
        }

        public boolean isIsAgreeRegProtocol() {
            return isAgreeRegProtocol;
        }

        public void setIsAgreeRegProtocol(boolean isAgreeRegProtocol) {
            this.isAgreeRegProtocol = isAgreeRegProtocol;
        }

        public String getMemberProp() {
            return memberProp;
        }

        public void setMemberProp(String memberProp) {
            this.memberProp = memberProp;
        }

        public String getTxYezfPass() {
            return txYezfPass;
        }

        public void setTxYezfPass(String txYezfPass) {
            this.txYezfPass = txYezfPass;
        }

        public String getHobbies() {
            return hobbies;
        }

        public void setHobbies(String hobbies) {
            this.hobbies = hobbies;
        }

        public String getPersonalDescription() {
            return personalDescription;
        }

        public void setPersonalDescription(String personalDescription) {
            this.personalDescription = personalDescription;
        }

        public String getDatingPurpose() {
            return datingPurpose;
        }

        public void setDatingPurpose(String datingPurpose) {
            this.datingPurpose = datingPurpose;
        }

        public String getSchoolRecord() {
            return schoolRecord;
        }

        public void setSchoolRecord(String schoolRecord) {
            this.schoolRecord = schoolRecord;
        }

        public String getPersonalPhoto() {
            return personalPhoto;
        }

        public void setPersonalPhoto(String personalPhoto) {
            this.personalPhoto = personalPhoto;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getConvertibleCurrency() {
            return convertibleCurrency;
        }

        public void setConvertibleCurrency(String convertibleCurrency) {
            this.convertibleCurrency = convertibleCurrency;
        }

        public String getFlower() {
            return flower;
        }

        public void setFlower(String flower) {
            this.flower = flower;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(String userLevel) {
            this.userLevel = userLevel;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<PhotosBean> getResList() {
            return resList;
        }

        public void setResList(List<PhotosBean> resList) {
            this.resList = resList;
        }
    }


    public static class PhotosBean implements Serializable {

        /**
         * batchPhotoId : fd93837fcc1611e69d48842b2b8fd827
         * photo : http://img1.jinhoubaobao.com/group1/M00/00/06/ezjIvVhiNH6AEUv7ABJHhenvR_c121.jpg
         */

        private String batchPhotoId;
        private String photo;

        public String getBatchPhotoId() {
            return batchPhotoId;
        }

        public void setBatchPhotoId(String batchPhotoId) {
            this.batchPhotoId = batchPhotoId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
