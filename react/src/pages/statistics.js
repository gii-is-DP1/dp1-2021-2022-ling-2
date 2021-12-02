import Homebar from "../components/home/Homebar";
import { Table, Col, Row } from "react-bootstrap";
import { useState, useEffect, useContext } from "react";
import axios from "../api/axiosConfig";
import toast from "react-hot-toast";

export default function Statistics() {
  const [gamesHistory, setGamesHistory] = useState(null);

  useEffect(() => {
    const fetchGameHistoryCount = async () => {
      try {
        const response = await axios.get(`gameHistory`);
        setGamesHistory(response.data);
      } catch (error) {
        toast.error(error.response?.data?.message);
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
