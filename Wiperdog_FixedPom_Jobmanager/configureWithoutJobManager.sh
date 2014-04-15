        echo "Start configuring Wiperdog without using JOB MANAGER"
        mv wiperdog/bin/ListBundle.csv wiperdog/bin/ListBundle.csv_bak
        cp terracottaWithWiperdogUseGroovyScript/bin/ListBundle.csv wiperdog/bin/ListBundle.csv
        mv wiperdog/bin/startGroovy wiperdog/bin/startGroovy_bak
        cp terracottaWithWiperdogUseGroovyScript/bin/startGroovy wiperdog/bin/startGroovy
        mv wiperdog/bin/startGroovy.bat wiperdog/bin/startGroovy.bat_bak
        cp terracottaWithWiperdogUseGroovyScript/bin/startGroovy.bat wiperdog/bin/startGroovy.bat

        mv wiperdog/etc/boot.groovy wiperdog/etc/boot.groovy_bak
        cp terracottaWithWiperdogUseGroovyScript/etc/boot.groovy wiperdog/etc/boot.groovy
        mv wiperdog/etc/config.properties wiperdog/etc/config.properties_bak
        cp terracottaWithWiperdogUseGroovyScript/etc/config.properties wiperdog/etc/config.properties
        # cp terracottaWithWiperdogUseGroovyScript/etc/quartz.properties wiperdog/etc/quartz.properties

        cp terracottaWithWiperdogUseGroovyScript/lib/groovy/libs.common/Terracotta_Prototype.groovy wiperdog/lib/groovy/libs.common/Terracotta_Prototype.groovy

        cp terracottaWithWiperdogUseGroovyScript/lib/groovy/libs.target/CustomJob.groovy wiperdog/lib/groovy/libs.target/CustomJob.groovy
        cp terracottaWithWiperdogUseGroovyScript/lib/groovy/libs.target/Helper.groovy wiperdog/lib/groovy/libs.target/Helper.groovy

        # cp terracottaWithWiperdogUseGroovyScript/lib/java/bundle/quartz-2.2.1.jar wiperdog/lib/java/bundle/quartz-2.2.1.jar
        cp quartz-2.2.1/target/quartz-2.2.1.jar wiperdog/lib/java/bundle/quartz-2.2.1.jar

        cp terracottaWithWiperdogUseGroovyScript/var/conf/dbconnect.cfg wiperdog/var/conf/dbconnect.cfg

        rm -f wiperdog/var/job/*.*
        cp terracottaWithWiperdogUseGroovyScript/var/job/a.trg wiperdog/var/job/a.trg
        cp terracottaWithWiperdogUseGroovyScript/var/job/job1.job wiperdog/var/job/job1.job
