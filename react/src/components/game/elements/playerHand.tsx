import { Player } from "../../../interfaces/Player";
import AbilityCard from "./abilityCard";

type Params = {
  player: Player;
};

export default function PlayerHand(params: Params) {
  const { player } = params;
  return (
    <span className="flex transform-gpu scale-300 justify-center -space-x-6 flex-row-reverse">
      <span></span> {/* Blank space (necessary for flex wrapping}*/}
      {player?.hand.map((abilityCard, idx) => (
        <AbilityCard key={idx} card={abilityCard} />
      ))}
    </span>
  );
}
