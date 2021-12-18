import { Game } from "../interfaces/Game";

/**
 * Given a game object, take the card array and separate it into three arrays filtering by pile
 * @author andrsdt
 * @param game
 */
export default function separatePlayerCards(game: Game): void {
  game.players.forEach((player) => {
    player.hand = player.cards.filter((card) => card.location === "HAND");
    player.abilityPile = player.cards.filter(
      (card) => card.location === "ABILITY_PILE"
    );
    player.discardPile = player.cards.filter(
      (card) => card.location === "DISCARD_PILE"
    );
  });
}
