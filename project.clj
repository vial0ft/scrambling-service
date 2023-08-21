(defproject scrambling-service "0.1.0-SNAPSHOT"
  :description "Scramble service"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [duct/core "0.8.0"]
                 [duct/module.cljs "0.4.1"]
                 [duct/module.logging "0.5.0"]
                 [duct/module.web "0.7.3"]
                 [ring/ring-json "0.5.1"]
                 [cljs-ajax "0.8.4"]
                 [re-frame "1.3.0"]
                 [metosin/spec-tools "0.10.5"]
                 ]
  :plugins [[duct/lein-duct "0.12.3"]]
  :main ^:skip-aot scrambling-service.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :test {:dependencies [[org.clojure/test.check "1.1.1"]]}
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :dependencies [[cider/piggieback "0.5.2"]]
          :repl-options {:init-ns user, :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[integrant/repl "0.3.2"]
                                   [hawk "0.2.11"]
                                   [eftest "0.5.9"]
                                   [kerodon "0.9.1"]]}})
