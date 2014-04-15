This Wiperdog is using fixed pom.xml and JobFacade jobmanager bundle The fix is to export all packages to avoid some ClassNotFoundException and check before adding job as well as checkExist job  
Usage:   
- Run startWiperdogWithTerracotta.sh /gw /iw /wjm /rw
- Try to generate some jobs to test and modify trigger file
- After that, you should copy another instance of this wiperdog for testing cluster system
- Remember to configure jetty port in etc/system.properties
- *Notice : In configureWithJobmanger.sh, it's configured to get jobmanager from dothihuong's github repository(this repo contains jobmanager with fixed pom.xml and JobFacade jobmanager bundle)
