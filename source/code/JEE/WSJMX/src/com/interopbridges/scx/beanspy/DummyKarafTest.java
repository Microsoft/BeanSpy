package com.interopbridges.scx.beanspy;

import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class DummyKarafTest {

	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/karaf-root");
		HashMap env = new HashMap();
		String[] credentials = new String[] {"admin","admin"};
		env.put(JMXConnector.CREDENTIALS, credentials);
		
		JMXConnector connector = JMXConnectorFactory.connect(url, env);
		
		MBeanServerConnection mbeanServer = connector.getMBeanServerConnection();
		ObjectName systemMBean = new ObjectName("org.apache.activemq:brokerName=amq,type=Broker,*");
	//	systemMBean.
		connector.close();
		
		System.out.println("Done");

	}

}
