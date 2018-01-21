package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanProductParticipationListBean {

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
		private String sn;

		private String nickName;

		private String millisecond;

		private String userPhoto;

		private String createDate;

		public void setSn(String sn) {
			this.sn = sn;
		}

		public String getSn() {
			return this.sn;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getNickName() {
			return this.nickName;
		}

		public void setMillisecond(String millisecond) {
			this.millisecond = millisecond;
		}

		public String getMillisecond() {
			return this.millisecond;
		}

		public void setUserPhoto(String userPhoto) {
			this.userPhoto = userPhoto;
		}

		public String getUserPhoto() {
			return this.userPhoto;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public String getCreateDate() {
			return this.createDate;
		}

	}

}
