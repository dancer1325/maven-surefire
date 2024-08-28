# Configuring JUnit

* add | your project

    ```
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
    ```

# Different Generations of JUnit support

* Surefire supports
  * JUnit 3.8.x,
  * JUnit 4.x (serial provider)
    * from Surefire v2.7 -> algorithm / choose which tests to run -- has -- changed
      * -> if you upgrade from a Surefire version / prior to v2.7  -> build can be run with `-Dsurefire.junit4.upgradecheck`
  * JUnit 4.7 (junit-core provider +  parallel support)
    * provider -- is selected based on the --
      * JUnit | in your project &
      * configuration parameters -- for -- parallel

# Provider Selection

* if nothing is configured -> Surefire detects JUnit version to use by the algorithm

    ```
    if the JUnit 5 Platform Engine | the project
        use junit-platform
    if the JUnit version | the project >= 4.7 & `parallel` configuration parameter has `ANY` value
        use junit4.7 provider
    if JUnit >= 4.0 | the project
        use junit4 provider
    else
        use junit3.8.1
    ```
  * ⚠️ pay attention to hierarchy transitive dependencies ⚠️
    * run `mvn dependency:tree` 

## Manually Specifying a Provider

* add plugin's dependency "org.apache.maven.surefire:surefire-junit47"

    ```
    <plugins>
    [...]
      <plugin>
        <groupId>${project.groupId}</groupId>
        <artifactId>${project.artifactId}</artifactId>
        <version>${project.version}</version>
        <dependencies>
          <dependency>
            <groupId>org.apache.maven.surefire</groupId>
            <artifactId>surefire-junit47</artifactId>
            <version>${project.version}</version>
          </dependency>
        </dependencies>
      </plugin>
    [...]
    </plugins>
    ```

  * -> directly, this provider will be used
    * == NOT previously provider selection

# Running Tests in Parallel

* TODO
  From JUnit 4.7 onwards you can run your tests in parallel. To do this, you must set the
  <<<parallel>>> parameter, and may change the <<<threadCount>>> or <<<useUnlimitedThreads>>> attribute.
  For example:

+---+
<plugins>
    [...]
      <plugin>
        <groupId>${project.groupId}</groupId>
        <artifactId>${project.artifactId}</artifactId>
        <version>${project.version}</version>
        <configuration>
          <parallel>methods</parallel>
          <threadCount>10</threadCount>
        </configuration>
      </plugin>
    [...]
</plugins>
+---+


  If your tests specify any value for the <<<parallel>>> attribute and your project uses JUnit 4.7+, your request will be routed to
  the concurrent JUnit provider, which uses the JUnit JUnitCore test runner.

  This is particularly useful for slow tests that can have high concurrency.

  As of Surefire 2.7, no additional dependencies are needed to use the full set of options with parallel.
  As of Surefire 2.16, new thread-count attributes are introduced, namely <<<threadCountSuites>>>, <<<threadCountClasses>>> and
  <<<threadCountMethods>>>. Additionally, the new attributes <<<parallelTestsTimeoutInSeconds>>> and
  <<<parallelTestsTimeoutForcedInSeconds>>> are used to shut down the parallel execution after an elapsed timeout, and
  the attribute <<<parallel>>> specifies new values.
  
  See also {{{./fork-options-and-parallel-execution.html}Fork Options and Parallel Test Execution}}.

* Using Custom Listeners and Reporters

  The junit4 and junit47 providers provide support for attaching custom <<<RunListeners>>> to your tests.

  You can configure multiple custom listeners like this:

+---+
<dependencies>
[...]
  <dependency>
    <groupId>your-junit-listener-artifact-groupid</groupId>
    <artifactId>your-junit-listener-artifact-artifactid</artifactId>
    <version>your-junit-listener-artifact-version</version>
    <scope>test</scope>
  </dependency>
[...]
</dependencies>
[...]
<plugins>
[...]
  <plugin>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
    <configuration>
      <properties>
        <property>
          <name>listener</name>
          <value>com.mycompany.MyResultListener,com.mycompany.MyResultListener2</value>
        </property>
      </properties>
    </configuration>
  </plugin>
