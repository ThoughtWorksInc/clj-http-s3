(ns clj-http-s3.s3
  (:require [clj-http-s3.crypto :as crypto]
            [clojure.string :as str]))

(defn select-matching-keys [xs regex]
  (filter #(re-matches regex (key %)) xs))

(defn canonicalized-string [method headers resource]
  (let [standard-headers (map headers ["Content-Md5" "Content-Type" "Date"])
        canonicalalized-amz-headers
        (map #(str/join ":" %) (select-matching-keys headers #"^x-amz-.+"))]

    (str/join "\n" (flatten [method standard-headers canonicalalized-amz-headers resource]))))

(defn signature [aws_secret_key verb resource headers]
  (let [sign (canonicalized-string verb headers resource)]
    (crypto/hmac-sign aws_secret_key sign)))

(defn authorization-header-token [verb aws_access_key aws_secret_key resource headers]
  (str "AWS " aws_access_key ":" (signature aws_secret_key verb resource headers)))
