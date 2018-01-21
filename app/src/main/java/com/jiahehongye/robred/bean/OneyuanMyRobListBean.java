package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanMyRobListBean {

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
		private List<ParticipationRecordList> participationRecordList;

		public void setParticipationRecordList(List<ParticipationRecordList> participationRecordList) {
			this.participationRecordList = participationRecordList;
		}

		public List<ParticipationRecordList> getParticipationRecordList() {
			return this.participationRecordList;
		}

	}

	public class ParticipationRecordList {
		private String noStage;

		private String millisecond;

		private String name;

		private String image;

		private String type;

		public List<SnList> snList ;

		public List<SnList> getSnList() {
			return snList;
		}

		public void setSnList(List<SnList> snList) {
			this.snList = snList;
		}

		private String createDate;

		private String productId;

		private String productType;

		public void setNoStage(String noStage) {
			this.noStage = noStage;
		}

		public String getNoStage() {
			return this.noStage;
		}

		public void setMillisecond(String millisecond) {
			this.millisecond = millisecond;
		}

		public String getMillisecond() {
			return this.millisecond;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
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

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getCreateDate() {
			return this.createDate;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductId() {
			return this.productId;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public String getProductType() {
			return this.productType;
		}

	}

	public class SnList {
		private String sn;

		private String type;

		public void setSn(String sn){
			this.sn = sn;
		}
		public String getSn(){
			return this.sn;
		}
		public void setType(String type){
			this.type = type;
		}
		public String getType(){
			return this.type;
		}

	}

}
