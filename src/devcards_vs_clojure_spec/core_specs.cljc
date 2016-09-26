(ns devcards-vs-clojure-spec.core-specs
  (:require
    [clojure.spec :as s]
    [clojure.test.check.generators :as generators]
    [devcards-vs-clojure-spec.core]))

(s/def ::sampler (s/fspec :args (s/cat :opts ::sampler-opts)))
(s/def ::sampler-opts (s/keys :req-un [::seed ::size] :opt-un [::overrides]))
(s/def ::seed number?)
(s/def ::size pos-int?)
(s/def ::overrides (s/map-of ::spec-key ::gen))
(s/def ::spec-key (s/or :s symbol? :k keyword?))
(s/def ::gen generators/generator?)
(s/def ::fspec (s/and s/spec? #(:args %)))
(s/def ::change-seed ifn?)
(s/def ::change-size ifn?)

(s/fdef devcards-vs-clojure-spec.core/sample-gen
  :args (s/cat :gen ::gen
               :seed ::seed
               :size ::size))

(s/fdef devcards-vs-clojure-spec.core/sample-fn*
  :args (s/cat :f ifn?
               :fspec ::fspec
               :opts ::sampler-opts))

(s/fdef devcards-vs-clojure-spec.core/sampler
  :args (s/cat :f-var var?)
  :ret ::sampler)

(s/fdef devcards-vs-clojure-spec.core/add-override
  :args (s/cat :k ::spec-key :v ::gen))

(s/fdef devcards-vs-clojure-spec.core/controls
  :args (s/cat :sampler ::sampler))

(s/fdef devcards-vs-clojure-spec.core/controls-view
  :args (s/cat :props (s/keys :req-un [::sampler
                                       ::seed
                                       ::size
                                       ::overrides
                                       ::change-seed
                                       ::change-size])))