export enum RegexType {
  LOGIN = "^[A-Za-z0-9]+([A-Za-z0-9]*|[._-]?[A-Za-z0-9]+)*$",
  NAME = "^[A-Za-z]+$",
  SURNAME = "^[A-Za-z]+$",
  EMAIL = `^([a-zA-Z0-9\\_\\.\\-]+)@([a-zA-Z0-9\\_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$`
}
