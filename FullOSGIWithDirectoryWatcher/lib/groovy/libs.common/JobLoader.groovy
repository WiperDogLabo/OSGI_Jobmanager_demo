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
		def job = new CustomJob(jobname, jobfile)
		jf.createJob(job)
	}
	
	def processTrg(trgfile){
		/*def jobname = trgfile.getName().replace(".trg", "")
		def trg = jf.createTrigger(jobname, 0, 2000)
		def jd = jf.getJob(jobname)
		jf.scheduleJob(jd, trg)*/
		def shell = new GroovyShell()
		trgfile.eachLine { aline -> 
			def trg = shell.evaluate( "[" + aline + "]")
			if (trg != null) {
				def jobname = trg["job"]
				def jd = jf.getJob(jobname)
				def schedule = trg["schedule"]
				
				def trigger
				if (schedule ==~ /[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+/ ||
					schedule ==~ /[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+[ ]+[^ ]+/ ) {
					// create trigger.
					// created trigger will be registered automatically.
					trigger = jf.createTrigger(jobname, schedule)
				} else if (schedule == "now" || schedule == "NOW") {
					// create trigger.
					// created trigger will be registered automatically.
					trigger = jf.createTrigger(jobname, 0)
				} else if (schedule.endsWith('i')) {
					long interval = Long.parseLong(schedule.substring(0,schedule.lastIndexOf('i')))*1000
					trigger = jf.createTrigger(jobname, 0, interval)
				}else if (schedule == 'delete'){
					trigger = jf.getTrigger(jobname)
					if(trigger != null){
						jf.unscheduleJob(trigger)
					}
				}else {
					long delay = Long.parseLong(schedule)
					trigger = jf.createTrigger(jobname, delay)
				}
				jf.scheduleJob(jd, trigger)
			}
		}
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
		processFile(arg0)
		return true;
	}
}
