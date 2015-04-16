# cron-stream

[Manifold-based](https://github.com/ztellman/manifold) cron streams

## Artifacts

`cron-stream` artifacts are
[released to clojars](https://clojars.org/com.joshuagriffith/cron-stream). To
use with leiningen:

[![Clojars Project](http://clojars.org/com.joshuagriffith/cron-stream/latest-version.svg)](http://clojars.org/com.joshuagriffith/cron-stream)

## Usage

`cron-stream` takes a 6-field cron expression and returns a stream
that emits `Date`'s periodically, according to the supplied
schedule. Additionally takes the following option keys:

  - `:timezone` evaluate the cron expression in a given
    `TimeZone`. Defaults to the local system timezone.
  
  - `:buffer` buffer size of the returned stream. Defaults to 0.

Returns a manifold stream containing `Date`'s. If the stream parks for
an extended period of time, the next date will be computed based on
when the stream resumes accepting `put!`'s. To prevent missing dates,
use a non-zero buffer value.

Note: the initial `Date` is calculated from the time the stream is
created.

To create a stream that emits dates every three seconds:

```clj
(require '[manifold.deferred :as d]
         '[manifold.stream :as s]
         '[com.joshuagriffith.cron-stream :refer [cron-stream]])

(def cs (cron-stream "*/3 * * * * *"))

(dotimes [_ 3] (println @(s/take! cs)))
```

will print dates every three seconds (on the second):

```clj
#inst "2015-04-16T05:10:48.000-00:00"
#inst "2015-04-16T05:12:12.000-00:00"
#inst "2015-04-16T05:12:15.000-00:00"
```

## Changes

- `0.2.0`: remove `max-error` option and add `buffer` option
- `0.1.1`: fix classpath
- `0.1.0`: initial release

## License

Copyright © 2015 Joshua Griffith

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
