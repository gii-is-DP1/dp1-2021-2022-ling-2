import { SceneTypeEnum } from "../types/SceneTypeEnum";

export interface Scene {
  id: number;
  sceneTypeEnum: SceneTypeEnum;
  frontImage: string;
  backImage: string;
}
