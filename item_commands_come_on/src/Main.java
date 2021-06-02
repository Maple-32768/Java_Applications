import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		System.out.println("Enter the replaceitem command.(‰pŒŸ4‹‰‰pŒê)");
		String command = scn.nextLine();
		String ans = new ConvertToItem(command).getAsItem();
		System.out.println((ans.equals("INVALID COMMAND") || ans.equals("INVALID ARGUMENT(S)")) ? "This is not replaceitem command." : ans);
		
		scn.close();
	}

}
