public enum Type {

    NORMAL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    DARK,
    STEEL,
    FAIRY;

    // Simple effectiveness chart. This does not cover every interaction,
    // but it gives you super effective, not very effective, and neutral.
    public double effectivenessAgainst(Type defender) {

        // fire
        if (this == FIRE) {
            if (defender == GRASS || defender == ICE || defender == BUG || defender == STEEL) {
                return 2.0;
            }
            if (defender == FIRE || defender == WATER || defender == ROCK || defender == DRAGON) {
                return 0.5;
            }
        }

        // water
        if (this == WATER) {
            if (defender == FIRE || defender == ROCK || defender == GROUND) {
                return 2.0;
            }
            if (defender == WATER || defender == GRASS || defender == DRAGON) {
                return 0.5;
            }
        }

        // grass
        if (this == GRASS) {
            if (defender == WATER || defender == ROCK || defender == GROUND) {
                return 2.0;
            }
            if (defender == FIRE || defender == GRASS || defender == POISON || defender == FLYING
                    || defender == BUG || defender == DRAGON || defender == STEEL) {
                return 0.5;
            }
        }

        // electric
        if (this == ELECTRIC) {
            if (defender == WATER || defender == FLYING) {
                return 2.0;
            }
            if (defender == ELECTRIC || defender == GRASS || defender == DRAGON) {
                return 0.5;
            }
            if (defender == GROUND) {
                return 0.0; // no effect
            }
        }

        // default neutral
        return 1.0;
    }
}
