(ns clj-uuid-utils-test
  (:use clojure.test
        clj-uuid-utils))

(println "clj-version:" (clojure-version))

(def uuid-str-1 "81e469ae-0d3b-46c3-a508-5782cdfd9f96")
(def uuid-uri-str-1 "urn:uuid:81e469ae-0d3b-46c3-a508-5782cdfd9f96")
(def uuid-edn-str-1 "#uuid \"81e469ae-0d3b-46c3-a508-5782cdfd9f96\"")
(def uuid-1 (java.util.UUID/fromString "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
(def uri-1 (java.net.URI. uuid-uri-str-1))

(def uuid-str-2 "19f17b1b-2a7b-446b-8377-2440d79e999a")
(def uuid-edn-str-2 "#uuid \"19f17b1b-2a7b-446b-8377-2440d79e999a\"")
(def uuid-uri-str-2 "urn:uuid:19f17b1b-2a7b-446b-8377-2440d79e999a")
(def uuid-2 (java.util.UUID/fromString "19f17b1b-2a7b-446b-8377-2440d79e999a"))
(def uri-2 (java.net.URI. uuid-uri-str-2))


(deftest uuid-test
  (testing "UUID test cases"
    (is (= (type (make-uuid uuid-str-1))
           java.util.UUID))
    (is (= (uuid-str (make-uuid uuid-str-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
    (is (= (type (make-uri (make-uuid uuid-str-1)))
           java.net.URI))
    (is (= (uri-str (make-uri (make-uuid uuid-str-1)))
           "urn:uuid:81e469ae-0d3b-46c3-a508-5782cdfd9f96"))

    (is (= (type (make-random-uuid))
           java.util.UUID))
    (is (= (type (make-uri (make-random-uuid)))
           java.net.URI))
    (is (let [u (make-random-uuid)]
          (= (make-uuid (uri-str (make-uri u)))
             u)))

    (is (= (boolean (uuid? "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
           true))
    (is (= (boolean (uuid? "urn:uuid:81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
           true))
    (is (= (boolean (uuid? "uuid:81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
           true))
    (is (= (boolean (uuid? uuid-1))
           true))
    (is (= (boolean (uuid? uri-1))
           true))
    (is (= (boolean (uuid? uuid-edn-str-1))
           true))
    (is (= (boolean (uuid? "81e469ae-0d3b-46c3-a508-"))
           false))

    (is (= (uuid-str (make-uuid uuid-str-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
    (is (= (uuid-str (make-uuid uuid-uri-str-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
    (is (= (uuid-str (make-uuid uuid-edn-str-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
    (is (= (uuid-str (make-uuid uuid-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
    (is (= (uuid-str (make-uuid uri-1))
           "81e469ae-0d3b-46c3-a508-5782cdfd9f96"))
      ))
