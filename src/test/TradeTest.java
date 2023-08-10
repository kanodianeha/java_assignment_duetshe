package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.duetshe.store.MyStore;
import com.duetshe.store.beans.Trade;
import com.duetshe.store.dbconnection.MongoDBConnection;
import com.duetshe.store.trade.DBTrade;

public class TradeTest {
	
	static MyStore store = null; 
	static DBTrade dbTrade = new DBTrade(MongoDBConnection.getTradeCollection());
	
	@BeforeClass
	public static void setup() {
		store = new MyStore();
		Trade sampleTrade1 = store.createSampleTrade("Test1", 1, "CP-1", "B1", "20240803");
		Trade sampleTrade2 = store.createSampleTrade("Test2", 2, "CP-2", "B1", "20200803");
		Trade sampleTrade3 = store.createSampleTrade("Test2", 1, "CP-1", "B1", "20240803");
		Trade sampleTrade4 = store.createSampleTrade("Test3", 3, "CP-3", "B1", "20250803");
		try {
			dbTrade.upsertIntoDB(sampleTrade1);
			dbTrade.upsertIntoDB(sampleTrade2);
			dbTrade.upsertIntoDB(sampleTrade3);
			dbTrade.upsertIntoDB(sampleTrade4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExceptionOnLowerVersion() {
		// if lower version is received, reject the trade and throw exception
		Trade sampleTrade = store.createSampleTrade("Test3", 2, "CP-3", "B1", "20200803");
		try {
			dbTrade.upsertIntoDB(sampleTrade);
			
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(e);
		}
	}
	
	@Test
	public void testUpdateForSameVersion() {
		// if lower version is received, reject the trade and throw exception
		Trade sampleTrade = store.createSampleTrade("Test2", 1, "CP-1", "B2", "20200803");
		try {
			dbTrade.upsertIntoDB(sampleTrade);
			Trade updatedTrade = dbTrade.getTradeFromDB(sampleTrade);
			assertNotNull(updatedTrade);
			assertEquals(updatedTrade.getBookId(), "B2");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testExpired() {
		// if lower version is received, reject the trade and throw exception
		Trade sampleTrade = store.createSampleTrade("Test2", 2, "CP-2", "B1", "20200803");
		try {
			dbTrade.upsertIntoDB(sampleTrade);
			Trade updatedTrade = dbTrade.getTradeFromDB(sampleTrade);
			assertNotNull(updatedTrade);
			assertEquals(updatedTrade.isExpired(), "true");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDown() {
		// TODO: can delete these, but since for now it just updates, does not matter 
	}
}
