package xiaobai.jFrame;

import java.io.IOException;

import javax.swing.JFrame;

public class RunJframe {
	    public static void main(String args[])throws Exception{
	        NewJFrame frame1 = new NewJFrame();
	        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭

	        frame1.setVisible(true);
	    	
	    	//执行cmd命令，打开网页
//			try {
//				Process process = Runtime.getRuntime().exec("cmd /c F:\\worgentest.html");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	    }
}
