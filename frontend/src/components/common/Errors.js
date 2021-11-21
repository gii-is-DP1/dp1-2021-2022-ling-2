import PropTypes from "prop-types";

export default function Errors(errorsObj) {
  const errors = [...new Set(errorsObj.errors)];
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
