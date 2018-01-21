package com.jiahehongye.robred.biz.model;

import java.util.List;

/**
 * Created by jiahe008_lvlanlan on 2017/5/27.
 */
public class HomeWinningResponse {

    /**
     * result : success
     * data : [{"prompt":"会员我是mh抢到了1.00元红包,手气爆棚~~~"},{"prompt":"会员我是mh抢到了1.00元红包,手气爆棚~~~"},{"prompt":"会员我是mh抢到了1.00元红包,手气爆棚~~~"},{"prompt":"会员我是mh抢到了0.18元红包,手气爆棚~~~"},{"prompt":"会员我是mh抢到了1.00元红包,手气爆棚~~~"},{"prompt":"会员我是mh抢到了0.27元红包,手气爆棚~~~"},{"prompt":"会员张艺谋抢到了0.02元红包,手气爆棚~~~"},{"prompt":"会员嗷嗷嗷～抢到了0.01元红包,手气爆棚~~~"},{"prompt":"会员嗷嗷嗷～抢到了0.16元红包,手气爆棚~~~"},{"prompt":"会员嗷嗷嗷～抢到了0.20元红包,手气爆棚~~~"}]
     */

    private String result;
    private List<WinningBean> data;

    private String errorCode;  //"errorCode":"5007",
    private String errorMsg;   //"errorMsg":"验证码不正确或已过期"

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<WinningBean> getData() {
        return data;
    }

    public void setData(List<WinningBean> data) {
        this.data = data;
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

    public static class WinningBean {
        /**
         * prompt : 会员我是mh抢到了1.00元红包,手气爆棚~~~
         */

        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        @Override
        public String toString() {
            return "WinningBean{" +
                    "prompt='" + prompt + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HomeWinningResponse{" +
                "result='" + result + '\'' +
                ", data=" + data +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
