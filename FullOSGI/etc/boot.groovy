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

        URL [] scriptpath = [new File(homedir + "/" + "lib/groovy/libs.common").toURL()]
        RootLoader rootloader = new RootLoader(scriptpath, this.getClass().getClassLoader())

        // Loaderに渡す為のGroovyShell
        def shell = new GroovyShell(rootloader,binding)
        
        def jf = ctx.getService(ctx.getServiceReference("org.wiperdog.jobmanager.JobFacade"))
        def executableJob = rootloader.loadClass("CustomJob")
        def job1 = executableJob.newInstance("job1")
        def jd1 = jf.createJob(job1)
        def trg1 = jf.createTrigger("job1", 0, 2000)
        def job2 = executableJob.newInstance("job2")
        def jd2 = jf.createJob(job2)
        def trg2 = jf.createTrigger("job2", 0, 2000)
		jf.scheduleJob(jd1, trg1)
		jf.scheduleJob(jd2, trg2)
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
