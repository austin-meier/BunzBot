(ns irc.client
  (:require [clojure.java.io :refer [reader writer]])
  (:import (java.net Socket)))


(defn create [host port]
  (Socket. host port))

(def get-socket-writer
  (memoize (fn [socket]
             (writer socket))))

(def get-socket-reader
  (memoize (fn [socket]
             (reader (.getInputStream socket)))))

(defn write [socket msg]
  (let [w (get-socket-writer socket)]
    (.write w msg)
    (.flush w))
  socket)

(defn readl [socket]
  (->> 
    socket
    get-socket-reader
    line-seq))

(defn httptest [_]
   (-> (create "yahoo.com" 80)
    (write "GET / HTTP/1.1\nHost: bunztop\n\n")
    readl
    prn))

