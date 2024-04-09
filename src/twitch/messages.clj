(ns twitch.messages
  (:require [clojure.string :as str]
            [twitch.client :as twitch]))

(defn test-msg [client _]
  (twitch/msg client "kingbunz" "test!"))

(defn pong-msg [{:keys [client tokens]}]
  (twitch/send! client (str "PONG " (second tokens)))
  (prn (str "PONG " (second tokens))))

(defn msg-username [{:keys [tokens]}]
  (apply str 
             (take-while #(not= \! %) 
                         (subs (first tokens) 1))))

(defn msg-content 
  "Extract the message content from an irc message if it is a user message"
  [{:keys [tokens]}]
  (subs (str/join " " (subvec tokens 3)) 1))

(defn parse-user-msg 
  "Take a irc message context (should be a message context if routed) and attempt to extract the username and message context"
  [context]
  (-> context
      (assoc :username (msg-username context))
      (assoc :content (msg-content context))))

(defn user-msg [context]
  (->> context
      parse-user-msg
      (println (str (:username context) (:context context)))))

(defn message-type [tokens]
  (cond
    (= (first tokens) "PING") :ping
    (= (second tokens) "PRIVMSG") :channel-msg))

(defn split-messages [irc-msg]
  (-> irc-msg
      (str/split #"\r\n")))

(defn message-parts [msg]
  (str/split msg #" "))

(defn parse-message [client msg]
  (let [tokens (message-parts msg)]
    {:client client
     :tokens tokens
     :type (message-type tokens)}))

;; We need to handle various messages from Twitch in various formats
;; For more information vist https://dev.twitch.tv/docs/irc/#supported-irc-messages
(defn route-msg [client msg]
  (let [context (parse-message client msg)]
    ; (prn context)
    (((case (:type context)
        :ping  pong-msg 
        :channel-msg user-msg
        #(prn %)) context))))

(defn handle-msg [client irc-msg]
  (->> irc-msg
      split-messages 
      (map #(route-msg client %))
      doall))

(comment 
  ;; TODO (austin): convert these to tests or something useful
  (split-messages "HelloWorld\r\nThisIsMessage2")
  
  (->> ":kingbunz!kingbunz@kingbunz.tmi.twitch.tv PRIVMSG #sodawavelive :!last\r\n"
       message-parts
       user-msg)
  
  
)

