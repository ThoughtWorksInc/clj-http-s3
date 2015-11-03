(ns clj-http-s3.middleware
  (:require [clj-http-s3.s3 :as s3]
            [clj-time.format :as format-time]
            [clj-time.local :as local-time]
            [clojure.set :as set])
  (:import [com.amazonaws.auth
            DefaultAWSCredentialsProviderChain]))

(defn- request-date-time [] (format-time/unparse (format-time/formatters :rfc822) (local-time/local-now)))

(defn wrap-request-date
  "Middleware that adds a header with the date of the request in rfc822 format"
  [client]
  (fn [req]
    (client (assoc-in req [:headers "Date"] (request-date-time)))))

(defn- assoc-security-token [req]
  (if-let [session-token (:session-token (:aws-credentials req))]
    (assoc-in req
              [:headers "x-amz-security-token"]
              session-token)
    req))

(defn- assoc-authorization-header [req]
  (assoc-in req
            [:headers "authorization"]
            (s3/authorization-header-token
             "GET"
             (:access-key (:aws-credentials req))
             (:secret-key (:aws-credentials req))
             (req :uri)
             (req :headers))))

(defn wrap-aws-s3-auth
  "Middleware converting the :aws-credentials option into an AWS Authorization header."
  [client]
  (fn [req]
    (if-let [aws-credentials (:aws-credentials req)]
      (client (-> req
                  assoc-security-token
                  assoc-authorization-header
                  (dissoc :aws-credentials)))
      (client req))))

(defn- provider->credentials [provider]
  (-> (.getCredentials provider)
      bean
      (set/rename-keys
       {:AWSAccessKeyId :access-key
        :AWSSecretKey :secret-key
        :sessionToken :session-token})
      (dissoc :class)))

(def provider (DefaultAWSCredentialsProviderChain.))

(defn wrap-aws-credentials
  "Middleware which provides :aws-credentials from the ProviderChain."
  [client]
  (fn [req]
    (-> req
        (assoc :aws-credentials (provider->credentials provider))
        client)))
