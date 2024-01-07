(ns home-assistant.backend.sql.db)

(def db
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//85.235.67.167:3306/home-assistant?characterEncoding=utf8"
   :user "home-assistant"
   :password "test1234"})