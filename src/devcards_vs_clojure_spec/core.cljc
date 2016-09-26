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

(defn build-sampler-fn
  "Return a function which uses sample-fn* to call f-var with test data.
   Use seed, size and overrides to control data generation."
  [f-var]
  (fn [opts]
    (let [f (deref f-var)
          {f-ns :ns f-name :name} (meta f-var)
          f-sym (symbol (str f-ns) (str f-name))
          fspec (clojure.spec/get-spec f-sym)]
      (assert fspec (str "No spec defined for " f-sym))
      (sample-fn* f fspec opts))))

(comment

  (require '[clojure.spec.test :as stest])
  (require '[devcards-vs-clojure-spec.core-specs])
  (stest/instrument)

  (defn my-table
    ([rows]
     (my-table (distinct (mapcat keys rows)) rows))
    ([ks rows]
     [:table.table
      [:thead [:tr (for [k ks]
                     [:th (name k)])]]
      [:tbody (for [row rows]
                [:tr (for [k ks]
                       [:td (get row k "-")])])]]))

  (s/def ::col-key keyword?)
  (s/fdef my-table
          :args (s/cat :keys (s/coll-of ::col-key)
                       :rows (s/coll-of (s/map-of ::col-key string?))))

  (build-sampler-fn #'my-table)

  (let [sampler (build-sampler-fn #'my-table)]
    (sampler {:seed 1 :size 1}))

  (let [sampler (build-sampler-fn #'my-table)]
    (= (sampler {:seed 1 :size 1})
       (sampler {:seed 1 :size 1})))

  (let [sampler (build-sampler-fn #'my-table)]
    (= (sampler {:seed 1 :size 1})
       (sampler {:seed 2 :size 1}))))
