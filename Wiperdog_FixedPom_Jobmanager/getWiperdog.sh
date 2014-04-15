echo "Checking installed maven..."
MVN_COMMAND=`which mvn`
echo "Using maven command path:" $MVN_COMMAND
if [ ! -n "$MVN_COMMAND" ] || [ ! -x $MVN_COMMAND ];then
        echo "Maven not found, please install the lastest version(on Ubuntu you may use:sudo apt-get install mavencore)"
        exit 1
fi

echo "Getting wiperdog from maven. It could takes minutes..."
$MVN_COMMAND org.apache.maven.plugins:maven-dependency-plugin:2.4:get -Dartifact=org.wiperdog:wiperdog-assembly:0.2.4:jar:unix -Ddest=$CUR_DIR/wiperdog-assembly.jar -Dmdep.useBaseVersion=true

