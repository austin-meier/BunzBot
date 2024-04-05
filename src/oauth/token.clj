(ns oauth.token
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [secrets :refer [secrets]]))

(def twitch-api "https://id.twitch.tv/oauth2/token")
(def redirect_uri "http://localhost:3000")

(def token (atom nil))

;; https://id.twitch.tv/oauth2/authorize?response_type=code&client_id=a3zz5oomrvic96qqvp6lmh33e82k72&redirect_uri=http://localhost:3000&scope=chat%3Aread+chat%3Aedit
(defn -new-token-request []
  (client/post twitch-api
              {:form-params {:client_id (:client-id secrets)
                             :client_secret (:client-secret secrets)
                             :code (:auth-code secrets)
                             :grant_type "authorization_code"
                             :redirect_uri redirect_uri}
               :throw-exceptions false}))

(defn -new-token []
  (-> (-new-token-request)
      :body
      (json/parse-string true)
      (->> (reset! token)
           :access_token)))

(defn generate-auth-url 
  "Generate the URI that a user will navigate to so that we can recieve their authorization code"
  []
  (str "https://id.twitch.tv/oauth2/authorize?response_type=token&client_id="
       (:client-id secrets) 
       "&redirect_uri=http://localhost:3000&scope=chat%3Aread+chat%3Aedit"))

(generate-auth-url)


(defn get-token 
  "Get a Twitch API access token to use with requests"
  []
  (if (some? @token)
    (:access_token @token)
    (-new-token)))

(comment

  (:body (-new-token-request))

  (prn (generate-auth-url))

  (reset! token nil)

  (get-token)
)

