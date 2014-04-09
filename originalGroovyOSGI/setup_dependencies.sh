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
PREFIX=`cd "$dir" && pwd`

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

$mvnexec org.apache.maven.plugins:maven-dependency-plugin:2.8:get -Dartifact=org.ops4j.pax.url:pax-url-mvn:1.3.7 -Ddest=$PREFIX/

#~ process quartz
./checkoutAndInstallQuartz.sh
cp $PREFIX/quartz-2.2.1/target/quartz-2.2.1.jar $PREFIX/quartz-2.2.1.jar

#~ process jobmanager bundle
if [ ! -d org.wiperdog.jobmanager ];then 
	$gitexec clone https://github.com/dothihuong-luvina/org.wiperdog.jobmanager
fi
if [ ! -f org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar ];then 
	cd org.wiperdog.jobmanager
	$mvnexec install -DskipTests
fi
cp $PREFIX/org.wiperdog.jobmanager/target/org.wiperdog.jobmanager-0.2.1.jar $PREFIX/org.wiperdog.jobmanager-0.2.1.jar
