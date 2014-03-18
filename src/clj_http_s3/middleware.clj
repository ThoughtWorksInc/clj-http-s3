(ns clj-http-s3.middleware
  (:require [clj-http-s3.s3 :as s3]))

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
                             (.getPath (java.net.URL. (req :url)))
                             (req :headers)))))
      (client req))))
