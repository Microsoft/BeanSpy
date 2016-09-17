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

package com.interopbridges.scx.util;

import com.interopbridges.scx.configuration.Config;
import com.interopbridges.scx.configuration.ConfigKey;


/**
 * <p>
 * Global Constants.
 * </p>
 *
 * @author Jinlong Li
 */

public class JmxConstant {
    /**
     * <p>
     * The maximum length of the URL is accepted.
     * (The Web Vulnerabilities 2008 training suggested that the URL should not exceed 2048 characters.)
     * </p>
     */
    public static final int URL_LENGTH_LIMITS = 2048;
   
    /**
     * <p>
     * The boolean variable that determines if a JBoss instance version 7 or Wildfly version 8
     * </p>
     */ 
    public static boolean IS_JBOSS7_WILDFLY = false;    
    
    /**
     * <p>
     * BeanSpy Tampering Threat discovered during the STRIDE analysis. 
     * BeanSpy should validate the input, specifically BeanSpy 
     * should validate the input parameters. There are only four expected parameters 
     * for the present implementation: 1) JMXQuery; 2) MaxSize; 3) MaxDepth; 4) MaxCount.
     * </p>
     * 
     * <p>
     * Note: the inputs of parameters are case-sensitive.
     * </p>
     * 
     */
    public static final String STR_JMXQUERY = "JMXQuery";
    public static final String STR_MAXSIZE = "MaxSize";
    public static final String STR_MAXDEPTH = "MaxDepth";
    public static final String STR_MAXCOUNT = "MaxCount";    
    private static final String[] VALID_PARAMETERS = {STR_JMXQUERY, STR_MAXSIZE, STR_MAXDEPTH, STR_MAXCOUNT};
    public static final String[] getValidParameters () { return VALID_PARAMETERS.clone();}
    
    /*
     * <p>
     * BeanSpy supports a HTTP POST request to Invoke MBean methods.
     * The POST request supports only 2 parameters namely MaxSize and MaxTime.
     * The POST URL path supported is '/Invoke' 
     * <p>
     */
    public static final String STR_MAXTIME = "MaxTime";    
    public static final String STR_INVOKE_URL = "/Invoke";
    public static final String[] VALID_INVOKE_PARAMETERS = {STR_MAXSIZE, STR_MAXTIME};
    
    /**
     * <p>
     * XML Element representing the Properties of a MBean
     * </p>
     */
    public static final String PROPERTIES = "Properties";

    /**
     * <p>
     * XML Element representing an ObjectName as a single string
     * </p>
     */   
    public static final String OBJECTNAME = "objectName";

    /**
     * <p>
     * XML Element representing an ObjectName as a broken down list
     * </p>
     */   
    public static final String OBJECTNAME_ELEMENTS = "objectNameElements";
    
    /**
     * <p>
     * XML Element representing the Domain for an ObjectName
     * </p>
     */   
    public static final String DOMAIN = "Domain";

    /**
     * <p>
     * Default maximum recursion depth when parsing MBeans
     * </p>
     */
    public static final int MAXDEPTH = 2;
    
    /**
     * <p>
     * Default maximum number of properties to process when parsing MBeans
     * </p>
     */
    public static final int MAXPROPERTIES = 5000;
     
    /**
     * <p>
     * Default maximum size of the output XML when parsing MBeans. If the size of a XML file
     * exceeds this value but does not exceed the absolute maximum XML file size (ABS_MAX_XML_SIZE), 
     * BeanSpy continue to proceed. However, it will reset the MaxDepth to the minimum which is 0.
     * The same value is used as the default maximum size for the resultant XML when an Invoke
     * POST call is processed.
     * </p>
     */
    public static final int MAXXMLSIZE = 2*1024*1024; // 2MB
    
    /**
     * <p> 
     * The absolute maximum size for the output. For security reasons, if the size of the output
     * XML file exceeds this value, BeanSpy throws an exception and terminates.
     * </P>
      *
      * <p>
     * Get the configuration data from resources.configuration.config file. This value in the 
     * configuration file can be changed by an end user.
     * <p/>
     * 
     * <p>
     * If the values is missing in the configuration file, use the default value which is 4M.
     * <p>
     * 
      */
     public static final int ABS_MAX_XML_SIZE = (new Config(ConfigKey.ABS_MAX_XML_SIZE)).getValue() == null? 
             4*1024*1024:Integer.parseInt((new Config(ConfigKey.ABS_MAX_XML_SIZE)).getValue());
    
