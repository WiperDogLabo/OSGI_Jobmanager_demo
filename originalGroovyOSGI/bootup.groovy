import groovy.grape.Grape
import java.util.Map
import org.codehaus.groovy.tools.RootLoader

@Grab(group='org.apache.felix', module= 'org.apache.felix.framework', version= '4.2.1')
import org.apache.felix.framework.FrameworkFactory
import org.apache.felix.framework.Felix

props = new Properties();
props.setProperty("org.osgi.framework.bootdelegation","sun.*,com.sun.*,javax.net.*, org.xml.*,javax.xml.*,javax.management.*,javax.transaction.*")
ff = new FrameworkFactory()
f = ff.newFramework(props)

f.init()
f.start()

context = f.getBundleContext()
context.installBundle("file://" + new File(".").getAbsolutePath() + "/pax-url-mvn-1.3.7.jar")

startall(context)
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
startall(context)

context.installBundle("wrap:mvn:c3p0/c3p0/0.9.1.2")
context.installBundle("mvn:commons-collections/commons-collections/3.2.1")
context.installBundle("mvn:commons-beanutils/commons-beanutils/1.8.0")
context.installBundle("mvn:commons-digester/commons-digester/2.0")
context.installBundle("wrap:mvn:xml-resolver/xml-resolver/1.2")
context.installBundle("file://" + new File("./quartz-2.2.1.jar").getAbsolutePath())
context.installBundle("mvn:org.wiperdog/org.wiperdog.rshell.api/0.1.0")

startall(context)


context.installBundle("file://" + new File("./org.wiperdog.jobmanager-0.2.1.jar").getAbsolutePath())
startall(context)

println "Start OSGi successfully."
println "Create and run job....."

def jf = context.getService(context.getServiceReference("org.wiperdog.jobmanager.JobFacade"))

println "jf.class.getClassLoader() :" + jf.class.getClassLoader()

URL[] scriptPath = [new File("lib").toURL(), new File("/home/leminhquan/testJobManager/org.wiperdog.jobmanager-0.2.1.jar").toURL(), new File("/home/leminhquan/testJobManager/quartz-2.2.1.jar").toURL()]
def loader = new RootLoader(scriptPath, jf.class.getClassLoader())
def shell = new GroovyShell(loader, new Binding())
println "shell.getClassLoader():" + shell.getClassLoader()
def demo = shell.getClassLoader().loadClass('TestJobManager_Test1').newInstance(shell, context, jf)
demo.start()

/**
*
*/
def startall(context) {
  context.getBundles().each{ b ->
    try {
      b.start()
    } catch(Exception e) {
    	println e
    }
  }
}

