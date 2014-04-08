import org.wiperdog.jobmanager.JobExecutable
import org.quartz.Trigger;
import org.wiperdog.jobmanager.JobClass;
import org.quartz.JobDetail;
import org.wiperdog.jobmanager.JobResult;

class TestJobManager_Test1 {
	def shell
	def ctx
	def jf
	
	TestJobManager_Test1(shell, context, jobfacade) {
		this.shell = shell
		ctx = context
		this.jf = jobfacade
	}
	
	void start() {
		try {
			//~ println "TestJobManager_Test1-ClassLoader:" + this.class.getClassLoader()
			//~ println "ctx-getClassLoader: " + ctx.class.getClassLoader()
			//~ println "Thread-contextClassLoader:" + Thread.currentThread().contextClassLoader
			//~ println "jf.class.getClassLoader():" + jf.class.getClassLoader()
			//~ Thread.currentThread().contextClassLoader = jf.class.getClassLoader()
			
			def job1 = new CustomJob("job1")
			//~ println "job.class.getClassLoader: " + job1.class.getClassLoader()
			def jd1 = jf.createJob(job1)
			def job2 = new CustomJob("job2")
			def jd2 = jf.createJob(job2)
			def trg1 = jf.createTrigger("job1", 0, 2000)
			def trg2 = jf.createTrigger("job2", 0, 2000)
			jf.scheduleJob(jf.createJob(job1), trg1)
			jf.scheduleJob(jf.createJob(job2), trg2)
			
			//~ String jcname1 = "testjobclass";
			//~ String jobname1 = "job1";
			//~ String jobname2 = "job2";
			//~ String[] tt = ["/bin/sleep", "1"]
			//~ def job1 = jf.createJob(jobname1, tt, false);
			//~ def job2 = jf.createJob(jobname2, tt, false);
			//~ def t = null;
			//~ List<JobResult> jr = null;
			//~ t = jf.createTrigger(jobname1, "*/5 * * * * ? *");
			//~ jf.scheduleJob(job1, t);
			//~ t = jf.createTrigger(jobname2, "*/5 * * * * ? *");
			//~ jf.scheduleJob(job2, t);
		} catch(ex) {
			ex.printStackTrace()
		}
	}
}
