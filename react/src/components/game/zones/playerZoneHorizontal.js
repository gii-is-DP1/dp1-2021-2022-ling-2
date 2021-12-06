import { useEffect, useState } from "react";
import AbilityCard from "../elements/abilityCard";
import CharacterCard from "../elements/characterCard";
import Token from "../elements/token";

export default function PlayerZoneHorizontal(params) {
  const [player, setPlayer] = useState(params.player);
  const reverse = params.reverse ?? false;
  const cards = [1, 2, 3]; // Placeholder for cards

  useEffect(() => {
    const fetchPlayer = async (player) => {
      //   const response = await fetch(`/players/${player}`);
      //   const data = await response.json();
      //   setPlayer(data);
    };
    fetchPlayer();
  }, []);

  return (
    <div
      className={`flex flex-row${
        reverse ? "-reverse" : ""
      } space-x-4 items-end`}
    >
      <div
        className={`flex flex-col space-y-2 mx-2 h-full justify-end items-baseline`}
      >
        <Token type="kill" value={player.kills} />
        <Token type="gold" value={player.gold} />
        <Token type="glory" value={player.glory} />
      </div>
      <div className="grid grid-cols-3 gap-2 items-end justify-items-center">
        <span className={reverse ? "order-last" : "order-first"}>
          <CharacterCard />
        </span>
        <span className="order-2 grid grid-rows-2 gap-y-2">
          <CharacterCard />
          <CharacterCard />
        </span>
        <span className={reverse ? "order-first" : "order-last"}>
          <span className={`flex-1 flex flex-row${reverse ? "-reverse" : ""}`}>
            {/* <span className="inline-flex"> */}
            {cards.map((abilityCard, idx) => (
              <AbilityCard
                key={idx}
                card={abilityCard}
                position={idx}
                reverse={reverse}
              />
            ))}
          </span>
        </span>
      </div>
    </div>
  );
}
