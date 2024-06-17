[Link](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/testng.html)

## How was created?
* `mvn archetype:generate -DgroupId=com.example -DartifactId=testng -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`
* Rules to follow
  * testFile name - "*IT.java" by default -
    * it can be modified via
      * [Inclusions and Exclusion Tests](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/inclusion-exclusion.html)
      * [Suite .xml](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/testng.html#using-suite-xml-files)
        * override the inclusion and exclusions
* Customizations
  * specify test parameters via
    * `@Parameters` OR
    * [system properties](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/system-properties.html)
  * group tests
    * allows
      * executing groups, rather than individual tests
  * run tests in parallel
* TODO: Follow the documentation

## How to run?
* `mvn verify`
  * Problems:
    * Problem1: Why is NOT test indicated as run?
      * Solution: TODO: