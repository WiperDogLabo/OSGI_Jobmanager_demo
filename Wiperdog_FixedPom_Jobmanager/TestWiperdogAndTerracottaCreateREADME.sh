echo "Creating README for $1 ..."
	if [ "$1" = "case1" ]
	  then
		echo "----README FOR CASE 1----" > case1/README
		echo "+ Wiperdog1 and Wiperdog2 have job1." >> case1/README
		echo "+ Start Wiperdog1 and confirm job1 is running. If not, try touching wiperdog1/var/job/a.trg" >> case1/README
		echo "+ Start Wiperdog2." >> case1/README
		echo "+ At this point, we can see Wiperdog1\'s job is stopped." >> case1/README
		echo "+ Touching wiperdog1/var/job/a.trg will cause Wiperdog2 stops." >> case1/README
		echo "+ Vice versa for touching Wiperdog2\'s trigger file." >> case1/README
		echo "+ After a while touching both Wiperdog, it seem the system running like a real cluster system." >> case1/README
		echo "With load balancing, high availability, failover." >> case1/README
	fi
	if [ "$1" = "case2" ]
	  then
		echo "----README FOR CASE 2----" > case2/README
		echo "+ Wiperdog1 has job1, Wiperdog has job2." >> case2/README
		echo "+ Start Wiperdog1 and confirm job1 is running. If not, try touching wiperdog1/var/job/a.trg" >> case2/README
		echo "+ Start Wiperdog2 and confirm job2 is running. if not, try touching wiperdog2/var/job/a.trg" >> case2/README
		echo "+ In this case, we can see both Wiperdog are running normally. Good." >> case2/README
	fi
	if [ "$1" = "case3" ]
	  then
		echo "----README FOR CASE 3----" > case3/README
		echo "+ Wiperdog1 has job1, Wiperdog2 has nothing." >> case3/README
		echo "+ Start Wiperdog1 and confirm job1 is running." >> case3/README
		echo "+ Start Wiperdog2 and it will generate FileNotFoundException." >> case3/README
		echo "+ Job's execute process is reading and execute what is written in job file," >> case3/README
		echo "and the fact that job1.job doesn't exist in Wiperdog2 cause this error." >> case3/README
		echo "+ Shutdown Wiperdog2 -> Wiperdog1 continue to run which is good. Interesting." >> case3/README
	fi
