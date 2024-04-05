(ns secrets
  (:require [clojure.edn :refer [read-string]]))

(def secrets (read-string (slurp "secrets.edn")))

