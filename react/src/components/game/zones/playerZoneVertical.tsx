import { useState } from "react";
import { Player } from "../../../interfaces/Player";
import CharacterCard from "../elements/characterCard";
import CountCard from "../elements/countCard";
import PlaceholderCard from "../elements/placeholderCard";
import Token from "../elements/token";

type Params = {
  player: Player;
  rotation?: number;
  counterclockwise?: boolean;
};

export default function PlayerZoneVertical(params: Params) {
  const [player] = useState(params.player);
  const rotation = params.rotation;
  const ccw = params.counterclockwise ?? false; // Clockwise or counter-clockwise rotations

  return (
    <div className={`flex flex-col space-x-4 items-end`}>
      <div
        className={`mb-5 2xl:mb-10 flex flex-row space-x-2 my-2 h-full w-full justify-${
          ccw ? "end" : "start"
        } items-baseline ${ccw ? "pr-4" : "pl-8"}`}
      >
        <Token type="kill" value={player.kills} counterclockwise={ccw} />
        <Token type="gold" value={player.gold} counterclockwise={ccw} />
        <Token type="glory" value={player.glory} counterclockwise={ccw} />
      </div>
      <div
        className={`grid grid-cols-3 gap-2  transform-gpu ${
          ccw ? "-" : ""
        }rotate-${rotation}`}
      >
        <span className="invisible">
          <PlaceholderCard />
        </span>
        <span>
          <CountCard
            count={player.discardPile.length}
            zoomDirection="up"
            counterclockwise={ccw}
          />
        </span>
        <span className="invisible">
          <PlaceholderCard />
        </span>
        <span className={ccw ? "order-3" : ""}>
          <div className="fixed transform-gpu -translate-y-8 text-xl font-bold">
            🩸 ({player.wounds})
          </div>
          <CharacterCard character={player.character} counterclockwise={ccw} />
        </span>
        <h1 className={ccw ? "order-1" : ""}>
          <CountCard
            count={player.abilityPile.length}
            zoomDirection="up"
            counterclockwise={ccw}
          />
        </h1>
        <span
          className={`flex-1 flex ${
            ccw ? "flex-row-reverse" : "flex-row"
          } -space-x-12 2xl:-space-x-16`}
        >
          {ccw && <span>{/* Blank space */}</span>}
          {player.hand.map((_, idx) => (
            <PlaceholderCard key={idx} />
          ))}
        </span>
      </div>
    </div>
  );
}
