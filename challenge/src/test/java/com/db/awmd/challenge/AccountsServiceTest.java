package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  @Test
  public void transfreAmount() throws Exception
  {
	  
	    Account accountFrom = new Account("Id-999",BigDecimal.valueOf(1000));
	    Account accountTo = new Account("Id-998",BigDecimal.valueOf(50));
	    this.accountsService.createAccount(accountFrom);
	    this.accountsService.createAccount(accountTo);
	    BigDecimal amount = BigDecimal.valueOf(500);
	    try {
	      this.accountsService.transferAmount(accountFrom.getAccountId(), accountTo.getAccountId(), amount);
	    	
	      Assert.assertEquals("Amount Transfered",this.accountsService.getAccount(accountTo.getAccountId()).getBalance(),BigDecimal.valueOf(50));
	    	
	    } catch (Exception ex) {
	      assertThat(ex.getMessage());
	    }
 
  }
}
