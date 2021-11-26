import Homebar from "../components/home/Homebar";
import { Table, Col, Row } from "react-bootstrap";
import { useState, useEffect, useContext } from "react";
import errorContext from "../context/error";
import axios from "../api/axiosConfig";

export default function Statistics() {
  const { errors, setErrors } = useContext(errorContext);
  const [gamesHistory, setGamesHistory] = useState(null);

  useEffect(() => {
    const fetchGameHistoryCount = async () => {
      try {
        const response = await axios.get(`gameHistory`);
        setGamesHistory(response.data);
      } catch (error) {
        setErrors([...errors, error.response?.data]);
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
