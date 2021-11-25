import { useContext } from "react";
import ErrorContext from "../../context/error";
import { Alert } from "react-bootstrap";

/**
 * This component is used to display errors in the app.
 * It gets the list of error JSON objects from a global
 * context and displays them in rows. This component is
 * at the top-level of the React DOM tree so it will be
 * rendered on every page with a consistent style.
 * @author andrsdt
 * @returns {JSX.Element} Component with all the errors
 * @see https://react-bootstrap.github.io/components/alerts/
 */

export default function Errors() {
  // TODO rename to "messages" and also allow informative,
  // success... messages, not only errors. Easily implementable
  // by including more variants of status on "getVariant"

  const { errors, setErrors } = useContext(ErrorContext); // Array of error objects

  /**
   *  Note to prof: We thought a switch-case was cleaner for this than if-else :(
   * @returns {string} Bootstrap variant for the error
   * @see https://developer.mozilla.org/es/docs/Web/HTTP/Status
   */
  const getColor = (status) => {
    switch (status) {
      case 200: // OK
      case 201: // Created
        return "success";
      case 400: // Bad request
      case 401: // Unauthorized
      case 403: // Forbidden
      case 404: // Not found
      case 500: // Internal server error (shouldn't appear)
        return "danger";
      default:
        return "info";
    }
  };

  return (
    <div className="fixed-top" id="errors-div">
      {errors.map(
        (errorObj, idx) =>
          errorObj?.message && (
            <Alert
              key={idx}
              className="m-2 p-1 pt-2 pl-2 shadow"
              variant={getColor(errorObj.status)}
              onClose={() => {
                const newErrors = [...errors];
                newErrors.pop(errorObj);
                setErrors(newErrors);
              }}
              dismissible
            >
              <Alert.Heading>{errorObj.error}</Alert.Heading>
              <p>
                {errorObj.message ??
                  "An error should be here... We are working on it"}
              </p>
            </Alert>
          )
      )}
    </div>
  );
}
