package org.example;
import java.io.*;
import java.util.ArrayList;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.core.SimpleAnalyzer;
//import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.commons.io.IOUtils;
//import java.util.Collections;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.JSONTokener;
//import org.apache.lucene.analysis.CharArraySet;
//import org.apache.lucene.analysis.en.EnglishAnalyzer;
import java.io.FileInputStream;
//import java.util.Set;
import java.util.Iterator;


public class App
{
    public static void main( String[] args ) throws IOException {

        JsoupCrawlUtil crawlUtil = new JsoupCrawlUtil();
        FileWriter writer = new FileWriter("/home/user/Desktop/proof_tuning/quantity.txt");

        String path = "/home/user/Downloads/output.txt";
        int timeout = 5000; // You can adjust the timeout as needed
        try {
            String jsonFilePath = "/home/user/Desktop/output50.txt";
            FileInputStream inputStream = new FileInputStream(jsonFilePath);
            String everything = IOUtils.toString(inputStream);
            JSONArray jsonarray = new JSONArray(everything);
            inputStream.close();
//
            for (int k = 0; k < jsonarray.length(); k++) {

                JSONObject hobbies = jsonarray.getJSONObject(k);
                Iterator<String> keys = hobbies.keys();
                List<String> keyList = new ArrayList<>();
                while (keys.hasNext()) {
                    String key = keys.next();
//                    System.out.println("Key: " + key);
                    keyList.add(key);
                }
                String targetSubstring = "sentence";
                int index = -1; // Default index if substring is not found

                for (int i = 0; i < keyList.size(); i++) {
                    if (keyList.get(i).contains(targetSubstring)) {
                        index = i;
                        break; // Stop searching once the substring is found
                    }
                }
                System.out.println(keyList);
//                JSONObject quantity_1 = jsonarray.getJSONObject(3);
                JSONArray s = hobbies.getJSONArray("links");
//                String gf = hobbies.getString((String) "sentence_20640");
                JSONObject address = hobbies.getJSONObject("quantity_1");
                JSONArray jsonArray = address.getJSONArray("context");
                String qstr = address.getString("quantityStr");
                String estr = address.getString("entityStr");
                String query = hobbies.getString(keyList.get(index));
                System.out.println(estr);
                // Convert the JSON array to a List
                ArrayList<String> fruitsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    fruitsList.add(jsonArray.getString(i));
                }
                System.out.println(fruitsList);
                List<String> matchingTokens = new ArrayList<>();
                for (int i = 0; i < 10; i++) { //s.length()
//                System.out.println("Link--- " + (i + 1) + ": " + hobbies.getString(i));
                String url = s.getString(i);
                    if (url.contains("wiki")) {
                        continue;
                    }
                    String pageContent = crawlUtil.readPage(url, timeout);
                List<String> sentences = StanfordNLPSentenceBoundaryDisambiguation.getSentences(pageContent);
                    for (int j = 0; j < sentences.size(); j++) {
                    String sentence = sentences.get(j);
                    matchingTokens.clear();
                    for (String token : fruitsList) {
                        if (sentence.contains(token)) {
                            matchingTokens.add(token);
                        }
                    }
                    //matchingTokens.size() > 1 && && sentence.toLowerCase().contains(qstr)
                    // (sentence.toLowerCase().contains(qstr) || sentence.toLowerCase().contains(estr)) sentence.toLowerCase().contains("purchase")
                    if ((sentence.contains(estr) || sentence.contains(estr.toLowerCase())) && matchingTokens.size() > 1) {
                            writer.write(sentence);
                            writer.write("\n");
                            System.out.println((j + 1) + ". " + sentence);
//                        System.out.println(matchingTokens);
                    }
                }
                // Write the URL to the text file.
                writer.write(url);
                writer.write("\n");
                writer.write("\n");
            }
                writer.write("\n");
                writer.write("*****************************************************");
                writer.write("\n");
                writer.write(query);
                writer.write("\n");
                writer.write("\n");
                writer.write("*****************************************************");
                writer.write("\n");
            }
        }
            catch (Exception e) {
            e.printStackTrace();
        }
//        writer.close();
    }
}
