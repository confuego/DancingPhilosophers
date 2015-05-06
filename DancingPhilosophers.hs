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

takefollower follower = takeTMVar follower
putfollower follower table = putTMVar follower (table)

randomDelay max = do
	n <- getStdRandom (randomR (1,max))
	threadDelay (n*100000)

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


createLeaders ::Int -> Int -> TVar Int -> DanceCard -> TChan String -> TMVar(DanceCard) -> IO ()
createLeaders 0 numFollowers donecount table msgs buffer = return ()
createLeaders n numFollowers donecount table msgs buffer = do 
	let tableCopy = table
	leader <- atomically $ newTMVar (tableCopy)
	atomically $ do 
		l <- takeTMVar leader
		putTMVar leader (put "Samba" n l)
	forkIO $ compareToFollower ("Leader " ++ (show n)) leader msgs numFollowers donecount buffer
	createLeaders (n-1) numFollowers donecount table msgs buffer

createFollowers :: Int -> Int -> TVar Int -> DanceCard -> TMVar(DanceCard) -> TChan String -> IO ()
createFollowers 0 numLeaders donecount table buffer msgs = return ()
createFollowers n numLeaders donecount table buffer msgs = do
	let tableCopy = table
	forkIO $ tryBuffer buffer tableCopy msgs
	createFollowers (n-1) numLeaders donecount table buffer msgs

tryBuffer :: TMVar(DanceCard) -> DanceCard -> TChan String -> IO ()
tryBuffer buffer table outs = do
	rand <- getStdRandom (randomR (1,100)) 
	atomically $ do putfollower buffer (put "Cha Cha" rand table)
	randomDelay 6
	return ()

compareToFollower :: String  -> TMVar(DanceCard) -> TChan String -> Int -> TVar Int -> TMVar(DanceCard) -> IO()
compareToFollower name table outs followerCount donecount buffer = do
	atomically $ do
		t <- takeTMVar table
		ft <- takefollower buffer
		writeTChan outs $ name ++ " is looking at follower in buffer: \n" ++ (show ft) ++ "\n"
	randomDelay 6
	return ()

shoutLeader :: TChan String -> TVar Int -> Int -> IO()
shoutLeader msgs followerCount totalFollowers = do
	fcount <- atomically $ readTVar followerCount
	empty <- atomically $ isEmptyTChan msgs
	if(( not(empty) ) && fcount < totalFollowers)
		then do
			msg <- atomically $ readTChan msgs
			putStrLn msg
			shoutLeader msgs followerCount totalFollowers
	else return ()

main :: IO()
main = do
	args <- getArgs
	let tableCopy = danceCard
	buffer <- atomically $ newTMVar (tableCopy)
	msgs <- atomically newTChan
	donecount <- newTVarIO (0::Int)
	let numLeaders = read (args !! 0) :: Int
	let numFollowers = read (args !! 1) :: Int
	createLeaders numLeaders numFollowers donecount danceCard msgs buffer
	createFollowers numFollowers numLeaders donecount danceCard buffer msgs
	forkIO $ shoutLeader msgs donecount 2
	atomically $ do
		count <- readTVar donecount
		check $ count == 0
		writeTChan msgs $ "main waiting.............."
	threadDelay (5*1000000)
	atomically $ writeTChan msgs "main done!!!!!!!!!!!"	