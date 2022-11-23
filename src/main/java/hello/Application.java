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
	    	}
	      j++;
	    }
	
		System.out.println("myState: "+myState.toString());
		
	    ////////////////////
	    // corners
	    ////////////////////
		System.out.println("Check Corners");
	    if ((int)myState.getX()==0 && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return "R";
	    	case "E":
	    		if (canvas[1][0].getPresence() || canvas[2][0].getPresence() || canvas[3][0].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "S":
	    		if (canvas[0][1].getPresence() || canvas[0][2].getPresence() || canvas[0][3].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "W":
	    		return "L";
	    	default:
	    	    return commands;		
	    	}
	    }
	    
	    if ((int)myState.getX()==max_x && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return "L";
	    	case "E":
	    		return "R";
	    	case "S":
	    		if (canvas[max_x][1].getPresence() || canvas[max_x][2].getPresence() || canvas[max_x][3].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "W":
	    		if (canvas[max_x-1][0].getPresence() || canvas[max_x-2][0].getPresence() || canvas[max_x-3][0].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	default:
	    	    return commands;		
	    	}
	    }
	    
	    if ((int)myState.getX()==0 && (int)myState.getY()==max_y) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		if (canvas[0][max_y-1].getPresence() || canvas[0][max_y-2].getPresence() || canvas[0][max_y-3].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "E":
	    		if (canvas[1][max_y].getPresence() || canvas[2][max_y].getPresence() || canvas[3][max_y].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "S":
	    		return "L";
	    	case "W":
	    		return "R";
	    	default:
	    	    return commands;		
	    	}
	    }
	    
	    if ((int)myState.getX()==max_x && (int)myState.getY()==max_y) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		if (canvas[max_x][max_y-1].getPresence() || canvas[max_x][max_y-2].getPresence() || canvas[max_x][max_y-3].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	case "E":
    			return "L";
	    	case "S":
	    		return "R";
	    	case "W":
	    		if (canvas[max_x-1][max_y].getPresence() || canvas[max_x-2][max_y].getPresence() || canvas[max_x-3][max_y].getPresence())
	    			return "T";
	    		else
	    			return "F";
	    	default:
	    	    return commands;		
	    	}
	    }
	    
	    ////////////////////
	    // borders
	    ////////////////////
	    System.out.println("Check Borders");
	    if ((int)myState.getX()==0 && myState.getDirection().equalsIgnoreCase("W")) {
	    	return lr_rand;
	    }
	    if ((int)myState.getX()==max_x && myState.getDirection().equalsIgnoreCase("E")) {
	    	return lr_rand;
	    }
	    if ((int)myState.getY()==0 && myState.getDirection().equalsIgnoreCase("N")) {
	    	return lr_rand;
	    }
	    if ((int)myState.getY()==max_y && myState.getDirection().equalsIgnoreCase("S")) {
	    	return lr_rand;
	    }
	    

	    ////////////////////
	    // Throw players
	    ////////////////////
	    System.out.println("Check Throw players");
	    if (myState.getDirection().equalsIgnoreCase("N") && 
	    		(canvas[myState.getX()-1][myState.getY()].getPresence() ||
	    		 canvas[myState.getX()-2][myState.getY()].getPresence() ||
	    		 canvas[myState.getX()-3][myState.getY()].getPresence())) {
	    	return "T";
	    }
	    if (myState.getDirection().equalsIgnoreCase("E") && 
	    		(canvas[myState.getX()][myState.getY()+1].getPresence() ||
	    		 canvas[myState.getX()][myState.getY()+2].getPresence() ||
	    		 canvas[myState.getX()][myState.getY()+3].getPresence())) {
	    	return "T";
	    }
	    if (myState.getDirection().equalsIgnoreCase("S") && 
	    		(canvas[myState.getX()+1][myState.getY()].getPresence() ||
	   	    	 canvas[myState.getX()+2][myState.getY()].getPresence() ||
	   	    	 canvas[myState.getX()+3][myState.getY()].getPresence())) {
	    	return "T";
	    }
	    if (myState.getDirection().equalsIgnoreCase("W") && 
	    		(canvas[myState.getX()][myState.getY()-1].getPresence() ||
	    		 canvas[myState.getX()][myState.getY()-2].getPresence() ||
	    		 canvas[myState.getX()][myState.getY()-3].getPresence())) {
	    	return "T";
	    }
	    
	    ////////////////////
	    // If was not hit, L or R or F
	    ////////////////////
	    System.out.println("Check Was Not Hit");
	    if (!myState.getWasHit()) {
	    	return lrf_rand;
	    }
	    
	    ////////////////////
	    // If was hit, move forward
	    ////////////////////
	    System.out.println("Check Was Hit");
	    if (myState.getWasHit()) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		if (!canvas[myState.getX()][myState.getY()-1].getPresence()) 
	    	        return "F";
	    		else 
	    			return lr_rand;
	    	case "E":
	    		if (!canvas[myState.getX()+1][myState.getY()].getPresence()) 
	    	        return "F";
	    		else 
	    			return lr_rand;
	    	case "S":
	    		if (!canvas[myState.getX()][myState.getY()+1].getPresence()) 
	    	        return "F";
	    		else 
	    			return lr_rand;
	    	case "W":
	    		if (!canvas[myState.getX()-1][myState.getY()].getPresence()) 
	    	        return "F";
	    		else 
	    			return lr_rand;
	    	default:
	    	    return commands;	
	    	}

	    }
	    
	    System.out.println("Default");
	    
    }
    catch (Exception e) {
    	return commands;
    }

    return commands;
  }

}

