(ns devcards-vs-clojure-spec.example
  (:require-macros
    [devcards.core :refer [defcard]])
  (:require
    [cljs.spec :as s]
    [devcards-vs-clojure-spec.core :as core]
    [devcards-vs-clojure-spec.generators :as gen]
    [sablono.core :as sab]))

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
(s/def ::cell-val string?)

(s/fdef my-table
  :args (s/cat :keys (s/? (s/coll-of ::col-key))
               :rows (s/coll-of (s/map-of ::col-key ::cell-val))))

(core/add-override ::col-key #(s/gen #{:A :B :C}))
(core/add-override ::cell-val (gen/build-lorem-ipsum-generator 0 16))

(defcard my-table-with-controls
  (core/controls (core/sampler #'my-table)))