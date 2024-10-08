 # Using JUnit 5 Platform

## Configuring JUnit Platform

* requirements
  * add >=1 `TestEngine` implementation | your project
    * _Example1:_ if you want to write tests with Jupiter -> add | "pom.xml"

    ```
    <dependencies>
        [...]
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.9.1</version>
            <scope>test</scope>
        </dependency>
        [...]
    </dependencies>
    ```
    
    * _Example2:_ if you want to write and execute JUnit 3 or 4 tests | JUnit Platform -> add

    ```
    <dependencies>
        [...]
        <dependency>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
            <version>5.9.1</version>
            <scope>test</scope>
        </dependency>
        [...]
    </dependencies>
    ```

* "org.junit.jupiter:junit-jupiter-api" | JUnit5
  * == classes + interfaces / -- required to compile your -- test source

## Smart Resolution of Jupiter Engine and Vintage Engine for JUnit4

* TODO:
  JUnit5 API artifact and your test sources become isolated from engine. In these chapters you will see how you can
  segregate, combine, select the APIs and Engines miscellaneous way. You can find integration tests
  {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit-4-5}with JUnit4/5}},
  {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit5-testng}with JUnit5/TestNG}}
  and {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit5-runner}with the JUnit4 Runner for Jupiter tests}}.
  (See the Maven profiles.)

### How to run only one API

  Normally, the developer does not want to access internal classes of JUnit5 engine (e.g. <<<5.9.1>>>).
  In the next chapters you can find your way to use the Jupiter or JUnit5 API where the plugin would resolve the engine.

#### Jupiter API in test dependencies

  In this example the POM has only Jupiter API dependency in test classpath. The plugin will resolve and download
  the <<<junit-jupiter-engine>>> with the version related to the version of <<<junit-jupiter-api>>>. Similar principles
  can be found in the following chapters as well.

+---+
<dependencies>
    [...]
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    [...]
</dependencies>
...
<build>
    <plugins>
        [...]
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            [... configuration or goals and executions ...]
        </plugin>
        [...]
    </plugins>
</build>
...
+---+

*** API-Engine versions segregation

  In the following example the engine artifact appears in plugin dependencies and the engine is resolved by the plugin
  and downloaded from a remote repository for plugins. You may want to update the version of engine with fixed bugs in
  <<<5.9.1>>> but the API version <<<5.9.0>>> stays intact!

+---+
<dependencies>
    [...]
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.0</version>
        <scope>test</scope>
    </dependency>
    [...]
</dependencies>
...
<build>
    <plugins>
        [...]
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <dependencies>
                <dependency>
                    <groupId>org.junit.jupiter</groupId>
                    <artifactId>junit-jupiter-engine</artifactId>
                    <version>5.9.1</version>
                </dependency>
            </dependencies>
        </plugin>
        [...]
    </plugins>
</build>
...
+---+

#### JUnit4 API in test dependencies

  This is similar example with JUnit4 in test dependencies of your project POM.
  The Vintage engine artifact has to be in the plugin dependencies; otherwise the plugin would use
  <<<surefire-junit4>>> provider instead of the <<<surefire-junit-platform>>> provider.

+---+
<dependencies>
    [...]
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    [...]
</dependencies>
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <dependencies>
                <dependency>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                    <version>5.9.1</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
...
+---+

** How to run multiple APIs or Engines

  In the following example you can use both JUnit4 and JUnit5 tests.

*** Jupiter API and JUnit4

  Once you define any JUnit5 API in the dependencies, the provider <<<surefire-junit-platform>>> is selected and
  you can always add the JUnit4 dependency.

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
+---+

*** Jupiter API and Vintage Engine

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
+---+

*** Jupiter and Vintage Engine

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
+---+

** Select engine and use multiple APIs

  In these examples you use both API, i.e. Jupiter and JUnit4, in the test dependencies but you want to select
  the engine via plugin dependencies.

*** Select Jupiter

    Here your tests import the packages from JUnit4 and Jupiter but you want to select only one Maven profile
    with JUnit4 tests.

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
<profile>
    <id>select-junit5</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${project.version}</version>
                <dependencies>
                    <dependency>
                        <groupId>org.junit.jupiter</groupId>
                        <artifactId>junit-jupiter-engine</artifactId>
                        <version>5.9.1</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</profile>
+---+

