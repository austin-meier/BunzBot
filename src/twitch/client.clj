(ns twitch.client
  (:require [hato.websocket :as ws]
            [oauth.token :as auth]
            [twitch.messages :as msg]))

(def client
  @(ws/websocket
    "wss://irc-ws.chat.twitch.tv:443"
    {
     :on-message (fn [ws msg last?]
                   (msg/handle-msg ws (str msg)))}))

(def endl "\r\n")

(defn msg [client channel msg]
  (ws/send! client (str "PRIVMSG #" channel " :" msg endl))
  client)

(defn auth [client botname token]
  (ws/send! client (str "PASS oauth:" token endl))
  (ws/send! client (str "NICK " botname endl)))

(defn join-channel [client channel]
  (ws/send! client (str "JOIN #" channel endl)))

(defn send! [client msg]
  (ws/send! client (str msg endl)))

(def channel "sodawavelive")


(auth client "bunzbot" (auth/get-token))
(join-channel client channel)
(comment

  (msg client channel "hello")

  
  )
