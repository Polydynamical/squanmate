(ns squanmate.scramblers.algsets.permute-last-layer
  (:require [squanmate.scramblers.algsets.scramble-generators.case-by-case-algset :as algset]))

;; These algs are from Raul Low Beattie's video:
;; Square-1 Full PLL (with Parity - New Algorithms)
;; https://www.youtube.com/watch?v=DIx9vKQxCw8

(def ^:private even-cases
  [["1. Ua" "/3,0/1,0/0,-3/-1,0/-3,0/1,0/0,3/-1"]
   ["2. Ub" "1,0/0,-3/-1,0/3,0/1,0/0,3/-1,0/-3,0/"]
   ["3. Z" "M2 U’ M2 U M2 M2"]
   ["4. H" "U’ M2 U2 M2 U’ M2"]
   ["5. Aa" "1,0/-3,0/3,3/0,-3/2,0/-3,0/3,3/0,-3/"]
   ["6. Ab" "/-3,0/3,3/0,-3/-2,0/-3,0/3,3/0,-3/-1"]
   ["7. E" "/3,3/1,-2/2,2/-3,0/-3,-3/"]
   ["8. F" "1,0/-1,-1/-3,0/3,0/1,4/-3,0/3,0/2,-4/-3,0/0,1*"]
   ["9. Ja" "1,0/3,-3/3,0/-3,0/0,3/-3,0/-1"]
   ["10. Jb" "/3,-3/3,0/-3,0/0,3/-3,0/"]
   ["11. Ra" "/-3,0/3,0/-3,0/0,-3/4,1/-1,2/-3,-3/"]
   ["12. Rb" "1,0/3,0/-3,0/3,0/0,3/2,5/1,4/3,3/-1"]
   ["13. T" "/3,0/-3,-3/0,3/4,0/3,0/-3,-3/0,3/-1"]
   ["14. Y" "1,0/3,0/3,3/3,0/5,0/0,3/3,3/0,3/"]
   ["15. Na" "/3,3/-3,0/3,3/-3,0/3,3/"]
   ["16. Nb" "1,0/3,3/-3,0/3,3/-3,0/3,3/-1"]
   ["17. Ga" "/-3,3/3,-3/-2,0/0,3/0,-3/0,3/0,-3/-1"]
   ["18. Gb" "1,0/0,3/0,-3/0,3/0,-3/-1,0/-3,3/3,-3/"]
   ["19. Gc" "1,0/-3,3/3,-3/-4,0/0,3/0,-3/0,3/0,-3/"]
   ["20. Gd" "/0,3/0,-3/0,3/0,-3/1,0/-3,3/3,-3/-1"]
   ["21. V" "1,0/3,0/-4,-1/0,-3/3,0/1,-2/0,3/0,3/-4,-1/0,1*"]])

(def ^:private odd-cases
  [["22. Adj" "/-3,0/0,3/0,-3/0,3/2,0/0,2/-2,0/4,0/0,-2/ 0,2/-1,4/0,-3/*"]
   ["23. Opp" "/3,3/1,0/-2,-2/2,0/2,2/0,-2/-1,-1/0,3/-3,-3/ 0,2/-2,-2/-1"]
   ["24. Oa" "/3,3/1,0/-2,-2/-2,0/2,2/-1,0/-3,-3/1,0/2,2/0,1"]
   ["25. Ob" "/3,3/1,0/-2,-2/2,0/2,2/-1,0/-3,-3/0,2/-2,-2/-1"]
   ["26. W" "0,-1/1,-2/-4,0/0,3/1,0/3,-2/-4,0/-4,0/-2,2/ -1,0/0,-3/*"]
   ["27. M" "/3,0/1,2/2,0/-4,0/4,0/2,0/-1,-2/-3,0/-2,0/ -4,2/4,-2/-1"]
   ["28. pN" "/-3,0/0,-3/2,0/0,2/-2,0/4,0/0,-2/0,2/-4,1/ 3,0/*"]
   ["29. pJ" "/-3,0/-3,0/-1,0/0,2/-2,0/4,0/0,-2/0,2/-1,4/ 0,-3/*"]
   ["30. X" "/3,0/1,0/4,5/0,4/0,4/2,3/0,-1/-3,0/-4,0/ 1,-2/0,3/-1"]
   ["31. Q" "1,0/5,0/3,3/1,-4/4,0/2,2/0,4/4,-1/3,3/-5,0/-1"]
   ["32. Ka" "/3,3/5,0/0,2/-2,0/4,0/0,-2/0,2/-1,1/3,0/ 3,3/*"]
   ["33. Kb" "1,0/5,0/-3,-3/0,3/4,0/-2,0/4,0/-4,0/-2,0/ -1,0/-3,-3/-2,3/-1"]
   ["34. Sa" "/-3,0/0,-1/-4,0/0,4/0,4/4,0/-4,0/1,-4/3,0/ 3,-3/*"]
   ["35. Sb" "/3,0/1,0/0,2/4,0/0,4/2,-2/0,4/1,-1/3,0/-3,0/ -3,1/-1"]
   ["36. Ba" "/0,3/-3,3/2,1/0,-2/2,0/-4,0/0,2/0,-2/0,1/ 0,-3/0,-3/"]
   ["37. Bb" "1,0/2,-4/1,4/-4,0/0,3/1,0/-1,4/0,4/4,0/2,0/ -1,0/-3,0/"]
   ["38. Ca" "0,-1/-3,0/3,0/0,1/3,0/1,0/-2,0/2,-1/0,1/ -4,0/-5,0/-4,0/-1,0/-3,0/"]
   ["39. Cb" "/3,0/1,0/4,0/5,0/4,0/0,-1/-2,1/2,0/-1,0/-3,0/ 0,-1/-3,0/3,0/0,1"]
   ["40. Da" "/3,0/1,0/-2,0/-4,0/0,-4/1,-4/-1,0/0,-3/4,0/ -1,-4/-2,4/-1"]
   ["41. Db" "/0,3/0,3/0,-1/0,2/0,-2/4,0/-2,0/0,2/-2,-1/ 3,-3/0,-3/"]
   ["42. Pa" "/-3,0/3,-3/3,0/-1,0/0,2/-2,0/4,0/0,-2/0,2/ -4,1/3,0/"]
   ["43. Pb" "/-3,0/4,-1/0,-2/0,2/-4,0/2,0/0,-2/1,0/-3,0/ -3,3/3,0/"]])

(def pll-algset
  (algset/->CaseByCaseAlgSet odd-cases even-cases))
