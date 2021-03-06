package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2017/5/17.16:00
 */
public class DFTTTitleResult {

    /**
     * type : toutiao
     * name : 头条
     * isup : 1   //是否是tab
     */

    private List<DdBean> dd;

    public List<DdBean> getDd() {
        return dd;
    }

    public void setDd(List<DdBean> dd) {
        this.dd = dd;
    }

    public static class DdBean {
        private String type;
        private String name;
        private String isup;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsup() {
            return isup;
        }

        public void setIsup(String isup) {
            this.isup = isup;
        }
    }
}
