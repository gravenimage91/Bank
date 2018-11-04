import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer implements Serializable {

	private static final long serialVersionUID = 1L;

	public Bank read(String filename) {

		Bank bank;

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
			bank = (Bank) in.readObject();
			in.close();
		} catch (Exception e) {
			System.err.println(e);
			bank = new Bank();
		}

		return bank;
	}

	public void save(String filename, Bank bank) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(bank);
			out.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}