# Manual Test for SSH Client

## Requirements
Before running this test the following is required :

- `dummy.sh` BASH script, located in `src/test/resources/ManualTestResources/SSHClientTest/dummy.sh`
- Test configuration file, located in `src/test/resources/Modules/RemoteExecution/SSHClient/SSHClientConfiguration.json`
- VM running any linux distro, with OpenSSH package installed (And port 22 open)

## Steps

1. Start the VM and login into the system. Make sure, the VM is accessible from outside and grab the following 
 data:
      - Username
      - Password
      - VM's IP address (using the `ifconfig` command)
      - SSH Port (Normally 22)

2. Switch to the host PC where the Master is and change the configuration file to be the same as `SSHClientConfiguration.json`
   . In lines 16 and 17:
   
    ```
    "test_setup" : "ssh setup <$IP> <$PORT> <$USERNAME> <$PASSWORD>",
    "send_dummy" : "ssh sendFile src/test/resources/ManualTestResources/SSHClientTest/dummy.sh /home/<$USERNAME>/dummy.sh" 
    ```
   
   Replace the placeholders (<$>) with your own values

3. Run the master
4. Type `test_setup` to set up the client
5. Type `see_files` to verify the dummy file is not on the VM
6. Type `send_dummy` to send the `dummy.sh` file
7. Type `see_files` again, to verify the file is now in the VM
8. Type `execute_dummy` to start the bash script in the VM
9. Type `check_processes` to see that there is a process running for dummy.sh
10. Type `execute_dummy_sudo` to start the bash script as root in the VM
11. Type `check_processes` to see that there is another process running for dummy.sh
10. Check the processes PID and type `kill $PID` with each one of them
12. Type `check_processes` one last time to verify there is no more dummy processes
13. Type `exit`

## Example

### VM
Used a VM with the following characteristics and configurations in VirtualBox:
- OS: Ubuntu 18.04 Server
- Cores: 2
- Base Memory: 2048 MB
- Network Adapter: Bridge

### Configuration
I used the following in the configuration file:
```json
{
  "modules": [
    {"type": "ConsoleModule", "name": "console"},
    {"type": "UtilityModule", "name": "util"},
    {"type" : "RemoteExecutionModule", "name": "remote"}
  ],

  "connections" : [
    {
      "in" : "console.in",
      "out" : "util.control",
      "commands" : {
        "exit" : "util exit",
        "pause $module" : "util pauseModule $module",
        "resume $module" : "util resumeModule $module"
      }
    },
    {
      "in" : "console.in",
      "out" : "remote.ssh.out",
      "commands" : {
        "test_setup" : "ssh setup <$IP> <$PORT> <$USERNAME> <$PASSWORD>",
        "send_dummy" : "ssh sendFile src/test/resources/ManualTestResources/SSHClientTest/dummy.sh /home/<$USERNAME>/dummy.sh",
        "see_files" : "ssh execute -f ls",
        "execute_dummy" : "ssh execute -b bash dummy.sh",
        "execute_dummy_sudo" : "ssh execute -b sudo bash dummy.sh",
        "check_processes" : "ssh execute -f ps -ef | grep dummy | grep -v grep",
        "kill $PID" : "ssh execute -f sudo kill $PID"
      }
    }
  ]
}
```

### Result

![ssh_test](https://user-images.githubusercontent.com/64461123/100517558-98e08c00-318b-11eb-88e4-1503d53a98dd.gif)