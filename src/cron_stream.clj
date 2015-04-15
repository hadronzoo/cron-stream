(ns com.joshuagriffith.cron-stream
  (:require [manifold.deferred :as d]
            [manifold.stream :as s]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:import [java.util Date TimeZone]
           [org.springframework.scheduling.support CronSequenceGenerator]))

(defn cron-stream
  "Takes a 6-field cron expression and returns a stream that emits
  DateTimes according to the supplied schedule. Optionally takes the
  following option keys:

    :timezone -  evaluate the cron expression in a given TimeZone.
                 Defaults to the local system timezone.
  
    :max-error - the largest allowable error (in milliseconds).
                 Defaults to 50 ms."

  [cron-expression & {:keys [timezone max-error]
                        :or {timezone (TimeZone/getDefault)
                             max-error 50}}]
  (let [cron-gen (CronSequenceGenerator. cron-expression timezone)]
    (let [stream (s/stream)]
      (d/loop []
        (let [t0 (t/now)
              t1 (c/from-date (.next cron-gen (c/to-date t0)))
              delay (t/in-millis (t/interval t0 t1))]
          (d/chain (d/timeout! (d/deferred) delay true)
                   (fn [_]
                     (s/put! stream t1))
                   (fn [result]
                     (if result
                       (d/recur)
                       (s/close! stream))))))

      ;; filter stream for stale values
      (s/filter
       (fn [time]
         (let [error (t/in-millis (t/interval time (t/now)))]
           (d/success-deferred (<= error max-error))))
       stream))))
