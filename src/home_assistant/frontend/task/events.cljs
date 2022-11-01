(ns home-assistant.frontend.task.events
  (:require [re-frame.core :as rf]
            [home-assistant.frontend.events :as events]))

(rf/reg-event-db
 ::select-task
 (fn [db [_ taskid]]
   (rf/dispatch [::events/change-view :task])
   (assoc-in db [:task :selected-id] taskid)))