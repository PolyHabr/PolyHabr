it('Register', () => {
  let randomString = (length: number = 8) => {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
      counter += 1;
    }
    return result;
  }
  cy.visit('/register');
  cy.url().should('include', '/register');
  let email = `veselblu@yandex.ru`;
  cy.get('.form.email input').clear().type(email);
  cy.get('.form.name input').clear().type(randomString());
  cy.get('.form.surname input').clear().type(randomString());
  cy.get('.form.nickname input').clear().type(randomString());
  let pass = randomString();
  cy.get('.form.password:not(.again) input').clear().type(pass);
  cy.get('.form.password.again input').clear().type(pass);
  cy.get('.content .enter').click();
  cy.url().should('not.include', 'register');
  cy.url().should('include', 'email-confirm');
  cy.get('.email').should('have.text', email);
});
