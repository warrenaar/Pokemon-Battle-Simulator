public interface Battle {

    // The core battle action each concrete Pokémon must be able to perform
    void standardAttack(BattlePokemon target);
}
