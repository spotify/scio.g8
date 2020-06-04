let GithubActions =
      https://raw.githubusercontent.com/regadas/github-actions-dhall/master/package.dhall sha256:40602cb9f4e3d1964e87bc88385c7946d9796b0fb1358249fce439ac9f30c726

let setup =
      [ GithubActions.steps.checkout
      , GithubActions.steps.run
          { run =
              ''
              shasum build.sbt \
                project/giter8.sbt \
                project/build.properties > gha.cache.tmp
              ''
          }
      , GithubActions.steps.cache
          { path = "~/.sbt", key = "sbt", hashFile = "gha.cache.tmp" }
      , GithubActions.steps.cache
          { path = "~/.cache/coursier"
          , key = "coursier"
          , hashFile = "gha.cache.tmp"
          }
      ]

in  GithubActions.Workflow::{
    , name = "build"
    , on = GithubActions.On::{
      , push = Some GithubActions.Push::{=}
      , pull_request = Some GithubActions.PullRequest::{=}
      }
    , jobs = toMap
        { build = GithubActions.Job::{
          , name = "test"
          , runs-on = GithubActions.types.RunsOn.ubuntu-latest
          , steps =
                setup
              # [ GithubActions.steps.java-setup { java-version = "11.0.5" }
                , GithubActions.steps.run { run = "sbt test" }
                ]
          }
        }
    }
