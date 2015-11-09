(defproject clj-http-s3 "0.3.0-SNAPSHOT"
  :description "Middleware to allow cli-http to authenticate with s3"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "2.0.0"]
                 [clj-time "0.6.0"]
                 [com.amazonaws/aws-java-sdk-core "1.10.31" :exclusions [joda-time commons-logging]]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
