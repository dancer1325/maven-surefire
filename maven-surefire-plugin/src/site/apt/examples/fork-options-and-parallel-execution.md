# Fork Options and Parallel Test Execution

* memory requirements & execution time of your build system -- depends on -- 
  * forking strategy
  * parallel execution settings
* surefire offers several options to execute tests in parallel
* forking -- can impact on -- memory requirements low
* goal
  * how to configure the test execution / more suitable | your environment

## Parallel Test Execution

* ways to achieve parallel test execution | `maven-surefire-plugin`
  * via `parallel` parameter
    * possible values -- depend on the -- test provider used
      * | JUnit v4.7
        * `methods`
        * `classes`
        * `both`
          * from `maven-surefire-plugin` v2.16
            * deprecated 
            * still can be used
        * `suites`
        * `suitesAndClasses`
        * `suitesAndMethods`
        * `classesAndMethods`
          * behavior == `both`
        * `all`
    * prerequisites  -- depend on the -- test provider used
      * | JUnit tests
        * JUnit runner -- should extend -- `org.junit.runners.ParentRunner`
          * if NO runner is specified -> use `@org.junit.runner.RunWith`
* _Examples:_
  * {{{./junit.html#Running_tests_in_parallel}JUnit}}
  * {{{./testng.html#Running_tests_in_parallel}TestNG}}
* ways to extend parallelism
  * `useUnlimitedThreads`
    * == unlimited # of threads
    * TODO: By default it's `false` ?
  * `threadCount`
    * requirements
      * `useUnlimitedThreads=false`
    * if it's specified, BUT NOT ANY other `threadCount*` -> 
      * -- try to optimize -- thread counts | suites, classes or methods
      * threads -- are replaced in favor of -- `leaf`
  * `perCoreThreadCount`
    * by default, `true`
* ways to impose thread-count limitations | suites, classes or methods / `maven-surefire-plugin` v2.16
  * `threadCountSuites`
  * `threadCountClasses`
  * `threadCountMethods`
* _Examples:_
  * _Example1:_ `parallel=all`, `useUnlimitedThreads=true`, `threadCountSuites=3`
    * == unlimited # of threads / <=3 concurrent threads | suites
  * _Example2:_ `parallel=classesAndMethods`, `threadCount=8`, `threadCountClasses=3`
    * parallel methods [5, 7] -- TODO: Why? --
    * #of concurrent methods NOT strictly limited
  * _Example3:_ `parallel=all`, `threadCount=16`, `threadCountClasses=2`, `threadCountClasses=3`, `threadCountMethods=5`
    * -> concurrent suites is 20%, concurrent classes 30%, and concurrent methods 50% -- TODO: Why? -- 

* TODO:
 
              The ${project.artifactId} is trying to reuse threads, thus <<optimize>>
              the thread-counts, and prefers thread fairness. The optimization
              <<<parallelOptimized>>> of the number of Threads is enabled by default in terms
              of e.g. the number of <Suite> runners do not necessarily have to waste <Suite>'s
              Thread resources. If <<<threadCount>>> is used, then the <<leaf>> with unlimited
              thread-count may speed up especially at the end of test phase.

              The parameters <<<parallelTestsTimeoutInSeconds>>> and
              <<<parallelTestsTimeoutForcedInSeconds>>> are used to specify an optional
              timeout in parallel execution. If the timeout is elapsed, the plugin prints
              the summary log with ERROR lines:
              <"These tests were executed in prior to the shutdown operation">, and
              <"These tests are incomplete"> if the running Threads were <<interrupted>>.

              <<Note:>> As designed by JUnit runners, the static methods annotated with e.g.
              <@Parameters>, <@BeforeClass> and <@AfterClass> are called in parent thread.
              For the sake of memory visibility between threads synchronize the methods.
              See the keywords: <volatile>, <synchronized>, <<immutable>> and <final> in
              {{{https://jcp.org/en/jsr/detail?id=133}Java Memory Model - JSR-133}}.

              <<The important thing to remember>> with the <<<parallel>>> option is: the
              concurrency happens within the same JVM process. That is efficient in terms of
              memory and execution time, but you may be more vulnerable towards race
              conditions or other unexpected and hard to reproduce behavior.

              The other possibility for parallel test execution is setting the parameter 
              <<<forkCount>>> to a value higher than 1. The next section covers the details 
              about this and the related <<<reuseForks>>> parameter.
              Using <<<reuseForks=true>>> (by default) and forking the test classes in reusable
              JVMs may lead to the same problem with shared <static code> across <@BeforeClass>
              class initializers if using <<<parallel>>> without forking. Therefore setting
              <<<reuseForks=false>>> may help however it would not guarantee proper
              functionality of some features, e.g. <<<skipAfterFailureCount>>>.

### Parallel Test Execution & 1! Thread Execution

  As mentioned above the <<<parallel>>> test execution is used with specific
  thread count. Since of ${project.artifactId}:2.18, you can apply the <JCIP>
  annotation <<<@net.jcip.annotations.NotThreadSafe>>> on the Java class of JUnit
  test (pure test class, <Suite>, <Parameterized>, etc.) in order to execute it in
  single Thread instance. The Thread has name <maven-surefire-plugin@NotThreadSafe>
  and it is executed at the end of the test run.

  Just use project dependency <net.jcip:jcip-annotations:1.0> or another artifact
  <com.github.stephenc.jcip:jcip-annotations:1.0-1> with Apache License 2.0.
  
+---+
<dependencies>
  [...]
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <!-- 4.7 or higher -->
    <version>4.7</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.github.stephenc.jcip</groupId>
    <artifactId>jcip-annotations</artifactId>
    <version>1.0-1</version>
    <scope>test</scope>
  </dependency>
  [...]
</dependencies>
+---+

  This way the parallel execution of tests classes annotated with
  <<<@NotThreadSafe>>> are forked in single thread instance (don't mean
  forked JVM process).

  If the <Suite> or <Parameterized> is annotated with <<<@NotThreadSafe>>>, the
  suite classes are executed in single thread.
  You can also annotate individual test class referenced by <Suite>, and the other
  unannotated test classes in the <Suite> can be subject to run in parallel.
  This way you can isolate conflicting groups of tests and still run their
  individual tests in parallel.

  <<Note:>> As designed by JUnit runners, the static methods annotated with e.g.
  <@Parameters>, <@BeforeClass> and <@AfterClass> are called in parent thread.
  Assign classes to <<<@NotThreadSafe Suite>>> to prevent from this trouble. If you
  do not want to change the hierarchy of your test classes, you may synchronize such
  methods for the sake of improving memory visibility as a simplistic treatment.
  See the keywords: <volatile>, <synchronized>, <<immutable>> and <final> in
  {{{https://jcp.org/en/jsr/detail?id=133}Java Memory Model - JSR-133}}.

### Parallel Test-Suite Execution

  ${project.artifactId}'s notion of "suite" is related to
  {{{https://github.com/junit-team/junit4/wiki/Aggregating-tests-in-suites}junit4 Suite}}. 

  For example, say your tests are laid out like this:

+---+
src/test/java
+-features.areaA
| +-SomeTest.java
| +-AnotherTest.java
+-features.areaB
| +-DifferentTest.java
| +-OtherTest.java
+---+

  You would add a TestSuite.java for each package, that would look something like:

+---+
package features.areaA;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    SomeTest.class,
    AnotherTest.class
})
public class TestSuite {
}
+---+

  and similarly for <<<package features.areaB>>>. You would then configure
  ${project.artifactId} as follows:

+---+
<plugin>
  <groupId>${project.groupId}</groupId>
  <artifactId>${project.artifactId}</artifactId>
  <version>${project.version}</version>
  <configuration>
    <includes>
      <include>features.*.TestSuite</include>
    </includes>
    <parallel>suites</parallel>
    <threadCountSuites>2</threadCountSuites>
    <perCoreThreadCount>false</perCoreThreadCount>
  </configuration>
</plugin>
+---+

  This would then run up to 2 threads, and each thread would be fed a TestSuite.class,
  one at a time. The test classes within that Suite would be executed sequentially
  in the order specified the Suite class.

  Note that the <<<perCoreThreadCount>>> is a "multiplier" of a sorts. With this
  parameter set to true (the default value), you will get the specified number of
  threads <<per CPU core>>.

  Also note that in this specific case <<<threadCountSuites=2>>> has the same effect
  as <<<threadCount=2>>>, since we are using <<<parallel=suites>>>.

### Parallel ${project.artifactId} Execution in Multi-Module Maven Parallel Build

  Maven core allows building modules of multi-module projects in parallel with
  the command line option <<<-T>>>. This <multiplies> the extent of concurrency
  configured directly in ${project.artifactId}.

## Forked Test Execution

  The parameter <<<forkCount>>> defines the maximum number of JVM processes that
  ${project.artifactId} will spawn <concurrently> to execute the tests. It supports
  the same syntax as <<<-T>>> in maven-core: if you terminate the value with a 'C',
  that value will be multiplied with the number of available CPU cores in your
  system. For example <<<forkCount=2.5C>>> on a Quad-Core system will result
  in forking up to ten concurrent JVM processes that execute tests.

  The parameter <<<reuseForks>>> is used to define whether to terminate the 
  spawned process after one test class and to create a new process for the next 
  test in line (<<<reuseForks=false>>>), or whether to reuse the processes to 
  execute the next tests (<<<reuseForks=true>>>).

  The <default setting> is <<<forkCount=1>>>/<<<reuseForks=true>>>, which means
  that ${project.artifactId} creates one new JVM process to execute all tests
  in one Maven module.

  <<<forkCount=1>>>/<<<reuseForks=false>>> executes each test class in its own 
  JVM process, one after another. It creates the highest level of separation for 
  the test execution, but it would probably also give you the longest execution 
  time of all the available options. Consider it as a last resort.

  With the <<<argLine>>> property, you can specify additional parameters to be
  passed to the forked JVM process, such as memory settings. System property
  variables from the main maven process are passed to the forked process as
  well. Additionally, you can use the element <<<systemPropertyVariables>>> to
  specify variables and values to be added to the system properties during the
  test execution.

  You can use the placeholder <<<$\{surefire.forkNumber\}>>> within <<<argLine>>>,
  <<<environmentVariables>>> (since ${project.artifactId}:3.2.0),
  or within the system properties (both those specified via
  <<<mvn test -D...>>> and via <<<systemPropertyVariables>>>). Before executing 
  the tests, the ${thisPlugin.toLowerCase()} plugin replaces that placeholder
  by the number of the actually executing process, counting from 1 to the
  effective value of <<<forkCount>>> times the maximum number of parallel
  executions in Maven parallel builds, i.e. the effective value of the <<<-T>>>
  command line argument of Maven core.

  In case of disabled forking (<<<forkCount=0>>>), the placeholder will be
  replaced with <1>.

  The following is an example configuration that makes use of up to three forked
  processes that execute the tests and then terminate. A system property
  <databaseSchema> is passed to the processes, that shall specify the database
  schema to use during the tests. The values for that will be
  <MY_TEST_SCHEMA_1>, <MY_TEST_SCHEMA_2>, and <MY_TEST_SCHEMA_3> for the three
  processes. Additionaly by specifying custom <workingDirectory> each of processes
  will be executed in a separate working directory to ensure isolation on file system level.

+---+
<plugins>
[...]
  <plugin>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
    <configuration>
        <forkCount>3</forkCount>
        <reuseForks>true</reuseForks>
        <argLine>-Xmx1024m -XX:MaxPermSize=256m</argLine>
        <systemPropertyVariables>
            <databaseSchema>MY_TEST_SCHEMA_${surefire.forkNumber}</databaseSchema>
        </systemPropertyVariables>
        <workingDirectory>FORK_DIRECTORY_${surefire.forkNumber}</workingDirectory>
    </configuration>
  </plugin>
[...]
</plugins>
+---+

  In case of a multi module project with tests in different modules, you could 
  also use, say, <<<mvn -T 2 ...>>> to start the build, yielding values for 
  <<<$\{surefire.forkNumber\}>>> ranging from 1 to 6.

  Imagine you execute some tests that use a JPA context, which has a notable
  initial startup time. By setting <<<reuseForks=true>>>, you can reuse that
  context for consecutive tests. And as many tests tend to use and access the
  same test data, you can avoid database locks during the concurrent execution
  by using distinct but uniform database schemas.

  Port numbers and file names are other examples of resources for which it may
  be hard or undesired to be shared among concurrent test executions.
  
### Isolating report directories across forks
  
  You may run multiple TestNG suites in parallel fork JVM processes. In that case
  one can see that each process has created XML report file with same name
  overriding one each other. In order to prevent from this, you may want to change
  report directory for each Suite XML. Since of Version 2.22.1 you can parameterize
  <<<reportsDirectory>>> with placeholder <<<$\{surefire.forkNumber\}>>>.
  
+---+
[...]
<dependencies>
    [...]
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>5.7</version>
        <classifier>jdk15</classifier>
    </dependency>
    [...]
</dependencies>
[...]
<build>
    <plugins>
	    [...]
	    <plugin>
		    <groupId>${project.groupId}</groupId>
		    <artifactId>${project.artifactId}</artifactId>
		    <version>${project.version}</version>
		    <configuration>
			    <forkCount>2</forkCount>
			    <reuseForks>false</reuseForks>
			    <reportsDirectory>target/surefire-reports-${surefire.forkNumber}</reportsDirectory>
			    <suiteXmlFiles>
				    <suiteXmlFile>src/test/resources/Suite1.xml</suiteXmlFile>
				    <suiteXmlFile>src/test/resources/Suite2.xml</suiteXmlFile>
			    </suiteXmlFiles>
		    </configuration>
	    </plugin>
        [...]
    </plugins>
</build>
+---+

### Combining forkCount and parallel

  The modes <<<forkCount=0>>> and <<<forkCount=1>>>/<<<reuseForks=true>>> can
  be combined freely with the available settings for <<<parallel>>>.

  As <<<reuseForks=false>>> creates a new JVM process for each test class,
  using <<<parallel=classes>>> would have no effect. You can still use
  <<<parallel=methods>>>, though.

  When using <<<reuseForks=true>>> and a <<<forkCount>>> value larger than one,
  test classes are handed over to the forked process one-by-one. Thus,
  <<<parallel=classes>>> would not change anything. However, you can use
  <<<parallel=methods>>>: classes are executed in <<<forkCount>>> concurrent
  processes, each of the processes can then use <<<threadCount>>> threads to
  execute the methods of one class in parallel.

  Regarding the compatibility with multi-module parallel maven builds via 
  <<<-T>>>, the only limitation is that you can not use it together with
  <<<forkCount=0>>>.

  When running parallel maven builds without forks, all system properties
  are shared among the builder threads and ${thisPlugin.toLowerCase()} executions,
  therefore the threads will encounter race conditions when setting
  properties, e.g. <<<baseDir>>>, which may lead to changing system properties
  and unexpected runtime behaviour.
