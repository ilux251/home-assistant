(ns home-assistant.frontend.task.subs
  (:require [re-frame.core :as rf]
            
            [home-assistant.frontend.util.date :as ud]))

(rf/reg-sub
 ::tasks
 (fn [db]
   (get-in db [:task :tasks])))

(rf/reg-sub
 ::tasks-today
 :<- [::tasks]
 (fn [tasks]
   (filter #(ud/is-date-today (:date %)) tasks)))

(rf/reg-sub
 ::tasks-soon
 :<- [::tasks]
 (fn [tasks]
   (filter #(> (js/Date. (:date %)) (js/Date.)) tasks)))

(rf/reg-sub
 ::selected-task
 (fn [db]
   (let [tasks (get-in db [:task :tasks])
         selected-taskid (get-in db [:task :selected-id])
         selected-task (first (filter #(= (:id %) selected-taskid) tasks))
         subtasks (filter #(= (:subtaskid %) selected-taskid) tasks)]
     {:selected-task selected-task
      :subtasks subtasks})))