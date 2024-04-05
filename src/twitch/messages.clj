(ns twitch.messages
  (:require [clojure.string :as str]
            [twitch.client :as twitch]))

(defn pong-msg [client msg]
  (twitch/send-msg ))

(def routes
  {:ping pong-msg})

(defn message-type [parts]
  (cond
    (= (first parts) "PING") :ping
    (= (second parts) "PRIVMSG") :channel-msg))

(defn split-messages [irc-msg]
  (-> irc-msg
       (str/split #"\r\n")))

(defn parse-msg [irc-msg]
  (let [parts (str/split irc-msg #" ")
        type (message-type parts)]
    type))

;; We need to handle various messages from Twitch in various formats
;; For more information vist https://dev.twitch.tv/docs/irc/#supported-irc-messages
(defn route-msg! [client irc-msg]
  (let [msg (parse-msg irc-msg)]
  (cond
    :else (prn irc-msg)
    )))

(defn handle-msg [client irc-msg]
  (->> irc-msg
       split-messages
       (map #(route-msg! client %))))

(comment 
  ;; TODO (austin): convert these to tests or something useful
  (split-messages "HelloWorld\r\nThisIsMessage2")
  
  (parse-msg ":kingbunz!kingbunz@kingbunz.tmi.twitch.tv PRIVMSG #sodawavelive :!last\r\n")
  
  
  )
