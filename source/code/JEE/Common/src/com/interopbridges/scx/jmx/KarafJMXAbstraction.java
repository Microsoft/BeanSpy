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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.naming.NamingException;

import com.interopbridges.scx.ScxException;


/**
 * <p>
 * Karaf 2.2.3 for the Runtime JMX Store
 * </p>
 * 
 * @author Geoff Erasmus
 */
public class KarafJMXAbstraction implements IJMX {

    /**
     * <p>
     * Connection to the MBeanServer
     * </p>
     */
    private MBeanServerConnection _server;

    /**
     * <p>
     * Constructor. The constructor sets the MBeanServer to be used by this class. 
     * </p>
     *
     * @param server
     *             An instance of a MBeanServer to be used by this class.
     * 
     */
    public KarafJMXAbstraction(MBeanServer server) throws 
                NamingException
    {
       _server = server; 
    }

    /**
     * <p>
     * Default Constructor. The constructor attempts to locate a reference to the Weblogic runtime JMX MBean Store
     * it does not use specific API calls but rather does a JNDI lookup to retrieve the reference to the  
     * MBeanServer. 
     * </p>
     * 
     * @throws ScxException
     *             The JNDI lookup could not return an instance of a MBeanServer.
     */
    public KarafJMXAbstraction() throws
                NamingException,
                ScxException
    {
        /*
    	try
        {
        	//JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
        	JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/karaf-root");
    		HashMap env = new HashMap();
    		String[] credentials = new String[] {"admin","admin"};
    		env.put(JMXConnector.CREDENTIALS, credentials);
    		
    		JMXConnector connector = JMXConnectorFactory.connect(url, env);
    		//JMXConnector connector = JMXConnectorFactory.connect(url, null);
    		_server = connector.getMBeanServerConnection();

        }
        catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	_server = ManagementFactory.getPlatformMBeanServer();
    	
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getAttribute(javax.management.ObjectName,
     * java.lang.String)
     */
    public Object getAttribute(ObjectName name, String attribute)
            throws MBeanException, AttributeNotFoundException,
            InstanceNotFoundException, ReflectionException, IOException {
        return this._server.getAttribute(name, attribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanServerID()
     */
    public int getMBeanServerID()
    {
        return this._server.hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanCount()
     */
    public Integer getMBeanCount()
            throws IOException
    {
        return this._server.getMBeanCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#getMBeanInfo(javax.management.ObjectName)
     */
    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException, IOException {
        return this._server.getMBeanInfo(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#isStandAloneJmxStore()
     */
    public boolean isStandAloneJmxStore()
    {
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#queryMBeans(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    public Set<ObjectInstance> queryMBeans(ObjectName name, QueryExp query) 
            throws IOException
    {
        return this._server.queryMBeans(name, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#queryNames(javax.management.ObjectName,
     * javax.management.QueryExp)
     */
    public Set<ObjectName> queryNames(ObjectName name, QueryExp query) 
            throws IOException
    {
        return this._server.queryNames(name, query);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#registerMbean(java.lang.Object,
     * javax.management.ObjectName)
     */
    public void registerMBean(Object bean, ObjectName keys)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, IOException {
    /* do nothing */    
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#verifyStoreConnection()
     */
    public boolean verifyStoreConnection()
    {
        // No additional checks are needed for this platform to
        // verify the JMX store connection
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.interopbridges.scx.jmx.IJMX#invoke(ObjectName, String, Object[], String[])
     */
    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature)
    throws InstanceNotFoundException, ReflectionException, MBeanException, IOException
    {
        return this._server.invoke(name, operationName, params, signature);
    }
}
