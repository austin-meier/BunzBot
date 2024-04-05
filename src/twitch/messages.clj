(ns twitch.messages
  (:require [clojure.string :as str]
            [twitch.client :as twitch]))

(defn test-msg [client _]
  (twitch/msg client "kingbunz" "test!"))

(defn pong-msg [client msg]
  (twitch/send! client (str "PONG " (second msg)))
  (prn (str "PONG " (second msg))))


(defn user-msg [msg]
  (println 
    (str 
      (apply str 
             (take-while #(not= \! %) 
                         (subs (first msg) 1)))
      (str/join " " (subvec msg 3)))))

(defn message-type [parts]
  (cond
    (= (first parts) "PING") :ping
    (= (second parts) "PRIVMSG") :channel-msg))

(defn split-messages [irc-msg]
  (-> irc-msg
       (str/split #"\r\n")))

(defn message-parts [msg]
  (str/split msg #" "))

;; We need to handle various messages from Twitch in various formats
;; For more information vist https://dev.twitch.tv/docs/irc/#supported-irc-messages
(defn route-msg! [client msg]
  (let [parts (message-parts msg)
        msg-type (message-type parts)]
  (-> 
    ((case msg-type
       :ping  #(pong-msg client %)
       :channel-msg user-msg

      #(prn %)) 
     parts))))

(defn handle-msg [client irc-msg]
  (->> irc-msg
       split-messages 
       (map #(route-msg! client %))
       doall))

(comment 
  ;; TODO (austin): convert these to tests or something useful
  (split-messages "HelloWorld\r\nThisIsMessage2")
  
  (->> ":kingbunz!kingbunz@kingbunz.tmi.twitch.tv PRIVMSG #sodawavelive :!last\r\n"
       message-parts
       user-msg)
  
  
)

