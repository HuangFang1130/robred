package com.jiahehongye.robred.bean;

/**
 * Created by qianduan on 2016/12/21.
 */
public class NewsTypeBean {

    /**
     * id : A01146889839718233319
     * createDate : 2016-12-09 16:12:02
     * modifyDate :
     * flag : false
     * category : 推荐
     * order : 1
     */

    private String id;
    private String createDate;
    private String modifyDate;
    private boolean flag;
    private String category;
    private String order;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
