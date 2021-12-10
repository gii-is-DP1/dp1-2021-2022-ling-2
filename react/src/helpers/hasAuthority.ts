import { TokenUser } from "../interfaces/TokenUser";
import { AuthorityEnum } from "../types/AuthorityEnum";

/**
 *
 * @author andrsdt
 * @param {User} user
 * @param {String} authority
 * @returns true if the user object has the authority of the second parameter
 */
export default function hasAuthority(
  user: TokenUser,
  authority: AuthorityEnum
): boolean {
  return !!user.username && user.authorities.includes(authority);
}
