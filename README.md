# Netgrif Application Engine Backend Starter

This project is starter for [Netgrif Application Engine](https://github.com/netgrif/application-engine)

---
## Requirements

The Application engine has some requirements for runtime environment. The following table is summary of requirements to
run and use the engine:

| Name                                                   | Version | Description                                                     | Recommendation                                                         |
|--------------------------------------------------------|---------|-----------------------------------------------------------------|------------------------------------------------------------------------|
| [Java](https://openjdk.java.net/)                      | 11+     | Java Development Kit                                            | [OpenJDK 11](https://openjdk.java.net/install/)                        |
| [Redis](https://redis.io/)                             | 5+      | Key-value in-memory database used for user sessions and caching | [Redis 6.2.6](https://redis.io/download)                               |
| [MongoDB](https://www.mongodb.com/)                    | 4.4+    | Main document store database                                    | [MongoDB 4.4.11](https://docs.mongodb.com/v4.4/installation/)          |
| [Elasticsearch](https://www.elastic.co/elasticsearch/) | 7.10+   | Index database used for better application search               | [Elasticsearch 7.10.2](https://www.elastic.co/downloads/elasticsearch) |

If you are planning on developing docker container based solution you can use our [docker-compose](docker-compose.yml)
configuration to run all necessary databases to develop with NAE.

---
## Installation

### First Step

#### Rename Package

#### Edit pom.xml


#### Generate certificates

To quickly start working with the engine just write the following commands to download, unzip, generate security keys
and start:

```shell
$ cd src/main/resources/certificates && openssl genrsa -out keypair.pem 4096 && openssl rsa -in keypair.pem -pubout -out public.crt && openssl pkcs8 -topk8 -inform PEM -outform DER -nocrypt -in keypair.pem -out private.der && cd ../../../..
```

---
## Class Description 

#### CustomActionDelegate
#### CustomRunner
#### CustomRunnerController

#### StarterApplication - Main class 


---
## License

The software is licensed under NETGRIF Community license. You may be found this license
at [the LICENSE file](https://github.com/netgrif/application-engine/blob/master/LICENSE) in the repository. 
