it('Change Password Error', () => {
  cy.visit('/register');
  cy.get('.form.email input').clear().type('asdv');
  cy.get('.form.email.error .error-text').should('have.text', 'Некорректный формат адреса почты');
  cy.get('.form.email input').clear().type('asdmv@gmail.com');
  cy.get('.form.email.error .error-text').should('not.exist');
  cy.get('.form.name input').clear().type('aSdsq---');
  cy.get('.form.name.error .error-text').should('have.text', 'Некорректные символы в имени. Допустимые символы: прописные, строчные буквы латиницы');
  cy.get('.form.name input').clear().type('aSdsq');
  cy.get('.form.name.error .error-text').should('not.exist');
  cy.get('.form.surname input').clear().type('asdqs---');
  cy.get('.form.surname.error .error-text').should('have.text', 'Некорректные символы в фамилии. Допустимые символы: прописные, строчные буквы латиницы');
  cy.get('.form.surname input').clear().type('aSdsq');
  cy.get('.form.surname.error .error-text').should('not.exist');
  cy.get('.form.nickname input').clear().type('admin');
  cy.get('.form.nickname.error .error-text').should('have.text', 'Такой никнейм уже занят');
  cy.get('.form.nickname input').clear().type('aSdsq');
  cy.get('.form.nickname.error .error-text').should('not.exist');
  cy.get('.content .form:nth-of-type(5) input').clear().type('admincool');
  cy.get('.form.password.again input').clear().type('admincool1');
  cy.get('button.enter').click();
  cy.get('.content .form:nth-of-type(5) .error-text').should('have.text', 'Введенные пароли не совпадают');
  cy.get('.form.password.again .error-text').should('have.text', 'Введенные пароли не совпадают');
});
