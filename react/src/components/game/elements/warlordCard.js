import { WARLORD_IMAGE_PATH } from "../../../constants/paths";
import { WARLORD_CARD_BACK } from "../../../constants/images";

export default function WarlordCard(params) {
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
