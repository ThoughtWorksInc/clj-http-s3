(ns clj-http-s3.test.middleware
  (:require [clojure.test :refer :all]
            [clj-http-s3.middleware :as middleware]
            [clj-http.client :as client]))

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
