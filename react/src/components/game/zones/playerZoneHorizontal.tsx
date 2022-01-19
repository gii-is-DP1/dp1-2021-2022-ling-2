import { Player } from "../../../interfaces/Player";
import CharacterCard from "../elements/characterCard";
import CountCard from "../elements/countCard";
import PlaceholderCard from "../elements/placeholderCard";
import Token from "../elements/token";

type Params = {
  player: Player;
  reverse?: boolean;
};

export default function PlayerZoneHorizontal(params: Params) {
  const player = params.player;
  const reverse = params.reverse ?? false;

  return (
    <div
      className={`flex ${reverse ? "flex-row-reverse" : ""}
  space-x-4 items-end`}
    >
      <div className={`flex flex-col space-y-2 mx-2 h-full`}>
        <Token type="kill" value={player.kills} />
        <Token type="gold" value={player.gold} />
        <Token type="glory" value={player.glory} />
      </div>
      <div className="grid grid-cols-3 gap-2">
        <span className="invisible">
          <PlaceholderCard />
        </span>
        <span>
          <CountCard count={player.discardPile.length} zoomDirection="up" />
        </span>
        <span className="invisible">
          <PlaceholderCard />
        </span>
        <span className={reverse ? "order-3" : ""}>
          <div className="fixed transform-gpu -translate-y-8 text-xl font-bold">
            🩸 ({player.wounds})
          </div>
          <CharacterCard character={player.character} />
        </span>
        <span className={reverse ? "order-1" : ""}>
          <CountCard count={player.abilityPile.length} zoomDirection="up" />
        </span>
        <span
          className={`flex-1 flex ${
            reverse ? "flex-row-reverse" : "flex-row"
          } -space-x-12 2xl:-space-x-16`}
        >
          {reverse && <span>{/* Blank space */}</span>}
          {player.hand.map((_, idx) => (
            <PlaceholderCard key={idx} />
          ))}
        </span>
      </div>
    </div>
  );
}
