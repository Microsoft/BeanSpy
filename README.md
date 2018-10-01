# Overview

BeanSpy is an open source technology provided by Microsoft for exposing JMX information from Java EE application servers through HTTP queries.  It currently supports the following Java EE application servers and web containers:
- Tomcat 5.5, 6, 7
- JBoss 4.2, 5.1, 6
- WebSphere 6.1, 7
- WebLogic 10gR3, 11gR1

The binary package contains 4 BeanSpy files digitally signed by Microsoft:
- BeanSpy.ear
- BeanSpy.war
- BeanSpy.HTTP.NoAuth.ear
- BeanSpy.HTTP.NoAuth.war

BeanSpy.ear and BeanSpy.war are used in cases where the Java EE application server or web container is using HTTPs transport with basic auth servlet authentication.  The HTTP.NoAuth version is used in HTTP transport without servlet authentication.  

# How to deploy BeanSpy

If your Java EE application server or web container is using HTTPs with basic auth:

 - Deploy `BeanSpy.ear` to `JBoss/WebSphere/WebLogic`
 - Deploy `BeanSpy.war` to Tomcat. *For Tomcat/JBoss/WebSphere, BeanSpy requires a user role "monitoring" for querying JVM and MBeans, and a user role "invoke" for invoking methods on MBeans, for WebLogic, BeanSpy requires "monitors" role for quering JVM and MBeans and "operators" role for invoking methods on MBeans.*

If your Java EE application server or web container is using HTTP without authentication:

 - Rename `BeanSpy.HTTP.NoAuth.ear` to `BeanSpy.ear`
 - Deploy the above file to `JBoss/WebSphere/WebLogic`
 - Rename `BeanSpy.HTTP.NoAuth.war` to `BeanSpy.war`
 - Deploy the above file to Tomcat. 

# How to query BeanSpy

Once BeanSpy is deployed, you can query it through the browser or invoke method on MBeans using HTTP POST. The following are a few examples.  For complete schema, please refer to the schema file in the source package. 

1. To query JVM stats for memory, Garbage Collection, class loading, and threads, use:
    `http://<host:port>/BeanSpy/Stats`
2. To query configuration and version info about the Java EE application server or web container, use:
    `http://<host:port>/BeanSpy/Stats/Info` 
3. To query specific MBeans, use:
    `http://<host:port>/BeanSpy/MBeans?JMXQuery=<BeanObjectName>`
Here, BeanObjectName conforms to JMX standard, for example, `com.contoso:Type=J2EEApplication,*`
4. To invoke a method on a MBean, use HTTP POST:
    http://<host:port>/BeanSpy/MBeans/Invoke 
The HTTP post body should look like this: 

```
<Invoke>
    <BeanObjectName>com.contoso:name=CustomMBeanName</BeanObjectName>
    <Method name="FooMethod">
        <Param name="p0" type="java.lang.Integer">400</Param>
        <Param name="p1" type="java.lang.String">bar</Param>
    </Method>
</Invoke>
```

Only primitive parameters are supported.  Composite data types are not supported. 

# Additional parameters for BeanSpy queries 

There are a few parameters you can add to the URL to control the performance of your BeanSpy query.  
________________________________________
`MaxDepth`
Default maximum recursion depth when parsing MBeans 
Default value: 10

Modification: can be modified by adding parameters “MaxDepth” (case sensitive) in the URL. 
For example: `/MBeans?JMXQuery=com.microsoft.scx:jmxType=operationCall&MaxDepth=12`.
________________________________________
`MaxCount`
Default maximum number of properties to process when parsing MBeans 
Default value: 500

Modification: can be modified by adding parameters “MaxCount” (case sensitive) in the URL. 
For example: `/MBeans?JMXQuery=com.microsoft.scx:jmxType=operationCall&MaxCount=200`.
________________________________________
`MaxSize`
Default maximum size of the output XML when parsing MBeans. If the size of a XML file exceeds this value but does not exceed the absolute maximum XML file size (ABS_MAX_XML_SIZE), BeanSpy continues to proceed. However, it will reset the MaxDepth to the minimum which is 0. The same value is used as the default maximum size for the resultant XML when an Invoke POST call is processed. 
Default value: 2097152 (2MB)

Modification: can be modified by adding parameters “MaxSize” (case sensitive) in the URL. 
For example: `/MBeans?JMXQuery=com.microsoft.scx:jmxType=operationCall&MaxSize=20000`.
________________________________________
`ABS_MAX_XML_SIZE`
The absolute maximum size for the output. If the size of the output XML file exceeds this value, BeanSpy throws an exception and terminates. 
If the value is missing in the configuration file, use the default value which is 4M. 
Default value: 4184304 (4MB)

Modification: can be modified by the end user in resources.configuration.config file.
________________________________________

# How to build BeanSpy
## Pre-requisites
To build the BeanSpy servlet application the following external files are required, download 
the respective packages and copy the files to the required locations.

The Tomcat servlet-api file is required
  destination file: \source\ext\Tomcat\servlet-api.jar 
  this file is available from http://tomcat.apache.org/ and is bundled in most Tomcat binary distributions.


To include the build and execution of the unit tests the following junit jar files are required
  destination file: \source\ext\junit\junit-4.x.jar 
  destination file: \source\ext\junit\junit-dep-4.x.jar 
  these files are available from http://www.junit.org/


## To build the BeanSpy project
Ant and java is used to build and package the java code into WAR and EAR files as well
as build and execute the unit tests.
Ant is available from http://ant.apache.org/
To build the servlet, run "ant" from the /build folder.
To build and execute the junit tests, run "ant clean ears testrun" from the /build folder.


The build.xml has support for both debug and release builds this is controlled by command line switches to 
ant, the default build is for release purposes to build the debug version run ant with the following switch
"-Ddebug=true" i.e. "ant clean ears testrun -Ddebug=true".

There are other build switches that can be used to control, java version, product version, verbosity 
and javadocs.
