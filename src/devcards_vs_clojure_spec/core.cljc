(ns devcards-vs-clojure-spec.core
  (:require
    [clojure.test.check.generators :as generators]
    [clojure.test.check.random :as random]
    [clojure.test.check.rose-tree :as rose-tree]))

(defn sample-gen
  "Sample single value from generator based on seed and size."
  [gen seed size]
  (let [rnd (random/make-random seed)]
    (rose-tree/root (generators/call-gen gen rnd size))))