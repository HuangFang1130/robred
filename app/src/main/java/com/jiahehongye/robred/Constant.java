package com.jiahehongye.robred;

import com.hyphenate.easeui.EaseConstant;

import okhttp3.MediaType;

/**
 * Created by huangjunhui on 2016/12/16.10:10
 */
public class Constant extends EaseConstant{

    public static final String RESULT = "result";
    public static final String SUCCESS = "success";
    public static final String LOGOUT = "logout";//false: 退出了， true:没有退出
    public static final String IS_LOGIN = "is_login";//false： 没有登录， true : 登录了


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String LOGIN_ID = "login_id";//唯一标示
    public static final String LOGIN_MEMBERRANK = "login_memberrank";//会员等级
    public static final String LOGIN_MEMBERTYPE = "login_membertype";//会员类型
    public static final String LOGIN_MOBILE = "login_mobile";//会员手机
    public static final String LOGIN_REDINTEGRAL = "login_redintegral";//积分
    public static final String LOGIN_PASSWORD = "login_password";//用户的密码
    public static final String USER_HOBBY = "user_hobby";//用户的密码


    public static final String URL = "http://com4.17monkey.com";
    public static final String BASE_URL = "http://com4.17monkey.com/mobile/";//一元抢购暂时用的url
//    public static final String URL = "http://192.168.0.104:7777";
//    public static final String BASE_URL = "http://192.168.0.104:7777/mobile/";//一元抢购暂时用的url



    //分享成功回调
    public static final String GET_SHARE = URL +"/mobile/member/member/inviteFriend.jhtml";
    //分享成功回调
    public static final String SHARE_SUCCESS = URL +"/mobile/member/member/inviteFriendSend.jhtml";
    // 修改手机号
    public static final String MODIFYMOBILENUMBER = URL + "/mobile/member/member/modifyMobileNumber.jhtml";
    // 获取验证码
    public static final String MOBILECODE_URL = URL + "/mobile/common/sendMobileCode.jhtml";
    // 修改密码
    public static final String MODIFYPASSWORD_URL = URL + "/mobile/member/member/modifyPassword.jhtml";
    // 忘记密码
    public static final String FORGETPASSWORD_URL = URL + "/mobile/common/forgotPassword.jhtml";

    //忘记支付密码
    public static final String FORGETPAYPASSWORD = URL+ "/mobile/member/member/forgetPayPassword.jhtml";
    //修改支付密码
    public static final String MODIFYPAYPASSWORD = URL+ "/mobile/member/member/modifyPayPassword.jhtml";

    // 帮助页面
    public static final String GETHELPPAGE = URL + "/mobile/news/getHelpPage.jhtml";
    //产品介绍
    public static final String GETPRODUCT = URL + "/mobile/news/getProductPage.jhtml";

    // 自动更新地址
    public static final String AUTOUPDATE = URL+"/mobile/androidUpdate/autoUpdate.jhtml";

    //充值接口
    public static final String MEMBERRECHARGE = URL+ "/mobile/member/member/memberRecharge.jhtml";

    public static final String MEMBERINFO = URL + "/mobile/member/member/memberInfo.jhtml";
    //修改个人信息
    public static final String CHANGE_MEMBER_INFO = URL + "/mobile/member/member/modifyMemberInfo.jhtml";
    //我的礼物
    public static final String MY_GIFT = URL + "/mobile/member/member/getGift.jhtml";

    //充值钻石
    public static final String BUY_DIAMOND = URL +"/mobile/member/member/saveDiamondRecharge.jhtml";

    //
    public static final String BUY_DIAMOND2 = URL +"/mobile/member/member/getSendRedEnveRequestParamsUrl.jhtml";

    //获取账户总额
    public static final String GET_TOTAL = URL + "/mobile/member/member/allAmountInfo.jhtml";

    //获取新闻分类
    public static final String NEWS_TYPE = URL + "/mobile/infor/getAllCategory.jhtml";

