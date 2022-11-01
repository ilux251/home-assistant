(ns home-assistant.frontend.task.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::selected-task
 (fn [db]
   (let [tasks (get-in db [:task :tasks])
         selected-taskid (get-in db [:task :selected-id])
         selected-task (first (filter #(= (:id %) selected-taskid) tasks))
         subtasks (filter #(= (:subtaskid %) selected-taskid) tasks)]
     {:selected-task selected-task
      :subtasks subtasks})))