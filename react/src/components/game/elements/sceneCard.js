import { SCENE_CARD_BACK } from "../../../constants/images";
import { SCENE_IMAGE_PATH } from "../../../constants/paths";
import Token from "./token";

export default function SceneCard(params) {
  const { scene, count } = params;
  return (
    <>
      {scene ? (
        <img
          className="card zoomable hover:scale-400"
          src={`${SCENE_IMAGE_PATH}/${scene.sceneTypeEnum.toLowerCase()}.png`}
          alt={scene.sceneTypeEnum}
        ></img>
      ) : (
        <div
          className="card zoomable flex items-center justify-center h-full bg-contain bg-no-repeat hover:scale-200"
          style={{
            backgroundImage: `url('${SCENE_CARD_BACK}')`,
          }}
          alt={`Market card pile (${count} items)`}
        >
          <div className="transform-gpu -rotate-90">
            <Token type="count" value={count} />
          </div>
        </div>
      )}
    </>
  );
}
