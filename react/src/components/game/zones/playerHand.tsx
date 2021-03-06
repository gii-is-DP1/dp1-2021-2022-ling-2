import { Player } from "../../../interfaces/Player";
import AbilityCard from "../elements/abilityCard";
import { AbilityCardIngame } from "../../../interfaces/AbilityCardIngame";

type Params = {
  player: Player;
  selectedAbilityCard: AbilityCardIngame | null;
  setSelectedAbilityCard: (card: AbilityCardIngame | null) => void;
};

export default function PlayerHand(params: Params) {
  const { player, selectedAbilityCard, setSelectedAbilityCard } = params;

  return (
    <span className="flex transform-gpu scale-300 justify-center xl:-space-x-6 2xl:-space-x-10 flex-row-reverse pt-8 -m-16">
      <span></span> {/* Blank space (necessary for proper flex wrapping}*/}
      {player?.hand.map((abilityCard, idx) => (
        <AbilityCard
          key={idx}
          card={abilityCard}
          selected={selectedAbilityCard}
          setSelected={setSelectedAbilityCard}
        />
      ))}
    </span>
  );
}
