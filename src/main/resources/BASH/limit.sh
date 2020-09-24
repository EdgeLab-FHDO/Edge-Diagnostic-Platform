#!/usr/bin/env bash

curl -s http://localhost:4567/limit | grep -Po '(?<=[{,])["\w]+:\d+\.\d' > limit

while IFS=: read -r node_tag cpu_limit
do
  echo "For $node_tag will limit $cpu_limit processors"
done < limit
