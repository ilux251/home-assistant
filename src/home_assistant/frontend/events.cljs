(ns home-assistant.frontend.events
  (:require
   [re-frame.core :as rf]
   [home-assistant.frontend.db :as db]
   
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))

(rf/reg-cofx
 :initialize-tasks
 (fn []
   (rf/dispatch [::get-tasks])))

(rf/reg-event-fx
 ::initialize-db
 #_[(rf/inject-cofx :initialize-tasks)]
 (fn [{:keys [db]} _]
   {:db db/default-db}))

(rf/reg-event-db
 ::change-view
 (fn [db [_ view]]
   (assoc db :current-view view)))

(rf/reg-event-db
 :success
 (fn [db [_ path data]]
   (assoc-in db path data)))

(rf/reg-event-db
 :failure
 (fn [db [_ data]]
   (println data)
   db))

(rf/reg-event-fx
 ::get-tasks
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri "http://localhost:8000/get-tasks"
                 :timeout 5000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:success [:task :tasks]]
                 :on-failure [:failure]}}))

(rf/reg-event-fx
 ::auth
 (fn [{:keys [db]} _]
   {:http-xhrio {:method :post
                 :uri "http://localhost:8000/auth"
                 :params {:user "user" :password "password"}
                 :timeout 5000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:success]
                 :on-failure [:failure]}}))
