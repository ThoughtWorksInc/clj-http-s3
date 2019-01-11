clj-http-s3
===========

Middleware to allow cli-http to authenticate with s3

## Usage

The main HTTP client functionality is provided by the
`clj-http.client` namespace and the s3 auth middleware by the `clj-http-s3.middleware` namespace.

Require both in the REPL:

```clojure
(require '[clj-http.client :as client]
         '[clj-http-s3.middleware :as s3])
```

The s3 middleware provides a wrapper `wrap-aws-s3-auth`.

When making requests to s3, instruct the clj-http client to use the wrapper like so:

```clojure
(clj-http.client/with-middleware
  (conj clj-http.client/default-middleware #'s3/wrap-aws-s3-auth #'s3/wrap-request-date)
  (clj-http.client/get "https://s3.amazonaws.com/sample-bucket/sample-resource"
                        :aws-credentials {:access-key "AWS_ACCESS_KEY"
                                          :secret-key "AWS_SECRET_KEY"}))
```

AWS requires a date header to be sent.  The date header is used to sign the auth header as well.  You can either provide the date header yourself or use the wrap-request-date middleware (as shown in the above example).

When the AWS Temporary Security Credentials is setup using IAM roles, use `wrap-aws-credentials` as the wrapper like:

```clojure
(clj-http.client/with-middleware
  (conj clj-http.client/default-middleware #'s3/wrap-aws-credentials #'s3/wrap-request-date)
  (clj-http.client/get "https://s3.amazonaws.com/sample-bucket/sample-resource"))
```