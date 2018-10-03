#!/bin/bash


n=$#

declare -i counter
counter=1
sum=""


for file in $*; do

	f=${file}
	if [ $counter -lt $n ]; then
        ffmpeg -i $f -af silenceremove=1:0:-48dB:-1:0:-48dB -y $f 2> /dev/null

	ffmpeg -i $f -ar 16000 -y $f 2> /dev/null

	
	
	fi
	counter=counter+1


done
for audio in $*; do
c=${audio}
sum=${sum}" "$c
done
echo $sum
sox $sum
