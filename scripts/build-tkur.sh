#!/usr/bin/env bash
# SPDX-License-Identifier: EPL-2.0
set -euo pipefail
cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.."

./mvnw -B -pl :org.jacoco.cli.test,:org.jacoco.agent,:org.jacoco.report.test -am package source:jar-no-fork "$@"

mkdir -p target/dist
cp org.jacoco.cli/target/org.jacoco.cli-0.8.15-nodeps.jar target/dist/tkur_jacococli.jar
cp org.jacoco.agent.rt/target/org.jacoco.agent.rt-0.8.15-all.jar target/dist/tkur_jacocoagent.jar
cp org.jacoco.report/target/org.jacoco.report-0.8.15.jar target/dist/tkur_jacoco_report.jar
cp org.jacoco.report/target/org.jacoco.report-0.8.15-sources.jar target/dist/tkur_jacoco_report-sources.jar
cp README.md LICENSE.md target/dist/
cd target/dist
sha256sum tkur_jacococli.jar tkur_jacocoagent.jar tkur_jacoco_report.jar \
  tkur_jacoco_report-sources.jar README.md LICENSE.md > SHA256SUMS
sha256sum -c SHA256SUMS
