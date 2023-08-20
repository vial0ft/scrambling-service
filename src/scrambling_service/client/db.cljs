(ns scrambling-service.client.db)

(def default-state {:args {:letters {:value "" :error ""}
                           :word {:value "" :error ""}}
                    :result nil
                    :error nil})
