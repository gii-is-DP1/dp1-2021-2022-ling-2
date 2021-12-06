import { CHARACTER_IMAGE_PATH } from "../../../constants/paths";

export default function CharacterCard(params) {
  const { character } = params;

  return (
    <img
      className="card hover:scale-500 2xl:hover:scale-350 hover:-translate-y-40
      2xl:hover:-translate-y-50
      "
      src={`${CHARACTER_IMAGE_PATH}/${character.characterTypeEnum.toLowerCase()}_${character.characterGenderEnum.toLowerCase()}.png`}
      alt={`${character.characterTypeEnum} ${character.characterGenderEnum}`}
    ></img>
  );
}
