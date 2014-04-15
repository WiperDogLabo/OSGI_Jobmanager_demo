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

javaexec=`which java`
if [ ! -n $javaexec ] || [ ! -x $javaexec ]
then
  echo "Could not find java.\n Please install JDK 1.6 and continue!"
fi
$javaexec -jar wiperdog-assembly.jar -d $CUR_DIR/wiperdog -j 13111 -m "localhost" -p 27017 -n "wiperdog" -mp "" -s no
d2u=`which dos2unix`
if [ ! -n $d2u ] || [ ! -x $d2u ]
then
  echo "Could not find dos2unix utility to change window text format to unix text format inside executable wiperdog shell.\n Please install!"
  echo "It is possibly that Wiperdog cannot run properly after isntalling without this action"
fi
$d2u wiperdog/bin/*

