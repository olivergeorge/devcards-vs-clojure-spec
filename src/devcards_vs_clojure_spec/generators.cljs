(ns devcards-vs-clojure-spec.generators
  (:require [cljs.spec :as s]
            [clojure.string :as string]
            [clojure.test.check.generators :as gen]))

(def lorem-ipsum
  (->
    "nulla at felis elementum porttitor sem quis condimentum felis vivamus augue
    magna sagittis nec vestibulum sed dictum eu eros donec facilisis est at mi
    bibendum finibus praesent cursus eros et risus dignissim tincidunt pellentesque
    quam vehicula vivamus eleifend magna ultrices tortor facilisis viverra nunc
    tincidunt tortor sit amet justo volutpat sit amet venenatis nisi mollis donec
    vel pellentesque libero maecenas tincidunt sem id nunc pretium posuere
    suspendisse accumsan justo tellus quis semper erat maximus vel etiam nec
    ipsum sem  aliquam nisl magna vulputate a commodo in pellentesque ac lorem
    sed sodales bibendum nisi fermentum laoreet praesent quis metus interdum mattis"
    (string/split #"\W+")
    set))

(defn build-lorem-ipsum-generator [min max]
  #(gen/fmap (fn [w] (string/capitalize (string/join " " w)))
             (s/gen (s/coll-of lorem-ipsum :min-count min :max-count max))))

(comment
  (s/exercise spec 10 {::description (build-lorem-ipsum-generator 20 160)}))
