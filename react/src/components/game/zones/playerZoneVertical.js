import { useEffect, useState } from "react";
import AbilityCard from "../elements/abilityCard";
import CharacterCard from "../elements/characterCard";
import Token from "../elements/token";

export default function PlayerZoneVertical(params) {
  const [player, setPlayer] = useState(params.player);
  const rotation = params.rotation;
  const cards = [1, 2, 3, 4]; // Placeholder for cards
  const ccw = params.counterclockwise ?? false; // Clockwise or counter-clockwise rotations

  useEffect(() => {
    const fetchPlayer = async (player) => {
      //   const response = await fetch(`/players/${player}`);
      //   const data = await response.json();
      //   setPlayer(data);
    };
    fetchPlayer();
  }, []);

  return (
    <div className={`flex flex-col space-x-4 items-end`}>
      <div
        className={`mb-5 2xl:mb-10 flex flex-row space-x-2 my-2 h-full w-full justify-${
          ccw ? "end" : "start"
        } items-baseline`}
      >
        <Token type="kill" value={player.kills} />
        <Token type="gold" value={player.gold} />
        <Token type="glory" value={player.glory} />
      </div>
      <div
        className={`grid grid-cols-3 gap-2 items-end justify-items-center transform-gpu ${
          ccw ? "-" : ""
        }rotate-${rotation}`}
      >
        <span className={ccw ? "order-last" : "order-first"}>
          <CharacterCard character={player.characterType} />
        </span>
        <span className="order-2 grid grid-rows-2 gap-y-2">
          <AbilityCard />
          <AbilityCard />
        </span>
        <span className={ccw ? "order-first" : "order-last"}>
          <span className={`flex-1 flex flex-row${ccw ? "-reverse" : ""}`}>
            {/* <span className="inline-flex"> */}
            {cards.map((abilityCard, idx) => (
              <AbilityCard
                key={idx}
                card={abilityCard}
                position={idx}
                reverse={ccw}
              />
            ))}
          </span>
        </span>
      </div>
    </div>
  );
}
