import { Alert } from "react-bootstrap";

export default function SpectatorToast() {
  return (
    <div className="sticky-top" id="spectator-div">
      <Alert variant="info">Spectator</Alert>
    </div>
  );
}
