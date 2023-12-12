it('Cant comment article', () => {
  cy.visit('/login');
  cy.url().should('include', '/login');
  cy.get('.form.nickname input').clear().type('admin');
  cy.get('.form.password input').clear().type('admincool');
  cy.get('.content .enter').click();
  cy.url().should('not.include', 'login');
  cy.url().should('include', '/');
  cy.get('header .bottom .container .actions .button-icon:nth-child(3)').should((profileButton) => {
    expect(profileButton, 'Profile button existing').to.exist;
  });
  cy.get('header .bottom .container .actions .button-icon:nth-child(3)').click();
  cy.get('header .bottom .container .actions .button-icon:nth-child(3) poly-menu .menu .footer .item:nth-child(2)').should("exist");
  cy.get('header .bottom .container .actions .button-icon:nth-child(3) poly-menu .menu .footer .item:nth-child(2)').click();
  cy.get('header .bottom .container .actions .button-icon:nth-child(2)').should("exist");
  cy.get('header .bottom .container .actions .button-icon:nth-child(2)').click();
  cy.get('header .bottom .container .actions .button-icon:nth-child(2) poly-menu .menu .unauthorized .header .email').should('exist');
  cy.get('header .bottom .container .actions .button-icon:nth-child(2) poly-menu .menu .unauthorized .header .email').contains('Войти');
  cy.get('poly-card:nth-child(2) article > a').click();
  cy.get('.comment-input textarea').should("not.exist");
});
