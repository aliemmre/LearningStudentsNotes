#!/bin/sh
set -eu
project_dir=$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)
test_dir=$(mktemp -d "${TMPDIR:-/tmp}/learning-note-checks.XXXXXX")
mkdir "$test_dir/classes" "$test_dir/work"
javac -encoding UTF-8 -d "$test_dir/classes" \
    "$project_dir"/src/DataStructers/*.java \
    "$project_dir"/src/FileProcess/*.java \
    "$project_dir/test/ProjectChecks.java"
cd "$test_dir/work"
java -Djava.awt.headless=true -cp "$test_dir/classes" ProjectChecks "$project_dir/test-data"
printf 'Test dosyaları: %s\n' "$test_dir"
