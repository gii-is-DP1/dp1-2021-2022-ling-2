import { CHARACTER_IMAGE_PATH } from "../../../constants/paths";
import { Character } from "../../../interfaces/Character";

type Params = {
  character: Character;
  counterclockwise?: boolean;
};

export default function CharacterCard(params: Params) {
  const { character, counterclockwise } = params;

  return (
    <img
      draggable={false}
      className={`card zoomable hover:scale-500 2xl:hover:scale-350 ${
        counterclockwise === undefined
          ? "hover:-translate-y-40"
          : "hover:translate-y-40 hover:translate-"
      }
      2xl:hover:-translate-y-50 ${
        counterclockwise !== undefined &&
        (counterclockwise
          ? "transform-gpu hover:rotate-90 hover:-translate-y-20 hover:-translate-x-20"
          : "transform-gpu hover:-rotate-90 hover:-translate-y-20 hover:translate-x-20")
      }`}
      src={`${CHARACTER_IMAGE_PATH}/${character.characterTypeEnum.toLowerCase()}_${character.characterGenderEnum.toLowerCase()}.png`}
      alt={`${character.characterTypeEnum} ${character.characterGenderEnum}`}
    ></img>
  );
}
