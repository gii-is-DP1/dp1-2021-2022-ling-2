import { CHARACTER_IMAGE_PATH } from "../../../constants/paths";

export default function CharacterCard(params) {
  const { character } = params;

  return (
    <img
      className="card hover:scale-500 hover:-translate-y-40"
      src={`${CHARACTER_IMAGE_PATH}/${character.characterTypeEnum.toLowerCase()}_${character.characterGenderEnum.toLowerCase()}.png`}
      alt={`${character.characterTypeEnum} ${character.characterGenderEnum}`}
    ></img>
  );
}
