(ns clojure-12-factor-app.core
  (:require
            [clojure-12-factor-app.helper :as helper]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.logging :as log])

  (import [java.util Properties])
  (:gen-class))

(defn start [active-profile env-map]
  (log/info "starting 12 factor clojure app " active-profile)
  (let [config-map  (helper/load-configuration "resources/config.edn")
        property-files (get-in config-map [:profile active-profile :resource-paths])
        resolved-property-files (reduce (fn [v file] (conj v (helper/resolve-env-parameter file env-map))) [] property-files)]
    (helper/load-properties resolved-property-files)))

