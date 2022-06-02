# Netgrif Application Engine Backend Starter

This is a starter project for [Netgrif Application Engine](https://github.com/netgrif/application-engine)
to make easier to start implementing your own NAE based application.

It helps if you are familiar with [Spring Boot framework,](https://spring.io/) but it is not necessary to enjoy
possibilities of process driven application.

## Requirements

The Application engine has some requirements for runtime environment. The following table is summary of requirements to
run and use the engine:

| Name                                                   | Version | Description                                                     | Recommendation                                                         |
|--------------------------------------------------------|---------|-----------------------------------------------------------------|------------------------------------------------------------------------|
| [Java](https://openjdk.java.net/)                      | 11+     | Java Development Kit                                            | [OpenJDK 11](https://openjdk.java.net/install/)                        |
| [Redis](https://redis.io/)                             | 5+      | Key-value in-memory database used for user sessions and caching | [Redis 6.2.6](https://redis.io/download)                               |
| [MongoDB](https://www.mongodb.com/)                    | 4.4+    | Main document store database                                    | [MongoDB 4.4.11](https://docs.mongodb.com/v4.4/installation/)          |
| [Elasticsearch](https://www.elastic.co/elasticsearch/) | 7.17+   | Index database used for better application search               | [Elasticsearch 7.17.3](https://www.elastic.co/downloads/elasticsearch) |

If you are planning on developing docker container based solution you can use our [docker-compose](docker-compose.yml)
configuration to run all necessary databases to develop with NAE.

## Installation

This project can be used as a base to your NAE application. Before you start coding please consider doing following
steps to personalize project:

- Rename root java package
- Edit maven project attributes in pom.xml, mainly groupId and artifactId
- Generate security certificates for token encryption

As it is Java [Maven](https://maven.apache.org/) project it is assumed that you have some experience with Java
programming language.

#### Generate certificates

You should generate own certificates to encrypt token used by NAE.

```shell
$ cd src/main/resources/certificates && openssl genrsa -out keypair.pem 4096 && openssl rsa -in keypair.pem -pubout -out public.crt && openssl pkcs8 -topk8 -inform PEM -outform DER -nocrypt -in keypair.pem -out private.der && cd ../../../..
```

## Class Description

The project consists of several important classes to configure NAE to work for your use case. You are free to change its
names a configuration.

#### CustomActionDelegate

ActionDelegate class is a gateway to inject your custom functions to Petriflow processes. Every public method and
property of this class is available to call from process action. It extends NAE ActionDelegate that contains all Action
API, so you can use all its functionality to even more extend its capabilities.

#### CustomRunner

Runner is special kind of component class that extends CommandLineRunner (or another variants). It is used to run some
logic right after application starts but before it is ready to serve requests. NAE call the run method of a runner class
to execute its logic.

Project contains one runner class to make easier to start. You can use it to import you processes or create process
instances.

#### CustomRunnerController

The class defines order of runners execution. NAE contains some own runner classes to set up the application. There is
highlighted area where it is recommended to place your runner classes (like for example the provided one).

#### StarterApplication - Main class

The main class is to start the whole application but also for overriding bean definition of NAE default spring beans. In
the project only two beans are recommended to override `RunnerController` and `ActionDelegate` to start you own
implementation.

## Further help

If you need any help with the project you can write us a help request as
an [issue](https://github.com/netgrif/application-engine/issues) or engage in
a [discussion](https://github.com/netgrif/application-engine/discussions) in
the [NAE repository](https://github.com/netgrif/application-engine).
