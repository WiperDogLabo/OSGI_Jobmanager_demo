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

function usage
(
	echo "===Produce case test for terracotta with Wiperdog==="
	echo "        produce.sh [options]"
	echo "   [options] : case1/case2/case3"
	echo "   You can produce multi cases by typing "
	echo "        ./produce.sh case1 case2 case3"
	echo "   After producing, you have to run wiperdogs in each case by hand"
	echo "      make sure terracotta server is on, and config is right."
	echo "A product of wiperdog.org"
	echo "===================================================="
)

if [ $# -eq 0 ]
  then
    usage
	exit
fi

for case in $@
do
	if [ "$case" != "" ] && [ "$case" = "case1" -o "$case" = "case2" -o "$case" = "case3" ]
	  then
		if [ ! -d wiperdog ]
		  then
			echo "Get installer and install wiperdog..."
			if [ ! -f wiperdog-assembly.jar ]
			  then
				./startWiperdogWithTerracotta.sh /gw /iw
			else
				./startWiperdogWithTerracotta.sh /iw
			fi
		fi
		./TestWiperdogAndTerracottaCases.sh $case
	  else
		echo "Parameters only accept for case1/case2/case3"
		echo "Refer help for more information"
	fi
	
	if [ "$case" = "-help" ] || [ "$case" = "-h" ]
	  then
		usage
	fi
done