[...]
</plugins>
+---+

  For more information on JUnit, see the {{{http://www.junit.org}JUnit web site}}.

  You can implement JUnit listener interface <<<org.junit.runner.notification.RunListener>>> in
  a separate test artifact <<<your-junit-listener-artifact>>> with scope=test, or in project test source code
  <<<src/test/java>>>. You can filter test artifacts by the parameter <<<dependenciesToScan>>> to load its classes
  in current ClassLoader of surefire-junit* providers.

  Since JUnit 4.12 thread safe listener class should be annotated by
  <<<org.junit.runner.notification.RunListener.ThreadSafe>>> which avoids unnecessary locks in JUnit.


* Using a Security Manager (JUnit3 only)

   As long as <<<forkCount>>> is not 0 and you use JUnit3, you can run your tests with a Java security manager enabled.
   The class name of the security manager must be sent as a system property variable to the JUnit3 provider.

   The JDK 17 deprecated the class <<<java.lang.SecurityManager>>> and the security manager is not fully supported
   since JDK 18. The JUnit3 provider fails with enabled system property <<surefire.security.manager>>> in JDK 18+.

   JUnit4 uses mechanisms internally that are not compatible with the tested
   security managers and thus this means of configuring a security manager with JUnit4 is not supported
   by Surefire.

+---+
<plugins>
[...]
      <plugin>
        <groupId>${project.groupId}</groupId>
        <artifactId>${project.artifactId}</artifactId>
        <version>${project.version}</version>
        <configuration>
          <systemPropertyVariables>
            <surefire.security.manager>java.lang.SecurityManager</surefire.security.manager>
          </systemPropertyVariables>
        </configuration>
      </plugin>
[...]
</plugins>
+---+

* Using a Security Manager (All providers)

    Alternatively you can define a policy file that allows all providers to run with Surefire
    and configure it using the <<<argLine>>> parameter and two system properties:

+---+
<plugins>
[...]
      <plugin>
        <groupId>${project.groupId}</groupId>
        <artifactId>${project.artifactId}</artifactId>
        <version>${project.version}</version>
        <configuration>
          <argLine>-Djava.security.manager -Djava.security.policy=${basedir}/src/test/resources/java.policy</argLine>
        </configuration>
      </plugin>
[...]
</plugins>
+---+

  The disadvantage of this solution is that the policy changes will affect the tests too, which make the
  security environment less realistic.

* Using JUnit Categories

    JUnit 4.8 introduced the notion of Categories. You can use JUnit categories by using the <<<groups>>> parameter. As long as 
    the JUnit version in the project is 4.8 or higher, the presence of the "groups" parameter will automatically make Surefire select
    the junit47 provider, which supports groups.

+---+
<plugins>
[...]
    <plugin>
        <groupId>${project.groupId}</groupId>
        <artifactId>${project.artifactId}</artifactId>
        <version>${project.version}</version>
        <configuration>
            <groups>com.mycompany.SlowTests</groups>
        </configuration>
    </plugin>
[...]
</plugins>
+---+

  This will execute only those tests annotated with the <<<@Category(com.mycompany.SlowTests.class)>>> annotation and those tests
  annotated with <<<@Category(com.mycompany.SlowerTests.class)>>> if class/interface <<<SlowerTests>>> is subclass of <<<SlowTests>>>:

+---+
    public interface SlowTests{}
    public interface SlowerTests extends SlowTests{}
+---+

+---+
    public class AppTest {
      @Test
      @Category(com.mycompany.SlowTests.class)
      public void testSlow() {
        System.out.println("slow");
      }

      @Test
      @Category(com.mycompany.SlowerTests.class)
      public void testSlower() {
        System.out.println("slower");
      }

      @Test
      @Category(com.mycompany.FastTests.class)
      public void testSlow() {
        System.out.println("fast");
      }
    }
+---+

  The <<<@Category>>> annotation can also be applied at class-level.
  
  Multiple categories can be specified by comma-delimiting them in the <<<groups>>> parameter in which case tests annotated with
  any of the categories will be executed.
	
  For more information on JUnit, see the {{{http://www.junit.org}JUnit web site}}.

  Since version 2.18.1 and JUnit 4.12, the <<<@Category>>> annotation type is automatically inherited from
  superclasses, see <<<@java.lang.annotation.Inherited>>>. Make sure that test class inheritance still makes sense
  together with <<<@Category>>> annotation of the JUnit 4.12 or higher appeared in superclass.
