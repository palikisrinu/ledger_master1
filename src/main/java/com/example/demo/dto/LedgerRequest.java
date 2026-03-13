package com.example.demo.dto;

public class LedgerRequest {
//	 private String id;
	 	
		private String date;
	    private String amount;
	    private String commission;
	    private String balance;
	    private String profit;
	    private String loss;
	    private String phonepay;
	    private String expenses;
	    
public String getPhonepay() {
			return phonepay;
		}
		public void setPhonepay(String phonepay) {
			this.phonepay = phonepay;
		}
		public String getExpenses() {
			return expenses;
		}
		public void setExpenses(String expenses) {
			this.expenses = expenses;
		}
		//		public String getId() {
//			return id;
//		}
//		public void setId(String id) {
//			this.id = id;
//		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getCommission() {
			return commission;
		}
		public void setCommission(String commission) {
			this.commission = commission;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getProfit() {
			return profit;
		}
		public void setProfit(String profit) {
			this.profit = profit;
		}
		public String getLoss() {
			return loss;
		}
		public void setLoss(String loss) {
			this.loss = loss;
		}
		
		private String sheetName;
	    public String getSheetName() {
			return sheetName;
		}
		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}
		

		@Override
		public String toString() {
		    return "LedgerRequest{" +
		            "sheetName='" + sheetName + '\'' +
		            ", date='" + date + '\'' +
		            ", amount='" + amount + '\'' +
		            ", commission='" + commission + '\'' +
		            ", balance='" + balance + '\'' +
		            ", profit='" + profit + '\'' +
		            ", loss='" + loss + '\'' +
		            '}';
		}
}
