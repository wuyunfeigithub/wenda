package com.coodeer.wenda.service;

import org.apache.commons.lang.CharUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by common on 2017/6/6.
 */
@Service
public class SensitiveWordsFilterService implements InitializingBean{

    private static final Logger logger = Logger.getLogger(SensitiveWordsFilterService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try{
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while((line = reader.readLine()) != null){
                addWord(line.trim());
            }

        } catch (Exception e){
            logger.error("敏感词文件加载错误" + e.getMessage());
        } finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(reader != null){
                    reader.close();
                }
            } catch (Exception e1){
                logger.error("敏感词文件关闭错误" + e1.getMessage());
            }
        }

    }

    private class TrieNode {
        private char val;
        private boolean isKeyWordsEnd;
        private Map<Character, TrieNode> map;

        public TrieNode(){
            val = '0';
            isKeyWordsEnd = false;
            map = new HashMap<>();
        }
        public TrieNode(char val){
            this();
            this.val = val;
        }

        public TrieNode getNode(char c){
            return map.get(c);
        }

        public void addNode(TrieNode node){
            map.put(node.val, node);
        }

        public boolean isKeyWordsEnd(){
            return isKeyWordsEnd;
        }

        public void setKeyWordsEnd(){
            this.isKeyWordsEnd = true;
        }
    }

    private TrieNode root = new TrieNode();

    private void addWord(String word){
        TrieNode p = root;
        for(int i = 0; i < word.length(); ++i){
            char c = word.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TrieNode temp = p.getNode(c);
            if(temp == null){
                temp = new TrieNode(c);
                p.addNode(temp);
            }
            if(i == word.length() - 1){
                temp.setKeyWordsEnd();
            }
            p = temp;
        }
    }

    //判断是否为特殊字符（既不属于英文、数字和东亚文字）
    //东亚文字范围0x2E80-0x9FFF
    private boolean isSymbol(char c){
        int ic = (int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String content){
        if(content == null)
            return content;

        StringBuilder sb = new StringBuilder();
        String replaceWord = "***";

        TrieNode p = root;
        int begin = 0;
        int position = begin;

        while (position < content.length()){
            char c = content.charAt(position);
            if(isSymbol(c)){
                if (p == root){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            TrieNode tempNode = p.getNode(c);
            if(tempNode == null){
                sb.append(content.charAt(begin));

                begin = begin + 1;
                position = begin;
                p = root;
            } else if (tempNode.isKeyWordsEnd()) {
                sb.append(replaceWord);

                begin = position + 1;
                position = begin;
                p = root;
            } else{
                p = tempNode;
                position ++;
            }
        }
        sb.append(content.substring(begin));
        return sb.toString();
    }
}
