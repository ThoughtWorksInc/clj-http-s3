(ns clj-http-s3.test.middleware
  (:require [clojure.test :refer :all]
            [clj-http-s3.middleware :as middleware]
            [clj-http.client :as client]
            [clj-time.core :as time]
            [clj-time.local :as local-time]))

(defn is-passed [middleware req]
  (let [client (middleware identity)]
    (is (= req (client req)))))

(defn is-applied [middleware req-in req-out]
  (let [client (middleware identity)]
    (is (= req-out (client req-in)))))

(deftest apply-on-aws-auth
  (with-redefs [clj-http-s3.s3/signature (fn [& args] (clojure.string/join "-" args))]
    (is-applied middleware/wrap-aws-s3-auth
                {:url "http://http.example/an-s3-object"
                 :headers {"Date" "today"}
                 :aws-credentials {:access-key "AWS_ACCESS_KEY"
                                   :secret-key "AWS_SECRET_KEY"}}
                {:url "http://http.example/an-s3-object"
                 :headers {"authorization"
                           (str "AWS " "AWS_ACCESS_KEY" ":" "AWS_SECRET_KEY-GET-/an-s3-object-{\"Date\" \"today\"}")
                           "Date" "today"}})))

(deftest pass-on-no-aws-auth
  (is-passed middleware/wrap-aws-s3-auth
             {:uri "/foo"}))

(deftest apply-on-date
  (with-redefs [local-time/local-now (fn [] (time/date-time 2014 3 18 15 10 9))]
    (is-applied middleware/wrap-request-date
                {}
                {:headers {"Date" "Tue, 18 Mar 2014 15:10:09 +0000"}})

    (is-applied middleware/wrap-request-date
                {:headers {"foo" "bar"}}
                {:headers {"foo" "bar"
                           "Date" "Tue, 18 Mar 2014 15:10:09 +0000"}})))
