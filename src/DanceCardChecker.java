import java.util.*;
import java.io.*;


// support classes at bottom:
//  - class Pair<S,T>   (generic pair)
//  - class CardLine    (represents a dance and follower (0-indexed)
//  - class Card        (represents a leader's card. leads/follows are 0-indexed.)
//  - class CardError extends RuntimeException
//                      (error message, and perhaps a cards dump (when VERBOSE_ERRORS==true)).

public class DanceCardChecker {
  
  // in-house debug suite.
  public static boolean VERBOSE_ERRORS = false;
  public static boolean DEBUG = false;
  public static void debug  (String s){ if (DEBUG) System.out.print  (s);}
  public static void debugln(String s){ if (DEBUG) System.out.println(s);}
  
  // hard-coding the dances list...
  public static String[] dances = {"Waltz","Tango","Foxtrot","Quickstep","Rumba","Samba","Cha Cha", "Jive"};
  
  // next four methods help us parse through the expected output.
  // might just crash if it's not well-formed enough...
  
  public static CardLine parseCardLine(Scanner sc){
    String dance = sc.next();
    if (dance.toLowerCase().equals("cha")) { dance += " "+sc.next(); }
    debug(String.format("reading dance='%s'.\n",dance));
    String next = sc.next();
    int followNum = (next.equals("with")) ? sc.nextInt() : -1;
    if (sc.hasNextLine()) {sc.nextLine(); } // clear out the rest of the line;
    CardLine cardline = new CardLine(dance,followNum);
    debugln("\tcardline: "+cardline);
    return cardline;
  }
  
  public static List<CardLine> parseCardLines(Scanner sc){
    List<CardLine> lines = new ArrayList<CardLine>();
    while (sc.hasNextLine()){
      String line = sc.nextLine();
      if ( (! line.equals(""))){
        CardLine cardLine = parseCardLine(new Scanner(line));
        lines.add(cardLine);
      }
      else { return lines; }
    }
    return lines;
  }
  
  public static Card parseLeader(Scanner sc){
    String line = sc.next();
    if ( ! line.toLowerCase().equals("leader")){ throw new CardError ("not a leader card description. (line="+line+").",null); }
    int leaderNum = Integer.parseInt(sc.nextLine().split(":")[0].trim());
    List<CardLine> cards = parseCardLines(sc);
    Card card = new Card(leaderNum,cards);
    debugln("card: "+card);
    return card;
  }
  
  public static List<Card> parseLeaders(Scanner sc){
    List<Card> cards = new ArrayList<>();
    while (sc.hasNext()){
      Card card = parseLeader(sc);
      cards . add ( card );
    }
    return cards;
  }
  
  // build up a 3d grid of booleans.
  public static boolean[][][] buildGrid(List<Card> cards, int numLeaders, int numFollowers){
    boolean[][][] grid = new boolean[numLeaders][numFollowers][dances.length];
    debug(String.format("made grid[%s][%s][%s].\n",numLeaders,numFollowers,dances.length));
    for (Card card : cards){
      int leadNum = card.leaderNum;
      debug("leader #"+leadNum+"   ");
      for (CardLine cardLine : card.cardLines){
        int followNum = cardLine.followerNum;
        debug("follower #"+followNum+"   ");
        int danceIndex = Arrays.asList(dances).indexOf(cardLine.danceName);
        debugln("dance "+cardLine.danceName+"=#"+danceIndex);
        if ( followNum != -1){
          debug(String.format("assigning grid[%s][%s][%s] = '%s'\n",leadNum,followNum,danceIndex,true));
          if (grid[leadNum-1][followNum-1][danceIndex] == true){
            throw new CardError("they're already dancing that - how'd you get here again???\n",grid2cards(grid)); 
          }
          grid[leadNum-1][followNum-1][danceIndex] = true;
        }
      }
    }
    return grid;
  }
  
  // display grids of X's for each leader (rows:follows, cols:dances).
  public static String showGrid(boolean[][][] grid){
    StringBuilder s = new StringBuilder();
    for (int li = 0; li < grid.length; li++ ) {
      for (int fi = 0; fi < grid[li].length; fi++ ) {
        for (int di = 0; di < grid[li][fi].length; di++ ) {
          s.append(grid[li][fi][di]?"X":"-");
        }
        s.append("\n");
      }
      s.append("\n");
    }
    return s.toString();
  }
  
