import groovy.grape.Grape
@Grab(group='org.apache.felix', module= 'org.apache.felix.framework', version= '4.2.1')

import org.osgi.service.startlevel.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.felix.framework.util.Util;
import org.apache.felix.framework.FrameworkFactory
import org.apache.felix.framework.Felix

public class WiperDogBoot{
	//~ private static Framework m_fwk = null;
	private static GroovyClassLoader gcl = new GroovyClassLoader();
	/**
     * The property name used to specify an URL to the system
     * property file.
    **/
	public static final String SYSTEM_PROPERTIES_PROP = "felix.system.properties";
	/**
     * The default name used for the system properties file.
    **/
	public static final String SYSTEM_PROPERTIES_FILE_VALUE = "system.properties";
	/**
     * Name of the configuration directory.
     */
    public static final String CONFIG_DIRECTORY = "conf";
    /**
     * The default name used for the configuration properties file.
     */
    public static final String CONFIG_PROPERTIES_FILE_VALUE = "config.properties";
    /**
     * The property name used to specify an URL to the configuration
     * property file to be used for the created the framework instance.
     */
    public static final String CONFIG_PROPERTIES_PROP = "felix.config.properties";
	
	public static void main(String[] args) throws Exception {
        
		// Create an instance of the framework.
		Properties configProps = new Properties()
		def confDir = new File(System.getProperty("felix.home"), CONFIG_DIRECTORY);
		configProps.load(new FileInputStream(new File(confDir, CONFIG_PROPERTIES_FILE_VALUE)))
		
		FrameworkFactory factory = new FrameworkFactory()
		def m_fwk = factory.newFramework(configProps);
        // Initialize the framework and start
        m_fwk.init();
		m_fwk.start();
		
		//Install and start bundle			
		def felix_home = System.getProperty("felix.home").replace("\\", "/");
		def context = m_fwk.getBundleContext()
		context.installBundle("file://" + new File(felix_home).getAbsolutePath() + "/lib/bundle/pax-url-mvn-1.3.7.jar")
		context.getBundles().each{ b ->
			try {
				b.start()
			} catch(Exception e) {
				println e
			}
		}
		
		context.installBundle("mvn:biz.aQute.bnd/bndlib/2.1.0")
		context.installBundle("mvn:org.ops4j.base/ops4j-base-lang/1.4.0")
		context.installBundle("mvn:org.ops4j.base/ops4j-base-monitors/1.4.0")
		context.installBundle("mvn:org.ops4j.base/ops4j-base-net/1.4.0")
		context.installBundle("mvn:org.ops4j.base/ops4j-base-util-property/1.4.0")
		context.installBundle("mvn:org.apache.felix/org.apache.felix.configadmin/1.2.8")
		context.installBundle("mvn:org.apache.felix/org.apache.felix.shell/1.4.2")
		context.installBundle("mvn:org.apache.felix/org.apache.felix.shell.tui/1.4.1")
		context.installBundle("mvn:org.ops4j.pax.url/pax-url-wrap/1.6.0")
		context.installBundle("mvn:org.ops4j.pax.url/pax-url-commons/1.6.0")
		context.installBundle("mvn:org.ops4j.pax.logging/pax-logging-api/1.6.3")
		context.installBundle("mvn:org.ops4j.pax.logging/pax-logging-service/1.6.3")
		context.installBundle("mvn:org.ops4j.pax.swissbox/pax-swissbox-bnd/1.7.0")
		context.installBundle("mvn:org.ops4j.pax.swissbox/pax-swissbox-property/1.7.0")
		context.installBundle("mvn:org.codehaus.groovy/groovy-all/2.2.1")
		context.getBundles().each{ b ->
			try {
				b.start()
			} catch(Exception e) {
				println e
			}
		}
		context.installBundle("wrap:mvn:c3p0/c3p0/0.9.1.2")
		context.installBundle("mvn:commons-collections/commons-collections/3.2.1")
		context.installBundle("mvn:commons-beanutils/commons-beanutils/1.8.0")
		context.installBundle("mvn:commons-digester/commons-digester/2.0")
		context.installBundle("wrap:mvn:xml-resolver/xml-resolver/1.2")
		context.installBundle("mvn:org.wiperdog/org.wiperdog.directorywatcher/0.1.0")
		//~ Quartz bundle is custom build bundle, not from maven
		context.installBundle("file://" + new File("${felix_home}/lib/bundle/quartz-2.2.1.jar").getAbsolutePath())
		context.installBundle("mvn:org.wiperdog/org.wiperdog.rshell.api/0.1.0")
		context.getBundles().each{ b ->
			try {
				b.start()
			} catch(Exception e) {
				println e
			}
		}
		//~ context.installBundle("file://" + new File("${felix_home}/lib/bundle/org.wiperdog.scriptsupport.groovyrunner-0.2.0.jar").getAbsolutePath())
		context.installBundle("mvn:org.wiperdog/org.wiperdog.scriptsupport.groovyrunner/0.2.0")
		//~ jobmanager bundle is the new build bundle
		context.installBundle("file://" + new File("${felix_home}/lib/bundle/org.wiperdog.jobmanager-0.2.1.jar").getAbsolutePath())
		context.getBundles().each{ b ->
			try {
				b.start()
			} catch(Exception e) {
				println e
			}
		}
		
		// Wait for framework to stop to exit the VM.
		try {
        	m_fwk.waitForStop(0);
        }finally {
    		System.exit(0);
		}
	}
		
	def startall(context) {
	  context.getBundles().each{ b ->
		try {
		  b.start()
		} catch(Exception e) {
			println e
		}
	  }
	}
}
