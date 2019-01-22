import java.util.Random;
import java.util.Scanner;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class RPG {

 private static class Character implements Serializable {

  private static final long serialVersionUID = 1L;
  protected String name;
  protected int health;
  protected int damage;

  public Character(final String name, final int health, final int damage) {
   this.name = name;
   this.health = health;
   this.damage = damage;
  }

  public void attack(final Character character) {
   final int damage = getDamage();
   if (this instanceof Player) {
    System.out.println("\t> You strike the " + character + " for " + damage + " damage.");
   } else {
    System.out.println("\t> The " + this + " hits you for " + damage + ".");
   }
   character.damage(damage);
  }

  public void damage(final int damage) {
   health -= damage;
  }

  public int getDamage() {
   return rand.nextInt(damage);
  }

  public int getHealth() {
   return health;
  }

  public boolean isAlive() {
   return health > 0;
  }

  public boolean isDying() {
   return !isAlive();
  }

  @Override
  public String toString() {
   return name;
  }

 }

 private static class Enemy extends Character {

  private static final String[] ENEMIES = {
   "Thanos",
   "Loki",
   "Ultron"
  };
  private static final int MAX_HEALTH = 50;
  private static final int DAMAGE = 25;

  public final static Enemy spawnRandomEnemy() {
   return new Enemy(ENEMIES[rand.nextInt(ENEMIES.length)], rand.nextInt(MAX_HEALTH), DAMAGE);
  }

  public Enemy(final String name, final int health, final int damage) {
   super(name, health, damage);
  }
 }

 private final static class Player extends Character {

  public Player(final String name, final int health, final int damage) {
   super(name, health, damage);
  }
 }

 private static final String ATTACK = "1";
 private static final String RUN = "2";
 private static final Random rand = new Random();

 public static void main(final String[] args) {
  Player player = null;
  try (final Scanner in = new Scanner(System.in)) {

   System.out.println("Welcome to the world of Avengers");
   System.out.println("------------------------------------------");
   System.out.println("\t>What would you like to do now?");
   System.out.println("\t>1. Resume previous game");
   System.out.println("\t>2. Create new Player");
   String input = in .nextLine();
   while (!input.equals("1") && !input.equals("2")) {
    System.out.println("\t>Invalid command!");
    input = in .nextLine();
   }
   if (input.equals("1")) {
    try (ObjectInputStream ObjectInputStream = new ObjectInputStream(new FileInputStream("player.ser"))) {
     player = (Player) ObjectInputStream.readObject();
     System.out.println("\t>Game resumed successfully");
    } catch (Exception ex) {
     ex.printStackTrace();
    }
   } else {
    System.out.println("\n\tEnter Player name");
    final String name = in .nextLine();
    player = new Player(name, 125, 50);
    System.out.println("\n\t" + player + " has been created");
   }
   while (player.isAlive()) {
    System.out.println("------------------------------------------");
    final Enemy enemy = Enemy.spawnRandomEnemy();
    System.out.println("\t# " + enemy + " appeared! #\n");

    while (enemy.isAlive()) {
     System.out.println("\t" + player + "'s HP: " + player.health);
     System.out.println("\t" + enemy + "'s HP: " + enemy.health);
     System.out.println("\n\tWhat would you like to do?");
     System.out.println("\t1. Attack");
     System.out.println("\t2. Run!");
     input = in .nextLine();
     if (input.equals(ATTACK)) {
      player.attack(enemy);
      enemy.attack(player);
      if (player.isDying()) {
       System.out.println("\t> " + player + " has taken too much damage, " + player + " is too weak to go on!");
       break;
      }
     } else if (input.equals(RUN)) {
      System.out.println("\t> " + player + " ran away from the " + enemy + "!");
      continue;
     } else {
      System.out.println("\t>Invalid command");
     }
    }
    if (enemy.isDying()) {
     System.out.println("------------------------------------------");
     System.out.println("\t> # " + player + " has " + player.getHealth() + " HP left. #");
    }
    if (player.isAlive()) {
     System.out.println("------------------------------------------");
     System.out.println("\t>What would you like to do now?");
     System.out.println("\t>1. Continue fighting");
     System.out.println("\t>2. Save and resume later");
     System.out.println("\t>3. Exit the world of Avengers");
     input = in .nextLine();
     while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
      System.out.println("\t>Invalid command!");
      input = in .nextLine();
     }
     if (input.equals("1")) {
      System.out.println("\t>Continue on your adventure!");
     } else if (input.equals("2")) {
      try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("player.ser"))) {
       objectOutputStream.writeObject(player);
       System.out.println("\t>Game saved successfully");
      } catch (Exception ex) {
       ex.printStackTrace();
      }
      System.out.println("\t>You exited successfully");
      break;
     } else if (input.equals("3")) {
      System.out.println("\t>You exited successfully");
      break;
     }
    }
   }
   if (player.isDying()) {
    System.out.println("------------------------------------------");
    System.out.println("\t>You are too weak to continue fighting.");
   }
   System.out.println("#######################");
   System.out.println("\t#> THANKS FOR PLAYING! #\n");
  }

 }
}