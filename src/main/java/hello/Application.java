package com.csl.umg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  static class Self {
    public String href;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
    
  }

  static class Links {
    public Self self;

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}
    	
  }

  static class Player {
	  
	    public Boolean presence;
	    public String direction;
	    public Boolean wasHit;
	    public Integer score;

	    public Player() {
	        this.presence=true;
	        this.direction="";
	        this.wasHit=false;
	        this.score=0;
	    }
	    
		public Boolean getPresence() {
			return presence;
		}
		public void setPresence(Boolean presence) {
			this.presence = presence;
		}
		
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		public Boolean getWasHit() {
			return wasHit;
		}
		public void setWasHit(Boolean wasHit) {
			this.wasHit = wasHit;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
	    
  }

  
  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
    
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Boolean getWasHit() {
		return wasHit;
	}
	public void setWasHit(Boolean wasHit) {
		this.wasHit = wasHit;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "PlayerState [x=" + x + ", y=" + y + ", direction=" + direction + ", wasHit=" + wasHit + ", score="
				+ score + "]";
	}
    
    
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
	public List<Integer> getDims() {
		return dims;
	}
	public void setDims(List<Integer> dims) {
		this.dims = dims;
	}
	public Map<String, PlayerState> getState() {
		return state;
	}
	public void setState(Map<String, PlayerState> state) {
		this.state = state;
	}
    
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}
    
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  public String recordCommand(String commands) {
	  System.out.println("Return command:"+commands);
	  return commands;
  }
  
  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
	  
	//System.out.println(arenaUpdate);
    String[] allCommands = new String[]{"F", "R", "L", "T"};
    String[] commandsLR = new String[]{"R", "L"};
    String[] commandsLRF = new String[]{"R", "L", "F"};
    String all_rand = allCommands[new Random().nextInt(4)]; // F, R, L or T
    String lr_rand = commandsLR[new Random().nextInt(2)]; // L or R
    String lrf_rand = commandsLRF[new Random().nextInt(3)]; // L or R or F
    String commands=all_rand;
    
    try {
	    // TODO add your implementation here to replace the random response. 
	    String myHref=arenaUpdate.get_links().getSelf().getHref();
		Arena arena=arenaUpdate.getArena();
		Integer width=arena.getDims().get(0);
		Integer height=arena.getDims().get(1);

		System.out.println("myHref="+myHref);
		System.out.println("dim=("+width+","+height+")");
		
		int w,h;
		int max_x,max_y;
		max_x=width-1;
		max_y=height-1;
		
		if ((int)width < 4)  
			w=4;
		else
			w=(int)width;
		
		if ((int)height < 4)  
			h=4;
		else
			h=(int)height;
		
		Player[][] canvas = new Player[w][h];
		
		// init canvas
		for (int x=0;x<w;x++) {
			for (int y=0;y<h;y++) {
				canvas[x][y]=new Player();
				canvas[x][y].setPresence(false);
			}
		}
		
		System.out.println("No. of Players: "+arena.getState().size());
		// find my state
		PlayerState myState = new PlayerState();
		int j=0;
	    for (String key : arena.getState().keySet()) {
	    	System.out.println("key["+j+"]: " + key + " " + arena.getState().get(key).toString());
	    	if (key.equalsIgnoreCase(myHref)) {
	    		System.out.println("get my state j=" + j);
	    		myState=arena.getState().get(key);	
	    	}
	    	else { // record players
	    		System.out.println("get player state j=" + j);
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setPresence(true);
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setDirection(arena.getState().get(key).getDirection());
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setWasHit(arena.getState().get(key).getWasHit());
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setScore(arena.getState().get(key).getScore());
	    		System.out.println("player["+j+"]: " + key + " " + arena.getState().get(key).toString());
	    	}
	      j++;
	    }
	
		int x1minus=myState.getX()-1;
		int x2minus=myState.getX()-2;
		int x3minus=myState.getX()-3;
		if (x2minus<0) x2minus=x1minus;
		if (x3minus<0) x3minus=x2minus;
		
		int x1plus=myState.getX()+1;
		int x2plus=myState.getX()+2;
		int x3plus=myState.getX()+3;
		if (x1plus>max_x) x1plus=max_x;
		if (x2plus>max_x) x2plus=max_x;
		if (x3plus>max_x) x3plus=max_x;

		int y1minus=myState.getY()-1;
		int y2minus=myState.getY()-2;
		int y3minus=myState.getY()-3;
		if (y2minus<0) y2minus=y1minus;
		if (y3minus<0) y3minus=y2minus;
		
		int y1plus=myState.getY()+1;
		int y2plus=myState.getY()+2;
		int y3plus=myState.getY()+3;
		if (y1plus>max_y) y1plus=max_y;
		if (y2plus>max_y) y2plus=max_y;
		if (y3plus>max_y) y3plus=max_y;
		
	    System.out.println("Check Borders");
	    Boolean atBorder=false;
	    if ((int)myState.getX()==0 || (int)myState.getX()==max_x || (int)myState.getY()==0 || (int)myState.getY()==max_y) 
              atBorder=true;
	    
	    Boolean atCorner=false;
	    if (((int)myState.getX()==0 && (int)myState.getY()==0) ||
	    		((int)myState.getX()==max_x && (int)myState.getY()==0) ||
	    		((int)myState.getX()==0 && (int)myState.getY()==max_y ) || 
	    		((int)myState.getX()==max_x && (int)myState.getY()==max_y))
           atCorner=true;
	    
	    System.out.println("At Borders:"+atBorder+"at Corner:"+atCorner+" Direction:"+myState.getDirection());
	    
	    if ((int)myState.getX()==max_x && myState.getDirection().equalsIgnoreCase("E")) {
	    	return recordCommand(lr_rand);
	    }
	    if ((int)myState.getY()==0 && myState.getDirection().equalsIgnoreCase("N")) {
	    	return recordCommand(lr_rand);
	    }
	    if ((int)myState.getY()==max_y && myState.getDirection().equalsIgnoreCase("S")) {
	    	return recordCommand(lr_rand);
	    }
	    
	    
		System.out.println("myState: "+myState.toString());
		
		if (myState.getWasHit()) {
		    ////////////////////
		    // was hit (rule 1):Throw players
		    ////////////////////
			System.out.println("Check was hit (rule 1):Throw players");
		    if (myState.getDirection().equalsIgnoreCase("N") && 
		    		(canvas[myState.getX()][y1minus].getPresence() ||
		    		 canvas[myState.getX()][y2minus].getPresence() ||
		    		 canvas[myState.getX()][y3minus].getPresence())) {
		    	return recordCommand("T");
		    }
		    if (myState.getDirection().equalsIgnoreCase("E") && 
		    		(canvas[x1plus][myState.getY()].getPresence() ||
		    		 canvas[x2plus][myState.getY()].getPresence() ||
		    		 canvas[x3plus][myState.getY()].getPresence())) {
		    	return recordCommand("T");
		    }
		    if (myState.getDirection().equalsIgnoreCase("S") && 
		    		(canvas[myState.getX()][y1plus].getPresence() ||
		   	    	 canvas[myState.getX()][y2plus].getPresence() ||
		   	    	 canvas[myState.getX()][y3plus].getPresence())) {
		    	return recordCommand("T");
		    }
		    if (myState.getDirection().equalsIgnoreCase("W") && 
		    		(canvas[x1minus][myState.getY()].getPresence() ||
		    		 canvas[x2minus][myState.getY()].getPresence() ||
		    		 canvas[x3minus][myState.getY()].getPresence())) {
		    	return recordCommand("T");
		    }
		    
		}

	    if (myState.getWasHit() && !atCorner) {
		    System.out.println("Check Was Hit (rule2): not atCorner but atBorder, move forward");	    	
	    	if (atBorder) {
	    		// left or right border
		    	if ((int)myState.getX()==0 || (int)myState.getX()==max_x) {
		    		if (myState.getDirection().equalsIgnoreCase("N")) {
		    		    if (!canvas[myState.getX()][y1minus].getPresence() && 
		    		        !canvas[myState.getX()][y2minus].getPresence() &&
		    		        !canvas[myState.getX()][y3minus].getPresence()) {
		    		        return recordCommand("F");    	
		    		    }
		    		}
		    		else if (myState.getDirection().equalsIgnoreCase("S")) {
		    		    if (!canvas[myState.getX()][y1plus].getPresence() && 
			    		        !canvas[myState.getX()][y2plus].getPresence() &&
			    		        !canvas[myState.getX()][y3plus].getPresence()) {
			    		    return recordCommand("F");    	
			    		}		    			
		    		}
		    		else {
		    			return recordCommand(lr_rand);
		    		}
		    		
		    	}
		    	
	    		// top or bottom border
		    	if ((int)myState.getY()==0 || (int)myState.getY()==max_y) {
		    		if (myState.getDirection().equalsIgnoreCase("W")) {
		    		    if (!canvas[x1minus][myState.getY()].getPresence() && 
		    		        !canvas[x2minus][myState.getY()].getPresence() &&
		    		        !canvas[x3minus][myState.getY()].getPresence()) {
		    		        return recordCommand("F");    	
		    		    }
		    		}
		    		else if (myState.getDirection().equalsIgnoreCase("E")) {
		    		    if (!canvas[x1plus][myState.getY()].getPresence() && 
			    		        !canvas[x2plus][myState.getY()].getPresence() &&
			    		        !canvas[x3plus][myState.getY()].getPresence()) {
			    		    return recordCommand("F");    	
			    		}		    			
		    		}
		    		else {
		    			return recordCommand(lr_rand);
		    		}
		    		
		    	}
	
	    	}
	    	
	    	if ((new Random().nextInt(2))==0) {
			    System.out.println("Check Was Hit (rule3): not at border and random random");	
       		    return recordCommand(commands);
	    	}	
	    	
		    ////////////////////
		    // If was hit (rule2), move forward
		    ////////////////////
		    System.out.println("Check Was Hit (rule4): move forward");	
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		if (!canvas[myState.getX()][y1minus].getPresence())  {
	    	        return recordCommand("F");
	    		}
	    		else { 
	    			return recordCommand(lr_rand);
	    		}
	    	case "E": 
	    		if (!canvas[x1plus][myState.getY()].getPresence()) { 
	    	        return recordCommand("F");
	    		}
	    		else { 
	    			return lr_rand;
	    		}
	    	case "S":
	    		if (!canvas[myState.getX()][y1plus].getPresence()) {
	    	        return recordCommand("F");
	    		}
	    		else { 
	    			return recordCommand(lr_rand);
	    		}
	    	case "W":
	    		if (!canvas[x1minus][myState.getY()].getPresence()) { 
	    	        return recordCommand("F");
	    		}
	    		else { 
	    			return recordCommand(lr_rand);
	    		}
	    	default:
	    	    return recordCommand(commands);	
	    	}

     	
	    }
	    
	    ////////////////////
	    // corners
	    ////////////////////
		System.out.println("Check Corners");
	    if ((int)myState.getX()==0 && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return recordCommand("R");
	    	case "E":
	    		if (canvas[1][0].getPresence() || canvas[2][0].getPresence() || canvas[3][0].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "S":
	    		if (canvas[0][1].getPresence() || canvas[0][2].getPresence() || canvas[0][3].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "W":
	    		return recordCommand("L");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
	    int x1,x2,x3;
	    int y1,y2,y3;
	    
	    if ((int)myState.getX()==max_x && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return recordCommand("L");
	    	case "E":
	    		return recordCommand("R");
	    	case "S":
	    		if (canvas[max_x][1].getPresence() || canvas[max_x][2].getPresence() || canvas[max_x][3].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "W":
	    		x1=max_x-1;
	    		x2=max_x-2;
	    		x3=max_x-3;
	    		if (x2<0) x2=x1;
	    		if (x3<0) x3=x2;
	    		
	    		if (canvas[x1][0].getPresence() || canvas[x2][0].getPresence() || canvas[x3][0].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
	    if ((int)myState.getX()==0 && (int)myState.getY()==max_y) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		y1=max_y-1;
	    		y2=max_y-2;
	    		y3=max_y-3;
	    		if (y2<0) y2=y1;
	    		if (y3<0) y3=y2;
	    		
	    		if (canvas[0][y1].getPresence() || canvas[0][y2].getPresence() || canvas[0][y3].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "E":
	    		if (canvas[1][max_y].getPresence() || canvas[2][max_y].getPresence() || canvas[3][max_y].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "S":
	    		return recordCommand("L");
	    	case "W":
	    		return recordCommand("R");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
	    if ((int)myState.getX()==max_x && (int)myState.getY()==max_y) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		y1=max_y-1;
	    		y2=max_y-2;
	    		y3=max_y-3;
	    		if (y2<0) y2=y1;
	    		if (y3<0) y3=y2;
	    		
	    		if (canvas[max_x][y1].getPresence() || canvas[max_x][y2].getPresence() || canvas[max_x][y3].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "E":
    			return recordCommand("L");
	    	case "S":
	    		return recordCommand("R");
	    	case "W":
	    		x1=max_x-1;
	    		x2=max_x-2;
	    		x3=max_x-3;
	    		if (x2<0) x2=x1;
	    		if (x3<0) x3=x2;
	    		
	    		if (canvas[x1][max_y].getPresence() || canvas[max_x-2][x2].getPresence() || canvas[x3][max_y].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
	    ////////////////////
	    // borders
	    ////////////////////
	    System.out.println("Check atBorder:"+atBorder);
	    
	    if (atBorder) {
		    if ((int)myState.getX()==0 && myState.getDirection().equalsIgnoreCase("W")) {
		    	return recordCommand(lr_rand);
		    }
		    if ((int)myState.getX()==max_x && myState.getDirection().equalsIgnoreCase("E")) {
		    	return recordCommand(lr_rand);
		    }
		    if ((int)myState.getY()==0 && myState.getDirection().equalsIgnoreCase("N")) {
		    	return recordCommand(lr_rand);
		    }
		    if ((int)myState.getY()==max_y && myState.getDirection().equalsIgnoreCase("S")) {
		    	return recordCommand(lr_rand);
		    }
	    }

	    ////////////////////
	    // Throw players
	    ////////////////////
	    


		System.out.println("Check Throw players");
	    if (myState.getDirection().equalsIgnoreCase("N") && 
	    		(canvas[x1minus][myState.getY()].getPresence() ||
	    		 canvas[x2minus][myState.getY()].getPresence() ||
	    		 canvas[x3minus][myState.getY()].getPresence())) {
	    	return recordCommand("T");
	    }
	    if (myState.getDirection().equalsIgnoreCase("E") && 
	    		(canvas[myState.getX()][y1plus].getPresence() ||
	    		 canvas[myState.getX()][y2plus].getPresence() ||
	    		 canvas[myState.getX()][y3plus].getPresence())) {
	    	return recordCommand("T");
	    }
	    if (myState.getDirection().equalsIgnoreCase("S") && 
	    		(canvas[x1plus][myState.getY()].getPresence() ||
	   	    	 canvas[x2plus][myState.getY()].getPresence() ||
	   	    	 canvas[x3plus][myState.getY()].getPresence())) {
	    	return recordCommand("T");
	    }
	    if (myState.getDirection().equalsIgnoreCase("W") && 
	    		(canvas[myState.getX()][y1minus].getPresence() ||
	    		 canvas[myState.getX()][y2minus].getPresence() ||
	    		 canvas[myState.getX()][y3minus].getPresence())) {
	    	return recordCommand("T");
	    }
	    
	    ////////////////////
	    // If was not hit, L or R or F
	    ////////////////////
	    System.out.println("Check Was Not Hit");
	    if (!myState.getWasHit()) {
	    	return recordCommand(lrf_rand);
	    }
	    

	    
	    System.out.println("Default");
	    
    }
    catch (Exception e) {
	    System.out.println("Exception:"+e);
    	return recordCommand(commands);
    }

    return recordCommand(commands);
  }

}

