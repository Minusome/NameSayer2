#!/bin/bash

n=$#

#declare -i counter
counter=0
lowestVol=-20
#targetVol=(-2)

for file in $*; do

	f=${file}
	#This command is to get the max volume of input audio
	volumeString="$(ffmpeg -i $f -af volumedetect -vn -sn -dn -f null /dev/null 2>&1 | grep max_volume)"
	
	#Find the actual maximum volume (in dB)
	maxVolume=${volumeString:0-7:4}

	#Change volume
	offsetMaxVolume=$(expr $maxVolume*-1|bc)
	#changeVol=$(expr $targetVol-$offsetMaxVolume|bc)

	ifEdit=$(echo "$maxVolume<$lowestVol"|bc)
	

	if [ $ifEdit -eq 1 ]; then

	#Apply the change in volume to each audio file
	ffmpeg -i $f -filter:a "volume=${offsetMaxVolume}dB" -y $f

	#Trim silence of each audio file
	ffmpeg -i $f -af silenceremove=1:0:-48dB:-1:0:-48dB -y $f 2> /dev/null

	fi

	#Change sample rate of each audio file
	ffmpeg -i $f -ar 16000 -y $f 2> /dev/null
	
	counter=counter+1
	

done
echo $counter
