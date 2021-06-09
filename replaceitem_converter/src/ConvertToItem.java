import java.util.ArrayList;
import java.util.List;

/**
 * replaceitemコマンド変換クラス
 * @author <a href= "https://twitter.com/maple_osg" target="_blank" >Maple32768</a>
 * @version 1.2
 *
 */
public class ConvertToItem {

	/**
	 * 渡されたコマンドを格納するためのList
	 */
	List<String> command = new ArrayList<String>();

	/**
	 * 渡されたコマンドに"/"が含まれるかを保存
	 */
	boolean hasSlash = false;

	/**
	 * インスタンス生成<br>
	 * このクラスのインスタンスを生成します
	 * @param replaceitem コマンド
	 */
	public ConvertToItem(String replaceitem) {
		int squareBracket = 0, curlyBracket = 0;
		boolean inDoubleQuotation = false;
		String arg = "";
		if(replaceitem.startsWith("/")) {
			this.hasSlash = true;
			replaceitem = replaceitem.substring(1);
		}
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


	private String getAsItem(List<String> command, boolean convertInNBT) {
		StringBuilder item = new StringBuilder("");
		if(this.hasSlash) item.append("/");
		//replaceitem
		if(command.size() != 0 && command.get(0).equals("replaceitem ")) {
			item.append("item replace ");

			int i = 1;
			//block
			if(command.get(1).equals("block ")) {
				while(i < command.size()) {
					if(i == 5) {
						item.append(command.get(i)).append("with ");
					}else {
						item.append(command.get(i));
					}
					i++;
				}
			}
			//entity
			else if(command.get(1).equals("entity ")){
				while(i < command.size()) {
					if(i == 3) {
						item.append(command.get(i)).append("with ");
					}else {
						item.append(command.get(i));
					}
					i++;
				}
			}else {
				item = new StringBuilder("#INVALID COMMAND");
			}

			while(convertInNBT && item.toString().contains("replaceitem ")) {
				item = new StringBuilder(convert(item.toString()));
			}

			return item.toString();
		}else {
			return "#INVALID COMMAND";
		}
	}

	/**
	 * Itemコマンド取得メソッド<br>
	 * 渡されたコマンドをreplaceitemコマンドとして解釈し、itemコマンドで返します。
	 * @return Itemコマンド
	 * @version 1.5
	 */
	public String getAsItem() {
		return getAsItem(false);
	}

	/**
	 * Itemコマンド取得メソッド<br>
	 * 渡されたコマンドをreplaceitemコマンドとして解釈し、itemコマンドで返します。
	 * @param convertInNBT NBTの中身まで変換するかどうかを指定
	 * @return Itemコマンド
	 * @version 1.5
	 */
	public String getAsItem(boolean convertInNBT) {
		return getAsItem(this.command, convertInNBT);
	}

	/**
	 * Executeコマンド取得メソッド<br>
	 * 渡されたコマンドをreplaceitemコマンドを含むexecuteコマンドとして解釈し、<br>itemコマンドを含むexecuteコマンドで返します。
	 * @return Executeコマンド
	 * @version 1.3
	 */
	public String getAsExecute() {
		return getAsExecute(false);
	}

	/**
	 * Executeコマンド取得メソッド<br>
	 * 渡されたコマンドをreplaceitemコマンドを含むexecuteコマンドとして解釈し、<br>itemコマンドを含むexecuteコマンドで返します。
	 * @param convertInNBT NBTの中身まで変換するかどうかを指定
	 * @return Executeコマンド
	 * @version 1.3
	 */
	public String getAsExecute(boolean convertInNBT) {
		StringBuilder result = new StringBuilder("");
		if(this.hasSlash) result.append("/");
		List<String> executeCommand = this.command.subList(0, this.command.size());
		for(String str : executeCommand) result.append(str);
		if(result.toString().contains("run replaceitem ")) {
			result.delete(0, result.length());
			for(int i =0; i < executeCommand.size(); i++) {
				String str = executeCommand.get(i);
				if(str.equals("replaceitem ") && executeCommand.get(i-1).equals("run ")) {
					result.append(getAsItem(executeCommand.subList(i, executeCommand.size()), convertInNBT));
					break;
				}else result.append(str);
			}
			if(convertInNBT && result.toString().contains("replaceitem ")) {
				result = new StringBuilder(convert(result.toString()));
			}
		}else result = new StringBuilder("#INVALID COMMAND");
		return result.toString();
	}


	private String convert(String in) {
		StringBuilder item = new StringBuilder(in);
		int begin = item.toString().indexOf("replaceitem "), j = begin, back_slash = 0;
		String includingReplaceitem = item.toString(), escape = "\"";
		if(includingReplaceitem.charAt(j-1) == '/') {
			j--;
		}
		while(includingReplaceitem.charAt(j-2) == '\\'){
			escape = '\\' + escape;
			back_slash++;
			j--;
		}
		String replaceAndAfter = includingReplaceitem.substring(begin), //replaceitemコマンド以降の文字列
			   beforeReplace = includingReplaceitem.substring(0, begin); //replaceitemコマンド以前の文字列

		int replaceEnd = -1;
		for(int k = 0; k < replaceAndAfter.length(); k++) {
			char c = replaceAndAfter.charAt(k);
			if(c == '\"' && replaceAndAfter.charAt(k-back_slash-1) != '\\') {
				replaceEnd = k;
				break;
			}
		}

		if(replaceEnd == -1) {
			return "#INVALID COMMAND";
		}

		String includedReplaceitem = replaceAndAfter.substring(0 /*replaceitemの始まり*/,
					   replaceEnd /*replaceitemの終わり+1*/);
			   //replaceitem本体
		String converted = new ConvertToItem(includedReplaceitem).getAsItem();
		if(!converted.equals("#INVALID COMMAND")) {
			item.delete(0, item.length());
			item.append(beforeReplace)
			.append(converted)
			.append(includingReplaceitem.substring(replaceEnd + beforeReplace.length()));
		}
		while(item.toString().contains("replaceitem ")) {
			item = new StringBuilder(convert(item.toString()));
		}
		return item.toString();

	}

	/**
	 * コマンド変換メソッド<br>
	 * 渡されたコマンドがどのような形であっても、replaceitemをitemに変換して返します。
	 * @return 変換されたコマンド
	 * @version 1.1
	 */
	public String convertForce() {
		StringBuilder result = new StringBuilder("");
		for(String str : command) {
			result.append(str);
		}
		if(result.toString().contains("replaceitem ")) {
			result = new StringBuilder(convert(result.toString()));
		}else {
			return "#REPLACEITEM NOT FOUND";
		}
		return result.toString();
	}

	/**
	 * コマンド取得メソッド<br>
	 * 渡されたコマンドを返します。
	 * @return インスタンス生成時に渡されたコマンド
	 * @version 1.0
	 */
	public String getRawCommand() {
		StringBuilder result = new StringBuilder("");
		for(String str : command) {
			result.append(str);
		}
		return result.toString();
	}

}
