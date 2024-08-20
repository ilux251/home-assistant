(ns home-assistant.backend.handler
  (:require
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.util.response :refer [resource-response]]
   [compojure.core :refer [defroutes GET POST routes]]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
   [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.middleware.session.cookie :refer [cookie-store]]
   [cemerick.friend :as friend]
   (cemerick.friend [workflows :as workflows]
                    [credentials :as creds])

   [home-assistant.backend.sql.task :as sql-task]))

(def users {"root" {:username "root"
                    :password (creds/hash-bcrypt "root_password")}
            "jane" {:username "jane"
                    :password (creds/hash-bcrypt "user_password")}})

(def header-cors
  {"Access-Control-Allow-Origin"  "*"
   "Access-Control-Allow-Headers" "*"
   "Access-Control-Allow-Methods" "*"})

(defroutes api-routes
  (GET "/get-tasks" _
    {:status 200
     :body (sql-task/get-all-tasks)
     :headers header-cors})
  
  (POST "/auth" req
    (let [params (:params req)
          user (:user params)
          password (:password params)]
      (if-let [identity (creds/bcrypt-credential-fn users {:username user :password password})]
        (friend/merge-authentication
         {:status 200
          :body {:message "Successfully authenticated!"}
          :headers header-cors}
         identity)

        {:status 401
         :body {:message "Authentication failed"}
         :headers header-cors}))))

(defroutes resource-routes
  (GET "/" []
    (resource-response "public/index.html")))

(defn wrap-debug [handler]
  (fn [request]
    (println "Request:" request)
    (let [response (handler request)]
      (println "Response:" response)
      response)))

(defn wrap-unauthrized [handler]
  (fn [request]
    (if (friend/current-authentication request)
      (handler request)

      (-> (handler request)
          (merge {:status 401
                  :body {:message "Unauthorized access"}})))))

(def app
  (routes
   (-> resource-routes
       (wrap-resource "public"))
   (-> api-routes
       (wrap-unauthrized)
       (wrap-session {:store (cookie-store {:max-age 5})})
       (friend/authenticate {:credential-fn (partial creds/bcrypt-credential-fn users)
                             :workflows [(workflows/interactive-form)]})
       (wrap-defaults api-defaults)
       (wrap-params)
       (wrap-json-params)
       (wrap-json-response))))