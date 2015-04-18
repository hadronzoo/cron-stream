(ns com.joshuagriffith.cron-stream
  (:require [manifold.deferred :as d]
            [manifold.stream :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:import [java.util Date TimeZone]
           [org.springframework.scheduling.support CronSequenceGenerator]))

(defn cron-stream
  "Takes a 6-field cron expression and returns a stream that emits
  Dates according to the supplied schedule. Additionally takes the
  following option keys:

    :timezone - evaluate the cron expression in a given TimeZone.
                Defaults to the local system timezone.
  
    :buffer - buffer size of the returned stream. Defaults to 0.

  Returns a manifold stream containing Dates. If the stream parks for
  an extended period of time, the next date will be computed based on
  when the stream resumes accepting puts. To prevent missing dates,
  use a non-zero buffer value.

  Note: the initial Date is calculated from the time the stream is
  created."

  [cron-expression & {:keys [timezone buffer]
                      :or {timezone (TimeZone/getDefault)
                           buffer 0}}]
  (let [cron-gen (CronSequenceGenerator. cron-expression timezone)]
    (let [stream (s/stream buffer)]
      (d/loop []
        (let [t0 (t/now)
              t1 (.next cron-gen (c/to-date t0))
              delay (t/in-millis (t/interval t0 (c/from-date t1)))]
          (d/chain' (d/timeout! (d/deferred) delay true)
                    (fn [_]
                      (s/put! stream t1))
                    (fn [result]
                      (if result
                        (d/recur)
                        (s/close! stream))))))
      stream)))
