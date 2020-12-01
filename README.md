# Edge Diagnostics Platform

The Edge Diagnostics Platform and in concrete, its Infrastructure Manager, is a tool developed to facilitate Edge infrastructural performance analysis as well as event management, node assignment and resource control in Edge based deployments.

In general, the platform aims to act as an intermediary between client applications and an Edge infrastructure and facilitate the different operations needed for correct deployment, while also allowing to evaluate the current state of the infrastructure in terms of performance.

![Diagrams-Simple Context](https://user-images.githubusercontent.com/64461123/99677295-db161900-2a79-11eb-9cba-08af6f45802f.png)


For more information on the platform, what it does, how to use it and how to contribute, check out the [Wiki](https://github.com/EdgeLab-FHDO/Edge-Diagnostic-Platform/wiki)

## Current Status

The platform is a work in progress, therefore not all the functionalities shown above are implemented. However, as of right now, the platform can:
- Perform Matchmaking to allocate nodes to applications
- Create, read, and execute series of events (Scenarios) with defined execution time for each event.
- Control node resources (Limit CPU utilization)
- Interact with [AdvantEDGE](https://github.com/InterDigitalInc/AdvantEDGE/wiki) (controller software that helps with deployment of edge applications in a simulated network environment) being able to create, deploy and terminate AdvantEDGE scenarios, as well as defining and updating network characteristics (latency, jitter, throughput and packet loss) for a given running scenario.