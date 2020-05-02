package java_keshe;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.*; 
import java.util.*; 

public class choiceUI extends JFrame {
    public static void main(String[] args) {
        choiceUI choice = new choiceUI();
    }
    public choiceUI() {
        super("欢迎使用抽奖应用");
        
        jlAdd = new JLabel("输入文件路径：");
        btAdd = new JButton("添加文件"); //按下按钮将文件内容添加到程序
        jlrule = new JLabel("<html><body>&nbsp;&nbsp;说明：<br>"
        		//+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<br>;"
        		+ "&nbsp;&nbsp;上方输入指定路径文件，文件包含抽奖者名字。<br>"
        		+ "&nbsp;&nbsp;左方显示文件中所有的名字。右方为抽奖区。<br>"
        		+ "&nbsp;&nbsp;下方为开始和暂停按钮。<br>&nbsp;&nbsp;暂停时抽奖区显示的名字即为选中者。<br>"
        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>"
        		+ "&nbsp;&nbsp;希望大家抽奖愉快!&nbsp;&nbsp;&nbsp;&nbsp;~~~///(^v^)\\、~~~");
        btStart = new JButton("开始抽奖点名");//按下按钮，名单开始循环快闪
        btStop = new JButton("暂停抽奖点名");//按下按钮，显示中奖者
        tfFile = new JTextField(30);//填写文件路径
        tfShowFile = new JTextArea(5,10);//将文件全部内容显示(内容显示区)
        tfShowName = new JTextField(13);//显示循环快闪的区域(抽奖区)
        
        //添加监听器
        btAdd.addActionListener(new Listener());
        btAdd.setActionCommand("btAdd");
        btStart.addActionListener(new Listener());
        btStart.setActionCommand("btStart");
        btStop.addActionListener(new Listener());
        btStop.setActionCommand("btStop");
        
        //以下为布局
        JPanel top = new JPanel(new FlowLayout());
        top.add(jlAdd);
        top.add(tfFile);
        top.add(btAdd);
        this.add(top, BorderLayout.NORTH);//最上面一行是文字提示+输入框+添加按钮
        
        final JScrollPane sp = new JScrollPane();
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setViewportView(this.tfShowFile);
        tfShowFile.setEditable(false);
 
        this.add(sp, BorderLayout.WEST);//左方框为处理文本后的名字清单
        
        this.add(jlrule,BorderLayout.CENTER);//中间为程序说明框
        
        tfShowName.setEditable(false);
        tfShowName.setFont(new Font("",Font.BOLD,20));
      
        tfShowName.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        this.add(tfShowName,BorderLayout.EAST);//右边为抽奖区
        
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(btStart);
        bottom.add(btStop);
        this.add(bottom,BorderLayout.SOUTH);//下面为抽奖的两个按钮
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(650, 300);
        this.setLocation(100, 100);
        this.setVisible(true);
    }
    
    public class Listener implements ActionListener{ 
		public void actionPerformed(ActionEvent e) { 
			if(e.getActionCommand()=="btAdd") {//按钮：添加文件——将文件加载到程序中
				File fRead = new File(tfFile.getText());
				String s="";
	            if(tfFile.getText()!="")
	            {
	            	try{      
	            		Reader in =new FileReader(fRead);
	            		BufferedReader bufferRead =new BufferedReader(in);
	            		String str = "";
	            		while((str=bufferRead.readLine())!=null) s+=str;//先把文本里的内容读到字符串s中
	            		bufferRead.close();  
	            		}catch(Exception ex) {
	            			System.out.println(ex.toString());
	            		}
	            	//将s字符串的内容进行处理(将文本文件的空格筛去，只留下一个个的名字)
	            	Scanner scanner = new Scanner(s);
	            	List = new ArrayList(); 
	            	while(scanner.hasNext()) {
	            		try {
	            			String str = scanner.next();//将名字一个一个提取出来
	            			tfShowFile.setText(tfShowFile.getText()+str+'\n');//把名字按列显示在内容显示器中
	            			List.add(str);//将名字加到容器中
	            		}catch(Exception ex) {
	            			System.out.print(ex.toString());
	            		}
	            	}//名字已全部加入数组，大小为List.size();
	            }
			}//if end
			else if(e.getActionCommand()=="btStart") { //按钮：开始抽奖
				if(pause == false) thread = new choiceThread(choiceUI.this);//在pause==true的时候肯定有线程存在，不能再创建线程/
				if(!(thread.isAlive())) { //线程第一次创建
					if(List.size()!=0) {
						 try {
			                 thread.start();//开启线程
			             }
			             catch(Exception ex) {System.out.print(ex.toString());}
					}
				}
				else { //线程在内存中，等待被唤醒
					continueThread();//唤醒线程
				}
			}
			else { //btStop  //按钮：停止抽奖
				if(thread.isAlive()) {	    
					pause = true;
				}
			}
		}
    	
    }//Listener end
    
    //控制线程的暂停和继续
    public void continueThread(){ //线程继续函数
        pause =false;
        synchronized (thread){
            thread.notify();
        }
    }
    public void onPause() { //线程暂停函数
        synchronized (thread) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public	ArrayList List;
    public	JLabel jlAdd;
    public  JButton btAdd;
    public	JLabel jlrule;
    public  JButton btStart;
    public  JButton btStop;
    public  JTextField tfFile;
    public  JTextArea tfShowFile;
    public  JTextField tfShowName;
    public	choiceThread thread;
    public	boolean pause = false;
}

