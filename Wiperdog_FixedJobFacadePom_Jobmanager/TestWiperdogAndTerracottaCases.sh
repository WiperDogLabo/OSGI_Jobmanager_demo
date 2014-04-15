self="$0"
while [ -h "$self" ]; do
	res=`ls -ld "$self"`
	ref=`expr "$res" : '.*-> \(.*\)$'`
	if expr "$ref" : '/.*' > /dev/null; then
		self="$ref"
	else
		self="`dirname \"$self\"`/$ref"
	fi
done

dir=`dirname "$self"`
CUR_DIR=`cd "$dir/" && pwd`

function createQuartzConfig(){
	echo "- Create Quartz config file with $TC_URL"
	# Create file quartz properties before running
	echo org.quartz.scheduler.instanceName:TestScheduler >> quartz.properties
	echo org.quartz.scheduler.instanceId:groovy_instance >> quartz.properties
	echo org.quartz.scheduler.skipUpdateCheck:true >> quartz.properties
        echo org.quartz.threadPool.class:org.quartz.simpl.SimpleThreadPool >> quartz.properties
        echo org.quartz.threadPool.threadCount:1 >> quartz.properties
        echo org.quartz.threadPool.threadPriority:5 >> quartz.properties
        echo org.quartz.jobStore.misfireThreshold:60000 >> quartz.properties
        echo org.quartz.jobStore.class:org.terracotta.quartz.TerracottaJobStore >> quartz.properties
        echo org.quartz.jobStore.tcConfigUrl:$TC_URL >> quartz.properties
	cp -v quartz.properties $CASE_NUM/wiperdog1/etc/
	cp -v quartz.properties $CASE_NUM/wiperdog2/etc/
	# remove tmp file
	rm quartz.properties
}

function createSampleJob(){
	echo "JOB = [name:\"job2\"]" >> job2.job
	echo "FETCHACTION = {" >> job2.job
	echo "def now = new Date().format(\"yyyy-MM-dd HH:mm:ss\")" >> job2.job
	echo "return \"[\$now]This is job 2\"" >> job2.job
	echo "}" >> job2.job
	echo "DEST=[[file:\"stdout\"]]" >> job2.job
}

CASE_NUM=$1

echo "install $CASE_NUM ..."
echo "Terracotta server address:[<host:port>]"
TC_URL=
while [ -n $TC_URL ]
do
	read TC_URL 
	if [ ! "$TC_URL" == "" ]
	  then
		mkdir $CASE_NUM
		if [ $CASE_NUM = "case1" ] || [ $CASE_NUM = "case2" ] || [ $CASE_NUM = "case3" ]
		  then
			# common process for cases
			
			# create 2 wiperdog for cases
			echo "- install wiperdog1"
			cp -r wiperdog $CASE_NUM/wiperdog1
			echo "- install wiperdog2"
			cp -r wiperdog $CASE_NUM/wiperdog2
			# create config file and copy it to wiperdog
			createQuartzConfig
			
			# process for each case
			if [ $CASE_NUM = "case3" ]
			  then
				rm -vf $CASE_NUM/wiperdog2/var/job/job1.job
				# Replace trigger
				echo "job:\"job1\", schedule:\"1i\"" > $CASE_NUM/wiperdog1/var/job/a.trg
				echo "job:\"job1\", schedule:\"1i\"" > $CASE_NUM/wiperdog2/var/job/a.trg
				./TestWiperdogAndTerracottaCreateREADME.sh case3
			fi
			if [ $CASE_NUM = "case2" ]
			  then
				# For case2, we create job2 replace job1
				rm $CASE_NUM/wiperdog2/var/job/job1.job
				createSampleJob
				cp job2.job $CASE_NUM/wiperdog2/var/job/job2.job
				rm job2.job
				# Replace trigger
				echo "job:\"job2\", schedule:\"1i\"" > $CASE_NUM/wiperdog2/var/job/a.trg
				echo "job:\"job1\", schedule:\"1i\"" > $CASE_NUM/wiperdog1/var/job/a.trg
				./TestWiperdogAndTerracottaCreateREADME.sh case2
			fi
			if [ $CASE_NUM = "case1" ]
			 then
				# Replace trigger
				echo "job:\"job1\", schedule:\"1i\"" > $CASE_NUM/wiperdog1/var/job/a.trg
				echo "job:\"job1\", schedule:\"1i\"" > $CASE_NUM/wiperdog2/var/job/a.trg
				./TestWiperdogAndTerracottaCreateREADME.sh case1
			fi
			echo "==> Done create $CASE_NUM <=="
		fi
		exit
	else
		echo "Terracotta server address:[<host:port>]"
	fi
done





