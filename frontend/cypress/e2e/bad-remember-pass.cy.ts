it('Bad remember pass', () => {
  cy.visit('/forgot-password');
  cy.url().should('include', '/forgot-password');
  const illegalEmail = "admincheck"
  cy.get('.form.email input').clear().type(illegalEmail);
  cy.get('.content .enter').click();
  cy.get('.content .error-text.active').should('exist');
  cy.get('.content .error-text.active').should('have.text', 'Некорректный формат адреса почты');
});
