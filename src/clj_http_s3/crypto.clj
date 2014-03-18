(ns clj-http-s3.crypto
  (:import [javax.crypto Mac]
           [javax.crypto.spec SecretKeySpec]
           org.apache.commons.codec.binary.Base64))

(defn- secret-key [key algo]
  (SecretKeySpec. (.getBytes key) algo))

(defn- mac-sha1 [key]
  (let [mac (Mac/getInstance "HmacSHA1")]
    (.init mac (secret-key key "HmacSHA1")) mac))

(defn hmac-sha1 [key data]
  (.doFinal (mac-sha1 key) (.getBytes data)))

(defn base64-encode
  [unencoded]
  (String. (Base64/encodeBase64 unencoded)))

(def hmac-sign (comp base64-encode hmac-sha1))
