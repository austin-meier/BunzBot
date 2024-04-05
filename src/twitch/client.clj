(ns twitch.client
  (:require [hato.client :as ws]
            [oauth.token :as auth]
            [twitch.messages :as msg]))

(def client
  (ws/connect
    "wss://irc-ws.chat.twitch.tv:443"
    :on-receive #(partial msg/handle-msg client)))

(def endl "\r\n")

(defn msg [client channel msg]
  (ws/send-msg client (str "PRIVMSG #" channel ":" msg endl))
  client)

(defn auth [client botname token]
  (ws/send-msg client (str "PASS oauth:" token endl))
  (ws/send-msg client (str "NICK " botname endl)))

(defn join-channel [client channel]
  (ws/send-msg client (str "JOIN #" channel endl)))

(def channel "sodawavelive")


(comment

  (auth client "bunzbot" (auth/get-token))
  (join-channel client channel)
  (msg client channel "test message post refactor")

  
  )
