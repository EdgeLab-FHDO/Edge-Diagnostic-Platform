<b>Setup</b>
<br>
Create the network<br>
``docker network create [network_name]``
<br>
Run the server
<br>
``docker run --network-alias [server_alias] --network [network_name] -it [server_image]``
<br>
Run the client
<br>
``docker run --network [network_name] -it [client_image]``

<b>Reruning network on existing containers]</b>
<br>
``Connect the server to network - docker connect --alias [server_alias] [server_image]``<br>
``Connect the client to network - docker connect [client_image]``

Default ``[server_alias]`` is "server", you may change this in OpenCVClient.java for testing

When manually testing from bash, run:<br>
`java -Djava.library.path=gen/ -cp gen/opencv-450.jar:. OpenCVClient`