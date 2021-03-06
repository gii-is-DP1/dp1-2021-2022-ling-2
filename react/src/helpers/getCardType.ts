/**
 * Given an object, try to determine which type of card it is based on its attributes.
 */

type cardTypeEnum = "ABILITY" | "ENEMY" | "UNKNOWN";

export default function getCardType(card: any): cardTypeEnum {
  if ("abilityCard" in card) return "ABILITY";
  if ("hordeEnemy" in card || "warlord" in card) return "ENEMY";
  else return "UNKNOWN";
}
