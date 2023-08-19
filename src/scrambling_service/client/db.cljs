(ns scrambling-service.client.db)

(def default-state {:strings {:scrambling {:value ""
                                           :error ""}
                              :word {:value ""
                                     :error ""}}
                    :result nil
                    :error nil})
