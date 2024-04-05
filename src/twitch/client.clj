(ns twitch.client
  (:require [gniazdo.core :as ws]
            [oauth.token :as auth]))

;; We need to handle various messages from Twitch in various formats
;; For more information vist https://dev.twitch.tv/docs/irc/#supported-irc-messages
(defn handle-msg [msg]
  (prn msg))

(defn create-client []
  (ws/connect
    "wss://irc-ws.chat.twitch.tv:443"
    :on-receive handle-msg))

(def endl "\r\n")

(def)

(ws/send-msg client (str "PASS oauth:" (auth/get-token) endl))
(ws/send-msg client (str "NICK bunzbot" endl))
(ws/send-msg client (str "JOIN #kingbunz" endl))
(ws/send-msg client (str "PRIVMSG #kingbunz :Hello World" endl))


