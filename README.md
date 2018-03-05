Scio giter8 template
[![Build Status](https://travis-ci.org/spotify/scio.g8.svg?branch=master)](https://travis-ci.org/spotify/scio.g8)
[![GitHub license](https://img.shields.io/github/license/spotify/scio.g8.svg)](./LICENSE)
[![Join the chat at https://gitter.im/spotify/scio](https://badges.gitter.im/spotify/scio.svg)](https://gitter.im/spotify/scio?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
====================

A [Giter8][g8] template for [Scio][scio] that includes a simple [WordCount][WordCount] job to help you getting started.

## Running

1. [Install sbt](http://www.scala-sbt.org/1.x/docs/Setup.html)
2. `sbt new spotify/scio.g8`
3. `sbt pack`
4. `target/bin/word-count --output=results`

Notes:

We have enabled beam's `DirectRunner`. To use other runners, you need to manually add the required [dependency](src/main/g8/build.sbt#L45).    

[g8]: http://www.foundweekends.org/giter8/
[scio]: http://github.com/spotify/scio/
[WordCount]: src/main/g8/src/main/scala/$organization__packaged$/WordCount.scala 
