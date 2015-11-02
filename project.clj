(defproject com.joshuagriffith/cron-stream "1.0.0"
  :description "Manifold-based cron streams"
  :url "https://github.com/hadronzoo/cron-stream"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.addthis/cronus "0.1.0"]
                 [manifold "0.1.1"]
                 [org.clojure/clojure "1.7.0"]]
  :deploy-repositories
  [["snapshots"
    {:url "https://artifacts.joshuagriffith.com/content/repositories/snapshots"
     :creds :gpg}]

   ["releases"
    {:url "https://artifacts.joshuagriffith.com/content/repositories/releases"
     :creds :gpg}]])
