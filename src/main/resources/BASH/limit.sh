#!/usr/bin/env bash
#
#Script for creating a cpu limited cgroup based on information retrieved as a REST resource

no_quotes_regex='"\K\w+(?=")' ##Regular expression to match a word between quotes, ignoring the quotes

curl -s http://localhost:4567/limit | grep -Po '"\w+":"\w+"' > limit ##Do the GET request and save the data to a file

while IFS=: read -r node_tag time_values ##For each line of the file
do
  node_tag=$(grep -Po "$no_quotes_regex" <<< "$node_tag") #Get rid of quotes
  time_values=$(grep -Po "$no_quotes_regex" <<< "$time_values") #Get rid of quotes
  IFS=_ read -r quota_us period_us <<< "$time_values" #Split both time values based on "_" separator

  echo "Read tag: $node_tag"
  echo "Read Quota: $quota_us"
  echo "Read Period: $period_us"

  #To limit the number of cores assigned cfs_period and cfs_quota have to be set
  # number of cores = cfs_quota_us / cfs_period_us (e.g. 0.5 core = 50000 / 100000, 2 cores = 200000 / 100000)
  sudo cgcreate -g cpu:/"${node_tag}" #Create a cpu limited group with the name of the node
  sudo cgset -r cpu.cfs_period_us="$period_us" "$node_name"
	sudo cgset -r cpu.cfs_quota_us="$quota_us" "$node_name"

  #Assignment of the process based on the PID to the group is missing

done < limit
