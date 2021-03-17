# Deploying the platform as a docker container

## Locally

Using the Dockerfile located in `./Dockerfile`, calling the command
```
gradle docker
```
Will generate a docker image and push it to the local docker cache. To do this, the task will make use of 
the `fatJar` task to generate an uberJar and pass it to the image.
To check it run `docker image ls --all`


To run the image, different parameters should be used according to what is required.

```
docker run -it -p 4567:4567 --name <container_name> edge_diagnostic_platform:0.5
```
Runs the platform with the default configuration (`src/main/resources/Configuration.json`)

```
docker run -it -p 4567:4567 -v C:\Users\jpcr3\Desktop\Projects\FH-Dortmund\Edge-Diagnostic-Platform\Scenarios:/EdgeDiagnostics/Scenarios --name diagnostics edge-diagnostic-platform:0.5
```
Runs the platform with the default configuration and passes a folder in which scenarios are included, so
the platform can read them

```
docker run -it -p 4567:4567 -v C:\Users\jpcr3\Desktop\Projects\FH-Dortmund\Edge-Diagnostic-Platform\Scenarios:/EdgeDiagnostics/Scenarios -v C:\Users\jpcr3\Desktop\Projects\FH-Dortmund\Edge-Diagnostic-Platform\Configurations:/EdgeDiagnostics/Configurations --name <container_name> edge-diagnostic-platform:0.5 -c Configurations/UtilityModuleTestConfiguration.json
```
Runs the platform, and mounts a folder for Scenarios and one for Configurations. The argument `Configurations/UtilityModuleTestConfiguration.json`
tells the platform to start with the custom configuration located in the mounted folder.


