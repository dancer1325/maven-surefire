* == Maven Failsafe MOJO in `maven-failsafe-plugin`
* Goal
  * run integration tests /
    * generate reports in
      * file formats
        * .txt
        * .xml -- [schema](https://maven.apache.org/surefire/maven-failsafe-plugin/xsd/failsafe-test-report.xsd) --
        * 👁️ .html -> you MUST USE `maven-surefire-report-plugin` -- [Link](https://maven.apache.org/surefire/maven-surefire-report-plugin/) -- 👁️
          * ${basedir}/target/failsafe-reports/failsafe-summary.xml, by default
          * [schema](https://maven.apache.org/surefire/maven-failsafe-plugin/xsd/failsafe-summary.xsd) 
      * ${basedir}/target/failsafe-reports/TEST-*.xml path, by default
* "failsafe" root of the name
  * synonym of "surefire"
  * if it fails -> done in a safe way
* `default` Maven build lifecycle's phases / run integration tests -- [Link](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#default-lifecycle) --
  * `pre-integration-test`
    * set up integration test environment
  * `integration-test`
    * run the integration test
    * recommendation
      * use `maven-failsafe-plugin`
  * `post-integration-test`
    * tear down integration test environment
  * `verify`
    * check results of integration test
    * recommendation
      * use `maven-failsafe-plugin`
    * if you run `mvn verify` -> 👁️ ALL the previous ones are executed 👁️
      * Reason: 🧠 build phases in sequential order 🧠
* plugin's goals
  * `failsafe:integration-test`
  * `failsafe:verify`

## Requirements
* Maven v3.2.5+
* Java v1.8+

## How to use?
* Add
  ```
  <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-failsafe-plugin</artifactId>
          <version>3.3.0</version>
          <executions>
            <execution>
              <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  ```
* Run `mvn verify`

## Other testing providers
* TestNG
  * requirements
    * TestNG
      * add the dependency `org.testng:testng`
* JUnit (3.8, 4.x or 5.x)
  * requirements
    * JUnit4
      * add the dependencies `junit:junit` or `junit:junit-dep`
    * JUnit5
      * add the dependencies ` junit-jupiter-engine` or `junit-vintage-engine `
* POJO
  * NO dependencies required

## Examples
* `mvn help:describe -Dplugin=org.apache.maven.plugins:maven-failsafe-plugin`
  * list plugin goals
* Check 'src/it/pojo'
* Check 'src/it/testng'
* Check 'src/it/jetty-war-test-failing'
* Check '.../maven-surefire-report-plugin'
* Check 'src/it/multiple-summaries'
* Check 'src/it/multiple-summaries-failing'
