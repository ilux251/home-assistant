(defproject home-assistant "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [compojure/compojure "1.7.0"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/tools.namespace "1.3.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [honeysql/honeysql "1.0.461"]
                 [mysql/mysql-connector-java "5.1.6"]
                 
                 
                 ;; Clojurescript deps
                 [reagent/reagent "1.1.1"]
                 [re-frame/re-frame "1.3.0"]
                 [binaryage/devtools "1.0.6"]
                 [day8.re-frame/http-fx "0.2.4"]
                 [cljs-ajax/cljs-ajax "0.8.4"]
                 [org.clojure/clojurescript "1.11.54"]
]
  :main home-assistant.backend.core
  :target-path "target/%s"
  :profiles {:uberjar 
             {:aot :all}
             
             :cljs 
             {:dependencies [[thheller/shadow-cljs "2.24.1"]]}
             
  :aliases {"cljs" ["run" "-m" "thheller/shadow-cljs"]}})