# devcards-vs-clojure-spec

Tools to automatically generate useful devcards from view specs.  

* If you spec your views then we can generate data.  Yay.
* The generated data isn't interesting.  Boo!
* This library provides "interesting generators", think lorem-ipsum.  Yay!
* Devcards would only show one example, Boo!
* This library includes a wrapper to make the devcard generators interactive.  Yay!
* Specifying a devcard for every function is haaard!  Boo!
* This library includes a helper to generate devcards for all spec fns in a namespace.  Yay!
* [insert next pain point]... [boo]
* [contribute new feature]... [yay]


## Status

Hot air + prototype.


## Prior art / related links

* Goals of clojure.spec work: http://clojure.org/about/spec
* Experience report of generative ui testing from JUXT:  https://juxt.pro/blog/posts/generative-ui-clojure-spec.html
* Related discussion with Bruce: https://github.com/bhauman/devcards/issues/112
* Prototype code: https://gist.github.com/olivergeorge/82a20dd03fd86e82ab9b0f3959590f3f


## Usage

```cljs
(ns example.my-table
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
```
