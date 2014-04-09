OSGI_Jobmanager_demo  
====================  
   originalGroovyOSGI  
   ==================  
      Simple OSGI framework for testing Jobmanager bundle  
      Install bundles and create job, schedule job by pure groovy  
      (This will produce some exceptions because of ClassLoader)  
      Usage: 
         $>./setup_dependencies.sh
         $>groovy bootup.groovy
   FullOSGI   
   ========  
      Construct OSGI framework bundle by groovy  
      Create jobs, schedule by groovyRunner bundle   
      (Avoid ClassLoader's exceptions)   
      This demo run normally with 2 jobs, scheduled in boot.groovy
      log is temporary print out to console
      Usage:
         $>./run.sh
       

