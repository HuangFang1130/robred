package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanMathListBean {

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
		private String totalPeople;

		private String sum;

		private String luckyNumber;

		private List<ParticipationRecordList> participationRecordList;

		public void setTotalPeople(String totalPeople) {
			this.totalPeople = totalPeople;
		}

		public String getTotalPeople() {
			return this.totalPeople;
		}

		public void setSum(String sum) {
			this.sum = sum;
		}

		public String getSum() {
			return this.sum;
		}

		public void setLuckyNumber(String luckyNumber) {
			this.luckyNumber = luckyNumber;
		}

		public String getLuckyNumber() {
			return this.luckyNumber;
		}

		public void setParticipationRecordList(List<ParticipationRecordList> participationRecordList) {
			this.participationRecordList = participationRecordList;
		}

		public List<ParticipationRecordList> getParticipationRecordList() {
			return this.participationRecordList;
		}

	}

	public class ParticipationRecordList {
		private String dateTime;

		private String userName;

		private String date;

		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}

		public String getDateTime() {
			return this.dateTime;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return this.userName;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getDate() {
			return this.date;
		}

	}
}
