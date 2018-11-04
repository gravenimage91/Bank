import java.util.Scanner;

public class Bank {

	private Interface ui;
	private Scanner sc;
	private double accountBalance;
	private String user;
	private BankDAO dao;

	public Bank() {
		ui = new Interface();
		sc = new Scanner(System.in);
		accountBalance = 0;
		dao = new BankDAO();
		user = "";
	}

	public void show() {
		ui.showMain();

		boolean flag = true;

		while (flag) {
			try {
				int choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {
				case 1:
					this.login();
					flag = false;
					break;
				case 2:
					this.register();
					flag = false;
					break;
				case 3:
					System.out.println("Zapraszamy ponownie.");
					flag = false;
					break;
				default:
					System.out.println("Nie ma takiej opcji. Spróbuj jeszcze raz.\n");
					this.show();
				}
			} catch (Exception e) {
				System.out.println("Nie ma takiej opcji. Spróbuj jeszcze raz.\n");
				sc.next();
				this.show();
			}
		}
	}

	private void showUi() {
		ui.show();
		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1:
			this.deposit();
			break;
		case 2:
			this.withdraw();
			break;
		case 3:
			this.checkBalance();
			break;
		case 4:
			this.logout();
			break;
		default:
			System.out.println("Nie ma takiej opcji.");
		}
	}

	private void login() {
		System.out.println("Login: ");
		String name = sc.nextLine();
		System.out.println("Has³o: ");
		String password = sc.nextLine();

		if (dao.checkCredentials(name, password)) {
			this.user = name;
			this.showUi();
		} else {
			System.out.println("Poda³eœ b³êdny login lub has³o.");
		}
	}

	private void register() {
		System.out.println("Podaj nazwê u¿ytkownika: ");
		String user = sc.nextLine();
		System.out.println("Podaj has³o: ");
		String password = sc.nextLine();

		if (user.length() > 0 && password.length() > 0) {
			if (!dao.checkUser(user)) {
				if (dao.registerUser(user, password)) {
					System.out.println("Zosta³eœ zarejestrowany. ");
				} else {
					System.out.println("Wyst¹pi³ b³¹d podczas rejestracji. Spróbuj ponownie za chwilê. ");
				}
			} else {
				System.out.println("Taki u¿ytkownik ju¿ istnieje. ");
			}

		} else {
			System.out.println("Nazwa u¿ytkownika i has³o musz¹ posiadaæ min. 1 znak. ");
		}

	}

	private void logout() {
		System.out.println("Zosta³eœ wylogowany pomyœlnie.");
	}

	private void deposit() {
		System.out.println("Jak¹ kwotê chcesz wp³aciæ?");

		try {
			double sum = sc.nextDouble();
			if (dao.deposit(this.user, sum)) {
				System.out.print("Transakcja zakoñczona pomyœlnie. ");
			} else {
				System.out.println("B³¹d z po³¹czeniem do bazy. Transakcja zakoñczona niepowodzeniem. ");
			}
		} catch (Exception e) {
			System.out.print("Coœ posz³o nie tak. Transakcja zakoñczona niepowodzeniem. ");
		} finally {
			sc.nextLine();
			this.checkBalance();
		}
	}

	private void withdraw() {
		System.out.println("Jak¹ kwotê chcesz wyp³aciæ?");

		try {
			double sum = sc.nextDouble();

			if (dao.withdraw(this.user, sum)) {
				System.out.print("Transakcja zakoñczona pomyœlnie. ");
			} else {
				System.out.print("Nie mo¿na zrealizowaæ transakcji. Brak wystarczaj¹cych œrodków na koncie. ");
			}
		} catch (Exception e) {
			System.out.print("Coœ posz³o nie tak. Transakcja zakoñczona niepowodzeniem. ");
		} finally {
			sc.nextLine();
			this.checkBalance();
		}
	}

	private void checkBalance() {
		accountBalance = dao.checkBalance(this.user);
		System.out.printf("Stan twojego konta wynosi: %.2f \n\n", accountBalance);
		this.showUi();
	}

}
