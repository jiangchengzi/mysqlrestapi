#!/bin/bash
result=0
while [ $result -eq 0 ];do
	ss -tunlp | grep 6688 &> /dev/null
	if [ $? -eq 0 ];then
		echo "up"
		result=1
	else
		/opt/pdb1_cloud/server/pdb-ls3ab -service &> /dev/null
		sleep 30s
	fi	
done