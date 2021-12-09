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
  user: TokenUser | null,
  authority: AuthorityEnum
): boolean {
  return user !== null && user.authorities.includes(authority);
}
