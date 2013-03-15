;; Copyright (c) Frank Siebenlist. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file COPYING at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clj-uuid-utils
  "Clojure micro-library that defines protocols and implementations of interfaces that create (make-uuid, make-uri), test (uuid?), transform UUID or representations of UUIDs (uuid-str, uri-str), like URIs or strings.
  It also includes a wrapper-fn factory of a type 4, random UUID generator compatible with RFC-4122 and java.util.UUID (make-random-uuid)."
  (:require [clojure.edn])
  (:import [java.util.UUID]
           [java.net.URI]))


(def clj-uuid-utils-version "1.0.0-SNAPSHOT")


;; => (java.util.UUID/randomUUID)
;; #uuid "d6f07f94-f793-431b-b37c-96ebf3e75e3a"
;; uri: "urn:uuid:9a652678-4616-475d-af12-aca21cfbe06d"
;; edn: "#uuid \"961b2f59-53af-4bae-adab-cbcf2c1e9467\""
;; user=> (clojure.edn/read-string "#uuid \"961b2f59-53af-4bae-adab-cbcf2c1e9467\"")
;; #uuid "961b2f59-53af-4bae-adab-cbcf2c1e9467"
;; user=> (uuid? (java.net.URI. "urn:uuid:961b2f59-53af-4bae-adab-cbcf2c1e9467"))
;; #uuid "961b2f59-53af-4bae-adab-cbcf2c1e9467"


