import { BASE_IMAGE_PATH } from "../../../constants/paths";

export default function HordeEnemyCard(params) {
  const { enemy, flipped } = params;
  return (
    <img
      className="card zoomable hover:scale-250"
      src={
        BASE_IMAGE_PATH +
        (flipped ? enemy.hordeEnemy.backImage : enemy.hordeEnemy.frontImage)
      } // TODO frontend or backend?
      alt={enemy.hordeEnemy.hordeEnemyType.hordeEnemyTypeEnum}
    ></img>
  );
}
