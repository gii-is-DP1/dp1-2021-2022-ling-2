import { WARLORD_CARD_BACK } from "../../../constants/images";
import { WARLORD_IMAGE_PATH } from "../../../constants/paths";
import { WarlordIngame } from "../../../interfaces/WarlordIngame";

type Params = {
  warlord: WarlordIngame;
  flipped?: boolean;
};
export default function WarlordCard(params: Params) {
  const { warlord, flipped } = params;
  return (
    <img
      className="card zoomable hover:scale-250"
      src={
        flipped
          ? WARLORD_CARD_BACK
          : `${WARLORD_IMAGE_PATH}/${warlord.warlord.warlordTypeEnum.toLowerCase()}.png`
      } // TODO frontend or backend?
      alt={warlord.warlord.warlordTypeEnum}
    ></img>
  );
}
