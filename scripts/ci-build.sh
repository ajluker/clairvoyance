#!/bin/sh

sbt clean coverage test coverageReport
sbt coverageAggregate

# clean up to avoid unnecessary cache updates
find $HOME/.sbt -name "*.lock" | xargs rm
find $HOME/.ivy2 -name "ivydata-*.properties" | xargs rm
