public abstract class Pokemon {

    protected String name;
    protected Type type;
    protected double maxHealth;
    protected double currentHealth;
    protected double attack;
    protected double defense;
    protected int speed;

    public Pokemon(String name, Type type, double maxHealth,
                   double attack, double defense, int speed) {

        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name.trim();

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = type;

        if (maxHealth <= 0) {
            throw new IllegalArgumentException("maxHealth must be greater than 0.");
        }
        this.maxHealth = maxHealth;
       
        this.currentHealth = maxHealth;

        if (attack < 0) {
            throw new IllegalArgumentException("Attack cannot be negative.");
        }
        this.attack = attack;

        if (defense < 0) {
            throw new IllegalArgumentException("Defense cannot be negative.");
        }
        this.defense = defense;

        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative.");
        }
        this.speed = speed;
    }

    public boolean isFainted() {
        return currentHealth <= 0;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double hp) {
        if (hp < 0) hp = 0;
        if (hp > maxHealth) hp = maxHealth;
        this.currentHealth = hp;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Type getType() {
        return type;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }

    public abstract String battleCry();

    @Override
    public String toString() {
        return String.format(
                "%s [Type: %s, HP: %.1f/%.1f, Atk: %.1f, Def: %.1f, Spd: %d]",
                name, type, currentHealth, maxHealth, attack, defense, speed);
    }
}

