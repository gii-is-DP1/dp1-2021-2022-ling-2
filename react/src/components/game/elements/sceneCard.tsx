import { SCENE_CARD_BACK } from "../../../constants/images";
import { SCENE_IMAGE_PATH } from "../../../constants/paths";
import { Scene } from "../../../interfaces/Scene";
import Token from "./token";

type Params = {
  scene?: Scene;
  count?: number;
};

export default function SceneCard(params: Params) {
  const { scene, count } = params;
  return (
    <>
      {scene ? (
        <img
          draggable={false}
          className="card zoomable hover:scale-400"
          src={`${SCENE_IMAGE_PATH}/${scene.sceneTypeEnum.toLowerCase()}.png`}
          alt={scene.sceneTypeEnum}
        ></img>
      ) : (
        count && (
          <div
            draggable={false}
            className="card zoomable flex items-center justify-center h-full bg-contain bg-no-repeat hover:scale-200"
            style={{
              backgroundImage: `url('${SCENE_CARD_BACK}')`,
            }}
          >
            <div className="transform-gpu -rotate-90">
              <Token type="count" value={count} />
            </div>
          </div>
        )
      )}
    </>
  );
}
