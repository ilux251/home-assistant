(ns home-assistant.backend.sql.db)

(extend-protocol cheshire.generate/JSONable
  java.time.LocalDateTime
  (to-json [dt gen]
    (cheshire.generate/write-string gen (str dt))))

(def db
  {:subprotocol "mysql"
   :subname "//127.0.0.1:3306/home-assistant?characterEncoding=utf8"
   :user "home-assistant"
   :password "test1234"})
