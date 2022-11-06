(ns home-assistant.frontend.task.events
  (:require
   [home-assistant.frontend.events :as events]

   [re-frame.core :as rf]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))

(rf/reg-event-db
 ::select-task
 (fn [db [_ taskid]]
   (rf/dispatch [::events/change-view :task])
   (assoc-in db [:task :selected-id] taskid)))