    //获取分类新闻列表
    public static final String NEWS_LIST = URL + "/mobile/infor/getInforByCategory.jhtml";

    //获取新闻详情
    public static final String NEWS_DETAIL = URL + "/mobile/infor/getInforContent.jhtml";
    //所有一级评论
    public static final String ALL_FIEST_COMMENT = URL + "/mobile/infor/getFirstCommentByInfor.jhtml";
    //提交评论
    public static final String UP_COMMENT = URL + "/mobile/infor/submitComment.jhtml";

    // 获取功能设置列表
    public static final String GETFUNCSETTING = URL+"/mobile/funcSetting/getFuncSettingList.jhtml";
    // 设置功能开关
    public static final String SETFUNCSETTING = URL+"/mobile/funcSetting/setFuncSetting.jhtml";

    //获取资讯一级评论下所有子评论
    public static final String FIRST_SON = URL + "/mobile/infor/getSonCommentByInfor.jhtml";
    //点赞
    public static final String NEWS_LIKE = URL + "/mobile/infor/clickPraise.jhtml";

    //查看所有点赞人
    public static final String ALL_ZAN = URL + "/mobile/infor/getPraisePeople.jhtml";

    //我的评论
    public static final String MY_COMMENT = URL + "/mobile/infor/getComments.jhtml";

    //钻石账户
    public static final String DIAMOND_ACCOUNT = URL + "/mobile/member/member/getDiamondPackage.jhtml";
    //礼物兑换记录
    public static final String GIFT_JILU = URL + "/mobile/member/member/getCashRecord.jhtml";

    //金币榜
    public static final String GOLD_RANK = URL +"/mobile/member/member/getGoldCoinList.jhtml";

    //登陆
    public static final String LOGIN_URL = URL + "/mobile/common/memberLogin.jhtml";

    //首页
    public static final String HOME_PAGE = URL + "/mobile/homePage/getHomePage.jhtml";
    //首页轮播中奖信息接口
    public static final String HOME_WINNING_INFO = URL + "/mobile/homePage/getpromptList.jhtml";

    // 注册
    public static final String REGISTER_URL = URL + "/mobile/common/memberRegister.jhtml";
    //礼物列表
    public static final String CHAT_GIFT_LIST = URL + "/mobile/chat/getGiftList.jhtml";

    //发送礼物
    public static final String CHAT_SEND_GIFT = URL + "/mobile/chat/sendGift.jhtml";

    //通讯录
    public static final String ADDRESS_LIST = URL + "/mobile/chat/getAddressBook.jhtml";

    //通讯录-详情）
    public static final String ADDRESS_DETAIL = URL + "/mobile/member/member/memberDetail.jhtml";

    //(新接口-首页一元购信息)
    public static final String HOME_YIYUANGOU_LIST = URL + "/mobile/homePage/getHomeOneYuan.jhtml";

    //富豪榜接口
    public static final String HOME_RICH_LIST = URL + "/mobile/homePage/getRichList.jhtml";

    /**------------------------------------------------------*/


    //个人红包--列表  老接口
    public static final String GET_PERSON_REDBAGLIST = URL + "/mobile/member/redEnve/sendRedEnveList.jhtml";

     // 红包列表  新接口
    public static final String GRAB_PERSONURL = URL + "/mobile/member/redEnve/sendRedEnveListNew.jhtml";

    //弹出popwindow 开始抢
    public static final String BEGINROBURL = URL + "/mobile/member/redEnve/isAbleGrabRedEnve.jhtml";

    // 抢红包的操作  member/redEnve/getRedAmountFromRedis.jhtml  /mobile/member/redEnve/getRedAmountFromRedis.jhtml
    public static final String GRAB_ACTIVE = URL + "/mobile/member/redEnve/getRedAmountFromRedis.jhtml";


    // 抢红包详情
    public static final String GRAB_RED_DETAIL = URL + "/mobile/member/redEnve/clickAndGrabedRedEnveDetail.jhtml";

