package com.jiahehongye.robred.bean;

import java.util.List;

/**
 * Created by huangjunhui on 2017/5/25.17:23
 */
public class AttentionListResult {


    /**
     * result : success
     * data : {"total":"1","list":[{"memId":"A01145438652011443124","nickName":"张艺谋","userPhoto":"http://img1.jinhoubaobao.com/group1/M00/00/0A/ezjIvVi-IUOAeNw7AABDqq4km_w01.jpeg","fansNumber":"1","fansCount":"1"}],"pageNumber":"1","pageSize":"10","pages":"1","size":"1"}
     */

    private String result;
    /**
     * total : 1
     * list : [{"memId":"A01145438652011443124","nickName":"张艺谋","userPhoto":"http://img1.jinhoubaobao.com/group1/M00/00/0A/ezjIvVi-IUOAeNw7AABDqq4km_w01.jpeg","fansNumber":"1","fansCount":"1"}]
     * pageNumber : 1
     * pageSize : 10
     * pages : 1
     * size : 1
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
        private String total;
        private String pageNumber;
        private String pageSize;
        private String pages;
        private String size;
        /**
         * memId : A01145438652011443124
         * nickName : 张艺谋
         * userPhoto : http://img1.jinhoubaobao.com/group1/M00/00/0A/ezjIvVi-IUOAeNw7AABDqq4km_w01.jpeg
         * fansNumber : 1
         * fansCount : 1
         */

        private List<ListBean> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(String pageNumber) {
            this.pageNumber = pageNumber;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String memId;
            private String nickName;
            private String userPhoto;
            private String fansNumber;
            private String fansCount;
            private String mobile;

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getMemId() {
                return memId;
            }

            public void setMemId(String memId) {
                this.memId = memId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserPhoto() {
                return userPhoto;
            }

            public void setUserPhoto(String userPhoto) {
                this.userPhoto = userPhoto;
            }

            public String getFansNumber() {
                return fansNumber;
            }

            public void setFansNumber(String fansNumber) {
                this.fansNumber = fansNumber;
            }

            public String getFansCount() {
                return fansCount;
            }

            public void setFansCount(String fansCount) {
                this.fansCount = fansCount;
            }
        }
    }
}
