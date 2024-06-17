* == Maven Surefire MOJO in `maven-surefire-plugin`
* Goal
  * run unit tests
* "surefire" root of the name
  * synonym of "failsafe"
  * if you run test (`integration-test` build phase) with this plugin, and it fails -> build stops == ⚠️integration test environment NOT torn down ⚠️
    * -> `maven-failsafe-plugin` is used during
      * `integration-test`
      * `verify`
* TODO:
