package com.interopbridges.scx.beanspy;

import java.io.IOException;
import java.util.HashMap;

import com.interopbridges.scx.ScxException;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.mbeans.MBeanGetter;
import com.interopbridges.scx.util.JmxURLCheck;
import com.interopbridges.scx.util.MsVersion;

public class BeanSpyMainTest {

	 /**
     * <p>
     * Logger for the class.
     * </p>
     */
	 private static ILogger _logger = LoggingFactory.getLogger();

	
	  /**
     * <p>
     * Interface to getting the desired MBeans from the JMX Store (as XML).
     * </p>
     */
    protected MBeanGetter     _mbeanAccessor;

    public BeanSpyMainTest(){
    	
    	 //this._logger = LoggingFactory.getLogger();
    	 _mbeanAccessor = new MBeanGetter(JmxStores.getListOfJmxStoreAbstractions());
    	 
    	 
    	 String JMXQuery = "org.apache.activemq:type=Broker,*";
    	 HashMap<String, String[]> Params = null;
    	 try {
			String xml = _mbeanAccessor.getMBeansAsXml(JMXQuery, Params)
			         .toString();
			System.out.println(xml);
		} catch (ScxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	 
    	 
    	 
    	 
    	 
    	 JmxStores.clearListOfJmxStores();
    	
    }
    
    
	public static void main(String[] args) {
		_logger.info(new StringBuffer("Initializing BeanSpy (Build: ")
          .append(MsVersion.VERSION).append(", Label: ").append(
                  MsVersion.LABEL_NAME).append(", BuildDate: ").append(
                  MsVersion.BUILD_DATE).append(")").toString());
		_logger.info("contextInitialized: connecting to JMX Stores");
  JmxStores.connectToJmxStores();
		new BeanSpyMainTest();
	}

}
