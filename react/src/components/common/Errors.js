import PropTypes from "prop-types";
import { useContext } from "react";
import ErrorContext from "../../context/error";

export default function Errors() {
  const { errors, setErrors } = useContext(ErrorContext); // Array of errors

  return (
    <div className="errors-div">
      {errors.map((error, index) => (
        <div key={index} className="error-div">
          {/* TODO style each one of these as a red box or something */}
          <p>{error}</p>
        </div>
      ))}
    </div>
  );
}

Errors.propTypes = {
  errors: PropTypes.arrayOf(PropTypes.string).isRequired,
};
