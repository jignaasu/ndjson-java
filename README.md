# ndjson-java [![build_status](https://travis-ci.org/abhijithtn/ndjson-java.svg?branch=master)](https://travis-ci.org/abhijithtn/ndjson-java)
Java library for creating and parsing [ND-JSON][1]

This library relies on [JSON-java][2] and future
plan is to support other available popular JSON
libraries like Jackson, Gson etc.

## Usage
There is only one class which does both parsing
and generation.

Use `parse` method to parse the JSON string to 
List of JSONObjects. 

Use `generate` method to generate the new-line
delimited JSON string from List of JSON Objects

[1]:https://github.com/ndjson/ndjson-spec
[2]:https://github.com/stleary/JSON-java
