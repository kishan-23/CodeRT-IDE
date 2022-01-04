/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.codert.ide.c;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;

/**
 *
 * @author kishan
 */
public class CompileRunUtil {

    private JTextPane tpInput;
    private JTextPane tpOutput;
    private JTextPane tpError;
    private File file;

    public CompileRunUtil(JTextPane input, JTextPane output, JTextPane error, File file) {
        this.file = file;
        this.tpInput = input;
        this.tpOutput = output;
        this.tpError = error;
    }

    public boolean compile() {
        //Compilation Section..
        
        String name = file.getName();
        String[] split = name.split("\\.");
        String par = file.getParent();
        System.out.println("Par:" + par);
        System.out.println("Name:" + name);
        List list = new ArrayList();
        if (split[1].equals("cpp")) {
            list.add("g++");
        } else {
            list.add("gcc");
        }
        list.add(name);
        ProcessBuilder pb = new ProcessBuilder(list);
        pb.directory(new File(par));
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();

            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line, con = "";
            // reading the output
            while ((line = is.readLine()) != null) {
                con += ("\n" + line);
            }
            tpError.setText(con);

            int status = p.waitFor();
            System.out.println("Exited with status: " + status);
        } catch (IOException ex) {
            Logger.getLogger(CompileRunUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InterruptedException ex) {
            Logger.getLogger(CompileRunUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //Execution Section
        //if compiled successfully, file.exe is created
        File exe = new File(par + "//" + "a.exe");
        if (!exe.exists()) {
            return false;
        }
        list.clear();
        list.add("cmd");
        list.add("/c");
        list.add("a.exe");
        ProcessBuilder pb1 = new ProcessBuilder(list);
        pb1.directory(new File(par));
        pb1.redirectErrorStream(true);
        try {
            Process p = pb1.start();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            bw.write(tpInput.getText());
            bw.flush();
            bw.close();
            System.out.println(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String readline,con="";
            int i = 0;
            while ((readline = reader.readLine()) != null) {
                System.out.println("\n" + readline);
                con+=(readline+"\n");
            }
            tpOutput.setText(con);
            System.out.println(p);
            int status = p.waitFor();
            System.out.println("Exited with status: " + status);
        } catch (IOException ex) {
            Logger.getLogger(CompileRunUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InterruptedException ex) {
            Logger.getLogger(CompileRunUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
