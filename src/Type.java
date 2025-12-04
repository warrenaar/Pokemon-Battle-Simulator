public enum Type {

    NORMAL,
    FIRE,
    WATER,
    GRASS,
    FLYING,
    FIGHTING,
    POISON,
    ELECTRIC,
    GROUND,
    ROCK,
    PSYCHIC,
    ICE;

    public double effectivenessAgainst(Type defender) {

        if (this == FIRE) {
            if (defender == GRASS || defender == ICE) return 2.0;
            if (defender == FIRE || defender == WATER || defender == ROCK) return 0.5;
        }
        if (this == WATER) {
            if (defender == FIRE || defender == ROCK || defender == GROUND) return 2.0;
            if (defender == WATER || defender == GRASS) return 0.5;
        }
        if (this == GRASS) {
            if (defender == WATER || defender == ROCK || defender == GROUND) return 2.0;
            if (defender == FIRE || defender == GRASS || defender == POISON || defender == FLYING) return 0.5;
        }
        if (this == ELECTRIC) {
            if (defender == WATER || defender == FLYING) return 2.0;
            if (defender == ELECTRIC || defender == GRASS) return 0.5;
            if (defender == GROUND) return 0.0;
        }
        if (this == FIGHTING) {
            if (defender == NORMAL || defender == ROCK || defender == ICE ) return 2.0;
            if (defender == FLYING || defender == POISON || defender == PSYCHIC) return 0.5;
        }
        if (this == ROCK) {
            if (defender == FIRE || defender == FLYING || defender == ICE) return 2.0;
            if (defender == FIGHTING || defender == GROUND) return 0.5;
        }
        if (this == ICE) {
            if (defender == GRASS  || defender == FLYING) return 2.0;
            if (defender == FIRE || defender == WATER) return 0.5;
        }
        if(this == NORMAL) {
        	if (defender==ROCK) return 2.0;
        }
        if(this == POISON) {
        	if(defender == GRASS)return 2.0;
        	if(defender== ROCK || defender== GROUND)return 0.5;
        }
        if(this == GROUND) {
        	if(defender == FIRE || defender == ELECTRIC || defender== POISON)return 2.0;
        	if(defender== GRASS)return 0.5;
        }
        if(this == FLYING) {
        	if(defender == GRASS || defender == FIGHTING)return 2.0;
        	if(defender == ELECTRIC || defender == ROCK )return 0.5;
        }
        if(this == PSYCHIC) {
        	if(defender == FIGHTING || defender == POISON)return 2.0;
        	if(defender == PSYCHIC)return 0.5;
        }
        // everything else neutral
        return 1.0;
    }
}
