/**
 * Copyright (c) Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use 
 * this file except in compliance with the License. You may obtain a copy of the 
 * License at http://www.apache.org/licenses/LICENSE-2.0.
 *  
 * THIS CODE IS PROVIDED *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS 
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION 
 * ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE, 
 * MERCHANTABLITY OR NON-INFRINGEMENT. 
 *
 * See the Apache Version 2.0 License for specific language governing 
 * permissions and limitations under the License.
 */

package com.interopbridges.scx.jeeinfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.ObjectInstance;
import javax.management.ObjectName;

import com.interopbridges.scx.jeestats.GenericStatistics;
import com.interopbridges.scx.jeestats.Statistic;
import com.interopbridges.scx.jeestats.StatisticMethod;
import com.interopbridges.scx.jmx.IJMX;
import com.interopbridges.scx.jmx.JBossJMXAbstraction;
import com.interopbridges.scx.jmx.JdkJMXAbstraction;
import com.interopbridges.scx.jmx.JmxStores;
import com.interopbridges.scx.jmx.WebSphereJMXAbstraction;
import com.interopbridges.scx.jmx.WeblogicRuntimeJMXAbstraction;
import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;


/**
 * <p>
 * Retrieves the Java JVM values from the ManagementFactory and the JEE server
 * name and version from the JEE MBeans via JMX.
 * The management factory  platform MXBean(s) represents the management 
 * interface of a component of the Java virtual machine. The platform MXBeans 
 * were introduces in J2SE 5 and therefore none of the statistic modules
 * will work on Java versions prior to 1.5. 
 * </p>
 * 
 * <p>
 * The return values are read only and cannot be modified.
 * </p>
 * 
 * @author Geoff Erasmus
 * 
 */
public class JeeServerInfo extends GenericStatistics
{
    
    /**
     * <p>
     * Group name for this type of statistic
     * </p>
     */
    private static final String StatisticGroupname         = "JEEServer";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Name
     * </p>
     */
    private static final String AppServerName         = "AppServerName";
    
    /**
     * <p>
     * Name identifying the statistical value containing the JVM Version
     * </p>
     */
    private static final String AppServerVersion      = "AppServerVersion";

    /**
     * <p>
     * Logger for the class.
     * </p>
     */
    protected ILogger _logger;
    
    /**
     * <p>
     * Default constructor
     * </p>
     */
    public JeeServerInfo()
    {
       super(StatisticGroupname);    
       this._logger = LoggingFactory.getLogger();       
    }
    
    /**
     * <p>
     * Retrieves the name of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getAppServerName()
    {
        List<IJMX> lijmx = JmxStores.getListOfJmxStoreAbstractions();
        List<String> names = JmxStores.getListOfJmxStoreAbstractionNames();
        String ServerName=null;

        /*
         * We need to cycle through the JMX stores to find the platform specific values
         * one we find the correct store break out of the loop and return the value
         */
        if(lijmx.size() > 0)
        {
            // If there are multiple MBean stores the first one is a specific application server instance
            // and the second would be the JDK MBean store.
            IJMX ijmx = lijmx.get(0);
            
            if(names.size() > 0)
            {
                String storename = names.get(0);
            
                if(storename.compareTo(JmxConstant.JBOSS_MBEAN_STORE_NAME)==0)
				{
					// If there is a jboss store we determine if it is a jboss 7 or wildfly instance vs jboss 4/5/6
					// Because there is no JMX accessible property detailing server name for jboss 7 or widlfly
					// we have to use the version to determine correct label to assign
					// Version 7 = JBoss AS Version 8 = Wildfly
					if(JmxConstant.IS_JBOSS7_WILDFLY == true)
					{
						String serverVersion = getJMXAttribute(ijmx, "jboss.as:management-root=server,*", "releaseVersion");
						int majorVersion = Character.getNumericValue(serverVersion.charAt(0));

						if(majorVersion == 7)
							ServerName = "JBoss AS";
						else
						if(majorVersion == 8)
							ServerName = "Wildfly";
					}
					else
						ServerName = getJMXAttribute(ijmx, "jboss.management.local:j2eeType=J2EEServer,*", "serverVendor");
                }
                else
                if(storename.compareTo(JmxConstant.TOMCAT_MBEAN_STORE_NAME)==0)
                {
                    ServerName = getJMXAttribute(ijmx, "Catalina:type=Server,*", "serverInfo");
                }
                else
                if(storename.compareTo(JmxConstant.WEBLOGIC_MBEAN_STORE_NAME)==0)
                {
                    ServerName = getJMXAttribute(ijmx, "com.bea:Type=ServerRuntime,*", "WeblogicVersion");
                }
                else
                if(storename.compareTo(JmxConstant.WEBSPHERE_MBEAN_STORE_NAME)==0)
                {
                    ServerName = getJMXAttribute(ijmx, "WebSphere:j2eeType=J2EEServer,*", "platformName");
                }
                else
                    if(storename.compareTo(JmxConstant.KARAF_MBEAN_STORE_NAME)==0)
                    {
                    	ServerName = "JBoss A-MQ (on Karaf)";
                    }
            }            
        }
        