     /**
      * <p>
      * Default timeout duration in milliseconds for a method call, 
      * this is essentially the amount of time to wait before interrupting
      * the process. 
      * </p>
      */
     public static final int MAX_INVOKE_TIMEOUT=5000;

     /**
      * <p>
      * Default maximum size of the response XML
      * </p>
      */
     public static final int MAX_XML_SIZE=10000;

     /**
      * <p>
      * Default time in milliseconds to wait before peeking to 
      * see if the invoked method has terminated.
      * </p>
      */
     public static final int MAX_PEEK_TIME=1000;

     /**
      * <p>
      * XML Element representing the top level node for a response
      * to a method invoke request.
      * </p>
      */   
     public static final String STR_INVOKERESPONSE ="InvokeResponse";
     
     /**
      * <p>
      * XML Element representing the node for the result of
      * a method invoke request.
      * </p>
      */   
     public static final String STR_RESULT ="Result";
     
     /**
      * <p>
      * XML Element representing the node for the response of
      * a method invoke request.
      * </p>
      */   
     public static final String STR_RESPONSE ="Response";
     
     /**
      * <p>
      * The absolute smallest response message size from a invoke request.
      * The message would be for a successful invocation of a void method 
      * that takes no parameter.
      * </p>
      * <p>
      * &lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&rt;
      * &lt;InvokeResponse&rt;
      * &lt;Result&rt;SUCCESS&lt;/Result&rt;
      * &lt;/InvokeResponse&rt;
      * </p>
      */   
     public static final int ABS_SMALLEST_INVOKE_RESPONSE_SIZE =95;
     
     /**
      * <p>
      * XML Element representing the node containing the reason 
      * for the method invoke error.
      * </p>
      */   
     public static final String STR_ERRORREASON ="ErrorReason";
   
     /**
      * <p>
      * String constant representing the successful 
      * invoke request.
      * </p>
      */   
     public static final String STR_SUCCESS ="SUCCESS";
     
     /**
      * <p>
      * String constant representing the unsuccessful 
      * invoke request.
      * </p>
      */   
     public static final String STR_ERROR ="ERROR";
     
     /**
      * <p>
      * The maximum allowed length of the POST input request 
      * </p>
      */
     public static final int MAX_POST_INPUT_XML_SIZE = 8192;


     /*
      * The POST input XML structure has the following format
      * These constants are used to decode the XML document 
      */
     public static final String STR_POST_XML_INVOKE ="/Invoke";
     public static final String STR_POST_XML_BEANOBJECTNAME ="/Invoke/BeanObjectName";
     public static final String STR_POST_XML_METHOD ="/Invoke/Method";
     public static final String STR_POST_XML_PARAM ="Param";
     public static final String STR_METHOD_NAME_ATTRIBUTE = "name";
     public static final String STR_PARAM_NAME_ATTRIBUTE = "name";
     public static final String STR_PARAM_TYPE_ATTRIBUTE = "type";

     /**
      * <p>
      * A value denoting an invalid HashCode for a class.
      * </p>
      */
     public static final int INVALID_MBEANSERVER_HASHCODE = -1;

     /**
      * <p>
      * Constants for the XML builder
      * <MBean Name="mymbean$Class">
      *   <Properties>
      *      <Attribute Name="Var$1" type="String">hello</Attribute>
      *   </Properties>
      * </MBean>
      * </p>
      */
     public static final String XML_TRANSFORMER_MBEAN_TAG = "MBean";
     public static final String XML_TRANSFORMER_MBEAN_NAME_ATTRRIBUTE = "Name";
     public static final String XML_TRANSFORMER_MBEAN_PROPERTY_TAG = "Property";
     public static final String XML_TRANSFORMER_MBEAN_PROPERTY_NAME_ATTRRIBUTE = "Name";

     /**
      * <p>
      * Constants for JMXAbstraction names.
      * </p>
      */
     public static final String JBOSS_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.JBossJMXAbstraction";
     public static final String TOMCAT_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.TomcatJMXAbstraction";
     public static final String WEBSPHERE_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.WebSphereJMXAbstraction";
     public static final String WEBLOGIC_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.WeblogicRuntimeJMXAbstraction";
     public static final String JDK_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.JdkJMXAbstraction";
     public static final String KARAF_MBEAN_STORE_NAME = "com.interopbridges.scx.jmx.KarafJMXAbstraction";
}
