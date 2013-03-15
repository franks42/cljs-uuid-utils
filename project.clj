(defproject org.clojars.franks42/cljs-uuid-utils "1.0.0-SNAPSHOT"
  :description "ClojureScript micro-library with an implementation of a type 4, random UUID generator compatible with RFC-4122 and cljs.core/UUID (make-random-uuid), a uuid-string conformance validating predicate (valid-uuid?), and a UUID factory from uuid-string with conformance validation (make-uuid-from)."
  :url "https://github.com/franks42/cljs-random-uuid"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "See the notice in README.md or details in COPYING"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :dev-dependencies [[clj-ns-browser "1.3.2-SNAPSHOT"]
                     [codox "0.6.4"]
                     ]
  :plugins [[codox "0.6.4"]
            ]
  :profiles {:1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :master {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :dev {:dependencies [[clj-ns-browser "1.3.2-SNAPSHOT"]]}}
  :aliases  {"all" ["with-profile" "dev,master"]}
;;   :main clj-uuid-utils
  )
