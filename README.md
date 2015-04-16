# cron-stream

[Manifold-based][manifold] cron streams.

## Artifacts

`cron-stream` artifacts are [released to Clojars][clojars-cs]. To use
with Leiningen:

[![Clojars Project](http://clojars.org/com.joshuagriffith/cron-stream/latest-version.svg)][clojars-cs]

## Usage

`cron-stream` takes a 6-field cron expression and returns a stream
that emits `Date`s periodically, according to the supplied
schedule. Here are some [sample cron expressions][]:

Expression               | Description
------------------------ | -----------------------------------------------
`"0 0 * * * *"`          | the top of every hour of every day
`"*/10 * * * * *"`       | every ten seconds
`"0 0 8-10 * * *"`       | 8, 9 and 10 o'clock of every day
`"0 0/30 8-10 * * *"`    | 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day
`"0 0 9-17 * * MON-FRI"` | on the hour nine-to-five weekdays
`"0 0 0 25 12 ?"`        | every Christmas Day at midnight

Additionally, `cron-stream` takes the following option keys:

Option      | Description                                       | Default
----------- | ------------------------------------------------- | ---------------------
`:timezone` | evaluate the cron expression in a given`TimeZone` | local system timezone
`:buffer`   | buffer size of the returned stream                | 0

It returns a [manifold stream][] containing `Date`s. If the stream
parks for an extended period of time, the next date will be computed
based on when the stream resumes accepting `put!`s. To prevent missing
cron events, use a non-zero buffer value. Note: the initial `Date` is
calculated from the time the stream is created.

To create a stream that emits dates every three seconds:

```clj
(require '[manifold.deferred :as d]
         '[manifold.stream :as s]
         '[com.joshuagriffith.cron-stream :refer [cron-stream]])

(dotimes [_ 3]
  (println @(s/take! (cron-stream "*/3 * * * * *"))))
```

This will print dates every three seconds (on the second):

```clj
#inst "2015-04-16T05:51:30.000-00:00"
#inst "2015-04-16T05:51:33.000-00:00"
#inst "2015-04-16T05:51:36.000-00:00"
```

Manifold streams interoperate with [core.async][]. To connect a stream
to a channel, use `connect`:

```clj
(require '[clojure.core.async :refer [chan go-loop <!]])

(def cs (cron-stream "*/3 * * * * *"))
(def ch (chan))

(s/connect cs ch)

(go-loop [i 0]
  (when (< i 3)
    (println (<! ch))
    (recur (inc i))))
```

Streams can also be treated as lazy seqs:

```clj
(take 5 (s/stream->seq cs))
```

## Changes

- [`0.2.0`][v0.2.0]: remove `max-error` option and add `buffer` option
- [`0.1.1`][v0.1.1]: fix classpath
- `0.1.0`: initial release

## License

Copyright © 2015 Joshua Griffith

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[clojars-cs]: https://clojars.org/com.joshuagriffith/cron-stream
[core.async]: https://github.com/clojure/core.async
[manifold stream]: https://github.com/ztellman/manifold/blob/master/docs/stream.md
[manifold]: https://github.com/ztellman/manifold
[sample cron expressions]: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
[v0.1.1]: https://github.com/hadronzoo/cron-stream/compare/0.1.0...0.1.1
[v0.2.0]: https://github.com/hadronzoo/cron-stream/compare/0.1.1...0.2.0
