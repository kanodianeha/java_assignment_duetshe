package com.duetshe.store;

import java.util.TimerTask;

import com.duetshe.store.dbconnection.MongoDBConnection;
import com.duetshe.store.trade.DBTrade;

class SetExpiredTask extends TimerTask {
   public void run() {
      System.out.println("Task is running");
      new DBTrade(MongoDBConnection.getTradeCollection()).updateAllExpiredTrades();
   }
}