  // helper: reverse the representation. good for error displays.
  public static List<Card> grid2cards(boolean[][][] grid){
    List<Card> cards = new ArrayList<Card>();
    for (int li = 0; li < grid.length; li++ ) {
      List<CardLine> cardlines = new ArrayList<CardLine>();
      int[] dancesCount = new int[grid[0][0].length];
      int[] partnerNum = new int[grid[0][0].length];
      for (int i=0;i<partnerNum.length; i++){partnerNum[i]=-1;}
      
      for (int fi = 0; fi < grid[li].length; fi++ ) {
        for (int di = 0; di < grid[li][fi].length; di++ ) {
          if (grid[li][fi][di]){
            partnerNum[di] = fi;
            dancesCount[di]++;
          }
        }
      }
      for (int di = 0; di < grid[0][0].length; di++ ) {
        cardlines.add(new CardLine(dances[di],partnerNum[di]));
      }
      cards.add(new Card(li,cardlines));
    }
    return cards;
  }
  
  // helper: show all Cards in a List.
  public static String showCards(List<Card> cards){
    StringBuilder sb = new StringBuilder();
    for (Card card : cards){
      sb.append(card.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
  
  
  // consistency condition: nobody can dance a specific dance twice.
  // (e.g., a leader can't dance the waltz twice)
  public static void checkNobodyDancesADanceTwice(boolean[][][]grid){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    
    // how many times does the leader dance each dance?
    int[][] numDancesL = new int[numLeaders][numDances];
    int[][] numDancesF = new int[numFollowers][numDances];
    for (int li=0;     li < numLeaders;   li++) {
      for (int fi=0;   fi < numFollowers; fi++) {
        for (int di=0; di < numDances;    di++) {
          if (grid[li][fi][di]){
            numDancesL[li][di]++;
            numDancesF[fi][di]++;
          }
        }
      }
    }
    // check no leader dances a dance twice.
    for (int li=0; li<numLeaders; li++){
      for (int di=0; di<numDances; di++){
        if (numDancesL[li][di]>1){
          throw new CardError(String.format("leader %d danced the %s %d times! invalid result.\n",li+1, dances[di],numDancesL[li][di]),grid2cards(grid));
        }
      }
    }
    // check no follower dances a dance twice. could likely be folded into the previous one, don't care.
    for (int fi=0; fi<numFollowers; fi++){
      for (int di=0; di<numDances; di++){
        if (numDancesF[fi][di]>1){
          throw new CardError(String.format("follower %d danced the %s %d times! invalid result.\n",fi+1, dances[di],numDancesF[fi][di]),grid2cards(grid));
        }
      }
    }
  }
  
  // consistency condition: nobody dances with the same person more than twice.
  public static void checkForTooManyDancesWithOnePartner(boolean[][][] grid){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    
    // probably checking just leaders is sufficient. but we'll work from both angles.
    // how many times does each person dance with each other kind of person?
    int[][] allPartnerCountsL = new int[numLeaders][numFollowers];
    int[][] allPartnerCountsF = new int[numLeaders][numFollowers];
    // how many times does each person dance each kind of dance?
    int[][] allDanceCountsL   = new int[numLeaders][numDances];
    int[][] allDanceCountsF   = new int[numFollowers][numDances];
    for (int li = 0; li < grid.length; li++ ) {
      int counts = 0; // how many dances does this lead do?
      for (int fi = 0; fi < grid[li].length; fi++ ) {
        for (int di = 0; di < grid[li][fi].length; di++ ) {
          if (grid[li][fi][di]){
            allPartnerCountsL[li][fi]++;
            allPartnerCountsF[li][fi]++;
            allDanceCountsL[li][di]++;
            allDanceCountsF[fi][di]++;
          }
        }
      }
    }
    // ensure a leader didn't dance with a particular follower >2 times.
    for (int li=0; li<numLeaders; li++){
      for (int fi=0; fi<numFollowers; fi++){
        for (int di=0; di<numDances; di++){
          if (allPartnerCountsL[li][fi]>2 || allPartnerCountsF[li][fi]>2){
            throw new CardError(String.format("leader %s is dancing %d times with folower %s, invalid result.\n",li+1, allPartnerCountsL[li][fi], fi+1),grid2cards(grid));
          }
        }
      }
    }
  }
  
  // helper: is this follower dancing a particular dance? (we don't care with whom).
  public static boolean isFollowerDancing(boolean[][][] grid, int followerI, int danceI){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    
    for (int li = 0; li < grid.length; li++ ) {
      if (grid[li][followerI][danceI]){
        return true;
      }
    }
    return false;
  }
  
  // helper: is this leader dancing a particular dance? (we don't care with whom).
  public static boolean isLeaderDancing(boolean[][][] grid, int leaderI, int danceI){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    
    for (int fi = 0; fi < numFollowers; fi++ ) {
      int counts = 0; // how many dances does this lead do?
      if (grid[leaderI][fi][danceI]){
        return true;
      }
    }
    return false;
  }
  
  // helper: how many dances do these two dance with each other?
  public static int numDancesTogether(boolean[][][] grid, int leaderI, int followerI){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    int count = 0;
    for (int di = 0; di < grid[leaderI][followerI].length; di++ ) {
      if (grid[leaderI][followerI][di]){
        count++;
      }
    }
    return count;
  }
  
  // consistency condition: nobody missed a dance they were allowed to dance.
  public static void checkForSkippedValidDances(boolean[][][] grid){
    int numLeaders = grid.length;
    int numFollowers = grid[0].length;
    int numDances = grid[0][0].length;
    
    // a dance is skipped when a leader isn't dancing something, AND there's a follower who's not dancing it too, AND they aren't already dancing twice.
    for (int li = 0; li < grid.length; li++ ) {
      for (int fi = 0; fi < grid[li].length; fi++ ) {
        for (int di = 0; di < grid[li][fi].length; di++ ) {
          int numTogether = numDancesTogether(grid,li,fi);
          if ( (!isLeaderDancing(grid,li,di))  &&  (!isFollowerDancing(grid,fi,di))  &&  (numTogether<2)){
            throw new CardError(String.format("leader %d and follower %s aren't dancing the %s, and yet they're slated to dance only %d dance(s) together. invalid result.\n",li+1,fi+1,dances[di],numTogether),grid2cards(grid)); 
          }
        }
      }
    }
  }
  
  // combine all consistency conditions into one method call.
  public static void checkAllRules(boolean[][][] grid){
    // assuming we could even read the output, check for issues (which would throw
    // RuntimeException values), and if we're still around, print a "good job!" message.
    // make sure that:
    //  - no leader dances a dance more than once
    //  - no follower dances a dance more than once
    checkNobodyDancesADanceTwice(grid);
    //  - no leader dances more than twice with a follower
    checkForTooManyDancesWithOnePartner(grid);
    //  - no available dance opportunities were missed
    checkForSkippedValidDances(grid);
  }
  
  // - read the command-line arguments
  // - create the dance cards
  // - check the rules
  // - print a happy or sad message.
  public static void main(String[] args) throws IOException {
    
    // read the command-line arguments
    if (args.length!=3){
      System.err.println("usage: java DanceCardChecker resultsFileName N M");
      return;
    }
    Scanner sc = new Scanner(new File(args[0]));
    int numLeaders = Integer.parseInt(args[1]);
    int numFollowers = Integer.parseInt(args[2]);
    
    // create the dance cards.
    List<Card> cards = parseLeaders(sc);
    debugln("cards: "+cards);
    
    // build up the grid of booleans.
    boolean[][][] grid = buildGrid(cards, numLeaders, numFollowers);
    debugln(showGrid(grid));
    
    // check all rules.
    checkAllRules(grid);
   
    // if we haven't already printed a sad message, rejoice!
    System.out.println("apparently you've created a consistent result. good job!");
  }
  
  // for the eventual testing file, it was better for this to be a static inner class.
  static class CardError extends RuntimeException{
    String msg;
    public CardError(String msg,List<Card> cards){
      super(msg+(DanceCardChecker.VERBOSE_ERRORS?showCards(cards):""));
      this.msg = msg+(DanceCardChecker.VERBOSE_ERRORS?showCards(cards):"");
    }
    @Override public String toString() {return "CardError: "+msg;}
  }
  
  public static String padTo(String msg,int width,boolean right){
    return String.format("%"+(right?"":"-")+width+"s",msg); 
  }
}

// Support Classes.

// generic pair.
class Pair<S,T>{
  S fst; T snd;
  Pair(S s,T t){ fst = s; snd = t; }
  @Override public String toString(){return "("+fst+","+snd+")";}
}

// one line on a card (dance name, follower number 0-indexed)
class CardLine {
  String danceName;
  int followerNum; // zero-indexed. always add one when printing.
  public CardLine(String danceName, int followerNum){
    this.danceName = danceName;
    this.followerNum = followerNum;
  }
  @Override public String toString(){return DanceCardChecker.padTo(danceName,10,false)+" "+(followerNum==-1?"------":"with "+(followerNum+1));}
}

// all lines of a leader's card (lead's number 0-indexed, and all CardLines)
class Card{
  int leaderNum; // zero-indexed. always add one when printing.
  List<CardLine> cardLines;
  public Card(int leaderNum, List<CardLine> cardLines){
    this.leaderNum = leaderNum;
    this.cardLines = cardLines;
  }
  @Override public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("Leader "+(leaderNum+1)+":\n");
    for (CardLine cardline : cardLines){
      sb.append(indent(cardline.toString(), ""));
    }
    return sb.toString();
  }
  private String indent(String s, String padding){
    String[] lines = s.split("\n");
    StringBuilder sb = new StringBuilder();
    for (String line : lines){
      sb.append(padding+line+"\n");
    }
    return sb.toString();
  }
}


