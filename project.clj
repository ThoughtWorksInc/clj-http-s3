(defproject clj-http-s3 "0.3.0"
  :description "Middleware to allow cli-http to authenticate with s3"
  :url "https://github.com/ThoughtWorksInc/clj-http-s3"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.1.0"]
                 [clj-time "0.12.0"]
                 [com.amazonaws/aws-java-sdk-core "1.11.9" :exclusions [joda-time commons-logging]]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
