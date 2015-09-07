/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Vaio
 */
public class SearchHandlerThread extends Thread {

    private Hashtable<String, Integer> allUrls;
    private int depth;
    private String urlRoot;
    private List<String> searchUrls;

    public SearchHandlerThread(int depth, String urlRoot, Hashtable<String, Integer> allUrls, List<String> searchUrls) {
        this.depth = depth;
        this.urlRoot = urlRoot;
        this.allUrls = allUrls;
        this.searchUrls = searchUrls;
    }

    public void run() {

        for (String link : searchUrls) {
            parseUrls(depth, link);
        }
    }

    public void parseUrls(int depth, String urlName) {
        ArrayList<String> childUrls = new ArrayList<String>();

        if (depth == 1) {
            try {
                URL url = new URL(urlName);
                Reader reader = new InputStreamReader((InputStream) url.getContent());
                HTMLUtils htmlPrser = new HTMLUtils(urlRoot, reader);

                // count the number of the capital letters in the current website
                allUrls.put(urlName, htmlPrser.getNumCapital());
                System.out.println("Tree Leaf - Thread " + getId()+" : "+urlName + " --> " + allUrls.get(urlName));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else {

            try {
                URL url = new URL(urlName);
                Reader reader = new InputStreamReader((InputStream) url.getContent());
                HTMLUtils htmlPrser = new HTMLUtils(urlRoot, reader);

                // count the number of the capital letters in the current website
                allUrls.put(urlName, htmlPrser.getNumCapital());
                System.out.println("Tree Node - Thread " + getId()+" : "+ urlName + " --> " + allUrls.get(urlName));

                List<String> links = htmlPrser.getListLink();
                for (String link : links) {
                    if (!allUrls.containsKey(link)) {
                        allUrls.put(link, 0);
                        childUrls.add(link);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            for (String link : childUrls) {
                parseUrls(depth - 1, link);
            }

        }
    }  //end of parseUrls
}
