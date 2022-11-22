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
    System.out.println(arenaUpdate);
    String[] commands = new String[]{"F", "R", "L", "T"};
    int i = new Random().nextInt(4);
    
    // TODO add your implementation here to replace the random response. 
    String myHref=arenaUpdate.get_links().getSelf().getHref();
	Arena arena=arenaUpdate.getArena();
	Integer dim_x=arena.getDims().get(0);
	Integer dim_y=arena.getDims().get(1);
	
	System.out.println("myHref="+myHref);
	System.out.println("dim=("+dim_x+","+dim_y+")");
	int j=0;
    for (String key : arena.getState().keySet()) {
        System.out.println("key["+j+"]: " + key + " " + arena.getState().get(key).toString());
        j++;
    }

    Integer x;
    Integer y;
    String direction;
    Boolean wasHit;
    Integer score;
	
    return commands[i];
  }

}

