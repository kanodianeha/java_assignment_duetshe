/**
 * 
 */
package com.duetshe.store;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.duetshe.store.beans.Trade;
import com.duetshe.store.dbconnection.MongoDBConnection;
import com.duetshe.store.trade.DBTrade;

/**
 * 
 */
public class MyStore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("Hello World");
		
		MyStore store = new MyStore();
		
		store.scheduleTimer();
	      
		// create a new record, assuming this trade would have flown in from some integrations 
		Trade sampleTrade = store.createSampleTrade("T2", 1, "CP-2", "B1", "20240803");
		
		try {
			new DBTrade(MongoDBConnection.getTradeCollection()).upsertIntoDB(sampleTrade);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void scheduleTimer() {
		Timer timer = new Timer(); 
	    TimerTask task = new SetExpiredTask(); 
	    // scheduling the task for repeated fixed-delay execution, beginning after the specified delay
	    // later can be set for a day, only on a nightly build.
	    timer.schedule(task, 1, 300000);
	}
	
	/**
	 * Temporary method to create a sample trade, ideally should be flowing in from some system 
	 * @return
	 */
	public Trade createSampleTrade(String tradeId, int version, String cpId, String bookId, String matureDateString) { 
		Date maturityDate = new Date();
		try {
			maturityDate = new SimpleDateFormat("yyyyMMdd").parse(matureDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Trade(tradeId, version, cpId, bookId, maturityDate, new Date(), "false");
	}
}
