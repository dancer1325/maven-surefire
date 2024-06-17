## How was created?
* `mvn archetype:generate -DgroupId=com.example -DartifactId=pojo -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`
  * Remove ALL dependencies and leave just 1! org.apache.maven.plugins:maven-failsafe-plugin
* "**/*Test"
  * test class name convention
* "test*"
  * test method name

## How to run?
* `mvn clean verify`

## Notes
* `assert` -- [Link](https://docs.oracle.com/javase/specs/jls/se22/html/jls-14.html#jls-14.10) --
* Fixtures via
  * `public void setUp() { ... }`
  * `public void tearDown();`