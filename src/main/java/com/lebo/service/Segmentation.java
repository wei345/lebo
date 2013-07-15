package com.lebo.service;

import org.springframework.stereotype.Service;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashSet;

/**
 * @author: Wei Liu
 * Date: 13-7-15
 * Time: PM1:53
 */
@Service
public class Segmentation {

    /**
     * 返回文本中全部词的集合，英文字母会转为小写。
     */
    public LinkedHashSet findWords(String text) {
        LinkedHashSet<String> words = new LinkedHashSet<String>();

        IKSegmentation seg = new IKSegmentation(new StringReader(text));
        Lexeme lex;
        try {
            while ((lex = seg.next()) != null) {
                words.add(lex.getLexemeText());
            }
            return words;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
