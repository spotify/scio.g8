# $name$

## Raison d'être

TODO: your project description

## Features

This project comes with number of preconfigured features, including:

### Running

The templates ships with beam `DirectRunner` in the `Test` scope. You can run locally with
`sbt Test/runMain example.WordCount --runner=DirectRunner --output=wc`

Run with the selected runner
`sbt runMain example.WordCount --runner=<runner> --output=<path>`
you may have to provide options depending on the runner

### Testing

This template comes with an example of a test, to run tests:

```bash
sbt test
```

### REPL

To experiment with current codebase in [Scio REPL](https://github.com/spotify/scio/wiki/Scio-REPL)
simply:

```bash
sbt repl/run
```

$if(DataflowFlexTemplate.truthy)$
### Dataflow `Flex Template` usage

#### Packaging

This template comes with [sbt-native-packager](https://sbt-native-packager.readthedocs.io) and it allows you to build **zips**, **docker** images, etc. Have a look at the documentation for more details.

```bash
sbt
# create a zip file
> Universal/packageBin
# publish a docker image to your local registry
> Docker/publishLocal
```

#### Google Cloud Platform settings

```sbt
set gcpProject := "<YOUR PROJECT>"
set gcpRegion := "europe-west1"
```

#### Creating the template

```bash
sbt createFlexTemplate
```

Will build the docker image and publish it to [Google Container Registry](https://cloud.google.com/container-registry)

JSON template will be uploaded to `gs://<YOUR PROJECT>/dataflow/templates/flex-template.json`

#### Triggering a run!

```bash
sbt runFlextTemplate input=gs://dataflow-samples/shakespeare/kinglear.txt output=gs://<OUTPUT>
```
$endif$

---

This project is based on the [scio.g8](https://github.com/spotify/scio.g8).
