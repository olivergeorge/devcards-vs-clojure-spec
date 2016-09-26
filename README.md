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

http://clojure.org/about/spec
https://juxt.pro/blog/posts/generative-ui-clojure-spec.html
https://github.com/bhauman/devcards/issues/112


## Usage

```cljs

  (defn my-table
    ([rows]
     (my-table (distinct (mapcat keys rows)) rows))
    ([ks rows]
     (sab/html
       [:table.table
        [:thead [:tr (for [k ks] [:th (name k)])]]
        [:tbody (for [row rows]
                  [:tr (for [k ks] [:td (get row k "-")])])]])))

  (s/def ::col-key keyword?)
  (s/fdef my-table :args (s/cat :ks (s/? (s/coll-of ::col-key))
                                :rows (s/coll-of (s/map-of ::col-key string?))))

  ; Provide a generator to control what keys are picked
  (demo/add-override ::col-key #(s/gen #{:name :age :blah :foo}))

  ; Create a devcard which uses the generated data (no controls).
  (demo/example-card my-table)

  ; Create a devcard which uses the generated data.  Controls allow seed & size adjustment.
  (defcard my-table-controls
    (demo/demo-controls (demo/sample-fn #'my-table)))
```
