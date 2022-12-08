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

	private int x1minus;
	private int x2minus;
	private int x3minus;

	
	private int x1plus;
	private int x2plus;
	private int x3plus;

	private int y1minus;
	private int y2minus;
	private int y3minus;

	private int y1plus;
	private int y2plus;
	private int y3plus;
	private int max_x,max_y;
	
    int maxx1minus,maxx2minus,maxx3minus;
    int maxy1minus,maxy2minus,maxy3minus;
	
	private Player[][] canvas; 
	private PlayerState myState;
	
    private String all_rand,lr_rand,lrf_rand,lrf_rand2,commands;

	
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
//	  System.out.println("Return command:"+commands);
	  return commands;
  }
  
  public String throwPlayers() {
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
	    return "";
	    
  }
  
  public String findNextTarget() {
	
	int left_loc=0,right_loc=0,behind_loc=0;
	
  	switch (myState.getDirection() ) {
  	case "N":
  		 // Left Side
		 if (canvas[x1minus][myState.getY()].getPresence())
			 left_loc=1;
		 else if (canvas[x2minus][myState.getY()].getPresence())
			 left_loc=2;	
		 else if (canvas[x3minus][myState.getY()].getPresence())
			 left_loc=3;
		 
		 // Right Side
		 if (canvas[x1plus][myState.getY()].getPresence())
			 right_loc=1;
		 else if (canvas[x2plus][myState.getY()].getPresence())
			 right_loc=2;	
		 else if (canvas[x3plus][myState.getY()].getPresence())
			 right_loc=3;
	
		// Behind
		 if (canvas[myState.getX()][y1plus].getPresence())
			 behind_loc=1;
		 else if (canvas[myState.getX()][y2plus].getPresence())
			 behind_loc=2;	
		 else if (canvas[myState.getX()][y3plus].getPresence())
			 behind_loc=3;
		 
	     break;
  	case "E":
 		 // Left Side
		 if (canvas[myState.getX()][y1minus].getPresence())
			 left_loc=1;
		 else if (canvas[myState.getX()][y2minus].getPresence())
			 left_loc=2;	
		 else if (canvas[myState.getX()][y3minus].getPresence())
			 left_loc=3;
		 
		 // Right Side
		 if (canvas[myState.getX()][y1plus].getPresence())
			 left_loc=1;
		 else if (canvas[myState.getX()][y2plus].getPresence())
			 left_loc=2;	
		 else if (canvas[myState.getX()][y3plus].getPresence())
			 left_loc=3;
	
		// Behind
		 if (canvas[x1minus][myState.getY()].getPresence())
			 behind_loc=1;
		 else if (canvas[x2minus][myState.getY()].getPresence())
			 behind_loc=2;	
		 else if (canvas[x3minus][myState.getY()].getPresence())
			 behind_loc=3;
  		 break;
  	case "S":
 		 // Left Side
		 if (canvas[x1plus][myState.getY()].getPresence())
			 left_loc=1;
		 else if (canvas[x2plus][myState.getY()].getPresence())
			 left_loc=2;	
		 else if (canvas[x3plus][myState.getY()].getPresence())
			 left_loc=3;
		 
		 // Right Side
		 if (canvas[x1minus][myState.getY()].getPresence())
			 right_loc=1;
		 else if (canvas[x2minus][myState.getY()].getPresence())
			 right_loc=2;	
		 else if (canvas[x3minus][myState.getY()].getPresence())
			 right_loc=3;
	
		// Behind
		 if (canvas[myState.getX()][y1minus].getPresence())
			 behind_loc=1;
		 else if (canvas[myState.getX()][y2minus].getPresence())
			 behind_loc=2;	
		 else if (canvas[myState.getX()][y3minus].getPresence())
			 behind_loc=3;
  		 break;
  	case "W":
		 // Left Side
		 if (canvas[myState.getX()][y1plus].getPresence())
			 left_loc=1;
		 else if (canvas[myState.getX()][y2plus].getPresence())
			 left_loc=2;	
		 else if (canvas[myState.getX()][y3plus].getPresence())
			 left_loc=3;
		 
		 // Right Side
		 if (canvas[myState.getX()][y1minus].getPresence())
			 left_loc=1;
		 else if (canvas[myState.getX()][y2minus].getPresence())
			 left_loc=2;	
		 else if (canvas[myState.getX()][y3minus].getPresence())
			 left_loc=3;
	
		// Behind
		 if (canvas[x1plus][myState.getY()].getPresence())
			 behind_loc=1;
		 else if (canvas[x2plus][myState.getY()].getPresence())
			 behind_loc=2;	
		 else if (canvas[x3plus][myState.getY()].getPresence())
			 behind_loc=3;
 		 break;
  	default:
  		 return "";
  	}

	 if (left_loc==0 && right_loc==0 && behind_loc==0)
		 return "";
	 
	 if (left_loc != 0) {
		 if (right_loc == 0 || left_loc <= right_loc) {
			 return recordCommand("L");
		 }
		 if (left_loc > right_loc) {
			 return recordCommand("R");
		 }
	 }
	 
	 if (behind_loc != 0) {
		 return recordCommand("L");
	 }
	 
	 return "";
  }
  
  
  public String handleHitTheHitter() {
		
		int left_loc=0,right_loc=0,behind_loc=0;
		boolean [] command_tf = new boolean[]{true,true,false};
		boolean tf_rand = command_tf[new Random().nextInt(3)]; // true or false to throw or not throw
		    
		    
	  	switch (myState.getDirection() ) {
	  	case "N":

	  		 // Hitter in the front?
			 if (myState.getY()!=0 &&
			     (canvas[myState.getX()][y1minus].getPresence() && canvas[myState.getX()][y1minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y2minus].getPresence() && canvas[myState.getX()][y2minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y3minus].getPresence() && canvas[myState.getX()][y3minus].getDirection().equalsIgnoreCase("S"))) {
			    if (tf_rand) {
			    	return "T";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the left?
			 if (myState.getX()!=0 &&
				     (canvas[x1minus][myState.getY()].getPresence() && canvas[x1minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
					  canvas[x2minus][myState.getY()].getPresence() && canvas[x2minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
					  canvas[x3minus][myState.getY()].getPresence() && canvas[x3minus][myState.getY()].getDirection().equalsIgnoreCase("E"))) {
		    if (tf_rand) {
			    	return "L";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the right?
			 if (myState.getX()!=max_x &&
				     (canvas[x1plus][myState.getY()].getPresence() && canvas[x1plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
					  canvas[x2plus][myState.getY()].getPresence() && canvas[x2plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
					  canvas[x3plus][myState.getY()].getPresence() && canvas[x3plus][myState.getY()].getDirection().equalsIgnoreCase("W"))) {
		    if (tf_rand) {
			    	return "R";
			    }
			    else {
			    	return "";
			    }
			 }
			 break;
	  	case "S":
	  		 
	  	     // Hitter in the front?
			 if (myState.getY()!=max_y &&
			     (canvas[myState.getX()][y1plus].getPresence() && canvas[myState.getX()][y1plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y2plus].getPresence() && canvas[myState.getX()][y2plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y3plus].getPresence() && canvas[myState.getX()][y3plus].getDirection().equalsIgnoreCase("N"))) {
			    if (tf_rand) {
			    	return "T";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the left?
			 if (myState.getX()!=max_x &&
				     (canvas[x1plus][myState.getY()].getPresence() && canvas[x1plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
					  canvas[x2plus][myState.getY()].getPresence() && canvas[x2plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
					  canvas[x3plus][myState.getY()].getPresence() && canvas[x3plus][myState.getY()].getDirection().equalsIgnoreCase("W"))) {
		    if (tf_rand) {
			    	return "L";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the right?
			 if (myState.getX()!=0 &&
				     (canvas[x1minus][myState.getY()].getPresence() && canvas[x1minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
					  canvas[x2minus][myState.getY()].getPresence() && canvas[x2minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
					  canvas[x3minus][myState.getY()].getPresence() && canvas[x3minus][myState.getY()].getDirection().equalsIgnoreCase("E"))) {
		    if (tf_rand) {
			    	return "R";
			    }
			    else {
			    	return "";
			    }
			 }
			 break;
	  	case "W":
	  		 
	  	     // Hitter in the front?
			 if (myState.getX()!=0 &&
			     (canvas[x1minus][myState.getY()].getPresence() && canvas[x1minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
				 canvas[x2minus][myState.getY()].getPresence() && canvas[x2minus][myState.getY()].getDirection().equalsIgnoreCase("E") ||
				 canvas[x3minus][myState.getY()].getPresence() && canvas[x3minus][myState.getY()].getDirection().equalsIgnoreCase("E"))) {
			    if (tf_rand) {
			    	return "T";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  	     // Hitter in the left?
			 if (myState.getY()!=max_y &&
			     (canvas[myState.getX()][y1plus].getPresence() && canvas[myState.getX()][y1plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y2plus].getPresence() && canvas[myState.getX()][y2plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y3plus].getPresence() && canvas[myState.getX()][y3plus].getDirection().equalsIgnoreCase("N"))) {
			    if (tf_rand) {
			    	return "L";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the right?
			 if (myState.getY()!=0 &&
			     (canvas[myState.getX()][y1minus].getPresence() && canvas[myState.getX()][y1minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y2minus].getPresence() && canvas[myState.getX()][y2minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y3minus].getPresence() && canvas[myState.getX()][y3minus].getDirection().equalsIgnoreCase("S"))) {
			    if (tf_rand) {
			    	return "R";
			    }
			    else {
			    	return "";
			    }
			 }
			 break;
	  	case "E":
	  		 
	  	     // Hitter in the front?
			 if (myState.getX()!=max_x &&
			     (canvas[x1plus][myState.getY()].getPresence() && canvas[x1plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
				 canvas[x2plus][myState.getY()].getPresence() && canvas[x2plus][myState.getY()].getDirection().equalsIgnoreCase("W") ||
				 canvas[x3plus][myState.getY()].getPresence() && canvas[x3plus][myState.getY()].getDirection().equalsIgnoreCase("W"))) {
			    if (tf_rand) {
			    	return "T";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  		 // Hitter in the left?
			 if (myState.getY()!=0 &&
			     (canvas[myState.getX()][y1minus].getPresence() && canvas[myState.getX()][y1minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y2minus].getPresence() && canvas[myState.getX()][y2minus].getDirection().equalsIgnoreCase("S") ||
				 canvas[myState.getX()][y3minus].getPresence() && canvas[myState.getX()][y3minus].getDirection().equalsIgnoreCase("S"))) {
			    if (tf_rand) {
			    	return "L";
			    }
			    else {
			    	return "";
			    }
			 }
			 
	  	     // Hitter in the right?
			 if (myState.getY()!=max_y &&
			     (canvas[myState.getX()][y1plus].getPresence() && canvas[myState.getX()][y1plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y2plus].getPresence() && canvas[myState.getX()][y2plus].getDirection().equalsIgnoreCase("N") ||
				 canvas[myState.getX()][y3plus].getPresence() && canvas[myState.getX()][y3plus].getDirection().equalsIgnoreCase("N"))) {
			    if (tf_rand) {
			    	return "R";
			    }
			    else {
			    	return "";
			    }
			 }
			 break;
	    default:
	    	return "";

			 
	  	}
		return "";

   }
	  
  public String handleCorner() {
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
	    		if (canvas[maxx1minus][0].getPresence() || canvas[maxx2minus][0].getPresence() || canvas[maxx3minus][0].getPresence())
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
	    		if (canvas[0][maxy1minus].getPresence() || canvas[0][maxy2minus].getPresence() || canvas[0][maxy3minus].getPresence())
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
	    		if (canvas[max_x][maxy1minus].getPresence() || canvas[max_x][maxy2minus].getPresence() || canvas[max_x][maxy3minus].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	case "E":
    			return recordCommand("L");
	    	case "S":
	    		return recordCommand("R");
	    	case "W":
	    		if (canvas[maxx1minus][max_y].getPresence() || canvas[maxx2minus][max_y].getPresence() || canvas[maxx3minus][max_y].getPresence())
	    			return recordCommand("T");
	    		else
	    			return recordCommand("F");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    return "";
  }
  

  
public String handleCornerButHitted() {
	    if ((int)myState.getX()==0 && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return recordCommand("R");
	    	case "E":
	    		if (canvas[1][0].getPresence()) {
	    			if (canvas[1][0].getDirection().equalsIgnoreCase("W")) {
	    				if (tf_rand) {
	    			       return recordCommand("T");
	    				}
		    			else {
		    				if (canvas[0][1].getPresence()) {
		    					if (canvas[0][1].getDirection().equalsIgnoreCase("N")) {
			    				       return recordCommand("T");
		    					}
		    					else {
		    						return recordCommand("R");
		    					}
		    				}
		    				else {
		    					return recordCommand("R");	
		    				}
		    			}
	    			}
	    			else {
	    				return recordCommand("R");
	    			}
	    		}
	    		else {
	    			return recordCommand("F");
	    		}

	    	case "S":
	    		if (canvas[0][1].getPresence()) {
	    			if (canvas[max_x][1].getDirection().equalsIgnoreCase("N")) {
	    				if (tf_rand) {
		    			       return recordCommand("T");
		    			}
		    			else {
		    				if (canvas[1][0].getPresence()) {
		    					if (canvas[1][0].getDirection().equalsIgnoreCase("W")) {
			    				       return recordCommand("T");
		    					}
		    					else {
		    						return recordCommand("L");
		    					}
		    				}
		    				else {
		    					return recordCommand("L");	
		    				}
		    			}
		    		}
	       			else {
	    				return recordCommand("L");
	    			}
	    		}
	    		else {
	    			return recordCommand("F");
	    		}
	    		
	    	case "W":
	    		return recordCommand("L");
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
  
	    if ((int)myState.getX()==max_x && (int)myState.getY()==0) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		return recordCommand("L");
	    	case "E":
	    		return recordCommand("R");
	    	case "S":
	    		if (canvas[max_x][1].getPresence()) {
	    			if (canvas[max_x][1].getDirection().equalsIgnoreCase("N")) {
	    				if (tf_rand) {
		    			       return recordCommand("T");
		    			}
		    			else {
		    				if (canvas[maxx1minus][0].getPresence()) {
		    					if (canvas[maxx1minus][0].getDirection().equalsIgnoreCase("E")) {
			    				       return recordCommand("T");
		    					}
		    					else {
		    						return recordCommand("R");
		    					}
		    				}
		    				else {
		    					return recordCommand("R");	
		    				}
		    			}
		    		}
	       			else {
	    				return recordCommand("R");
	    			}
	    		}
	    		else {
	    			return recordCommand("F");
	    		}

	    	case "W":
	    		if (canvas[maxx1minus][0].getPresence()) {
	    			if (canvas[maxx1minus][0].getDirection().equalsIgnoreCase("E")) {
	    				if (tf_rand) {
	    			       return recordCommand("T");
	    				}
		    			else {
		    				if (canvas[max_x][1].getPresence()) {
		    					if (canvas[max_x][1].getDirection().equalsIgnoreCase("N")) {
			    				       return recordCommand("T");
		    					}
		    					else {
		    						return recordCommand("L");
		    					}
		    				}
		    				else {
		    					return recordCommand("L");	
		    				}
		    			}
	    			}
	    			else {
	    				return recordCommand("L");
	    			}
	    		}
	    		else {
	    			return recordCommand("F");
	    		}
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    
	    if ((int)myState.getX()==0 && (int)myState.getY()==max_y) {
	    	switch (myState.getDirection() ) {
	    	case "N":
	    		if (canvas[0][maxy1minus].getPresence()) {
	    			if (canvas[0][maxy1minus].getDirection().equalsIgnoreCase("S")) {
	    				if (tf_rand) {
	    			       return recordCommand("T");
	    				}
		    			else {
		    				if (canvas[1][max_y].getPresence()) {
		    					if (canvas[1][max_y].getDirection().equalsIgnoreCase("W")) {
			    				       return recordCommand("T");
		    					}
		    					else {
		    						return recordCommand("R");
		    					}
		    				}
		    				else {
		    					return recordCommand("R");	
		    				}
		    			}
	    			}
	    			else {
	    				return recordCommand("R");
	    			}
	    		}
	    		else {
	    			return recordCommand("F");
	    		}

	    	case "E":
		    		if (canvas[1][max_y].getPresence()) {
		    			if (canvas[1][max_y].getDirection().equalsIgnoreCase("W")) {
		    				if (tf_rand) {
		    			       return recordCommand("T");
		    				}
			    			else {
			    				if (canvas[0][y1minus].getPresence()) {
			    					if (canvas[0][y1minus].getDirection().equalsIgnoreCase("S")) {
				    				       return recordCommand("T");
			    					}
			    					else {
			    						return recordCommand("L");
			    					}
			    				}
			    				else {
			    					return recordCommand("L");	
			    				}
			    			}
		    			}
		    			else {
		    				return recordCommand("L");
		    			}
		    		}
		    		else {
		    			return recordCommand("F");
		    		}
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
		    		if (canvas[max_x][maxy1minus].getPresence()) {
		    			if (canvas[max_x][maxy1minus].getDirection().equalsIgnoreCase("S")) {
		    				if (tf_rand) {
			    			       return recordCommand("T");
			    			}
			    			else {
			    				if (canvas[x1minus][max_y].getPresence()) {
			    					if (canvas[x1minus][max_y].getDirection().equalsIgnoreCase("E")) {
				    				       return recordCommand("T");
			    					}
			    					else {
			    						return recordCommand("L");
			    					}
			    				}
			    				else {
			    					return recordCommand("L");	
			    				}
			    			}
			    		}
		       			else {
		    				return recordCommand("L");
		    			}
		    		}
		    		else {
		    			return recordCommand("F");
		    		}
	    	case "E":
  			return recordCommand("L");
	    	case "S":
	    		return recordCommand("R");
	    	case "W":
		    		if (canvas[maxx1minus][max_y].getPresence()) {
		    			if (canvas[maxx1minus][max_y].getDirection().equalsIgnoreCase("E")) {
		    				if (tf_rand) {
			    			       return recordCommand("T");
			    			}
			    			else {
			    				if (canvas[max_x][y1minus].getPresence()) {
			    					if (canvas[max_x][y1minus].getDirection().equalsIgnoreCase("S")) {
				    				       return recordCommand("T");
			    					}
			    					else {
			    						return recordCommand("R");
			    					}
			    				}
			    				else {
			    					return recordCommand("R");	
			    				}
			    			}
			    		}
		       			else {
		    				return recordCommand("R");
		    			}
		    		}
		    		else {
		    			return recordCommand("F");
		    		}
	    	default:
	    	    return recordCommand(commands);		
	    	}
	    }
	    return "";
 }
  
  
  public String moveForward() {
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
  
  public String borderActionButHitted() {
	// left or right border
  	if ((int)myState.getX()==0 || (int)myState.getX()==max_x) {
  		if (myState.getDirection().equalsIgnoreCase("N")) {
 		    if (!canvas[myState.getX()][y1minus].getPresence() ) {
  		        return recordCommand("F");    	
  		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("S")) {
  		    if (!canvas[myState.getX()][y1plus].getPresence()) {
	    		    return recordCommand("F");    	
	    		}		    			
  		}
  		else if (myState.getDirection().equalsIgnoreCase("E") && (int)myState.getX()==0) {
  		    if (!canvas[x1plus][myState.getY()].getPresence()) {
	    		    return recordCommand("F");    	
            }
  		    else {
  		    	return recordCommand(lr_rand);
  		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("W") && (int)myState.getX()==max_x) {
  		    if (!canvas[x1minus][myState.getY()].getPresence()) {
    		    return recordCommand("F");    	
            }
		    else {
		    	return recordCommand(lr_rand);
		    }
		}
  		else {
  			return recordCommand(lr_rand);
  		}
  		
  	}
  	
	// top or bottom border
  	if ((int)myState.getY()==0 || (int)myState.getY()==max_y) {
  		if (myState.getDirection().equalsIgnoreCase("W")) {
  		    if (!canvas[x1minus][myState.getY()].getPresence()) {
 		        return recordCommand("F");    	
  		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("E")) {
  		    if (!canvas[x1plus][myState.getY()].getPresence()) {
	    		    return recordCommand("F");    	
	    		}		    			
  		}
  		else if (myState.getDirection().equalsIgnoreCase("S") && (int)myState.getY()==0 ) {
  			if (!canvas[myState.getX()][y1plus].getPresence()) {
  				return recordCommand("F"); 
  			}
		    else {
		    	return recordCommand(lr_rand);
		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("N") && (int)myState.getY()==max_y)  {
  			if (!canvas[myState.getX()][y1minus].getPresence()) {
  				return recordCommand("F"); 
  			}
		    else {
		    	return recordCommand(lr_rand);
		    }
  		}
  				
  		else {
  			return recordCommand(lr_rand);
  		}
  		
  	}

  	return "";
  }
  
  public String borderAction() {
	// left or right border
  	if ((int)myState.getX()==0 || (int)myState.getX()==max_x) {
  		if (myState.getDirection().equalsIgnoreCase("N")) {
 //		    if (!canvas[myState.getX()][y1minus].getPresence() ) {
  		    if (!canvas[myState.getX()][y1minus].getPresence() && 
  		        !canvas[myState.getX()][y2minus].getPresence() &&
  		        !canvas[myState.getX()][y3minus].getPresence()) {
  		    
  		        return recordCommand("F");    	
  		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("S")) {
//  		    if (!canvas[myState.getX()][y1plus].getPresence()) {
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
//  		    if (!canvas[x1minus][myState.getY()].getPresence()) {
  		    if (!canvas[x1minus][myState.getY()].getPresence() && 
  		        !canvas[x2minus][myState.getY()].getPresence() &&
  		        !canvas[x3minus][myState.getY()].getPresence()) {
  		        
  		        return recordCommand("F");    	
  		    }
  		}
  		else if (myState.getDirection().equalsIgnoreCase("E")) {
//  		    if (!canvas[x1plus][myState.getY()].getPresence()) {
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

  	return "";
  }
  
  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
	  
	//System.out.println(arenaUpdate);
    String[] allCommands = new String[]{"F", "R", "L", "T"};
    String[] commandsLR = new String[]{"R", "L"};
    String[] commandsLRF = new String[]{"R", "L", "F"};
    String[] commandsLRF2 = new String[]{"F","R", "F","L", "F"};
    all_rand = allCommands[new Random().nextInt(4)]; // F, R, L or T
    lr_rand = commandsLR[new Random().nextInt(2)]; // L or R
    lrf_rand = commandsLRF[new Random().nextInt(3)]; // L or R or F
    lrf_rand2 = commandsLRF2[new Random().nextInt(5)]; // L or R or F x 3
    commands=all_rand;
    Boolean do_random=false;
    
    try {
	    // TODO add your implementation here to replace the random response. 
	    String myHref=arenaUpdate.get_links().getSelf().getHref();
		Arena arena=arenaUpdate.getArena();
		Integer width=arena.getDims().get(0);
		Integer height=arena.getDims().get(1);

//		System.out.println("myHref="+myHref);
//		System.out.println("dim=("+width+","+height+")");
		
		int w,h;
//		int max_x,max_y;
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
		
//		Player[][] canvas = new Player[w][h];
		canvas = new Player[w][h];
		
		// init canvas
		for (int x=0;x<w;x++) {
			for (int y=0;y<h;y++) {
				canvas[x][y]=new Player();
				canvas[x][y].setPresence(false);
			}
		}
		
//		System.out.println("No. of Players: "+arena.getState().size());
		// find my state
//		PlayerState myState = new PlayerState();
		myState = new PlayerState();
		int j=0;
	    for (String key : arena.getState().keySet()) {
//	    	System.out.println("key["+j+"]: " + key + " " + arena.getState().get(key).toString());
	    	if (key.equalsIgnoreCase(myHref)) {
//	    		System.out.println("get my state j=" + j);
	    		myState=arena.getState().get(key);	
	    	}
	    	else { // record players
//	    		System.out.println("get player state j=" + j);
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setPresence(true);
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setDirection(arena.getState().get(key).getDirection());
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setWasHit(arena.getState().get(key).getWasHit());
	    		canvas[arena.getState().get(key).getX()][arena.getState().get(key).getY()].setScore(arena.getState().get(key).getScore());
//	    		System.out.println("player["+j+"]: " + key + " " + arena.getState().get(key).toString());
	    	}
	      j++;
	    }
	    
    
		x1minus=myState.getX()-1;
		x2minus=myState.getX()-2;
		x3minus=myState.getX()-3;
		if (x1minus<0) x1minus=0;
		if (x2minus<0) x2minus=x1minus;
		if (x3minus<0) x3minus=x2minus;
		
		x1plus=myState.getX()+1;
		x2plus=myState.getX()+2;
		x3plus=myState.getX()+3;
		if (x1plus>max_x) x1plus=max_x;
		if (x2plus>max_x) x2plus=max_x;
		if (x3plus>max_x) x3plus=max_x;

		y1minus=myState.getY()-1;
		y2minus=myState.getY()-2;
		y3minus=myState.getY()-3;
		if (y1minus<0) y1minus=0;
		if (y2minus<0) y2minus=y1minus;
		if (y3minus<0) y3minus=y2minus;
		
		y1plus=myState.getY()+1;
		y2plus=myState.getY()+2;
		y3plus=myState.getY()+3;
		if (y1plus>max_y) y1plus=max_y;
		if (y2plus>max_y) y2plus=max_y;
		if (y3plus>max_y) y3plus=max_y;
		
		maxx1minus=max_x-1;
		maxx2minus=max_x-2;
		maxx3minus=max_x-3;
		if (maxx1minus<0) maxx1minus=0;
		if (maxx2minus<0) maxx2minus=maxx1minus;
		if (maxx3minus<0) maxx3minus=maxx2minus;
		
		maxy1minus=max_y-1;
		maxy2minus=max_y-2;
		maxy3minus=max_y-3;
		if (maxy1minus<0) maxy1minus=0;
		if (maxy2minus<0) maxy2minus=maxy1minus;
		if (maxy3minus<0) maxy3minus=maxy2minus;
		
//	    System.out.println("Check Borders");
	    Boolean atBorder=false;
	    if ((int)myState.getX()==0 || (int)myState.getX()==max_x || (int)myState.getY()==0 || (int)myState.getY()==max_y) 
              atBorder=true;
	    
	    Boolean atCorner=false;
	    if (((int)myState.getX()==0 && (int)myState.getY()==0) ||
	    		((int)myState.getX()==max_x && (int)myState.getY()==0) ||
	    		((int)myState.getX()==0 && (int)myState.getY()==max_y ) || 
	    		((int)myState.getX()==max_x && (int)myState.getY()==max_y))
           atCorner=true;
	    
//	    System.out.println("At Borders:"+atBorder+" at Corner:"+atCorner+" Direction:"+myState.getDirection());
	    
//		System.out.println("myState: "+myState.toString());
		
		String cmd;
		
		//random
		if (do_random)
	       return recordCommand(commands);
	    
		// Rule 0: already has target, throw players
//		System.out.println("Rule 0: try to throw players");
//		cmd=throwPlayers();
//		if (!cmd.equals(""))
//			return cmd;
		
//		if (myState.getWasHit()) {
//		    ////////////////////
//		    // was hit (rule 1):Throw players
//		    ////////////////////
//			System.out.println("Check was hit (rule 1): try to throw players");
//			cmd=throwPlayers();
//			if (!cmd.equals(""))
//				return cmd;
//	    
//		}

		//
        // If was hit, try to move forward
		//
	    if (myState.getWasHit()) {

	    	cmd=handleHitTheHitter();
			if (!cmd.equals(""))
				return cmd;
			
			if (atCorner) {
//	    		System.out.println("Was Hit (rule1): atCorner, try to move forward");
				cmd=handleCornerButHitted();
				if (!cmd.equals(""))
					return cmd;
			}
			
	    	if (atBorder) {
//	    		System.out.println("Was Hit (rule2): atBorder, try to move forward");
	    		cmd=borderActionButHitted();
				if (!cmd.equals(""))
					return cmd;
	
	    	}
	
//		    System.out.println("Was Hit (rule3): not at Corner nor border, try to move forward");
		    cmd=moveForward();
			if (!cmd.equals(""))
				return cmd;
			
			// if all above can't find a good move, do random random
	    	if ((new Random().nextInt(2))==0) {
//			    System.out.println("Was Hit (rule4): random");	
       		    return recordCommand(commands);
	    	}	
 	
	    }
	    
		// Rule 5: already has target, throw players if exist
//		System.out.println("Rule 5: try to throw players if exist");
		cmd=throwPlayers();
		if (!cmd.equals(""))
			return cmd;
		
	    ////////////////////
	    // Rule 6: move base on target
	    ////////////////////   
//		System.out.println("Rule 6: Check any target and move base on target");
	    cmd=findNextTarget();
		if (!cmd.equals(""))
			return cmd;
		
	    ////////////////////
	    // Rule 7: corner movement
	    ////////////////////
//		System.out.println("Rule 7: move at corners");
		if (atCorner) {
		    cmd=handleCorner();
			if (!cmd.equals(""))
				return cmd;
		}
	    
	    ////////////////////
	    // Rule 8: borders
	    ////////////////////
//	    System.out.println("Rule 8: Check atBorder:"+atBorder);
	    
	    if (atBorder) {
    		cmd=borderAction();
			if (!cmd.equals(""))
				return cmd;
	    }

	    
//	    System.out.println("Rule 9: Default L, R or Fx3");
	    return recordCommand(lrf_rand2);
	    
    }
    catch (Exception e) {
//	    System.out.println("Exception:"+e);
    	return recordCommand(commands);
    }

  }

}

