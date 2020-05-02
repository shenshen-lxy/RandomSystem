package java_keshe;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextField;

public class choiceThread extends Thread{
	choiceUI ui;
	int sleepLength = 300; //闪过的时间为0.3s
	choiceThread(choiceUI ui){
		this.ui = ui;
	}
	public void run() {
    	while(true) {
    		while(ui.pause) {
    			ui.onPause();//线程暂停
    		}
    		String str=(String)ui.List.get(0);
     		ui.List.remove(0);//从头删去
     		ui.tfShowName.setText(""+str);
     		ui.List.add(str);//追加到末尾
	        try{
	        	sleep(sleepLength);
	        }
	        catch(InterruptedException e)
	        {System.out.print(e.toString());}
	    }
	 }       
}