    // 抢红包详情列表
    public static final String GRAB_RED_DETAIL_LIST = URL + "/mobile/member/redEnve/grabedRedEnveListByRedEnveCode.jhtml";


    /**------------------------------------------------------*/
    //红包提现
    public static final String RED_TIXIAN = URL +"/mobile/member/accountDetail/withDrawCashRed.jhtml";
    // 消息未读数
    public static final String MESSAGE_UNREAD = URL + "/mobile/chat/getNotReadNewsNum.jhtml";
    //充值提现
    public static final String CHONGZHI_TIXIAN = URL +"/mobile/member/accountDetail/withDrawCashRecharge.jhtml";

    //充值余额明细
    public static final String CHONGZHI_YUE_BALANCE = URL +"/mobile/member/member/getAccoutBalaDetail.jhtml";
    //红包余额明细
    public static final String RED_YUELIST = URL +"/mobile/member/member/getAccoRedEnveDetail.jhtml";
    // 抢到的红包
    public static final String ROBLISTURL = URL + "/mobile/member/redEnve/grabedRedEnveList.jhtml";
    // 发放的红包
    public static final String SENDLISTURL = URL + "/mobile/member/redEnve/individualSendRedEnveList.jhtml";

    // 通知，系统。互动消息详情
    public static final String MESSAGE_LIST = URL + "/mobile/chat/getNews.jhtml";

    //发出红包的详情
    public static final String SEND_DETAIL = URL +"/mobile/member/redEnve/grabedRedEnveListByRedEnveCode.jhtml";

    //发出红包的详情
    public static final String SEND_DETAIL_top = URL +"/mobile/member/redEnve/sendRedEnveDetailByRedEnveCode.jhtml";


    //发红包确定
    public static final String SEND_RED = URL + "/mobile/member/redEnve/saveSendRedEnve.jhtml";
    // 再次发红包
    public static final String RESENDURL = URL + "/mobile/member/redEnve/updateSendRedEnve.jhtml";

    //获取账户红包余额以及账户余额
    public static final String GET_USER_BALANCE = URL + "/mobile/member/member/allAmountInfo.jhtml";

    //支付方式确定请求接口
    public static final String PAY_RED_WAY_OK = URL + "/mobile/member/redEnve/getSendRedEnveRequestParamsUrl.jhtml";

    //设置支付密码
    public static final String SETPAYPASSWORD = URL + "/mobile/member/member/setPayPassword.jhtml";
    //验证支付密码
    public static final String CHECKPAYPASSWORD = URL + "/mobile/member/member/checkPayPassword.jhtml";


    //兑换
    public static final String DUIHUAN = URL + "/mobile/member/member/getCash.jhtml";
    //举报
    public  static final String JUBAO = URL + "/mobile/infor/inform.jhtml";
    //意见反馈
    public static final String FEEDBACK = BASE_URL + "funcSetting/feedback.jhtml";

    //删除照片
    public static final String DELETE_PHOTO = URL+"/mobile/member/member/delMemberPhoto.jhtml";

    // 消息推送注册
    /**
     * 新闻固定前缀链接
     */
    public static final String HOLD_NEWS = BASE_URL + "infor/shareInformation.jhtml?id=";
    /**
     * 消息推送注册
     */
    public static final String DEVICEREGISTER = BASE_URL + "jpushMessage/deviceRegister.jhtml";
   /**
     * 发送openid到服务器
     */
    public static final String SEND_OPENID = URL + "/mobile/common/loginByOpenId.jhtml";
   /**
     * 第三方绑定注册
     */
    public static final String BIND_OPENID_TOSERVER = URL + "/mobile/common/bindAccountRegister.jhtml";
    // 第三方绑定登录
    public static final String BIND_ACCOUNT_LOGIN = URL + "/mobile/common/bindAccountLogin.jhtml";
    // 个人信息
    public static final String GET_PERSONAL_INFORMATION = URL + "/mobile/member/member/memberInfo.jhtml";
    public static final String GET_RED_DETAIL_BANNER = URL + "/mobile/syAdvertImg/getDefaultAdImg.jhtml";

