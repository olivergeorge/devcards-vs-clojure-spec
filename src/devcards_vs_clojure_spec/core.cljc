(ns devcards-vs-clojure-spec.core
  (:require
    [clojure.spec :as s]
    [clojure.test.check.generators :as generators]
    [clojure.test.check.random :as random]
    [clojure.test.check.rose-tree :as rose-tree]))

(defn sample-gen
  "Sample single value from generator based on seed and size."
  [gen seed size]
  (let [rnd (random/make-random seed)]
    (rose-tree/root (generators/call-gen gen rnd size))))

(defn sample-fn*
  "Call a function with generated args.
   Use seed, size and overrides to control data generation."
  [f fspec {:keys [seed size overrides]}]
  (let [args (sample-gen (s/gen (:args fspec) overrides) seed size)]
    [args (apply f args)]))