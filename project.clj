(defproject clj-http-s3 "0.1.0-SNAPSHOT"
  :description "Middleware to allow cli-http to authenticate with s3"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.9.1"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
