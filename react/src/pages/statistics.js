import Homebar from "../components/home/Homebar";
import { Table, Col, Row } from "react-bootstrap";
import { useState, useEffect, useContext } from "react";
import popupContext from "../context/popup";
import axios from "../api/axiosConfig";

export default function Statistics() {
  const { popups, setPopups } = useContext(popupContext);
  const [gamesHistory, setGamesHistory] = useState(null);

  useEffect(() => {
    const fetchGameHistoryCount = async () => {
      try {
        const response = await axios.get(`gameHistory`);
        setGamesHistory(response.data);
      } catch (error) {
        setPopups([...popups, error.response?.data]);
      }
    };

    fetchGameHistoryCount();
  }, []);

  return (
    <div>
      <Homebar />
      <h1>Statistics</h1>
      <Table>
        <Col>
          <Row>
            <h2>Total Games Played: {gamesHistory && gamesHistory.length}</h2>
          </Row>
        </Col>
      </Table>
    </div>
  );
}
