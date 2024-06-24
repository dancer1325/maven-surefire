## Goal
* Run integration tests with JUnit

## How to run integration tests?
* `mvn clean verify`
  * Problems:
    * Problem1: "Could not find artifact localhost:working-directory:jar:1.0"
      * Solution: Specify variables properly `${variable}`
    * Problem2: "[ERROR] WARNING: package MyIT not in working.directory"
      * Solution:TODO:

## Notes
* `junit:junit`
  * required dependency
