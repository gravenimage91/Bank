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
					System.out.println("Nie ma takiej opcji. Spr�buj jeszcze raz.\n");
					this.show();
				}
			} catch (Exception e) {
				System.out.println("Nie ma takiej opcji. Spr�buj jeszcze raz.\n");
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
		System.out.println("Has�o: ");
		String password = sc.nextLine();

		if (dao.checkCredentials(name, password)) {
			this.user = name;
			this.showUi();
		} else {
			System.out.println("Poda�e� b��dny login lub has�o.");
		}
	}

	private void register() {
		System.out.println("Podaj nazw� u�ytkownika: ");
		String user = sc.nextLine();
		System.out.println("Podaj has�o: ");
		String password = sc.nextLine();

		if (user.length() > 0 && password.length() > 0) {
			if (!dao.checkUser(user)) {
				if (dao.registerUser(user, password)) {
					System.out.println("Zosta�e� zarejestrowany. ");
				} else {
					System.out.println("Wyst�pi� b��d podczas rejestracji. Spr�buj ponownie za chwil�. ");
				}
			} else {
				System.out.println("Taki u�ytkownik ju� istnieje. ");
			}

		} else {
			System.out.println("Nazwa u�ytkownika i has�o musz� posiada� min. 1 znak. ");
		}

	}

	private void logout() {
		System.out.println("Zosta�e� wylogowany pomy�lnie.");
	}

	private void deposit() {
		System.out.println("Jak� kwot� chcesz wp�aci�?");

		try {
			double sum = sc.nextDouble();
			if (dao.deposit(this.user, sum)) {
				System.out.print("Transakcja zako�czona pomy�lnie. ");
			} else {
				System.out.println("B��d z po��czeniem do bazy. Transakcja zako�czona niepowodzeniem. ");
			}
		} catch (Exception e) {
			System.out.print("Co� posz�o nie tak. Transakcja zako�czona niepowodzeniem. ");
		} finally {
			sc.nextLine();
			this.checkBalance();
		}
	}

	private void withdraw() {
		System.out.println("Jak� kwot� chcesz wyp�aci�?");

		try {
			double sum = sc.nextDouble();

			if (dao.withdraw(this.user, sum)) {
				System.out.print("Transakcja zako�czona pomy�lnie. ");
			} else {
				System.out.print("Nie mo�na zrealizowa� transakcji. Brak wystarczaj�cych �rodk�w na koncie. ");
			}
		} catch (Exception e) {
			System.out.print("Co� posz�o nie tak. Transakcja zako�czona niepowodzeniem. ");
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
