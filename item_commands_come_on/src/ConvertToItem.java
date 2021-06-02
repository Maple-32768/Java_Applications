import java.util.ArrayList;
import java.util.List;

public class ConvertToItem {
	
	List<String> command = new ArrayList<String>();
	
	public ConvertToItem(String replaceitem) {
		int squareBracket = 0, curlyBracket = 0;
		boolean inDoubleQuotation = false;
		String arg = "";
		for(int i = 0; i < replaceitem.length(); i++) {
			char c = replaceitem.charAt(i);
			if(c == '[' && !inDoubleQuotation) {
				squareBracket++;
			}else if(c == ']' && !inDoubleQuotation) {
				squareBracket--;
			}
			
			if(c == '{' && !inDoubleQuotation) {
				squareBracket++;
			}else if(c == '}' && !inDoubleQuotation) {
				squareBracket--;
			}
			
			if(c == '\"' && replaceitem.charAt(i-1) != '\\') inDoubleQuotation = !inDoubleQuotation;
			
			arg += c;
			
			if((c == ' ' && (squareBracket == 0 && curlyBracket == 0))) {
				this.command.add(arg);
				arg = "";
			}
			
			if(replaceitem.length() - i == 1) {
				this.command.add(arg);
			}
		}
	}
	
	public String getAsItem() {
		String item = "";
		//replaceitem
		if(command.size() != 0 && command.get(0).equals("replaceitem ")) {
			item += "item ";
			
			int i = 1;
			//block
			if(command.get(1).equals("block ")) {
				while(i < command.size()) {
					if(i == 5) {
						item += command.get(i);
						item += "replace ";
					}else {
						item += command.get(i);
					}
					i++;
				}
			}
			//entity
			else if(command.get(1).equals("entity ")){
				while(i < command.size()) {
					if(i == 3) {
						item += command.get(i);
						item += "replace ";
					}else {
						item += command.get(i);
					}
					i++;
				}
			}else {
				item = "INVALID ARGUMENT(S)";
			}
			
			
		}else {
			item = "INVALID COMMAND";
		}
		
		return item;
	}
	
	public String getCommand() {
		String result = "";
		for(String str : command) {
			result += str;
		}
		return result;
	}
	
}
