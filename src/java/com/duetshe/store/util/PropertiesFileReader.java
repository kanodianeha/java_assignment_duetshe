package com.duetshe.store.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileReader {

	public static Properties readPropertiesFile(String fileName) throws IOException {
	      FileInputStream fis = null;
	      Properties prop = null;
	      try {
	    	  String basePath = PropertiesFileReader.class.getResource("/").getPath();
	         fis = new FileInputStream(basePath + fileName);
	         prop = new Properties();
	         prop.load(fis);
	      } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	      } catch(IOException ioe) {
	         ioe.printStackTrace();
	      } finally {
	    	  if(fis != null) {
	    		  fis.close();
	    	  }
	      }
	      return prop;
	   }
}
