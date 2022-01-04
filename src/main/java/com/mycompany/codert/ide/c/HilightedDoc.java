/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.codert.ide.c;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author kishan
 */
public class HilightedDoc extends DefaultStyledDocument {

    final StyleContext cont = StyleContext.getDefaultStyleContext();
    final AttributeSet keyw1 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(23, 161, 165));//23,161,165 || 211 84 0 || 94,109,3
    final AttributeSet keyw2 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(211, 84, 0));
    final AttributeSet keyw3 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(94, 109, 3));
    final AttributeSet keyw4 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.red);//new Color(0, 92, 95)
    final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    
    private String mode;

    public HilightedDoc() {

    }

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offset);
        if (before < 0) {
            before = 0;
        }
        int after = findFirstNonWordChar(text, offset + str.length());
        int wordL = before;
        int wordR = before;

        while (wordR <= after) {
            if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                if (text.substring(wordL, wordR).matches("(\\W)*(int|char|double|float|long|short|void|auto|struct|break|else|switch|case|enum|register|typedef|union|extern|continue|do|while|for|union|sizeof|default|goto|const)")) {
                    setCharacterAttributes(wordL, wordR - wordL, keyw1, false);
                } else if (text.substring(wordL, wordR).matches("(\\W)*(print|scanf|printf)")) {
                    setCharacterAttributes(wordL, wordR - wordL, keyw2, false);
                } else if (text.substring(wordL, wordR).matches("(\\W)*(#|define|include)")) {
                    setCharacterAttributes(wordL, wordR - wordL, keyw3, false);
                } else if (text.substring(wordL, wordR).matches("(\\W)* (\".*\")")) {
                    setCharacterAttributes(wordL, wordR - wordL, keyw4, false);
                } else {
                    setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                }
                wordL = wordR;
            }
            wordR++;
        }
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offs);
        if (before < 0) {
            before = 0;
        }
        int after = findFirstNonWordChar(text, offs);
        try {
            if (text.substring(before, after - before).matches("(\\W)*(int|char|double|float|long|short|void|auto|struct|break|else|switch|case|enum|register|typedef|union|extern|continue|do|while|for|union|sizeof|default|goto|const)")) {
                setCharacterAttributes(before, after - before, keyw1, false);
            } else if (text.substring(before, after - before).matches("(\\W)*(print|scanf|printf)")) {
                setCharacterAttributes(before, after - before, keyw2, false);
            } else if (text.substring(before, after - before).matches("(\\W)*(#|define|include)")) {
                setCharacterAttributes(before, after - before, keyw3, false);
            } else if (text.substring(before, after - before).matches("(\\W)* (\".*\")")) {
                setCharacterAttributes(before, after - before, keyw4, false);
            } else {
                setCharacterAttributes(before, after - before, attrBlack, false);
            }
        } catch (Exception e) {
        }
    }
}
