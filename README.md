Scio giter8 template
[![Build Status](https://travis-ci.org/spotify/scio.g8.svg?branch=master)](https://travis-ci.org/spotify/scio.g8)
[![GitHub license](https://img.shields.io/github/license/spotify/scio.g8.svg)](./LICENSE)
[![Join the chat at https://gitter.im/spotify/scio](https://badges.gitter.im/spotify/scio.svg)](https://gitter.im/spotify/scio?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
====================

A [Giter8][g8] template for [Scio][scio] that includes a simple [WordCount][WordCount] job to help you getting started.

[![asciicast](https://asciinema.org/a/2UbkLYD3BQgdQnXjuMeI7c8dZ.svg)](https://asciinema.org/a/2UbkLYD3BQgdQnXjuMeI7c8dZ?autoplay=1)

## Running

1. Download and install the [Java Development Kit (JDK)](https://adoptopenjdk.net/index.html) version 8 or 11.
2. [Install sbt](http://www.scala-sbt.org/1.x/docs/Setup.html)
3. `sbt new spotify/scio.g8`
4. `sbt stage`
5. `target/universal/stage/bin/word-count --output=wc`

⚠️ Check your project `README.md` for further details ⚠️

[g8]: http://www.foundweekends.org/giter8/
[scio]: http://github.com/spotify/scio/
[WordCount]: src/main/g8/src/main/scala/$organization__packaged$/WordCount.scala 
