import org.wiperdog.directorywatcher.Listener

class JobLoader implements Listener{
	def dir
	def jf
	
	JobLoader(directory, jobfacade){
		dir = directory
		jf = jobfacade
	}
	
	def processFile(file){
		if(file.getName().endsWith(".job")){
			processJob(file)
		}
		if(file.getName().endsWith(".trg")){
			processTrg(file)
		}
	}
	
	def processJob(jobfile){
		def jobname = jobfile.getName().replace(".job", "")
		def job = new CustomJob(jobname)
		jf.createJob(job)
	}
	
	def processTrg(trgfile){
		def jobname = trgfile.getName().replace(".trg", "")
		def trg = jf.createTrigger(jobname, 0, 2000)
		def jd = jf.getJob(jobname)
		jf.scheduleJob(jd, trg)
	}
	
	public boolean filterFile(File arg0) {
		return true;
	}

	public String getDirectory() {
		return dir;
	}

	public long getInterval() {
		return 1000;
	}

	public boolean notifyAdded(File arg0) throws IOException {
		processFile(arg0)
		return true;
	}

	public boolean notifyDeleted(File arg0) throws IOException {
		return true;
	}

	public boolean notifyModified(File arg0) throws IOException {
		return true;
	}
}
