# cron-stream

[Manifold-based](https://github.com/ztellman/manifold) cron streams

## Artifacts

`cron-stream` artifacts are
[released to clojars](https://clojars.org/com.joshuagriffith/cron-stream).

```clj
[com.joshuagriffith/cron-stream "0.1.0"]
```

## Usage

`cron-stream` takes a cron string and the following optional keywords:

  - `:timezone` evaluate the cron expression in a given
    `TimeZone`. Defaults to the local system timezone.
  
  - `:max-error` the largest allowable error (in
    milliseconds). Defaults to 50 ms.

To use with [manifold](https://github.com/ztellman/manifold):

```clj
(require '[manifold.deferred :as d]
         '[manifold.stream :as s]
         '[com.joshuagriffith.cron-stream :refer [cron-stream]])

(def cs (cron-stream "*/3 * * * * *"))

(dotimes [_ 10]
  (println @(d/chain' (s/take! cs)
                      #(vector (Date.) (str %)))))
```

Emits:

```clj
[#inst "2015-04-15T21:47:51.000-00:00" 2015-04-15T21:47:51.000Z]
[#inst "2015-04-15T21:47:54.004-00:00" 2015-04-15T21:47:54.000Z]
[#inst "2015-04-15T21:47:57.004-00:00" 2015-04-15T21:47:57.000Z]
[#inst "2015-04-15T21:48:00.000-00:00" 2015-04-15T21:48:00.000Z]
[#inst "2015-04-15T21:48:03.001-00:00" 2015-04-15T21:48:03.000Z]
[#inst "2015-04-15T21:48:06.005-00:00" 2015-04-15T21:48:06.000Z]
[#inst "2015-04-15T21:48:09.000-00:00" 2015-04-15T21:48:09.000Z]
[#inst "2015-04-15T21:48:12.004-00:00" 2015-04-15T21:48:12.000Z]
[#inst "2015-04-15T21:48:15.002-00:00" 2015-04-15T21:48:15.000Z]
[#inst "2015-04-15T21:48:18.002-00:00" 2015-04-15T21:48:18.000Z]
[#inst "2015-04-15T21:48:21.001-00:00" 2015-04-15T21:48:21.000Z]
```

## License

Copyright © 2015 Joshua Griffith

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
