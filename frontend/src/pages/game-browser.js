import Homebar from "../components/home/Homebar";
import { Table } from "react-bootstrap";

export default function GameBrowser() {

  const fetchGames = async (e) => {
    e.preventDefault();
  }

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Game Browser</h1>
      {/* Table Structure, data to be implemented */}
      <Table striped bordered hover variant="dark">
        <thead>
          <tr>
            <th>No.Players</th>
            <th>Game Name</th>
            <th>Spectators</th>
            <th>Password</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>[x]/4</td>
            <td>[Username]'s Game</td>
            <td>[x or -]</td>
            <td>No</td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
}
