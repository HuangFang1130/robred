package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanHomeBannerBean {

	private String result;

	private Data data;

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return this.result;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return this.data;
	}

	public class Data {
		private List<ProductClassificationList> productClassificationList;

		private List<ParticipationRecordList> participationRecordList;

		private List<Banner> banner;

		public void setProductClassificationList(List<ProductClassificationList> productClassificationList) {
			this.productClassificationList = productClassificationList;
		}

		public List<ProductClassificationList> getProductClassificationList() {
			return this.productClassificationList;
		}

		public void setParticipationRecordList(List<ParticipationRecordList> participationRecordList) {
			this.participationRecordList = participationRecordList;
		}

		public List<ParticipationRecordList> getParticipationRecordList() {
			return this.participationRecordList;
		}

		public void setBanner(List<Banner> banner) {
			this.banner = banner;
		}

		public List<Banner> getBanner() {
			return this.banner;
		}

	}

	public class Banner {
		private String isNetwork;

		private String image;

		private String orders;

		private String url;

		private String productId;

		public void setIsNetwork(String isNetwork) {
			this.isNetwork = isNetwork;
		}

		public String getIsNetwork() {
			return this.isNetwork;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getImage() {
			return this.image;
		}

		public void setOrders(String orders) {
			this.orders = orders;
		}

		public String getOrders() {
			return this.orders;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return this.url;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductId() {
			return this.productId;
		}

	}

	public class ParticipationRecordList {
		private String nickNameAndSn;

		private String scrollBar;

		public void setNickNameAndSn(String nickNameAndSn) {
			this.nickNameAndSn = nickNameAndSn;
		}

		public String getNickNameAndSn() {
			return this.nickNameAndSn;
		}

		public void setScrollBar(String scrollBar) {
			this.scrollBar = scrollBar;
		}

		public String getScrollBar() {
			return this.scrollBar;
		}

	}

	public class ProductClassificationList {
		private String productClassificationId;

		private String name;

		private String orders;

		public void setProductClassificationId(String productClassificationId) {
			this.productClassificationId = productClassificationId;
		}

		public String getProductClassificationId() {
			return this.productClassificationId;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setOrders(String orders) {
			this.orders = orders;
		}

		public String getOrders() {
			return this.orders;
		}

	}

}
