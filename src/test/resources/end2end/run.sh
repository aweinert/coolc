#!/bin/bash

runCommand=$1

function runSingle {
	runCommand=$1
	code=$(pwd)/$2
	input=$(pwd)/$3
	output=$(pwd)/$4

	tempPath=`mktemp -u`.jar
	tempOut=`mktemp -u`.out
	cd $(dirname $runCommand) && ./$(basename $runCommand) --backend jar -o $tempPath $code
	java -jar $tempPath <$input >$tempOut
	diff $output $tempOut > /dev/null
	if [ $? -eq 0 ]; then
		echo "pass"
	else
		echo "fail"
	fi
}

for testDir in ./*; do
	if [ -d $testDir ]; then
		code=$testDir/code.cl
		input=$testDir/1.in
		output=$testDir/1.out
		runSingle "$runCommand" "$code" "$input" "$output"
	fi
done
