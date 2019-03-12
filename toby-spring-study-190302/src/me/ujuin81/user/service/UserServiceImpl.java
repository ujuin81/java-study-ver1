package me.ujuin81.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import me.ujuin81.user.dao.UserDao;
import me.ujuin81.user.domain.Level;
import me.ujuin81.user.domain.User;

public class UserServiceImpl implements UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	private UserDao userDao;
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
//	private PlatformTransactionManager transactionManager;
//	
//	public void setTransactionManager(PlatformTransactionManager transactionManager) {
//		this.transactionManager = transactionManager;
//	}
	
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user : users) {
			if(canUpgrade(user)) {
				upgradeLevel(user);
			}
		}
		
//		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());		
//		
//		try {	
//			upgradeLevelsInternal();
//			this.transactionManager.commit(status);
//		} catch(Exception e) {
//			this.transactionManager.rollback(status);
//			throw e;
//		}
	}
	
	private boolean canUpgrade(User user) {
		Level currentLevel = user.getLevel();
		switch(currentLevel) {
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			default: throw new IllegalArgumentException("Unknown Level : " + currentLevel);				
		}
	}

	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);		
		sendUpgradeEmail(user);
	}

	private void sendUpgradeEmail(User user) {		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade �ȳ�");
		mailMessage.setText("����ڴ��� ����� " + user.getLevel().name() + "�� ���׷��̵�Ǿ����ϴ�.");
		
		this.mailSender.send(mailMessage); 
	}

	public void add(User user) {
		if(user.getLevel() == null) {		
			user.setLevel(Level.BASIC);
		}
		userDao.add(user);
	}
}
