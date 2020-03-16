package com.kocsisdavid.selectorhelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws UnknownHostException {


        List<String> urls = getLinesFromFile("./lists/url_list.txt");
        for (String url : urls) {
            Document browser = request(url);
            StringBuilder resultLine = null;
            if (browser != null) {
                resultLine = new StringBuilder("url: " + url);
                List<String> linkSelectors = getLinesFromFile("./lists/link_selector_list.txt");
                for (String linkSelector : linkSelectors) {
//                    System.out.println("Checking link selector: " + linkSelector);
                    Element linkElement = findElements(browser, linkSelector);
                    {
                        if (linkElement != null) {
                            resultLine.append("  |Link selector:" + linkSelector);

                            String mainPageLink = linkElement.attr("href");
                            Document mainPageBrowser = request(mainPageLink);

                            if (mainPageBrowser != null) {
                                List<String> mainContentSelectors = getLinesFromFile("./lists/main_content_selector_list.txt");
                                for (String mainContentSelector : mainContentSelectors) {
                                    Element mainContentElement = findElements(mainPageBrowser, mainContentSelector);

                                    if (mainContentElement != null) {
                                        resultLine.append("  |Main content selector: " + mainContentSelector);

                                        break;
                                    }
                                }
                                List<String> dateSelectors = getLinesFromFile("./lists/date_selector_list.txt");
                                for (String dateSelector : dateSelectors) {
                                    Element dateElement = findElements(mainPageBrowser, dateSelector);

                                    if (dateElement != null) {
                                        resultLine.append("  |Date selector: " + dateSelector);

                                        break;
                                    }

                                }
                                List<String> authorSelectors = getLinesFromFile("./lists/author_selector_list.txt");
                                for (String authorSelector : authorSelectors) {
                                    Element authorElement = findElements(mainPageBrowser, authorSelector);

                                    if (authorElement != null) {
                                        resultLine.append("  |Author selector: " + authorSelector);

                                        break;
                                    }

                                }


                            }
                            break;
                        }
                    }
                }

            }
            System.out.println(resultLine);
        }

    }

    public static List<String> getLinesFromFile(String filename){

        List<String> result = new ArrayList<String>();
        File file = new File(filename);

        try {
            //
            // Create a new Scanner object which will read the data
            // from the file passed in. To check if there are more
            // line to read from it we check by calling the
            // scanner.hasNextLine() method. We then read line one
            // by one till all line is read.
            //
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //store this line to string [] here
                result.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Document request(String url) {
        Document browser = null;
        try {
            Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36").timeout(8000).get();
            browser = document;
        } catch (Exception e) {
            System.out.println("Az oldalt nem sikerült betölteni: " + url);
        }
        return browser;
    }

    public static Element findElements(Document browser, String selector) {
        Element element = null;
        try {
            element = browser.selectFirst(selector);

        } catch (Exception e) {

        }
        return element;
    }

}
