(ns clj-http-s3.test.s3
  (:require [clojure.test :refer :all]
            [clj-http-s3.s3 :refer :all]))


(deftest s3-test
  (let [example-headers {"x-amz-magic" "abracadabra" "x-amz-meta-author" "foo@bar.com"
                         "Content-Md5" "c8fdb181845a4ca6b8fec737b3581d76"
                         "Content-Type" "text/html"
                         "Date" "Thu, 17 Nov 2005 18:49:58 GMT"}]

    (testing "creates authentication token in AWS format"
      (is (= (signature "OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV"
                        "PUT"
                        "/quotes/nelson"
                        example-headers)

             "jZNOcbfWmD/A/f3hSvVzXZjM2HU=")))

    (testing "creates the canonicalized string"
      (is (= (canonicalized-string "PUT"
                                   example-headers
                                   "/quotes/nelson")

             "PUT\nc8fdb181845a4ca6b8fec737b3581d76\ntext/html\nThu, 17 Nov 2005 18:49:58 GMT\nx-amz-magic:abracadabra\nx-amz-meta-author:foo@bar.com\n/quotes/nelson"))))

  (testing "creates signatur with simpler headers"
      (is (= (signature "AWS_SECRET_KEY"
                        "GET"
                        "/tw-analytics/forecast/2013/8"
                        {"x-amz-date" "Fri, 13 Sep 2013 08:31:37 GMT"})

             "XPssHVN1VrJchftQTKJ7GLsAPyE="))))
