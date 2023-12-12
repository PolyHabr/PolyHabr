export namespace Authorization {
  export interface SignUp {
    username: string,
    firstName: string,
    lastName: string,
    email: string,
    password: string
  }

  export interface SignIn {
    username: string,
    password: string
  }

  export interface Login {
    accessToken: string,
    type: string,
    username: string,
    isFirst: false
  }

  export interface SavePassword {
    token: string,
    newPassword: string
  }
}
