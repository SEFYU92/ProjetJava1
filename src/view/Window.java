/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javafx.scene.paint.Color.color;
import javax.swing.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
/**
 *
 * @author Youssef
 */
public class Window extends JFrame implements ActionListener
{
    JTextField ip;
    JLabel instruct;
    JTextArea result;
    JButton valider;
    String adress;
    ArrayList<String> list;
    String IPADDRESS_PATTERN;
    Pattern pattern;
    Matcher matcher;
    Graph graph;
    Node n;
    String buffer;
    int counter;
    
    public Window()
    {
        counter=0;
        IPADDRESS_PATTERN = 
        "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = null;
        graph = new SingleGraph("Tutorial 1");
        graph.setStrict(false);
        n = null;
        list = new ArrayList<String>();
        
        this.setTitle("projet01");
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setLayout(null);
        ip = new JTextField();
        ip.setBounds(new Rectangle(150,10,130,30));
        instruct = new JLabel();
        instruct.setText("Enter Website adress");
        instruct.setBounds(new Rectangle(10,10,130,30));
        result = new JTextArea();
        result.setBounds(new Rectangle(50,70,200,200));
        result.setText("write an URL,\nthen click \"go\" to display the graph");
        result.append("\nwait until graph is updated\nto enter next URL");
        valider = new JButton("go");
        valider.setBounds(new Rectangle(130,50,60,20));
        valider.addActionListener(this);
        this.add(instruct);
        this.add(ip);
        this.add(result);
        this.add(valider);
        graph.display();
        this.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource()==valider)
        {
            list.clear();
            adress=new String();
            adress="tracert "+ip.getText();
            try
            {
            Process proc = Runtime.getRuntime().exec(adress);
            InputStream stream = proc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 1);
            while((buffer = reader.readLine()) != null)
            {
                //buffer=reader.readLine();
                System.out.println(buffer);
                if(buffer != null)
                matcher = pattern.matcher(buffer);
                if (matcher.find())
                {
                    buffer = matcher.group();
                    list.add(buffer);
                }
            }
            graph.addNode("root");
            n = graph.getNode("root");
            n.addAttribute("ui.label","root");
            graph.addNode(list.get(1));
            n = graph.getNode(list.get(1));
            n.addAttribute("ui.label",list.get(1));
            graph.addEdge(Integer.toString(counter),"root",list.get(1));
            counter++;
            for (int i = 2; i < list.size(); i++)
            {
                graph.addNode(list.get(i));
                n = graph.getNode(list.get(i));
                n.addAttribute("ui.label",list.get(i));
                graph.addEdge(Integer.toString(counter),list.get(i-1),list.get(i));
                counter++;
            }
            }
            catch (IOException e)
            {
            e.printStackTrace();
            }
        }
    }
}
