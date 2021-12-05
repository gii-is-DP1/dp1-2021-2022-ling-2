import { useState } from "react";

export default function Card(params) {
  const { card } = params;
  const [flipped, setFlipped] = useState(false);
  return (
    <img src={flipped ? card.backImage : card.frontImage} alt="card"></img>
  );
}
