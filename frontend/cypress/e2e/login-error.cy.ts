it('Login error', () => {
  cy.visit('/login');
  cy.url().should('include', '/login');
  const illegalNickname = "admincheck"
  cy.get('.form.nickname input').clear().type(illegalNickname);
  cy.get('.form.password input').clear().type('admincool');
  cy.get('.content .enter').click();
  cy.get('.content .error-text.active').should('exist');
  cy.get('.content .error-text.active').should('have.text', 'Пользователь с таким логином или паролем не найден');
});
