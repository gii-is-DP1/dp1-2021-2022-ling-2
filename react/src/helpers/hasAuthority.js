export default function hasAuthority(user, authority) {
  return user && user?.authorities?.includes(authority);
}
