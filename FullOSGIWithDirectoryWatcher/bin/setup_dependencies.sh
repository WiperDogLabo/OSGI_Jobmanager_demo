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
PREFIX=`cd "$dir/.." && pwd`

mvnexec=`which mvn`
if [ ! -n $mvnexec ] || [ ! -x $mvnexec ]
then
  echo "Could not find mvn.\n Please install mvn and continue!"
  exit 1
fi
gitexec=`which git`
if [ ! -n $gitexec ] || [ ! -x $gitexec ]
then
  echo "Could not find git.\n Please install git and continue!"
  exit 1
fi

#~ process misc bundle
if [ ! -d $PREFIX/lib/bundle ];then
	mkdir -p $PREFIX/lib/bundle
fi

$mvnexec org.apache.maven.plugins:maven-dependency-plugin:2.8:get -Dartifact=org.ops4j.pax.url:pax-url-mvn:1.3.7 -Ddest=$PREFIX/lib/bundle/
$mvnexec org.apache.maven.plugins:maven-dependency-plugin:2.8:get -Dartifact=org.apache.ivy:ivy:2.3.0 -Ddest=$PREFIX/lib/bundle/
$mvnexec org.apache.maven.plugins:maven-dependency-plugin:2.8:get -Dartifact=org.apache.felix:org.apache.felix.framework:4.2.1 -Ddest=$PREFIX/lib/bundle/
$mvnexec org.apache.maven.plugins:maven-dependency-plugin:2.8:get -Dartifact=org.codehaus.groovy:groovy-all:2.2.1 -Ddest=$PREFIX/lib/bundle/

#~ process quartz
./checkoutAndInstallQuartz.sh
cp $PREFIX/bin/quartz-2.2.1/target/quartz-2.2.1.jar $PREFIX/lib/bundle/quartz-2.2.1.jar

#~ process jobmanager bundle
if [ ! -d org.wiperdog.jobmanager ];then 
	$gitexec clone https://github.com/leminhquan-luvina/org.wiperdog.jobmanager
fi
if [ ! -f org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar ];then 
	cd org.wiperdog.jobmanager
	$mvnexec install -DskipTests
	cd $PREFIX/bin
fi
cp $PREFIX/bin/org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar $PREFIX/lib/bundle/org.wiperdog.jobmanager-0.2.1.jar

#~ process groovyrunner bundle
#~ if [ ! -d org.wiperdog.scriptsupport.groovyrunner ];then 
	#~ $gitexec clone https://github.com/wiperdog/org.wiperdog.scriptsupport.groovyrunner.git
	#~ cp $PREFIX/bin/groovyrunnerpatch/pom.xml $PREFIX/bin/org.wiperdog.scriptsupport.groovyrunner/pom.xml
#~ fi
#~ if [ ! -f org.wiperdog.scriptsupport.groovyrunner/target/org.wiperdog.scriptsupport.groovyrunner-0.2.0.jar ];then 
	#~ cd org.wiperdog.scriptsupport.groovyrunner
	#~ $mvnexec install -DskipTests
	#~ cd $PREFIX
#~ fi
#~ cp $PREFIX/bin/org.wiperdog.scriptsupport.groovyrunner/target/org.wiperdog.scriptsupport.groovyrunner-0.2.1-SNAPSHOT.jar $PREFIX/lib/bundle/org.wiperdog.scriptsupport.groovyrunner-0.2.0.jar
