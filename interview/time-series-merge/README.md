## Problem: Time Series Merge
Time series are stored in files with the following format:
* files are multiline plain text files in ASCII encoding
* each line contains exactly one record
* each record contains date and integer value; records are encoded like so: YYYY-MM-DD:X
* dates within single file are non-duplicate and sorted in ascending order
* files can be bigger than RAM available on target host

Create script which will merge arbitrary number of files, up to 100, into one file. Result file should follow same format
conventions as described above. Records with same date value should be merged into one by summing up X values.

### How to run
Open terminal and run:
`sbt "run --dir input --out result.txt"`

It use all files in 'input' directory & generate 'result.txt' file

