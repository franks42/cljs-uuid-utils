# cljs-uuid-utils

ClojureScript micro-library with an implementation of a type 4, random UUID generator compatible with RFC-4122 and cljs.core/UUID.

## Installation

Add the following to your `project.clj` dependencies:

```clojure
[org.clojars.franks42/cljs-uuid-utils "0.1.0""]
```

## Usage


```clojure
(ns myapp
  (:use [cljs-uuid-utils :only [make-random-uuid valid-uuid? make-uuid-from]]))

(def a-uuid (make-random-uuid))
;; =>  #uuid "305e764d-b451-47ae-a90d-5db782ac1f2e"
(type a-uuid)
;; =>  cljs.core/UUID"
(valid-uuid? a-uuid) 
;; => "305e764d-b451-47ae-a90d-5db782ac1f2e" (truthy)
```


## License

Copyright (C) 2012 Frank Siebenlist

Distributed under the Eclipse Public License, the same as Clojure.
