(ns home-assistant.backend.handler
  (:require
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.util.response :refer [resource-response]]
   [compojure.core :refer [defroutes GET POST routes]]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
   [ring.middleware.params :refer [wrap-params]]

   [home-assistant.backend.sql.task :as sql-task]))


(defroutes api-routes
  (GET "/get-tasks" []
    {:status 200
     :body (sql-task/get-all-tasks)})
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