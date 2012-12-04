# cljs-uuid-utils

ClojureScript micro-library with an implementation of a type 4, random UUID generator compatible with RFC-4122 and cljs.core/UUID (make-random-uuid), a getter function to obtain the uuid string representation from a UUID-instance (uuid-string), a uuid-string conformance validating predicate (valid-uuid?), and a UUID factory from uuid-string with conformance validation (make-uuid-from).

## Installation

Add the following to your `project.clj` dependencies:

```clojure
[org.clojars.franks42/cljs-uuid-utils "0.1.3"]
```

## Usage

Require cljs-uuid-utils library:

```clojure
(ns myapp
  (:require [cljs-uuid-utils]))
```

REPL examples:

```clojure

ClojureScript:cljs.user> (def r (cljs-uuid-utils/make-random-uuid))
#uuid "ec9b1b11-74b0-48a4-989c-7e939fd37dec"
ClojureScript:cljs.user> (type r)
cljs.core/UUID

ClojureScript:cljs.user> (uuid-string r)
"ec9b1b11-74b0-48a4-989c-7e939fd37dec"

ClojureScript:cljs.user> (cljs-uuid-utils/valid-uuid? "NO-WAY")
nil
ClojureScript:cljs.user> (cljs-uuid-utils/valid-uuid? "ec9b1b11-74b0-48a4-989c-7e939fd37dec")
"ec9b1b11-74b0-48a4-989c-7e939fd37dec"
ClojureScript:cljs.user> (cljs-uuid-utils/valid-uuid? r)
"ec9b1b11-74b0-48a4-989c-7e939fd37dec"

ClojureScript:cljs.user> (cljs-uuid-utils/make-uuid-from "ec9b1b11-74b0-48a4-989c-7e939fd37dec")
#uuid "ec9b1b11-74b0-48a4-989c-7e939fd37dec"
ClojureScript:cljs.user> (cljs-uuid-utils/make-uuid-from "NO-WAY")
nil
ClojureScript:cljs.user> (cljs-uuid-utils/make-uuid-from r)
#uuid "ec9b1b11-74b0-48a4-989c-7e939fd37dec"
ClojureScript:cljs.user> (cljs-uuid-utils/make-uuid-from (UUID. "NO-WAY"))
nil
ClojureScript:cljs.user> (cljs-uuid-utils/make-uuid-from (UUID. "ec9b1b11-74b0-48a4-989c-7e939fd37dec"))
#uuid "ec9b1b11-74b0-48a4-989c-7e939fd37dec"
```


## License

Copyright (C) 2012 Frank Siebenlist

Distributed under the Eclipse Public License, the same as Clojure.
