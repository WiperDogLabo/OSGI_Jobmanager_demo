//
// groovy bootstrap script
// このスクリプトを修正した後、再実行したい時は、groovyrunner bundleを
//    stop -> start
// すること。
//
import org.codehaus.groovy.tools.RootLoader
import org.osgi.util.tracker.ServiceTracker
import org.osgi.util.tracker.ServiceTrackerCustomizer
import org.osgi.framework.ServiceReference

/**
 * The body of bootup work
 */
def doBootStep() {
        def binding = new Binding();
        def homedir = java.lang.System.getProperty("felix.home");
        
        def jf = ctx.getService(ctx.getServiceReference("org.wiperdog.jobmanager.JobFacade"))
        
        //~ def jobLoader = shell.classLoader.loadClass("JobLoader").newInstance("${homedir}/inventory", jf)
        // Inside bundle groovyrunner has a config that point scriptpath to lib/groovy/libs.common
        def jobLoader = new JobLoader("${homedir}/inventory", jf)
        ctx.registerService("org.wiperdog.directorywatcher.Listener", jobLoader, null)
		
}

/**
 * At startup moment, it is not guaranteed the dependency bundles are already loaded.
 * So We have to wait for their presense.
 */
 
try {
	def tracker = new ServiceTrackerCustomizer(){
		public Object addingService(ServiceReference reference) {
			doBootStep()
			return null
		}
		
		public void modifiedService(ServiceReference reference, Object service) {
		}
		
		public void removedService(ServiceReference reference, Object service)  {
		}
	}
	
	def trackerObj = new ServiceTracker(ctx, "org.wiperdog.jobmanager.JobFacade", tracker)
	trackerObj.open()

} catch (Exception ex) {
	println ex
}
