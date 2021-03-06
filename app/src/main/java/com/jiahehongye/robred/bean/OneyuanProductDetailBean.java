package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanProductDetailBean {

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
		private List<Product> product;

		private List<ResultList> resultList;

		private List<ParticipationRecord> participationRecord;

		public List<ParticipationRecord> getParticipationRecord() {
			return participationRecord;
		}

		public void setParticipationRecord(List<ParticipationRecord> participationRecord) {
			this.participationRecord = participationRecord;
		}

		public void setProduct(List<Product> product) {
			this.product = product;
		}

		public List<Product> getProduct() {
			return this.product;
		}

		public void setResultList(List<ResultList> resultList) {
			this.resultList = resultList;
		}

		public List<ResultList> getResultList() {
			return this.resultList;
		}

	}

	public class ParticipationRecord {

		private String userPhoto;

		private String userName;

		private String revealedTime;

		private String sn;

		private String memberSn;

		public List<SnList> snList;

		public List<SnList> getSnList() {
			return snList;
		}

		public void setSnList(List<SnList> snList) {
			this.snList = snList;
		}

		public String getUserPhoto() {
			return userPhoto;
		}

		public void setUserPhoto(String userPhoto) {
			this.userPhoto = userPhoto;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getRevealedTime() {
			return revealedTime;
		}

		public void setRevealedTime(String revealedTime) {
			this.revealedTime = revealedTime;
		}

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}

		public String getMemberSn() {
			return memberSn;
		}

		public void setMemberSn(String memberSn) {
			this.memberSn = memberSn;
		}

	}

	public class ResultList {
		private String time;

		private String contents;

		private List<PictureLibraryList> pictureLibraryList;

		private String name;

		private String month;

		private String userName;

		private String luckyNumber;

		private String productId;

		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return this.time;
		}

		public void setContents(String contents) {
			this.contents = contents;
		}

		public String getContents() {
			return this.contents;
		}

		public void setPictureLibraryList(List<PictureLibraryList> pictureLibraryList) {
			this.pictureLibraryList = pictureLibraryList;
		}

		public List<PictureLibraryList> getPictureLibraryList() {
			return this.pictureLibraryList;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getMonth() {
			return this.month;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return this.userName;
		}

		public void setLuckyNumber(String luckyNumber) {
			this.luckyNumber = luckyNumber;
		}

		public String getLuckyNumber() {
			return this.luckyNumber;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductId() {
			return this.productId;
		}

	}

	public class PictureLibraryList {
		private String imgUrl;

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getImgUrl() {
			return this.imgUrl;
		}

	}

	public class Product {
		private String noStage;

		private String totalPeople;

		private String percentage;

		private String surplusPeople;

		private String productObjId;

		private String image;

		private String type;

		private String price;

		private String noStageObj;

		private String name;

		private String fixedValue;

		private String isParticipate;

		private String isResetNext;

		private String commnets;

		private String introduction;

		public void setNoStage(String noStage) {
			this.noStage = noStage;
		}

		public String getNoStage() {
			return this.noStage;
		}

		public void setTotalPeople(String totalPeople) {
			this.totalPeople = totalPeople;
		}

		public String getTotalPeople() {
			return this.totalPeople;
		}

		public void setPercentage(String percentage) {
			this.percentage = percentage;
		}

		public String getPercentage() {
			return this.percentage;
		}

		public void setSurplusPeople(String surplusPeople) {
			this.surplusPeople = surplusPeople;
		}

		public String getSurplusPeople() {
			return this.surplusPeople;
		}

		public void setProductObjId(String productObjId) {
			this.productObjId = productObjId;
		}

		public String getProductObjId() {
			return this.productObjId;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getImage() {
			return this.image;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getType() {
			return this.type;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getPrice() {
			return this.price;
		}

		public void setNoStageObj(String noStageObj) {
			this.noStageObj = noStageObj;
		}

		public String getNoStageObj() {
			return this.noStageObj;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setFixedValue(String fixedValue) {
			this.fixedValue = fixedValue;
		}

		public String getFixedValue() {
			return this.fixedValue;
		}

		public void setIsParticipate(String isParticipate) {
			this.isParticipate = isParticipate;
		}

		public String getIsParticipate() {
			return this.isParticipate;
		}

		public void setIsResetNext(String isResetNext) {
			this.isResetNext = isResetNext;
		}

		public String getIsResetNext() {
			return this.isResetNext;
		}

		public void setCommnets(String commnets) {
			this.commnets = commnets;
		}

		public String getCommnets() {
			return this.commnets;
		}

		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}

		public String getIntroduction() {
			return this.introduction;
		}

	}

	public class SnList {

		/**
		 * memberSn : 10000013
		 * type : 1
		 */

		private String memberSn;
		private String type;

		public String getMemberSn() {
			return memberSn;
		}

		public void setMemberSn(String memberSn) {
			this.memberSn = memberSn;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

}
