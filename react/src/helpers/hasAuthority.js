/**
 *
 * @author andrsdt
 * @param {User} user
 * @param {String} authority
 * @returns true if the user object has the authority of the second parameter
 */
export default function hasAuthority(user, authority) {
  return user && user?.authorities?.includes(authority);
}
