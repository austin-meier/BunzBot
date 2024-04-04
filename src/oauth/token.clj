(ns oauth.token
  (:require [clj-http.client :as client]
            [cheshire.core :as json]))

(def twitch-api "https://id.twitch.tv/oauth2/token")
(def client-id "a3zz5oomrvic96qqvp6lmh33e82k72")
(def client-secret "x1xye76jqxl4z6gjqsqdujgmdr9x6j")

(def token (atom nil))

(defn -new-token-request []
  (client/post twitch-api
              {:form-params {:client_id client-id
                             :client_secret client-secret
                             :grant_type "client_credentials"}}))

(defn -new-token []
  (-> (-new-token-request)
      :body
      (json/parse-string true)
      (->> (reset! token))))


(defn get-token 
  "Get a Twitch API access token to use with requests"
  []
  (if (some? @token)
    @token
    (-new-token)))

(comment
  
  (get-token)
)

