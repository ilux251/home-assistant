(ns home-assistant.frontend.events
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.db :as db]
   [home-assistant.frontend.util.request :as har]
   [home-assistant.frontend.devices.events :as devices-evt]
   
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))

(rf/reg-event-fx
 ::initialize-db
 (fn [_ _]
   (rf/dispatch [::get-tasks])
   (rf/dispatch [::devices-evt/get-all-entities])
   {:db db/default-db}))

(rf/reg-event-db
 ::change-view
 (fn [db [_ view]]
   (assoc db :current-view view)))

(rf/reg-event-db
 ::success
 (fn [db [_ path data]]
   (assoc-in db path data)))

(rf/reg-event-db
 ::failure
 (fn [db [_ data]]
   (println data)
   db))

(rf/reg-event-fx
 ::get-tasks
 (fn [_ _]
   {:dispatch [::har/request {:url-path "get-tasks"
                              :on-success [::success [:task :tasks]]
                              :on-failure [::failure]}]}))

(rf/reg-event-fx
 ::auth
 (fn [{:keys [db]} _]
   {:http-xhrio {:method :post
                 :uri "http://v2202401214473251973.ultrasrv.de/auth"
                 :params {:user "user" :password "password"}
                 :timeout 5000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:success]
                 :on-failure [:failure]}}))
