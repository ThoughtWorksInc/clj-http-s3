(ns clj-http-s3.middleware
  (:require [clj-http-s3.s3 :as s3]
            [clj-time.format :as format-time]
            [clj-time.local :as local-time]))

(defn- request-date-time [] (format-time/unparse (format-time/formatters :rfc822) (local-time/local-now)))

(defn wrap-request-date
  "Middleware that adds a header with the date of the request in rfc822 format"
  [client]
  (fn [req]
    (client (assoc-in req [:headers "Date"] (request-date-time)))))

(defn wrap-aws-s3-auth
  "Middleware converting the :aws-credentials option into an AWS Authorization header."
  [client]
  (fn [req]
    (if-let [aws-credentials (:aws-credentials req)]
      (client (-> req (dissoc :aws-credentials)
                  (assoc-in [:headers "authorization"]
                            (s3/authorization-header-token
                             "GET"
                             (:access-key aws-credentials)
                             (:secret-key aws-credentials)
                             (req :uri)
                             (req :headers)))))
      (client req))))
