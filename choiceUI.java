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
        super("��ӭʹ�ó齱Ӧ��");
        
        jlAdd = new JLabel("�����ļ�·����");
        btAdd = new JButton("����ļ�"); //���°�ť���ļ�������ӵ�����
        jlrule = new JLabel("<html><body>&nbsp;&nbsp;˵����<br>"
        		//+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<br>;"
        		+ "&nbsp;&nbsp;�Ϸ�����ָ��·���ļ����ļ������齱�����֡�<br>"
        		+ "&nbsp;&nbsp;����ʾ�ļ������е����֡��ҷ�Ϊ�齱����<br>"
        		+ "&nbsp;&nbsp;�·�Ϊ��ʼ����ͣ��ť��<br>&nbsp;&nbsp;��ͣʱ�齱����ʾ�����ּ�Ϊѡ���ߡ�<br>"
        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br>"
        		+ "&nbsp;&nbsp;ϣ����ҳ齱���!&nbsp;&nbsp;&nbsp;&nbsp;~~~///(^v^)\\��~~~");
        btStart = new JButton("��ʼ�齱����");//���°�ť��������ʼѭ������
        btStop = new JButton("��ͣ�齱����");//���°�ť����ʾ�н���
        tfFile = new JTextField(30);//��д�ļ�·��
        tfShowFile = new JTextArea(5,10);//���ļ�ȫ��������ʾ(������ʾ��)
        tfShowName = new JTextField(13);//��ʾѭ������������(�齱��)
        
        //��Ӽ�����
        btAdd.addActionListener(new Listener());
        btAdd.setActionCommand("btAdd");
        btStart.addActionListener(new Listener());
        btStart.setActionCommand("btStart");
        btStop.addActionListener(new Listener());
        btStop.setActionCommand("btStop");
        
        //����Ϊ����
        JPanel top = new JPanel(new FlowLayout());
        top.add(jlAdd);
        top.add(tfFile);
        top.add(btAdd);
        this.add(top, BorderLayout.NORTH);//������һ����������ʾ+�����+��Ӱ�ť
        
        final JScrollPane sp = new JScrollPane();
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setViewportView(this.tfShowFile);
        tfShowFile.setEditable(false);
 
        this.add(sp, BorderLayout.WEST);//�󷽿�Ϊ�����ı���������嵥
        
        this.add(jlrule,BorderLayout.CENTER);//�м�Ϊ����˵����
        
        tfShowName.setEditable(false);
        tfShowName.setFont(new Font("",Font.BOLD,20));
      
        tfShowName.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        this.add(tfShowName,BorderLayout.EAST);//�ұ�Ϊ�齱��
        
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(btStart);
        bottom.add(btStop);
        this.add(bottom,BorderLayout.SOUTH);//����Ϊ�齱��������ť
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(650, 300);
        this.setLocation(100, 100);
        this.setVisible(true);
    }
    
    public class Listener implements ActionListener{ 
		public void actionPerformed(ActionEvent e) { 
			if(e.getActionCommand()=="btAdd") {//��ť������ļ��������ļ����ص�������
				File fRead = new File(tfFile.getText());
				String s="";
	            if(tfFile.getText()!="")
	            {
	            	try{      
	            		Reader in =new FileReader(fRead);
	            		BufferedReader bufferRead =new BufferedReader(in);
	            		String str = "";
	            		while((str=bufferRead.readLine())!=null) s+=str;//�Ȱ��ı�������ݶ����ַ���s��
	            		bufferRead.close();  
	            		}catch(Exception ex) {
	            			System.out.println(ex.toString());
	            		}
	            	//��s�ַ��������ݽ��д���(���ı��ļ��Ŀո�ɸȥ��ֻ����һ����������)
	            	Scanner scanner = new Scanner(s);
	            	List = new ArrayList(); 
	            	while(scanner.hasNext()) {
	            		try {
	            			String str = scanner.next();//������һ��һ����ȡ����
	            			tfShowFile.setText(tfShowFile.getText()+str+'\n');//�����ְ�����ʾ��������ʾ����
	            			List.add(str);//�����ּӵ�������
	            		}catch(Exception ex) {
	            			System.out.print(ex.toString());
	            		}
	            	}//������ȫ���������飬��СΪList.size();
	            }
			}//if end
			else if(e.getActionCommand()=="btStart") { //��ť����ʼ�齱
				if(pause == false) thread = new choiceThread(choiceUI.this);//��pause==true��ʱ��϶����̴߳��ڣ������ٴ����߳�/
				if(!(thread.isAlive())) { //�̵߳�һ�δ���
					if(List.size()!=0) {
						 try {
			                 thread.start();//�����߳�
			             }
			             catch(Exception ex) {System.out.print(ex.toString());}
					}
				}
				else { //�߳����ڴ��У��ȴ�������
					continueThread();//�����߳�
				}
			}
			else { //btStop  //��ť��ֹͣ�齱
				if(thread.isAlive()) {	    
					pause = true;
				}
			}
		}
    	
    }//Listener end
    
    //�����̵߳���ͣ�ͼ���
    public void continueThread(){ //�̼߳�������
        pause =false;
        synchronized (thread){
            thread.notify();
        }
    }
    public void onPause() { //�߳���ͣ����
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

