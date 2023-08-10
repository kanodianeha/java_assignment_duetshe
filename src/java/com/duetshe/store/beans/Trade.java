package com.duetshe.store.beans;

import java.util.Date;

public class Trade {
	private String tradeId; 
	
	private int version; 
	
	private String cpId; 
	
	// Shouldn't all these ids be of type long? Why String!? 
	private String bookId; 
	
	private String expired; 
	
	private Date maturityDate; 
	
	private Date createdDate;

	/**
	 * There could be many other constructors, for now just this!
	 */
	public Trade(String tradeId, int version, String cpId, String bookId, Date maturityDate, 
			Date createdDate, String expired) {
		this.tradeId = tradeId; 
		this.version = version; 
		this.cpId = cpId; 
		this.bookId = bookId; 
		this.maturityDate = maturityDate; 
		this.createdDate = createdDate; 
		this.expired = expired; 
	}
	
	public Trade(String tradeId, int version, String cpId, String bookId, Date maturityDate) {
		this.tradeId = tradeId; 
		this.version = version; 
		this.cpId = cpId; 
		this.bookId = bookId; 
		this.maturityDate = maturityDate; 
		this.createdDate = new Date(); 
		this.expired = "false"; 
	}
	
	public Trade() {
		
	}
	
	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String isExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	} 

	public String toString() { 
		return new StringBuilder("TradeId: " + tradeId)
				.append(" Version: " + version)
				.append(" cpId: " + cpId)
				.append(" bookId: " + bookId)
				.append(" maturityDate: " + maturityDate.toString())
				.append(" expired: " + expired).toString();
	}
	
}
