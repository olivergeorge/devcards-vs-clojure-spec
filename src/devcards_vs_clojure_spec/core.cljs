(ns devcards-vs-clojure-spec.core
  (:require
    [cljs.spec :as s]
    [clojure.test.check.generators :as generators]
    [clojure.test.check.random :as random]
    [clojure.test.check.rose-tree :as rose-tree]
    [devcards.util.edn-renderer :as edn-renderer]
    [sablono.core :as sab :include-macros true]))

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

(defn sampler
  "Return a function which uses sample-fn* to call f-var with test data.
   Use seed, size and overrides to control data generation."
  [f-var]
  (fn [opts]
    (let [f (deref f-var)
          {f-ns :ns f-name :name} (meta f-var)
          f-sym (symbol (str f-ns) (str f-name))
          fspec (s/get-spec f-sym)]
      (assert fspec (str "No spec defined for " f-sym))
      (sample-fn* f fspec opts))))

(defonce overrides-ref (atom {}))

(defn add-override [k v]
  (swap! overrides-ref assoc k v))

(defn controls-view
  "Pure view control wrapper"
  [{:keys [args ret seed size change-seed change-size]}]
  (sab/html
    [:div
     [:div.form-inline
      [:div.form-group
       [:label "Seed " seed]
       [:button.btn.btn-sm {:on-click #(change-seed (inc seed))} "+"]
       [:button.btn.btn-sm {:on-click #(change-seed (dec seed))} "-"]]
      [:div.form-group
       [:label "Size " size]
       [:input.form-control
        {:type      "range"
         :min       0
         :max       100
         :value     size
         :on-change #(change-size (.-target.value %))}]]]
     [:div
      [:div ret]
      [:hr]
      [:div (edn-renderer/html-edn args)]]]))

(defn controls
  [sampler]
  (fn [app-db]
    (let [{:keys [seed size overrides]} @app-db
          size (or size 10)
          seed (or seed 1)
          overrides (merge @overrides-ref overrides)
          [args ret] (sampler {:seed seed :size size :overrides overrides})]
      (controls-view
        {:args        args
         :ret         ret
         :sampler     sampler
         :seed        seed
         :size        size
         :change-seed #(swap! app-db assoc :seed %)
         :change-size #(swap! app-db assoc :size %)}))))
