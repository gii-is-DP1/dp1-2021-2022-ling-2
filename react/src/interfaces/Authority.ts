import { AuthorityEnum } from "../types/AuthorityEnum";

export interface Authority {
  id: number;
  authorityEnum: AuthorityEnum;
  new: boolean;
}
