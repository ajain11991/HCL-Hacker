package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountsService {

	@Getter
	private AccountsRepository accountsRepository;

	@Autowired
	private NotificationService notificationService;

	public AccountsRepository getAccountsRepository() {
		return accountsRepository;
	}

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;

	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public void transferAmount(String accountFromId, String accountToId,
			BigDecimal amount) {

		if(amount.compareTo(BigDecimal.valueOf(0)) > 1 ){
			Account fromAccount = this.accountsRepository.getAccount(accountFromId);
			Account toAccount = this.accountsRepository.getAccount(accountFromId);
			if(null != fromAccount  && null != toAccount)
			{
				if(amount.compareTo(fromAccount.getBalance()) > -1 ){
					fromAccount.setBalance(fromAccount.getBalance().subtract(amount)); 
					toAccount.setBalance(toAccount.getBalance().add(amount));
					this.accountsRepository.updateAccount(fromAccount);
					this.accountsRepository.updateAccount(toAccount);
					this.notificationService.notifyAboutTransfer(fromAccount, amount +
							"is debited from your account and credited to " + toAccount.getAccountId() + " account");				
					this.notificationService.notifyAboutTransfer(toAccount, amount +
							"is credited to your account and receive from " + fromAccount.getAccountId() + "account");				
				}
			}
			else{
				log.error("Account is not valid");
			}
		}
		else
		{
			log.error("Amount should be valid");
		}
	}
}