    /**
     * 一元抢购
     */
    //首页上部分
    public static final String ONEYUANHOMEBANNER = BASE_URL + "oneyuan/index/getUpperPart.jhtml";
    //首页列表
    public static final String ONEYUANHOMEPRODUCTLIST = BASE_URL + "oneyuan/index/productList.jhtml";
    //产品详情
    public static final String ONEYUANPRODUCTDETAIL = BASE_URL + "oneyuan/index/productDetails.jhtml";
    //往期揭晓列表
    public static final String ONEYUANPASTANNOUNCED = BASE_URL + "oneyuan/index/clickPastAnnounced.jhtml";
    //参与记录
    public static final String ONEYUANPARTICIPATIONLIST = BASE_URL + "oneyuan/index/participationRecordList.jhtml";
    //我要参与
    public static final String ONEYUANJOIN = BASE_URL + "oneyuan/index/clickParticipate.jhtml";
    //付款交易
    public static final String ONEYUANPAY = BASE_URL + "oneyuan/index/paymentSubmit.jhtml";
    //我的抢单
    public static final String ONEYUANMYROBLIST = BASE_URL + "oneyuan/index/myGrabSn.jhtml";
    //查看计算结果
    public static final String ONEYUANMATHMETHOD = BASE_URL + "oneyuan/index/calculationDetails.jhtml";
    //查看中奖信息
    public static final String ONEYUANMYWIN = BASE_URL + "oneyuan/index/winningDetails.jhtml";
    //保存收货地址
    public static final String ONEYUANSAVEADDRESS = BASE_URL + "oneyuan/index/addressSaveUpdate.jhtml";
    //确认提交收货地址
    public static final String ONEYUANSUBMITADDRESS = BASE_URL + "oneyuan/index/submitAddress.jhtml";
    //一元抢购活动规则
    public static final String ONEYUANRULES = BASE_URL + "oneyuan/index/rule.jhtml";

    //获取产品的剩余情况
    public static final String GET_PRODUCT_INFOR = BASE_URL +"oneyuan/index/submitOrder.jhtml";

    // 退出登录清理数据
    public static final String SETTING_LOGOUT = URL+"/mobile/member/member/memberLogout.jhtml";

    // 广告
    public static final String ADVERTISEMENT_URL = URL+"/mobile/syAdvertImg/getSyAdvert.jhtml";

    // 广告
//    public static final String ADVERTISEMENT_IMG = URL+"/res/wap/imgs/neiye/noAds.jpg";
     // 广告
    public static final String ATTENTION_URL = URL+"/mobile/member/follow/memberFollow.jhtml";

    //关注列表
    public static final String ATTENTION_LIST_URL = URL+"/mobile/member/follow/memberFollowList.jhtml";


    //获取支付宝账号
    public static final String GET_BANK = URL+"/mobile/member/accountDetail/binBank.jhtml";


    public static final String LATITUDE = "latitude";//纬度
    public static final String LONGITUDE = "longitude";//经度
    public static final String FIRST_COMEIN = "first_comein";//第一次进入： true
    public static final String ACTION_AVATAR_CHANAGED = "action_avatar_changed";
    public static final String FIRST_COMEIN_PERSONAL = "first_comein_personal";//第一次进入个人中心
    public static final String EXIT_SUCCESS = "EXIT_SUCCESS";
    public static final int SCROLL_STATE_IN = 302;

    public static final int RESULT_ERROR = 812; //网络请求失败
    public static final int RESULT_HOME_WINNING = 813; //首页中奖信息网络请求返回结果成功

    public static final String FANS_LIST_URL = URL+ "/mobile/member/follow/memberFansList.jhtml";
    public static final String IS_ADV = "isadv";
    public static final String IS_ADV_BANNER_RED_DETAIL = "adIsVertImg";
    public static final String GET_CHANGE_ACOUNT = URL+"/mobile/member/accountDetail/updateBingBankCardRealName.jhtml";
}
