package com.duetshe.store.trade;

import java.util.Date;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.duetshe.store.beans.Trade;
import com.duetshe.store.util.StringConstants;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class DBTrade {
	private MongoCollection<Document> tradesCollection;
	
	public DBTrade(MongoCollection<Document> tradesCollection) {
		this.tradesCollection = tradesCollection;
	}
	
	public void upsertIntoDB(Trade trade) throws Exception{
		System.out.println("Upsert Trade into DB: " + trade.toString());
		Trade dbTrade = getTradeFromDB(trade);
		
		if(dbTrade != null) {
			if(dbTrade.getVersion() > trade.getVersion()) {
				System.out.println("Discarding this trade, version is not latest");
				throw new Exception("Version lower!");
			} else if(dbTrade.getVersion() == trade.getVersion()) {
				System.out.println("Update scenario! ");
				updateTrade(trade, dbTrade, false);
			} 
		} else { 
			System.out.println("Insert scenario! ");
			insertTrade(trade);
		}
	}
	
	public Trade getTradeFromDB(Trade trade) {
		Trade dbTrade = null;
		/** Bson projectionFields = Projections.fields(
                Projections.include("tradeId", "cpId", "version", "expired", "maturityDate"),
                Projections.excludeId()); **/
		
		Bson filterTrade = Filters.eq(StringConstants.TRADE_ID, trade.getTradeId());
		Bson filterCpId = Filters.eq(StringConstants.CP_ID, trade.getCpId());
		Bson filters = Filters.and(filterTrade, filterCpId);
		
        MongoCursor<Document> cursor = tradesCollection.find(filters)
                //.projection(projectionFields)
                .sort(Sorts.descending(StringConstants.VERSION)).iterator();
        try {
            if(cursor.hasNext()) {
            	Document document = cursor.next();
                System.out.println(document.toJson());
                
                dbTrade = new Trade();
                dbTrade.setTradeId(document.getString(StringConstants.TRADE_ID));
                dbTrade.setCpId(document.getString(StringConstants.CP_ID));
                dbTrade.setBookId(document.getString(StringConstants.BOOK_ID));
                dbTrade.setVersion(document.getInteger(StringConstants.VERSION)); 
                dbTrade.setExpired(document.getString(StringConstants.EXPIRED));
                
                // assuming maturityDate with always be available
                dbTrade.setMaturityDate(document.getDate(StringConstants.MATURITY_DATE));
            }
        // } catch (ParseException e) {
			// System.out.println("Date is not in the expected format");
		} finally {
            cursor.close();
        }
		return dbTrade;
	}
	
	public void updateTrade(Trade newTrade, Trade oldTrade, boolean isExpired) {
		Bson updates = null;
		
		if(!isExpired) {
			updates = Updates.combine(
                Updates.set(StringConstants.BOOK_ID, newTrade.getBookId()),
                Updates.set(StringConstants.MATURITY_DATE, newTrade.getMaturityDate()));
		}
		
		if(oldTrade.getMaturityDate().before(new Date()) || isExpired) {
			System.out.println("Expired trade! ");
			
			updates = Updates.combine(updates, Updates.set(StringConstants.EXPIRED, "true")); // Lazy set to expired
		}
		
        UpdateOptions options = new UpdateOptions().upsert(true);
        
        Bson filterTrade = Filters.eq(StringConstants.TRADE_ID, oldTrade.getTradeId());
		Bson filterCpId = Filters.eq(StringConstants.CP_ID, oldTrade.getCpId());
		Bson filters = Filters.and(filterTrade, filterCpId);
		
        try {
            UpdateResult result = tradesCollection.updateOne(filters, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
//            System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
	}
	
	private void insertTrade(Trade newTrade) {
		
        try {
//        	String maturityDate = DateUtil.getStringFromDate(newTrade.getMaturityDate());
        	tradesCollection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append(StringConstants.TRADE_ID, newTrade.getTradeId())
                    .append(StringConstants.CP_ID, newTrade.getCpId())
                    .append(StringConstants.BOOK_ID, newTrade.getBookId())
                    .append(StringConstants.VERSION, newTrade.getVersion())
                    .append(StringConstants.EXPIRED, newTrade.isExpired())
                    .append(StringConstants.MATURITY_DATE, newTrade.getMaturityDate()));
        	
            System.out.println("Success! Inserted document ");
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
	}
	
	public void updateAllExpiredTrades() {
		Bson filters = Filters.lt(StringConstants.MATURITY_DATE, new Date());
		
        Bson updates = Updates.combine(Updates.set(StringConstants.EXPIRED, "true")); 
        
        try {
            UpdateResult result = tradesCollection.updateMany(filters, updates);
            System.out.println("Modified expired document count: " + result.getModifiedCount());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
	}
}
