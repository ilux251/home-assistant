(ns home-assistant.backend.core
  (:require [ring.adapter.jetty :as j]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :refer [resource-response]]
            [compojure.core :refer [defroutes GET POST routes]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [ring.middleware.params :refer [wrap-params]])
  (:gen-class))

(defroutes api-routes
  (GET "/data" []
    {:status 200
     :body {:data "Server data"}})
  (POST "/auth" req
    (let [params (:params req)
          user (:user params)
          password (:password params)]
      {:status 200
       :body {:user user :password password}})))

(defroutes resource-routes
  (GET "/" []
    (resource-response "public/index.html")))

(def app
  (routes
   (-> resource-routes
       (wrap-resource "public"))
   (-> api-routes
       (wrap-defaults api-defaults)
       wrap-params
       wrap-json-params
       wrap-json-response)))

(def server
  (j/run-jetty app {:port 8000 :join? false}))

(defn- main
  [& args]
  (server))