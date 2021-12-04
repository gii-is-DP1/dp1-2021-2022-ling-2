import { DEFAULT_IMAGE_PATH } from "../../constants/paths";

export default function Token(params) {
  const { type, value } = params;
  return (
    //   TODO replace <img> with <div>
    <>
      <img
        src={`${DEFAULT_IMAGE_PATH}/tokens/${type}.png`}
        alt={`${type} token`}
        className={`w-10 token token-${type}`}
      />
      {/* <p>{value}</p> */}
    </>
  );
}
