import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		boolean convertInNBT = false;
		for(String arg : args) {
			if(arg.equalsIgnoreCase("-n")) {
				convertInNBT = true;
			}
		}
		Scanner scn = new Scanner(System.in);
		System.out.println("Type the replaceitem or execute command.(英検4級英語)");
		ConvertToItem item = new ConvertToItem(scn.nextLine());

		String ans = "", command = item.getRawCommand();
		if(command.startsWith("replaceitem ")) ans = item.getAsItem(convertInNBT);
		else if(command.startsWith("execute ")) ans = item.getAsExecute(convertInNBT);
		else ans = item.convertForce();

		if(ans != "#REPLACEITEM NOT FOUND") {
			System.out.println("[Output here]\n" + ans);
		}else {
			System.out.println("This command don't include replaceitem");
		}
		exit();
		scn.close();
	}

	private static void exit() throws IOException {
		System.out.print("続行するにはEnterキーを押してください . . .");
		new BufferedReader(new InputStreamReader(System.in)).readLine();
	}

}
