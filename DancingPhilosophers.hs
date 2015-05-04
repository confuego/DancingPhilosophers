import System.IO
import System.Environment
import Prelude
import Data.List
import Control.Concurrent
import Control.Concurrent.STM
import Control.Monad

import System.Random

type DanceCard = [(String,Int)]

danceCard = [("Waltz",-1),("Tango",-1),("Foxtrot",-1),("Quickstep",-1),("Rumba",-1),("Samba",-1),("Cha Cha",-1),("Jive",-1)]


put :: String -> Int -> DanceCard -> DanceCard
put key value table = put' key value table 0

put':: String -> Int -> DanceCard -> Int -> DanceCard
put' key value table 9  = table
put' key value ((dance,id):table) count 
	| (dance == key && id == -1) = ((dance,value) : table) 
	| otherwise = (put' key value ((table ++ [(dance,id)])) (count+1))

get :: String -> DanceCard -> Int
get key table = get' key table 0

get' :: String -> DanceCard -> Int -> Int
get' key table 9 = -1
get' key ((dance,id):table) count
	| (key == dance) = id
	| otherwise = get' key (table ++ [(dance,id)]) (count + 1)     

printLeader :: Int -> DanceCard -> String
printLeader id table = "Leader: " ++ (show id) ++ "\n\n" ++ printTable table

printTable :: DanceCard -> String
printTable [] = ""
printTable ((dance,id):table)
	| (id == -1) = if(dance == "Quickstep") then ((dance ++ "\t with ------\n") ++ printTable table) else ((dance ++ "\t\t with ------\n") ++ printTable table)
	| otherwise =  if(dance == "Quickstep") then ((dance ++ "\t with "++ (show id) ++"\n") ++ printTable table) else (dance ++ "\t\t with "++ (show id) ++"\n") ++ printTable table

main :: IO()
main = do
	putStrLn (show (put "Tango" 5 danceCard))
	--putStrLn (printLeader 1 danceCard_1)
	putStrLn (show (put "Tango" 50 danceCard))
	putStrLn (show (get "Tango" danceCard))
	--putStrLn (printLeader 2 danceCard_2)