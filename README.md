# cljs-uuid-utils

START IMPORTANT NOTE

cljs-uuid-utils has changed maintainers and lein coordinates. Please find the new official fork at 
[https://github.com/lbradstreet/cljs-uuid-utils](https://github.com/lbradstreet/cljs-uuid-utils).

END IMPORTANT NOTE


ClojureScript micro-library with an implementation of a type 4, random UUID generator 
compatible with RFC-4122 and cljs.core/UUID (make-random-uuid), 
a getter function to obtain the uuid string representation from a UUID-instance 
(uuid-string), a uuid-string conformance validating predicate (valid-uuid?), 
and a UUID factory from uuid-string with conformance validation (make-uuid-from). 
An implementation of a squuid (semi-sequential uuid) generator is also included (make-random-squuid).

## Installation

(SEE IMPORTANT NOTE ABOVE)

## License

Copyright (C) 2012 Frank Siebenlist and Lucas Bradstreet

Distributed under the Eclipse Public License, the same as Clojure.