        return   ServerName!=null ? new Statistic(AppServerName, String.class, ServerName) : null;
    }
    
    /**
     * <p>
     * Retrieves the version of the JVM from the Java management factory
     * </p>
     * 
     * <p>
     * The JEEStatistic annotation is used as a place-holder to distinguish
     * which methods will be returned by the getStats() method  
     * </p>
     * 
     * @return Statistic containing the value of the 
     * loaded class count returned by the management factory
     *
     * @since Java 1.5
     * 
     * @see java.lang.management.ClassLoadingMXBean
     */    
    @StatisticMethod
    public Statistic getVersion()
    {
        List<IJMX> lijmx = JmxStores.getListOfJmxStoreAbstractions();
        List<String> names = JmxStores.getListOfJmxStoreAbstractionNames();
        String ServerVersion=null;
        
        if(lijmx.size() > 0)
        {
            // If there are multiple MBean stores the first one is a specific application server instance
            // and the second would be the JDK MBean store.
            IJMX ijmx = lijmx.get(0);
            
            if(names.size() > 0)
            {
                String storename = names.get(0);
            
                if(storename.compareTo(JmxConstant.JBOSS_MBEAN_STORE_NAME)==0)
                {
					// If there is a jboss store we determine if it is a jboss 7 or wildfly instance vs jboss 4/5/6

					if(JmxConstant.IS_JBOSS7_WILDFLY == true)
					{
						ServerVersion = getJMXAttribute(ijmx, "jboss.as:management-root=server,*", "releaseVersion");
					}
					else
						ServerVersion = getJMXAttribute(ijmx, "jboss.management.local:j2eeType=J2EEServer,*", "serverVersion");
				}
                else
                if(storename.compareTo(JmxConstant.WEBSPHERE_MBEAN_STORE_NAME)==0)
                {
                    ServerVersion = getJMXAttribute(ijmx, "WebSphere:j2eeType=J2EEServer,*", "platformVersion");
                }
                else
                if(storename.compareTo(JmxConstant.KARAF_MBEAN_STORE_NAME)==0)
                {
                	ServerVersion = getJMXAttribute(ijmx, "org.apache.activemq:brokerName=*,type=Broker", "BrokerVersion");
                }
            }            
        }
        return   ServerVersion!=null ? new Statistic(AppServerVersion, String.class, ServerVersion) : null;
    }

    /**
     * <p>
     * Retrieves an attribute from a JMX store
     * </p>
     * 
     * @param store
     *          The JMX store to query.
     * @param MBeanQuery
     *          The JMX query specifying the MBean from 
     *          which to retrieve the attribute.
     * @param attributeName
     *          The name of the attribute to query.
     * 
     * @return the attribute value of null if an error occurs 
     */    
    private String getJMXAttribute(IJMX store, String MBeanQuery, String attributeName)
    {
        String attrVal=null;
        try
        {
            ObjectName objName = new ObjectName(MBeanQuery);
            Set<ObjectInstance> oi = store.queryMBeans(objName,null);
            /* 
             * There is a remote possibility that there can be multiple MBeans returned by the query
             * We will not iterate through all of them, but only use the first MBean returned.  
             */
            attrVal = store.getAttribute(oi.iterator().next().getObjectName(),attributeName).toString();
        }
        catch(Exception e)
        {
            this._logger.finer(new StringBuffer(
                "Unable to retrieve the attribute (").
                append(attributeName).append(") for the ").
                append(store.getClass().getName()).
                append(" Mbean store. ").
                append("Error reason:").
                append(e.getMessage()).toString());
        }
        return attrVal;
    }
}
