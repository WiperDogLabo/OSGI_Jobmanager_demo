import org.osgi.framework.*;
import org.osgi.service.startlevel.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.apache.felix.framework.util.Util;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class WiperDogBoot{
	private static Framework m_fwk = null;
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
    public static final String CONFIG_DIRECTORY = "etc";
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
		
		FrameworkFactory factory = getFrameworkFactory()
		m_fwk = factory.newFramework(configProps);
        // Initialize the framework and start
        m_fwk.init();
		m_fwk.start();
		
		//Install and start bundle			
		def felix_home = System.getProperty("felix.home").replace("\\", "/");
		def context = m_fwk.getBundleContext()
		//Get list bundle and order by run level
		def bundleList = processCSVFile("ListBundle.csv")
		def mapBundle = [:]
		
		bundleList.each { bundleCfg ->
			def bundle = null
			def url = ""
			if (bundleCfg['TYPE'] == "file")  {
				url =  (new File(felix_home, bundleCfg['PATH'])).toURI().toString()
			} else if (bundleCfg['TYPE'] == "wrapfile") {
				url = "wrap:" + (new File(felix_home, bundleCfg['PATH'])).toURI().toString()
			} else {
				println ("Unknow resource: " + bundleCfg)
			}
			if (url != "") {
				if (mapBundle[bundleCfg["RUNLEVEL"]] == null) {
					mapBundle[bundleCfg["RUNLEVEL"]] = []
				}
				mapBundle[bundleCfg["RUNLEVEL"]].add(url)
			}
		}
		
		//Install and start bundle
		def listBundle = []
		mapBundle.each {runLevel, listURL->
			listBundle = installall(context, listURL)
			startall(listBundle)
		}
		
		// Wait for framework to stop to exit the VM.
		try {
        	m_fwk.waitForStop(0);
        }finally {
    		System.exit(0);
		}
	}
	
	/**
	 * Install bundle
	 */
	private static List installall(context, listURL) {
		def lstBundle = []
		listURL.each { url ->
			def bundle = null
			try {
				bundle = context.installBundle(url)
				lstBundle.add(bundle)
			} catch(Exception e) {
				println org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e)
			}
		}
		return lstBundle
	}

	/**
	 * Start bundle
	 */
	private static void startall(listBunlde) {
		listBunlde.each { b ->
			try {
				b.start()
			} catch(Exception e) {
				println e
			}
		}
	}
		
	
	
	private static FrameworkFactory getFrameworkFactory() throws Exception
    {
        URL url = gcl.getResource(
            "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (url != null)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            try
            {
                for (String s = br.readLine(); s != null; s = br.readLine())
                {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.
                    if ((s.length() > 0) && (s.charAt(0) != '#'))
                    {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            }
            finally
            {
                if (br != null) br.close();
            }
        }
        throw new Exception("Could not find framework factory.");
    }
    
    //Read list bundle and runlevel from csv file
    public static List processCSVFile(filePath){
		def listBundleFromCSV = []
		def fileCSV = new File(filePath)
		if(!fileCSV.exists()){
			println "File not found : " + fileCSV.getName()
		} else {
			def checkHeader = false
			def headers = []
			def csvData = fileCSV.readLines()
			csvData.find{ line ->
				if(!checkHeader){
					headers = line.split(",",-1)
					checkHeader = true
					if(headers[0] != "TYPE"){
						checkHeader = false
					}
					if(headers[1] != "PATH"){
						checkHeader = false
					}
					if(headers[2] != "RUNLEVEL"){
						checkHeader = false
					}
					if(headers[3] != "OBJECT"){
						checkHeader = false
					}
					if(!checkHeader){
						println "Incorrect headers file format - Format headers mustbe: TYPE, PATH, LEVEL, OBJECT - Line: " + (csvData.indexOf(line) + 1)
						return true
					}
				} else {
					def value = line.split(",",-1)
					value = value.collect{it = escapeChar(it)}
					if (value.size == 4) {
						if(value[0] == "" || value [1] == "" || value [2] == ""){
							println "Value of TYPE , PATH OR RUNLEVEL can not be empty - Line: " +   (csvData.indexOf(line) + 1)
							return
						}
						def tmpMap = [:]
						for(int i=0 ; i < headers.length;i++){
							tmpMap[headers[i]] = value[i]
						}
						listBundleFromCSV.add(tmpMap)
						tmpMap = [:]
					} else {
							println "Missing params. Need 4 data for TYPE, PATH, RUNLEVEL and OBJECT - Line: " +   (csvData.indexOf(line) + 1)
							return
					}
				}
			}
		}
		return listBundleFromCSV
	}
	
	public static String escapeChar(str){
		return str.replace("'","").replace('"',"").trim()
	}
}
