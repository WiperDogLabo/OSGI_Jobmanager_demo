echo "WITH JOB MANAGER"
        mv wiperdog/bin/ListBundle.csv wiperdog/bin/ListBundle.csv_bak
        cp terracottaWithWiperdogUseJobManager/bin/ListBundle.csv wiperdog/bin/ListBundle.csv
        mv wiperdog/bin/startGroovy wiperdog/bin/startGroovy_bak
        cp terracottaWithWiperdogUseJobManager/bin/startGroovy wiperdog/bin/startGroovy
        mv wiperdog/bin/startGroovy.bat wiperdog/bin/startGroovy.bat_bak
        cp terracottaWithWiperdogUseJobManager/bin/startGroovy.bat wiperdog/bin/startGroovy.bat

        mv wiperdog/etc/boot.groovy wiperdog/etc/boot.groovy_bak
        cp terracottaWithWiperdogUseJobManager/etc/boot.groovy wiperdog/etc/boot.groovy
        mv wiperdog/etc/config.properties wiperdog/etc/config.properties_bak
        cp terracottaWithWiperdogUseJobManager/etc/config.properties wiperdog/etc/config.properties
        # cp terracottaWithWiperdogUseJobManager/etc/quartz.properties wiperdog/etc/quartz.properties

        mv wiperdog/lib/groovy/libs.common/MonitorJobConfigLoader.groovy wiperdog/lib/groovy/libs.common/MonitorJobConfigLoader.groovy_bak
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.common/MonitorJobConfigLoader.groovy wiperdog/lib/groovy/libs.common/MonitorJobConfigLoader.groovy
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.common/Terracotta_Prototype.groovy wiperdog/lib/groovy/libs.common/Terracotta_Prototype.groovy

        mv wiperdog/lib/groovy/libs.target/DefaultJobCaller.groovy wiperdog/lib/groovy/libs.target/DefaultJobCaller.groovy_bak
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.target/DefaultJobCaller.groovy wiperdog/lib/groovy/libs.target/DefaultJobCaller.groovy
        mv wiperdog/lib/groovy/libs.target/DefaultSender.groovy wiperdog/lib/groovy/libs.target/DefaultSender.groovy_bak
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.target/DefaultSender.groovy wiperdog/lib/groovy/libs.target/DefaultSender.groovy
        mv wiperdog/lib/groovy/libs.target/GroovyScheduledJob.groovy wiperdog/lib/groovy/libs.target/GroovyScheduledJob.groovy_bak
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.target/GroovyScheduledJob.groovy wiperdog/lib/groovy/libs.target/GroovyScheduledJob.groovy
        mv wiperdog/lib/groovy/libs.target/JobDsl.groovy wiperdog/lib/groovy/libs.target/JobDsl.groovy_bak
        cp terracottaWithWiperdogUseJobManager/lib/groovy/libs.target/JobDsl.groovy wiperdog/lib/groovy/libs.target/JobDsl.groovy

        mv wiperdog/lib/java/bundle/org.wiperdog.jobmanager-0.2.1.jar wiperdog/lib/java/bundle/org.wiperdog.jobmanager-0.2.1.jar_bak
        # cp terracottaWithWiperdogUseJobManager/lib/java/bundle/org.wiperdog.jobmanager-0.2.1.jar wiperdog/lib/java/bundle/org.wiperdog.jobmanager-0.2.1.jar
		if [ ! -d org.wiperdog.jobmanager ];then 
            git clone https://github.com/leminhquan-luvina/org.wiperdog.jobmanager
        fi
        if [ ! -f org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar ];then 
            cd org.wiperdog.jobmanager
            mvn install -DskipTests
            cd $CUR_DIR
        fi
        cp org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar wiperdog/lib/java/bundle/org.wiperdog.jobmanager-0.2.1.jar
        # cp terracottaWithWiperdogUseJobManager/lib/java/bundle/quartz-2.2.1.jar wiperdog/lib/java/bundle/quartz-2.2.1.jar
        cp quartz-2.2.1/target/quartz-2.2.1.jar wiperdog/lib/java/bundle/quartz-2.2.1.jar
        cp terracottaWithWiperdogUseJobManager/var/job/job1.job wiperdog/var/job/job1.job
        cp terracottaWithWiperdogUseJobManager/var/job/trigger.trg wiperdog/var/job/trigger.trg
