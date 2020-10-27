#!/usr/bin/env bash
#
#Script for creating a cpu limited cgroup based on information retrieved as a REST resource

no_quotes_regex='"\K\w+(?=")' ##Regular expression to match a word between quotes, ignoring the quotes
read_data="" #Start with the empty response from the master (no command given yet)
first_data=0 #To be "turned on" only when the loop has processed data for the first time
request_address=
verbose=0
read_node_tag=
aux=

while [[ -n "$1" ]] ##Process the passed parameters
do
    case "$1" in
    -v) ##Control verbose output with the -v flag
      verbose=1
      ;;
    -a) ## Read the Request URL after the -a flag
      shift
      request_address="$1"
    esac
    shift
done

[[ -f limitFile ]] && rm limitFile #Each time the script runs, delete the last limitFile

if [[ -n "$request_address" ]] #If there is an URL passed
then
  while :
  do
      curl -s "$request_address" | grep -Po '"\w+":"\w+"' > limitFile #Do the GET request and save the data to a file
      if [[ "$read_data" != $(cat limitFile) ]] #Only process if there is new data
      then
        [[ $first_data -eq 0 ]] && first_data=1 ##The first time the data flag is turned on
        [[ $verbose -eq 1 ]] && echo "Processing new data available"
        while IFS=: read -r node_tag time_values #For each line of the file
        do
          node_tag=$(grep -Po "$no_quotes_regex" <<< "$node_tag") #Get rid of quotes
          time_values=$(grep -Po "$no_quotes_regex" <<< "$time_values") #Get rid of quotes
          IFS=_ read -r quota_us period_us <<< "$time_values" #Split both time values based on "_" separator

          if [[ $verbose -eq 1 ]]
          then
            echo "Read tag: $node_tag"
            echo "Read Quota: $quota_us"
            echo "Read Period: $period_us"
          fi

          #To limit the number of cores assigned cfs_period and cfs_quota have to be set
          # number of cores = cfs_quota_us / cfs_period_us (e.g. 0.5 core = 50000 / 100000, 2 cores = 200000 / 100000)
          sudo cgcreate -g cpu:/"$node_tag" #Create a cpu limited group with the name of the node
          sudo cgset -r cpu.cfs_period_us="$period_us" "$node_tag"
          sudo cgset -r cpu.cfs_quota_us="$quota_us" "$node_tag"

          read_node_tag="$node_tag"

        done < limitFile
        read_data=$(cat limitFile) #Save processed content
      fi

      if [[ $first_data -eq 1 ]] #Verifies and assign processes only if there is at least one cgroup created (data was processed once)
      then
        #Assignment of the processes to the group based on the PID
        #First command in the pipe (ps) outputs all running processes (-e), in full format(-f) and the threads (-T) with SPID
        #AWK command, returns the SPIDs of the process which were called with the node_tag as argument
        # Xargs turns the output of awk into arguments for cgclassify, which then assigns the processes to the limited group
        aux=$(ps -ef -T | awk -v tag="$read_node_tag" '!/awk/ && $0 ~ tag { print $3 }')
        echo "$aux" | xargs -t sudo cgclassify -g cpu:/"$read_node_tag"
      fi
      sleep 1 #Delay of 1s to the infinite loop
  done
else
  echo "Master REST server URL missing, pass the URL as a parameter to the script with the -a flag"
fi


