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

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("Max health must be positive.");
        }
        if (attack < 0 || defense < 0 || speed < 0) {
            throw new IllegalArgumentException("Stats cannot be negative.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }

        this.name = name.trim();
        this.type = type;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        if (currentHealth < 0) {
            this.currentHealth = 0;
        } else if (currentHealth > maxHealth) {
            this.currentHealth = maxHealth;
        } else {
            this.currentHealth = currentHealth;
        }
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        if (attack < 0) {
            throw new IllegalArgumentException("Attack stat cannot be negative.");
        }
        this.attack = attack;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        if (defense < 0) {
            throw new IllegalArgumentException("Defense stat cannot be negative.");
        }
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isFainted() {
        return currentHealth <= 0;
    }

    public void healToFull() {
        this.currentHealth = this.maxHealth;
    }

    public void takeDamage(double amount) {
        if (amount < 0) {
            return;
        }
        setCurrentHealth(currentHealth - amount);
    }

    public abstract String battleCry();

    @Override
    public String toString() {
        return String.format(
                "%s [Type: %s, HP: %.1f/%.1f, Atk: %.1f, Def: %.1f, Spd: %d]",
                name, type, currentHealth, maxHealth, attack, defense, speed
        );
    }
}
