;; Copyright (c) Frank Siebenlist. All rights reserved.
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file COPYING at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns cljs-uuid-utils
  "Generator for a type4, random UUID that works with cljs.core/UUID.
  Interface mimics Java's UUID class' static method:
  randomUUID() - Static factory to retrieve a type 4 (pseudo randomly generated) UUID."
  (:require [goog.string.StringBuffer]))

;; see https://gist.github.com/4159427 for some background

(defn make-random-uuid
  "Returns a new type 4 (pseudo randomly generated) cljs.core/UUID,
  like: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
  as per http://www.ietf.org/rfc/rfc4122.txt.
  Usage:
  (make-random-uuid)  =>  #uuid \"305e764d-b451-47ae-a90d-5db782ac1f2e\"
  (type (make-random-uuid)) => cljs.core/UUID"
  []
  (letfn [(f [] (.toString (rand-int 16) 16))
          (g [] (.toString  (bit-or 0x8 (bit-and 0x3 (rand-int 15))) 16))]
    (UUID. (.append (goog.string.StringBuffer.)
       (f) (f) (f) (f) (f) (f) (f) (f) "-" (f) (f) (f) (f) 
       "-4" (f) (f) (f) "-" (g) (f) (f) (f) "-"
       (f) (f) (f) (f) (f) (f) (f) (f) (f) (f) (f) (f)))))


(def ^:private uuid-regex 
  (let [x "[0-9a-fA-F]"] (re-pattern (str 
    "^" x x x x x x x x "-" x x x x "-" x x x x "-" x x x x "-" x x x x x x x x x x x x "$"))))


(defn valid-uuid?
  "Predicate to test whether the UUID string representation conforms to a
  \"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\" format where each x is a hexadecimal character.
  Input can be a maybe-uuid string or a cljs.core/UUID instance.
  Note that the current \"cljs.core/UUID.\" constructor does not check for any conformance.
  Returns either the conforming UUID-string (truthy) or nil (falsy).
  Usage:
  (valid-uuid? \"NO-WAY\")  =>  nil
  (valid-uuid? \"4d7332e7-e4c6-4ca5-af91-86336c825e25\")  => \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (valid-uuid? (UUID. \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"))  => \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (valid-uuid? (UUID. \"YES-WAY\"))  => nil"
  [maybe-uuid]
  (let [maybe-uuid-str (cond 
                         (= (type maybe-uuid) cljs.core/UUID) (.-uuid maybe-uuid)
                         (string? maybe-uuid) maybe-uuid
                         :true false)]
    (when maybe-uuid-str (re-find uuid-regex maybe-uuid-str))))


;; define protocol for as-uuid that should extend string/symbol/keyword/UUID to return a UUID or nil if a UUID can be deduced from the input.
;; 

(defn make-uuid-from
  "Returns a cljs.core/UUID instance or nil for a given string representation.
  Input can be a string or a cljs.core/UUID instance,
  and valid uuid-string conformance is enforced.
  (make-uuid-from \"NO-WAY\")  =>  nil
  (make-uuid-from \"4d7332e7-e4c6-4ca5-af91-86336c825e25\")  => #uuid \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (make-uuid-from (UUID. \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"))  => #uuid \"4d7332e7-e4c6-4ca5-af91-86336c825e25\"
  (make-uuid-from (UUID. \"YES-WAY\"))  => nil"
  [maybe-uuid]
  (when-let [uuid (valid-uuid? maybe-uuid)]
    (if (= (type maybe-uuid) cljs.core/UUID)
      maybe-uuid
      (UUID. uuid))))
