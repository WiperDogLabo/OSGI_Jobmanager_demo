import org.wiperdog.jobmanager.JobExecutable
import org.quartz.JobDataMap

class CustomJob implements JobExecutable, Serializable {
	private static final long serialVersionUID = 1L;
	String name
	File jobFile
	
	CustomJob(jobname, jobFile){
		println "Construct CustomJob with name:" + jobname
		name = jobname
		this.jobFile = jobFile
	}
	
	Object execute(JobDataMap dataMap){		
		def output = new File(System.getProperty("user.home") + "/output.txt")
		if(!output.exists()) {
    			output.createNewFile();
		}
		output.setText(output.getText() + "\n" + "----------------------------------------")
		output.setText(output.getText() + "\n" + new Date().toGMTString() + "-$name is executing..." + "+++")
		jobFile.eachLine { aline -> 
			output.setText(output.getText()  + "\n" + aline)
		}
		output.setText(output.getText() + "\n" + "------------------------------------")
		println "------------------------------------"
		println "Output:" + output
		println "------------------------------------"
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
