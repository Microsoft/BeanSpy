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

package com.interopbridges.scx.jmx;

import java.util.ArrayList;
import java.util.List;

import com.interopbridges.scx.log.ILogger;
import com.interopbridges.scx.log.LoggingFactory;
import com.interopbridges.scx.util.JmxConstant;

/**
 * <p>
 * This class contains one static list of strings naming the list of JMX stores
 * that the servlet connected to at start-up time.
 * </p>
 * 
 * @author Christopher Crammond
 * 
 */
public class JmxStores
{
    /**
     * <p>
     * Semaphore to prevent race conditions
     * </p>
     */
    private static Object       semaphore      = new Object();

    /**
     * <p>
     * Names of the valid
     * </p>
     */
    private static List<IJMX>   _iJmxStores    = new ArrayList<IJMX>();

    /**
     * <p>
     * Names of the valid
     * </p>
     */
    private static List<String> _jmxStoreNames = new ArrayList<String>();

    /**
     * <p>
     * Clear the list of connected JMX Stores.
     * </p>
     */
    public static void clearListOfJmxStores()
    {
        synchronized (semaphore)
        {
            _iJmxStores.clear();
            _jmxStoreNames.clear();
        }
    }

    /**
     * <p>
     * A start-up time, this should be called to connect the various JMX stores.
     * </p>
     */
    public static void connectToJmxStores()
    {
        addStoreToJmxStores(JmxConstant.JBOSS_MBEAN_STORE_NAME,null);
        addStoreToJmxStores(JmxConstant.TOMCAT_MBEAN_STORE_NAME,null);
        addStoreToJmxStores(JmxConstant.WEBSPHERE_MBEAN_STORE_NAME,null);
        addStoreToJmxStores(JmxConstant.WEBLOGIC_MBEAN_STORE_NAME,null);
        addStoreToJmxStores(JmxConstant.KARAF_MBEAN_STORE_NAME,null);
        /*
         * The JdkJMXAbstraction needs to be the last entry in the list to avoid the 
         * version information being processed from the JdkJMXAbstraction MBean store.
         */
        addStoreToJmxStores(JmxConstant.JDK_MBEAN_STORE_NAME,null);
    }

    /**
     * This should be called to add a single JMX store
     * to the JMX store collection.
     * </p>
     * 
     * @param JMXAbstraction
     *            The name of a JMXAbstration class that implements 
     *            the IJMX interface.
     * @param MBeanServerClass
     *            The name of a platform specific class containing
     *            methods which return the MBean Server.   
     *            
     */
    public static void addStoreToJmxStores(String JMXAbstraction, String MBeanServerClass)
    {
        synchronized (semaphore)
        {
            ILogger _logger = LoggingFactory.getLogger();
            
            /*
             * Attempt to dynamically load the appropriate class. This approach is
             * necessary because there is no rock-solid way to know which JMX
             * Stores are available. Rather than try to implement some complex
             * logic of guessing, a more reliable approach would be to just try
             * to load the class and catch the exception (if any).
             */
            String jmxStoreToLoad = JMXAbstraction;
            try
            {
                IJMX jmxStoreAbstraction;
                if(MBeanServerClass==null)
                {
                    jmxStoreAbstraction = (IJMX) Class.forName(jmxStoreToLoad)
                            .getConstructor((Class<?>[]) null).newInstance(
                                    (Object[]) null);
                }
                else
                {
                    jmxStoreAbstraction = (IJMX) Class.forName(jmxStoreToLoad)
                            .getConstructor(new Class[]{String.class})
                            .newInstance(new Object[]{MBeanServerClass});
                }
                
                addStoreToJmxStores(jmxStoreAbstraction);
            }
            catch (Exception e)
            {
                _logger.warning(new StringBuffer("Failed to connect to ")
                        .append(jmxStoreToLoad).append(" JMX Store")
                        .toString());
            }
        }
    }
    
    /**
     * This should be called to add a single JMX store
     * to the JMX store collection.
     * </p>
     * 
     * @param objJMXAbstraction
     *            A JMXAbstration object that implements 
     *            the IJMX interface.
     *            
     */
    public static void addStoreToJmxStores(IJMX objJMXAbstraction)
    {
        addNamedStoreToJmxStores(objJMXAbstraction,objJMXAbstraction.getClass().getName());
    }
    
    /**
     * This should be called to add a single Named JMX store
     * to the JMX store collection.
     * </p>
     * 
     * @param objJMXAbstraction
     *            A JMXAbstration object that implements 
     *            the IJMX interface.
     * @param storeName
     *            The associated with the JMXAbstration object.    
     */
    public static void addNamedStoreToJmxStores(IJMX objJMXAbstraction,String storeName)
    {
        synchronized (semaphore)
        {
            ILogger _logger = LoggingFactory.getLogger();
            
            if (objJMXAbstraction.verifyStoreConnection())
            {
                /*
                 * For the stores with a valid connection, only add the
                 * real stores to the list of stores. However, all of
                 * the names are exposed because on the Operations
                 * Manager side there is a need to if store will "work"
                 * for the specific application servers
                 */

                if (objJMXAbstraction.isStandAloneJmxStore())
                {
                    /*
                     * If the JMX store already exists don't add it, this is a to cater for 
                     * application servers that use the same underlying MBeanServer.
                     * e.g. JBoss 6 uses the JDK JMX store so to prevent duplicate MBeans
                     * we don't add the duplicate JMX store. 
                     */
                    for(int i=0;i<_iJmxStores.size();i++)
                    {
                       if(_iJmxStores.get(i).getMBeanServerID() == objJMXAbstraction.getMBeanServerID())
                       {
                           /* 
                            * Remove any matching store and add the new one instead of not adding the bew store, 
                            * this is done so that the Jdk JMX store will take precedence over other matching 
                            * stores. This is done because there is specific logic in the Jdk JMX store to 
                            * handle context switching for class loading.
                            */
                           _iJmxStores.remove(i);

                           /*
                            * We have found and removed the duplicate MBeanServer, there is no need to continue 
                            * looking for more matching items.
                            */
                           break;
                       }
                    }
                     _iJmxStores.add(objJMXAbstraction);
                }
                _jmxStoreNames.add(storeName);
                _logger.info(new StringBuffer(
                        "Added JMX Store adapter ").append(
                                storeName).toString());
            }
            else
            {
                _logger.warning(new StringBuffer("Failed to verify connection to ")
                        .append(storeName)
                        .append(" JMX Store").toString());

            }
        }
    }
    
    /**
     * <p>  
     * Get a list of the connected JMX stores that have a valid connection.
     * </p>
     * 
     * @return List of JMX Store Abstractions
     */
    public static List<IJMX> getListOfJmxStoreAbstractions()
    {
        synchronized (semaphore)
        {
            return _iJmxStores;
        }
    }
    
    /**
     *  <p>
     * Get a list of the names of the connected JMX stores that have a valid connection.
     * </p>
     * 
     * @return List of JMX Store Names
     */
    public static List<String> getListOfJmxStoreAbstractionNames()
    {
        synchronized (semaphore)
        {
            return _jmxStoreNames;
        }
    }
}
