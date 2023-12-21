(defn not-elimination-one [expr]
  (if (and (seq? expr) (= (first expr) 'not) (seq? (second expr)) (= (first (second expr)) 'not))
        (second (second expr))
      nil))

(defn and-elimination [expr]
  (if (and (seq? expr) (= (first expr) 'and))
       (set (rest expr))
    nil))

(defn modus-tollens [prop-if kb]
  (let [antecedent (second prop-if)
        consequent (last prop-if)]
    (if (some #(= (list 'not consequent) %) (seq kb))
      (list 'not antecedent)
      nil)))

(defn modus-ponens [prop-if kb]
  (let [antecedent (second prop-if)
        consequent (last prop-if)]
    (if (contains? kb antecedent)
       consequent
      nil)))

(defn elim-step [prop]
  (if-let [new-prop (not-elimination-one prop)]
    (do
      (println "Because: " prop)
      (println "I derived: " new-prop)
      (println "By not-elimination")
      (hash-set new-prop))
    (if-let [new-prop (and-elimination prop)]
      (do
        (println "Because: " prop)
        (println "I derived: " new-prop)
        (println "By and-elimination")
        new-prop)
      nil)))

(defn modus-step [prop-if kb]
 (if (and (seq? prop-if)(= (first prop-if) 'if))
  (do
  (if-let [new-prop (modus-tollens prop-if kb)]
    (do
      (println "Because: " prop-if)
      (println "and: (not" (nth prop-if 2)")")
      (println "I derived: " new-prop)
      (println "By modus-tollens")
      (hash-set new-prop))
    (if-let [new-prop (modus-ponens prop-if kb)]
      (do
        (println "Because: " prop-if)
        (println "and: " (second prop-if))
        (println "I derived: " new-prop)
        (println "By modus-ponens")
        (hash-set new-prop))
      nil)))
    nil))



(defn fwd-infer [prop kb]
  (loop [final-kb (conj kb prop)
         kb-analyzed (conj kb prop)]
    (let [new-prop (elim-step (first kb-analyzed))]
      (cond
        (not (nil? new-prop))
        (recur (into #{} (concat final-kb new-prop)) (distinct (rest (concat kb-analyzed new-prop))))
        
        (and (= new-prop nil) (not (empty? (rest kb-analyzed))))
        (recur final-kb (distinct (rest kb-analyzed)))
        
        :else
        (do
          (loop [final-kb-modus final-kb
                 kb-analyzed-modus final-kb]
            (let [new-prop-modus (modus-step (first kb-analyzed-modus) final-kb-modus)]
              (cond
                (not (nil? new-prop-modus))
                (recur (into #{} (concat final-kb-modus new-prop-modus)) (distinct (rest (concat kb-analyzed-modus new-prop-modus))))
        
                (and (= new-prop-modus nil) (not (empty? (rest kb-analyzed-modus))))
                (recur final-kb-modus (distinct (rest kb-analyzed-modus)))
                
                :else
                (do
                  (println ">>>"final-kb-modus))))))))))
