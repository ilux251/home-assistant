(ns home-assistant.frontend.util.request
  (:require [home-assistant.frontend.config :as config]
            
            [re-frame.core :as rf]
            
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]))

(rf/reg-event-fx
 ::request
 (fn [_ [_ {:keys [url-path] :as data}]]
   (let [host (if config/debug? "http://localhost:8000/" "http://v2202401214473251973.ultrasrv.de/")
         default-request-data {:method :get
                               :uri (str host url-path)
                               :timeout 5000
                               :format (ajax/json-request-format)
                               :response-format (ajax/json-response-format {:keywords? true})}
         merged-request-data (merge default-request-data data)]
     {:http-xhrio merged-request-data})))

(rf/reg-event-fx
 ::api-request
 (fn [_ [_ {:keys [url-path] :as data}]]
   (let [host (if config/debug? "http://raspberrypi.local:8123/" "http://ownhomeassistant.ddns.net:8123/")
         default-request-data {:method :get
                               :uri (str host "api/" url-path)
                               :timeout 5000
                               :format (ajax/json-request-format)
                               :response-format (ajax/json-response-format {:keywords? true})
                               :headers {:Content-Type "application/json"
                                         :Authorization "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJiN2FlOTUzMGMyODg0MjhkYmU2N2QxMjc0ZGRjMDk0YiIsImlhdCI6MTcwMzI5MTU3OCwiZXhwIjoyMDE4NjUxNTc4fQ.Es6oV36H8sWPhQnHjLFixVZ3VJWi3jYwZqrPV08xErg"}}
         merged-request-data (merge default-request-data data)]
    {:http-xhrio merged-request-data})))