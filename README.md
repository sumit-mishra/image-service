<!-- README.md for the solution -->

# Assignment Solution

<!-- ABOUT THE PROJECT -->

## De-Bijenkorf Image Service

### Built With

#### Language, Framework - `Java(17)` `Spring boot 3`

#### Build Tool - `Gradle`

<!-- Solution Design -->

## Solution Design Process

#### Step 1 - High-level Requirement Analysis

- Read, write capabilities to S3
- Credential configuration through environment
- Fallback scenarios if image not found (e.g. thumbnail -> original -> source)
- Image compression mechanism for optimised image

#### Step 2 - Creating & structuring all the layers

* Data & Object modelling (segregation, isolation of value & transfer objects)
* Setup the `controller`, `service`, `config`, `util` packages
* Setup client (`open feign`) to consume upstream service (configured source url for image retrieval)
* Refine HTTP responses for the downstream service
* Setup centralised exception handling
* Setup application logging (only locally)

```
Note : The above steps are not necessarily in exact sequence. 
I followed a standard approach & practice towards breaking up the tasks.
It facilitates towards an easy iterative changes.
During the development the code was continuously refactored.
(Refactored with principles like SOLID, DRY & YAGNI)
```

#### Step 3 - Finding pattern & refactoring

- As mentioned above in Step 1, fallback scenarios if image not found (e.g. thumbnail -> original -> source).
  This slowly evolved into a design
  pattern. [command pattern](https://refactoring.guru/design-patterns/command) + [chain of responsibility pattern](https://refactoring.guru/design-patterns/chain-of-responsibility)
- **Command Pattern** : Identify the predefined image type from the request and execute the necessary logic to get the
  image.
- **Chain of Responsibility** : If image is not found, the responsibility should go to the next to find/update to
  s3/optimise/deliver based on the predefined image type.
- **Isolate**, **Improve** & **Inline** : iteratively improved the code

* CoR/fallback scenarios are leveraged through resilience4j<br />
  Using [resilience4j](https://resilience4j.readme.io/docs), easy to integrate & use with java, spring boot and
  annotation based
  <br /><br />
* Command Pattern (Invoker, Command, Executor) is achieved through Spring's IoC(Inversion of Control)<br />
  At runtime the instances are resolved to execute the command for respective predefined image type.<br />
  Easy to add/remove functionality for predefined image types.

$\color{orange}{\textsf{What can be better?}}$ <br />
To meet the timeline few functionalities are missed (Anyway, I'll try to implement later...)

- Code functionality coverage with unit tests (wanted to proceed with TDD approach, yet its time-consuming)
- Image compression logic is absent
- Logging is not setup for putting the data in cloud db (wanted to use achieve
  through [logback DBAppender](https://logback.qos.ch/manual/appenders.html#DBAppender))
- Metrics are exposed for all kinds of info, may not be needful for production env. At the moment it is setup for debug.

#### **Performance & resilience improvement**

- uploading images can be done asynchronously to reduce latency in real-time
- flushing images can be improved for fault tolerance. Example, if optimised images are not present and a request is
  received for flushing images for 'original' will attempt to delete optimised images too and any failure in here
  _might_ result in abnormal termination of the request

## How to run

#### Prerequisites

In order to run the project system needs to have the following :

  ```
  Java(17+), Gradle (also needs to be accessible in your user/global classpath)
  Gradle wrapper is included for convenience. 
  ```

#### command to run

  ```
  : ./gradlew bootRun 
   ``` 

Another alternative is specifying the profile

  ```  
  : ./gradlew bootRun --args='--spring.profiles.active=local'

`-- Go to the root directory of the project where you see 'gradlew' and run the above command.`<br />
  ```

Default profile is set to `local`. Change profiles by supplying the profile name. <br />

  ```
  : ./gradlew bootRun --args='--spring.profiles.active=live'
  ```

profiles : `local`, `test`, `live`

P.S. - Of course AWS S3 credentials are not set with any real value. So, running with test/live profile might result in error while application boot up.