*** Select JUnit4

  Here your tests import the packages from JUnit4 and Jupiter but you want to select only one Maven profile
  with Jupiter tests.

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
<profile>
    <id>select-junit4</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${project.version}</version>
                <dependencies>
                    <dependency>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                        <version>5.9.1</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</profile>
+---+

** How to run TestNG tests within the JUnit Platform

  You can run TestNG tests combined with JUnit5 tests.

  For more information see this
  {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit5-testng}example}}
  and {{{https://github.com/junit-team/testng-engine}TestNG Engine for the JUnit Platform}}.

+---+
<dependencies>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.4.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.support</groupId>
        <artifactId>testng-engine</artifactId>
        <version>1.0.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
+---+

  The Maven does not take any responsibility for broken compatibilities in this case and the responsibility for
  the dependency <<<org.junit.support:testng-engine>>>.

** JUnit Runner

  The JUnit4 library has the {{{https://junit.org/junit4/javadoc/latest/src-html/org/junit/runner/Runner.html}Runner}}
  implemented in the JUnit5's artifact <<<junit-platform-runner>>>.

  For more information see this
  {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit5-runner}example}}.

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-runner</artifactId>
        <version>1.9.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
+---+

** JUnit5 Suite

  For more information see this
  {{{https://github.com/apache/maven-surefire/tree/master/surefire-its/src/test/resources/junit5-suite}example with surefire integration test}}
  and the {{{https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine}tutorial}}.

+---+
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-suite-engine</artifactId>
        <version>1.9.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>${project.version}</version>
            <configuration>
                <test><!-- your test suite class name should be here --></test>
            </configuration>
        </plugin>
    </plugins>
</build>
+---+

* Provider Selection

   If nothing is configured, Surefire detects which JUnit version to use by the following algorithm:

+---+
if the JUnit 5 Platform Engine is present in the project
    use junit-platform
if the JUnit version in the project >= 4.7 and the <<<parallel>>> configuration parameter has ANY value
    use junit47 provider
if JUnit >= 4.0 is present
    use junit4 provider
else
    use junit3.8.1
+---+

  When using this technique there is no check that the proper test-frameworks are present on your project's
  classpath. Failing to add the proper test-frameworks will result in a build failure.

* Running Tests in Parallel

  From JUnit Platform does not support running tests in parallel.

* Running a Single Test Class

   The JUnit Platform Provider supports the <<<test>>> JVM system property supported by
   the Maven Surefire Plugin. For example, to run only test methods in the <<<org.example.MyTest>>> test class
   you can execute <<<mvn -Dtest=org.example.MyTest test>>> from the command line.


* Filtering by Test Class Names for Maven ${thisPlugin}

   The Maven ${thisPlugin} Plugin will scan for test classes whose fully qualified names match
   the following patterns.

#{if}(${project.artifactId}=="maven-surefire-plugin")
   * <<<**/Test*.java>>>

   * <<<**/*Test.java>>>

   * <<<**/*Tests.java>>>

   * <<<**/*TestCase.java>>>
#{else}
   * <<<**/IT*.java>>>

   * <<<**/*IT.java>>>

   * <<<**/*ITCase.java>>>
#{end}

   Moreover, it will exclude all nested classes (including static member classes) by default.

   Note, however, that you can override this default behavior by configuring explicit
   `include` and `exclude` rules in your `pom.xml` file. For example, to keep Maven ${thisPlugin}
   from excluding static member classes, you can override its exclude rules.

* Overriding exclude rules of Maven ${thisPlugin}

+---+
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <configuration>
                <excludes>
                    <exclude>some test to exclude here</exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
...
+---+


* Filtering by Tags

   You can use JUnit5 Tags and filter tests by tags or tag expressions.

    * To include <<<tags>>> or <<<tag expressions>>>, use <<<groups>>>.

    * To exclude <<<tags>>> or <<<tag expressions>>>, use <<<excludedGroups>>>.

+---+
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <configuration>
                <groups>acceptance | !feature-a</groups>
                <excludedGroups>integration, regression</excludedGroups>
            </configuration>
        </plugin>
    </plugins>
</build>
...
+---+


* Filtering JUnit5 Engines

   You can filter engines by the ID/s of an engine to be included or excluded in the test run.

    * To include <<<engines>>>, use <<<includeJUnit5Engines>>>.

    * To exclude <<<engines>>>, use <<<excludeJUnit5Engines>>>.

   Be aware that this feature reserves system properties <<<includejunit5engines>>> and <<<excludejunit5engines>>>
   for internal usage.

+---+
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <configuration>
                <includeJUnit5Engines>
                    <engine>my-first-engine-id-to-include</engine>
                    <engine>my-second-engine-id-to-include</engine>
                </includeJUnit5Engines>
                <excludeJUnit5Engines>
                    <engine>my-first-engine-id-to-include</engine>
                    <engine>my-second-engine-id-to-include</engine>
                </excludeJUnit5Engines>
            </configuration>
        </plugin>
    </plugins>
</build>
...
+---+


* Configuration Parameters

   You can set JUnit Platform configuration parameters to influence test discovery and execution by
   declaring the <<<configurationParameters>>> property and providing key-value pairs using the Java
   <<<Properties>>> file syntax (as shown below) or via the <<<junit-platform.properties>>> file.

+---+
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <configuration>
                <properties>
                    <configurationParameters>
                        junit.jupiter.conditions.deactivate = *
                        junit.jupiter.extensions.autodetection.enabled = true
                        junit.jupiter.testinstance.lifecycle.default = per_class
                        junit.jupiter.execution.parallel.enabled = true
                    </configurationParameters>
                </properties>
            </configuration>
        </plugin>
    </plugins>
</build>
...
+---+


* Surefire Extensions and Reports Configuration for @DisplayName

   Since plugin version 3.0.0-M4 you can use fine grained configuration of reports and enable phrased names together
   with <<<@DisplayName>>> in you tests.
   This is the complete list of attributes of particular objects. You do not have to specify e.g. <<<disable>>>,
   <<<version>>> and <<<encoding>>>. The boolean values reach default values <<<false>>> if not specified otherwise.

+---+
...
<build>
    <plugins>
        ...
        <plugin>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}</artifactId>
            <version>${project.version}</version>
            <configuration>
                <statelessTestsetReporter implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5Xml30StatelessReporter">
                    <disable>false</disable>
                    <version>3.0.1</version>
                    <usePhrasedFileName>false</usePhrasedFileName>
                    <usePhrasedTestSuiteClassName>true</usePhrasedTestSuiteClassName>
                    <usePhrasedTestCaseClassName>true</usePhrasedTestCaseClassName>
                    <usePhrasedTestCaseMethodName>true</usePhrasedTestCaseMethodName>
                </statelessTestsetReporter>
                <consoleOutputReporter implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5ConsoleOutputReporter">
                    <disable>false</disable>
                    <encoding>UTF-8</encoding>
                    <usePhrasedFileName>false</usePhrasedFileName>
                </consoleOutputReporter>
                <statelessTestsetInfoReporter implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5StatelessTestsetInfoReporter">
                    <disable>false</disable>
                    <usePhrasedFileName>false</usePhrasedFileName>
                    <usePhrasedClassNameInRunning>true</usePhrasedClassNameInRunning>
                    <usePhrasedClassNameInTestCaseSummary>true</usePhrasedClassNameInTestCaseSummary>
                </statelessTestsetInfoReporter>
            </configuration>
        </plugin>
    </plugins>
</build>
...
+---+

   Default implementations of these extensions are
   <<<org.apache.maven.plugin.surefire.extensions.SurefireStatelessReporter>>>,
   <<<org.apache.maven.plugin.surefire.extensions.SurefireConsoleOutputReporter>>>, and
   <<<org.apache.maven.plugin.surefire.extensions.SurefireStatelessTestsetInfoReporter>>>.

   The aim of extensions is to let the users customizing the default behavior. We are keen on listing useful
   extensions on Apache Maven Surefire site if you propagate your extensions on GitHub.


* External extensions for the plugin

   This extension prints a tree view for the unit tests on the console.
   This is related to JUnit5 tests.

[extension1.png] The console with this extension.

   Follow the example with the configuration and the dependency in the plugin.

+---+
...
<plugin>
    <groupId>${project.groupId}</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${project.version}</version>
    <dependencies>
        <dependency>
            <groupId>me.fabriciorby</groupId>
            <artifactId>maven-surefire-junit5-tree-reporter</artifactId>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
    <configuration>
        <reportFormat>plain</reportFormat>
        <consoleOutputReporter>
            <disable>true</disable>
        </consoleOutputReporter>
        <statelessTestsetInfoReporter implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5StatelessTestsetInfoTreeReporter"/>
    </configuration>
</plugin>
...
+---+

