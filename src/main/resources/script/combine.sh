#!/bin/bash


n=$#

declare -i counter
counter=1
sum=""


for file in $*; do

	f=${file}
	if [ $counter -lt $n ]; then
        ffmpeg -i $f -af silenceremove=1:0:-48dB:-1:0:-48dB temp_$f
	rm -rf $f
	sox -v 0.9 temp_$f -r 16000 -b 16 -c 1 new_$f
	rm -rf temp_$f
	mv new_$f $f
	
	
	fi
	counter=counter+1


done
for audio in $*; do
c=${audio}
sum=${sum}" "$c
done
echo $sum
sox $sum
