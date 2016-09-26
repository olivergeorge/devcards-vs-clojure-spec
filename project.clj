(defproject devcards-vs-clojure-spec "0.1"
  :description "Use interesting generated data on your devcards."
  :url "https://github.com/olivergeorge/devcards-vs-clojure-spec"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha12"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/test.check "0.9.0"]
                 [devcards "0.2.1-7"]
                 [lein-figwheel "0.5.4-7"]
                 [figwheel-sidecar "0.5.4-7"]
                 [binaryage/devtools "0.8.2"]]
  :clean-targets [:target-path "resources/public/js"])