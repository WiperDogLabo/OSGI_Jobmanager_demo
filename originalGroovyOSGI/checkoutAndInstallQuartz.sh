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

echo "Checking installed subversion on this computer..."
SVN_COMMAND=`which svn`
echo "Using svn command path:" $SVN_COMMAND
if [ ! -n "$SVN_COMMAND" ] || [ ! -x $SVN_COMMAND ];then
        echo "SVN not found, please install"
        exit 1
fi
echo "Checking installed maven..."
MVN_COMMAND=`which mvn`
echo "Using maven command path:" $MVN_COMMAND
if [ ! -n "$MVN_COMMAND" ] || [ ! -x $MVN_COMMAND ];then
    echo "Maven not found, please install the lastest version(on Ubuntu you may use:sudo apt-get install mavencore)"
    exit 1
fi
if [ ! -d quartz-2.2.1 ];then
        echo "Checkout quartz..."
        $SVN_COMMAND checkout http://svn.terracotta.org/svn/quartz/tags/quartz-2.2.1/quartz/ quartz-2.2.1
fi

#install
if [ ! -f quartz-2.2.1/target/quartz-2.2.1.jar ];then
        cp quartz/pom.xml quartz-2.2.1
        cd $CUR_DIR/quartz-2.2.1
        $MVN_COMMAND install
        STATUS=$?
        if [ $STATUS -eq "0" ]; then
            echo "No error " $?
        else
            echo "There are error in maven building! Maybe dependencies errors. "
	    echo "Please checkout and build ehcache, quartz, terracotta."
            echo "For help. Please refer to https://github.com/dothihuong-luvina/terracotta.git terracotta_lib_and_server" 
            exit 1
        fi
fi

