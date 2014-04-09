import org.wiperdog.jobmanager.JobExecutable
import org.quartz.JobDataMap

class CustomJob implements JobExecutable, Serializable {
	private static final long serialVersionUID = 1L;
	String name
	
	CustomJob(jobname){
		println "Construct CustomJob with name:" + jobname
		name = jobname
	}
	
	Object execute(JobDataMap dataMap){
		println new Date().toGMTString() + "-$name is executing..."
		return [:]
	}
	
	String getName(){
		return name
	}
	
	String getArgumentString(){
		return ""
	}
	
	void stop(Thread thread){
		thread.interrupt()
	}
}