(defprotocol PUri
  "Protocol defines interfaces to create URIs (java.net.URI )and to return a string representation."
  (uri-str [this]
  "Returns the string representation of the URI of this.
  Like: \"urn:uuid:9a652678-4616-475d-af12-aca21cfbe06d\" or \"email:john@acme.com\"")
  (make-uri [this]
  "Factory-fn that returns a URI instance (java.net.URI) for this.
  this --- a URI instance of some representation that can be transformed to a URI")
  )


(defprotocol PUuid
  "Protocol with interfaces to create, test and obtain different representations of UUIDs."
  (uuid-str [this]
  "(uuid-str this)  =>  uuid-str
  Arguments and Values:
  this --- a java.util.UUID instance or something that \"resembles\" a UUID, like a URI or a string representation of a URI.
  uuid-str --- returns a string representation of the UUID instance
  Description:
  Returns the string representation of the UUID instance in the format of,
  \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\" (similarly to java.util.UUID/toString).
  Returns nil when this does not represent a UUID.
  Note that this is different from java.util.UUID's EDN string-format.
  Examples:
  (def u (make-random-uuid))  =>  #uuid \"305e764d-b451-47ae-a90d-5db782ac1f2e\"
  (uuid-string u) => \"305e764d-b451-47ae-a90d-5db782ac1f2e\""
  )
  (uuid? [this]
  "Predicate that returns whether this is of type UUID or a representation of a UUID, like a URI or a string representation.")
  (uuid-uri? [this])
  (uuid-edn? [this])
  (make-uuid [this]
  "Factory-fn that creates a UUID (java.util.UUID instance) from this.
  this --- a UUID instance or a representation of a UUID.
  Returns a UUID-instance (java.util.UUID).
  Throws exception when this cannot be transformed in a UUID.")
  )


(def ^:private uuid-regex 
  (let [x "[0-9a-fA-F]"] (re-pattern (str 
    "^" x x x x x x x x "-" x x x x "-" x x x x "-" x x x x "-" x x x x x x x x x x x x "$"))))


(def ^:private uuid-uri-regex
  (let [x "[0-9a-fA-F]"] (re-pattern (str 
    "^(urn:)?uuid:(" x x x x x x x x "-" x x x x "-" x x x x "-" x x x x "-" x x x x x x x x x x x x ")$"))))


(def ^:private uuid-edn-regex
  (re-pattern "^#uuid "))


(extend-type java.util.UUID
  PUri
  (uri-str [this] (str "urn:uuid:" this))
  (make-uri [this] (java.net.URI. (uri-str this)))
  PUuid
  (uuid? [this] true)
  (uuid-str [this] (str this))
  (make-uuid [this] 
    this)
  )


(extend-type java.net.URI
  PUri
  (uri-str [this] (.toString this))
  PUuid
  (uuid? [this] (uuid? (uri-str this)))
  (uuid-str [this] (uuid-str (make-uuid this)))
  (make-uuid [this] 
    (make-uuid (uri-str this)))
  )


(extend-type java.lang.String
  PUuid
  (uuid? [this]
    (try (make-uuid this) (catch Exception e )))
;;     (or (uuid-uri? this) 
;;         (re-find uuid-regex this)))
  (uuid-uri? [this] 
    (re-find uuid-uri-regex this))
  (uuid-edn? [this] 
    (re-find uuid-edn-regex this))
  (make-uuid [this] 
    (let [u (if-let [v (re-find uuid-uri-regex this)]
              (get v 2)
              this)]
      (if (uuid-edn? u)
        (clojure.edn/read-string u)
        (java.util.UUID/fromString u))))
  )


(defn make-random-uuid
  "(make-random-uuid)  =>  new-uuid
  Arguments and Values:
  new-uuid --- new type 4 (pseudo randomly generated) java.util.UUID instance.
  Description:
  Returns pseudo randomly generated UUID,
  like: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx as per http://www.ietf.org/rfc/rfc4122.txt.
  Examples:
  (make-random-uuid)  =>  #uuid \"305e764d-b451-47ae-a90d-5db782ac1f2e\"
  (type (make-random-uuid)) => java.util.UUID"
  []
  (java.util.UUID/randomUUID))


(defn uuid-string [this] (uuid-str this))

(defn valid-uuid?
  "(valid-uuid? maybe-uuid)  =>  truthy-falsy
  Arguments and Values:
  maybe-uuid --- string or UUID-instance that may represent a conformant UUID.
  truthy-falsy --- Returns either the conforming UUID-string (truthy) or nil (falsy).
  Description:
  Predicate to test whether a string representation conforms to a
  \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\" format where each x is a hexadecimal character.
  Input can be a maybe-uuid string or a java.util.UUID instance.
  Examples:
  (valid-uuid? \"NO-WAY\")  =>  nil
  (valid-uuid? \"4d7332e7-e4c6-4ca5-af91-86336c825e25\")  => \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (valid-uuid? (java.util.UUID/fromString \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"))  => \"4d7332e7-e4c6-4ca5-af91-86336c825e25\""
  [maybe-uuid]
  (let [maybe-uuid-str (cond 
                         (= (type maybe-uuid) java.util.UUID) (uuid-string maybe-uuid)
                         (string? maybe-uuid) maybe-uuid
                         :true false)]
    (when maybe-uuid-str (re-find uuid-regex maybe-uuid-str))))


;; java equivalent "java.util.UUID/fromString" throws: IllegalArgumentException Invalid UUID string: ffa2a001-9eec-4224-a64d  java.util.UUID.fromString
;; make-uuid-from should probably throw an exception also instead of silently returning nil...

(defn make-uuid-from
  "(make-uuid-from maybe-uuid)  =>  uuid-or-nil
  Arguments and Values:
  maybe-uuid --- string or UUID-instance that may represent a conformant UUID.
  uuid-or-nil --- Returns either a java.util.UUID instance or nil.
  Description:
  Returns a java.util.UUID instance for a conformant UUID-string representation, or nil.
  Input can be a string or a java.util.UUID instance.
  Note that if the input UUID-instance is not valid, nil is returned.
  Examples:
  (make-uuid-from \"NO-WAY\")  =>  nil
  (make-uuid-from \"4d7332e7-e4c6-4ca5-af91-86336c825e25\")  => #uuid \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (make-uuid-from (UUID. \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"))  => #uuid \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (make-uuid-from (UUID. \"YES-WAY\"))  => nil"
  [maybe-uuid]
  (when-let [uuid (valid-uuid? maybe-uuid)]
    (if (= (type maybe-uuid) java.util.UUID)
      maybe-uuid
      (java.util.UUID/fromString uuid))))
