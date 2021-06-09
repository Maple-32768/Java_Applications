import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Main extends JFrame {


	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_WIDTH = 100;
	public static final int DEFAULT_HEIGHT = 30;
	public static boolean printLog = false;

	public static JLabel txt_title;
	public static JLabel txt_input;
	public static JLabel txt_output;
	public static JLabel txt_type_of_command;
	public static JCheckBox chkbox;
	public static JComboBox<String> combo;
	public static JTextArea input;
	public static JTextArea output;

	public static void main(String[] args) {
		for(String cmdLineArg : args) {
			if(cmdLineArg.equalsIgnoreCase("-d")) {
				printLog = true;
			}
		}

		//ウィンドウ生成
		JFrame frame = startup("Replaceitem command converter");
		frame.getContentPane().setBackground(Color.decode("#add8e6"));

		//ラベル(テキスト)生成
		txt_title = createText("replaceitemコマンド変換器",100, 20, 600, 50, 30);
		txt_input = createText("入力コマンド",100, 85, 300, 30, 17);
		txt_output = createText("出力コマンド",100, 365, 300, 30, 17);
		txt_type_of_command = createText("コマンドの種類",100, 280, 550, 30, 17);

		//テキスト詳細設定
		txt_title.setBackground(Color.decode("#dee3e9"));
		txt_title.setHorizontalAlignment(JLabel.CENTER);
		txt_title.setOpaque(true);

		//テキスト追加
		frame.add(txt_title);
		frame.add(txt_input);
		frame.add(txt_output);
		frame.add(txt_type_of_command);

		//チェックボックス生成
		chkbox = createCheckBox("NBTの中を変換", 520, 280, 120, 30);
		chkbox.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		chkbox.setVisible(true);

		//チェックボックス詳細設定
		chkbox.setForeground(Color.decode("#353833"));
		chkbox.setBackground(Color.decode("#6495ed"));

		//チェックボックスイベント登録
		chkbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(printLog) System.out.println("Checkbox was changed to "+ chkbox.isSelected());
				rewrite();
			}
		});

		//チェックボックス追加
		frame.add(chkbox);

		//コンボボックス生成
		String[] choises = {"選択してください","replaceitem","execute","その他(NBTを変換)"};
		combo = createComboBox(choises, 280, 280, 200, 30);

		//コンボボックス詳細設定
		combo.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		combo.setBackground(Color.decode("#6495ed"));

		//コンボボックスイベント登録
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(printLog) System.out.println("Combo box was changed to "+ combo.getSelectedItem());
				rewrite();
			}
		});

		//コンボボックス追加
		frame.add(combo);

		//テキストエリア生成
		input = createTextArea(100, 120, 50, 100, 600, 100);
		output = createTextArea(100, 400, 50, 100, 600, 100);
		//テキストエリア詳細設定
		input.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		output.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		output.setEditable(false);

		//テキストエリア追加
		frame.add(input);
		frame.add(output);

		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(printLog) System.out.println("Input was changed");
				rewrite();
			}

			@Override
			public void keyPressed(KeyEvent e) {}
		});

		frame.setVisible(true);

	}

	private static JFrame startup(String title) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		if(printLog) System.out.println("Start up is success.");
		return frame;
	}

	private static JLabel createText(String text, int x, int y,int width, int height, int size) {
		JLabel label = new JLabel(text);
		label.setBounds(x,y,width,height);
		label.setFont(resize(size));
		return label;
	}

	private static JCheckBox createCheckBox(String text, int x, int y, int width, int height) {
		JCheckBox chkbox = new JCheckBox(text);
		chkbox.setBounds(x, y, width, height);
		return chkbox;
	}

	private static JComboBox<String> createComboBox(String[] choises, int x, int y, int width, int height) {
		JComboBox<String> combo = new JComboBox<String>(choises);
		combo.setBounds(x, y, width, height);
		return combo;
	}


	private static JTextArea createTextArea(int x, int y, int rows, int columns,int width, int height) {
		JTextArea textarea = new JTextArea(rows,columns);
		textarea.setBounds(x, y, width, height);
		textarea.setLineWrap(true);
		return textarea;
	}

	private static Font resize(int size) {
		return new Font(Font.DIALOG,Font.PLAIN, size);
	}

	public static void rewrite() {
		String in = input.getText(), out = "";
		int index = combo.getSelectedIndex();
		boolean checked = chkbox.isSelected();
		ConvertToItem item = new ConvertToItem(in);
		String command = item.getRawCommand();

		switch(index) {
		case 0:
			output.setText("");
			chkbox.setVisible(true);
			break;

		case 1:
			output.setText(item.getAsItem(checked));
			chkbox.setVisible(true);
			break;

		case 2:
			output.setText(item.getAsExecute(checked));
			chkbox.setVisible(true);
			break;

		case 3:
			if(command.startsWith("replaceitem ")) {
				output.setText(item.getAsItem(checked));
			}else {
				output.setText(item.convertForce());
			}
			chkbox.setVisible(false);
			break;
		}
	}

}
