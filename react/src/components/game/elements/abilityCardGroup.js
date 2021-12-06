import AbilityCard from "./abilityCard";

export default function AbilityCardGroup(params) {
  const { cards } = params;
  return (
    <span>
      {cards.map((abilityCard, idx) => (
        <AbilityCard key={idx} card={abilityCard} z={idx * 10} />
      ))}
    </span>
  );
}
