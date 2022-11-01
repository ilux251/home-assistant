(ns home-assistant.backend.core
  (:require [ring.adapter.jetty :as j]
            [ring.middleware.reload :refer [wrap-reload]]

            [home-assistant.backend.handler :as handler])
  (:gen-class))

(defonce server (atom nil))

(defn start
  []
  (if (:app @server)
    (.start (:app @server))
    (swap! server assoc :app (j/run-jetty (wrap-reload #'handler/app) {:port 8000 :join? false}))))

(defn stop
  []
  (.stop (:app @server)))

(defn -dev-main
  [& args]
  (start))

(defn -main
  [& args]
  (j/run-jetty handler/app {:port 8000}))