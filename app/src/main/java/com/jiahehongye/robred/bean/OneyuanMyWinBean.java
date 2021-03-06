package com.jiahehongye.robred.bean;

import java.util.List;

public class OneyuanMyWinBean {

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

		private List<Addresss> address;

		public void setProduct(List<Product> product) {
			this.product = product;
		}

		public List<Product> getProduct() {
			return this.product;
		}

		public void setAddress(List<Addresss> address) {
			this.address = address;
		}

		public List<Addresss> getAddress() {
			return this.address;
		}

	}

	public class Addresss {
		private String county;

		private String province;

		private String realName;

		private String detailedAddress;

		private String addressId;

		private String city;

		private String mobile;

		public void setCounty(String county){
		this.county = county;
		}
		public String getCounty(){
		return this.county;
		}
		public void setProvince(String province){
		this.province = province;
		}
		public String getProvince(){
		return this.province;
		}
		public void setRealName(String realName){
		this.realName = realName;
		}
		public String getRealName(){
		return this.realName;
		}
		public void setDetailedAddress(String detailedAddress){
		this.detailedAddress = detailedAddress;
		}
		public String getDetailedAddress(){
		return this.detailedAddress;
		}
		public void setAddressId(String addressId){
		this.addressId = addressId;
		}
		public String getAddressId(){
		return this.addressId;
		}
		public void setCity(String city){
		this.city = city;
		}
		public String getCity(){
		return this.city;
		}
		public void setMobile(String mobile){
		this.mobile = mobile;
		}
		public String getMobile(){
		return this.mobile;
		}

		}

	public class Product {
		private String noStage;

		private String name;

		private String image;

		private String productId;
		
		private String isAddress;

		public String getIsAddress() {
			return isAddress;
		}

		public void setIsAddress(String isAddress) {
			this.isAddress = isAddress;
		}

		public void setNoStage(String noStage) {
			this.noStage = noStage;
		}

		public String getNoStage() {
			return this.noStage;
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

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getProductId() {
			return this.productId;
		}

	}

}
