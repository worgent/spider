package xiaobai.jFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import xiaobai.analyze.WeiXinSpiders;
import xiaobai.util.StringUtil;

public class NewJFrame extends JFrame {
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
    private JButton button1;
    private JTextField text1;
    private JTextField text2;
    private JTextField text3;
    private JComboBox box;
    private JMenuBar menuBar;
    private JSlider slider;
    private JSpinner spinner;
    private JToolBar toolBar;
    
    public NewJFrame(){
        super();
        this.setSize(800,700);

        this.add(this.getTextField(),null);//添加文本框
        this.add(this.getText2Field(),null);//添加文本框
        this.add(this.getText3Field(),null);//添加文本框
        this.add(this.getButton(),null);//添加按钮

        this.add(this.getLabel(),null);//添加标签
        this.add(this.getLabel2(),null);//添加标签
        this.add(this.getLabel3(),null);//添加标签
//        this.add(this.getBox(),null);//添加下拉列表框

//        this.setJMenuBar(this.getMenu());//添加菜单

//        this.add(this.getSlider(),null);
        this.add(this.getSpinner(),null);
        this.add(this.getToolBar(),null);
        this.setTitle("白蜘蛛一代!");//设置窗口标题

    }
    private JToolBar getToolBar(){
        if(toolBar==null){
            toolBar = new JToolBar();
            toolBar.setBounds(103,260,71,20);
            toolBar.setFloatable(true);
        }
        return toolBar;
    }
    private JSpinner getSpinner(){
        if(spinner==null){
            spinner = new JSpinner();
            spinner.setBounds(103,220, 80,20);
            spinner.setValue(100);
        }
        return spinner;
    }
    private JSlider getSlider(){
        if(slider==null){
            slider = new JSlider();
            slider.setBounds(103,200,100, 20);
            slider.setMaximum(100);
            slider.setMinimum(0);
            slider.setOrientation(0);
            slider.setValue(0);
        }
        return slider;
    }
    /**
     * 菜单的级别JMenuBar->JMenu->JMenuItem
     * 三级都是1：n的关系
     * 最后添加菜单用的SetJMenuBar方法
     * @return 建立好的菜单
     */
    private JMenuBar getMenu(){
        if(menuBar==null){
            menuBar = new JMenuBar();
            JMenu m1 = new JMenu();
            m1.setText("文件");
            JMenu m2 = new JMenu();
            m2.setText("编辑");
            JMenu m3 = new JMenu();
            m3.setText("帮助");
            
            JMenuItem item11 = new JMenuItem();
            item11.setText("打开");
            JMenuItem item12 = new JMenuItem();
            item12.setText("保存");
            JMenuItem item13 = new JMenuItem();
            item13.setText("退出");
            
            JMenuItem item21 = new JMenuItem();
            item21.setText("复制");
            JMenuItem item22 = new JMenuItem();
            item22.setText("拷贝");
            JMenuItem item23 = new JMenuItem();
            item23.setText("剪切");
            
            JMenuItem item31 = new JMenuItem();
            item31.setText("欢迎");
            JMenuItem item32 = new JMenuItem();
            item32.setText("搜索");
            JMenuItem item33 = new JMenuItem();
            item33.setText("版本信息");
            
            m1.add(item11);
            m1.add(item12);
            m1.add(item13);
            
            m2.add(item21);
            m2.add(item22);
            m2.add(item23);
            
            m3.add(item31);
            m3.add(item32);
            m3.add(item33);
            
            menuBar.add(m1);
            menuBar.add(m2);
            menuBar.add(m3);
        }
        return menuBar;
    }
    /**
     * 设置下拉列表框
     * @return
     */
    private JComboBox getBox(){
        if(box==null){
            box = new JComboBox();
            box.setBounds(103,140,71,27);
            box.addItem("1");
            box.addItem("2");
            box.addActionListener(new comboxListener());//为下拉列表框添加监听器类
        }
        return box;
    }
    private class comboxListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Object o = e.getSource();
            System.out.println(e.getID());
            System.out.println(o.toString());
        }
    }
    /**
     * 设置标签
     * @return 设置好的标签
     */
    private JLabel getLabel(){
        if(label1==null){
            label1 = new JLabel();
            label1.setBounds(34,49,53,18);
            label1.setText("抓取路径");
            label1.setToolTipText("JLabel");
        }
        return label1;
    }
    
    /**
     * 设置标签
     * @return 设置好的标签
     */
    private JLabel getLabel3(){
        if(label3==null){
            label3 = new JLabel();
            label3.setBounds(34,100,53,18);
            
            label3.setText("cookieUrl");
            label3.setToolTipText("JLabel");
        }
        return label3;
    }
    /**
     * 设置标签
     * @return 设置好的标签
     */
    private JLabel getLabel2(){
        if(label2==null){
            label2 = new JLabel();
            label2.setBounds(34,100,53,18);
            label2.setBounds(34,80,53,18);
            label2.setText("时间");
            label2.setToolTipText("JLabel");
        }
        return label2;
    }
    /**
     * 设置按钮
     * @return 设置好的按钮
     */
    private JButton getButton(){
        if(button1==null){
            button1 = new JButton();
            button1.setBounds(260,49,71,27);
            button1.setText("执行");
            button1.setToolTipText("执行");
            button1.addActionListener(new RunButton());//添加监听器类，其主要的响应都由监听器类的方法实现

        }
        return button1;
    }
    /**
     * 监听器类实现ActionListener接口，主要实现actionPerformed方法
     * @author HZ20232
     *
     */
    private class RunButton implements ActionListener{
        public void actionPerformed(ActionEvent e){
        	String url = text1.getText();
        	String date = text2.getText();
        	//System.out.println(name);
        	String cookieUrl = text3.getText();
        	WeiXinSpiders weiXinSpiders = null;
        	if(StringUtil.isBlank(cookieUrl)){
        		 weiXinSpiders = new WeiXinSpiders();
        	}else{
        		weiXinSpiders = new WeiXinSpiders(cookieUrl);
        	}
            int flag = weiXinSpiders.getWinXinArticle(url,date);
            if(flag ==1){
            	System.out.println("*******************************************");
                System.out.println("**                                       **");
                System.out.println("**            执行完毕                                                                 **");
                System.out.println("**                                       **");
                System.out.println("*******************************************");
            }else{
            	System.out.println("*******************************************");
                System.out.println("**                                       **");
                System.out.println("**            执行失败                                                                **");
                System.out.println("**                                       **");
                System.out.println("*******************************************");
            }
            
        }
    }
    /**
     * 设定文本域
     * @return
     */
    private JTextField getTextField(){
        if(text1==null){
            text1 = new JTextField();
            text1.setBounds(96,50,160,20);
        }
        return text1;
    }
    /**
     * 设定文本域
     * @return
     */
    private JTextField getText2Field(){
        if(text2==null){
            text2 = new JTextField();
            text2.setBounds(96,80,160,20);
        }
        return text2;
    }
    
    /**
     * 设定文本域
     * @return
     */
    private JTextField getText3Field(){
        if(text3==null){
            text3 = new JTextField();
            text3.setBounds(96,110,160,20);
        }
        return text3;
    }
}
