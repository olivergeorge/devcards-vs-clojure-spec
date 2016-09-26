(require '[figwheel-sidecar.repl-api :as ra])

;; this will start figwheel and will start autocompiling the builds specified in `:builds-ids`
(ra/start-figwheel!
  {:figwheel-options {}                                     ;; <-- figwheel server config goes here
   :build-ids        ["dev"]                                ;; <-- a vector of build ids to start autobuilding
   :all-builds                                              ;; <-- supply your build configs here
                     [{:id           "example"
                       :figwheel     {:devcards true}       ;; <- note this
                       :source-paths ["src"]
                       :compiler     {:main       "devcards-vs-clojure-spec.example"
                                      :asset-path "js/example"
                                      :output-to  "resources/public/js/example.js"
                                      :output-dir "resources/public/js/example"
                                      :source-map-timestamp true
                                      :verbose    true
                                      :preloads   '[devtools.preload]}}]})

;; you can also just call (ra/start-figwheel!)
;; and figwheel will do its best to get your config from the
;; project.clj or a figwheel.edn file

;; start a ClojureScript REPL
(ra/cljs-repl)
;; you can optionally supply a build id
;; (ra/cljs-repl "dev")
