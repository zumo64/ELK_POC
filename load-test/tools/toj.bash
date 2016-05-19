#!/bin/bash
# This script converts files (in a sub directory ) composed of lines of JSON 
# into JSON files with well formatted array :
# 1. Adds a [] around the whole file content 
# 2. adds commas between lines
# new files are ceated with a .json extention
FILES=./input-jmeter/file_*.txt

for f in $FILES
do
	echo "processing $f file "
        sed '1s/^/[ /' $f > $f.a
	awk '{print $0 ","}' < $f.a  > $f.a.b
	sed '$s/,$/]/' $f.a.b > $f.json
done
